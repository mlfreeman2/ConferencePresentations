using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Security;
using System.Reflection;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Security.Cryptography.X509Certificates;
using System.ServiceModel.Syndication;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Xml;
using System.Xml.Serialization;
using log4net;
using RQM.API.ObjectModel;

namespace RQM.API
{
	/// <summary>
	/// My RQM API helper class.
	/// Recommended flow:
	/// 1. Construct (var session = new RQMSession(server, user, password); )
	/// 2. Login (session.LogIn(); )
	/// 3. Get project list (var projects = session.GetProjects(); )
	/// 3b. If LogIn() returns false, you're not logged in.
	/// 4. Whatever else you need to do.
	/// 
	/// Notes:
	/// Every function in here that needs a project name needs the REST API name for the project, which may not be the name shown in the UI.
	/// That's why I recommend you pull down the projects first. You can then do lookups between RQM UI name and REST API name if you want.
	/// </summary>
	public class RQMSession
	{
		private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

		#region Support for Self-Signed SSL
		private static bool _AllowSelfSignedSSL;

		private static bool AllowSelfSignedSSLCertificates
		{
			get { return _AllowSelfSignedSSL; }
			set
			{
				_AllowSelfSignedSSL = value;
				if (value)
				{
					ServicePointManager.ServerCertificateValidationCallback += ValidateRemoteCertificate;
					return;
				}

				if (ServicePointManager.ServerCertificateValidationCallback != null)
				{
					// ReSharper disable DelegateSubtraction
					ServicePointManager.ServerCertificateValidationCallback -= ValidateRemoteCertificate;
					// ReSharper restore DelegateSubtraction
				}
			}
		}

		// callback used to validate the certificate in an SSL conversation
		private static bool ValidateRemoteCertificate(object sender, X509Certificate certificate, X509Chain chain, SslPolicyErrors policyErrors)
		{
			return true;
		}
		#endregion

		/*
		* The relative path from the server's main login page to the REST API
		*/
		private const string IntegrationPath = "/service/com.ibm.rqm.integration.service.IIntegrationService/";

		/*
		 * The header returned by RQM when there's an authentication problem.
		 */
		private const string RQMAuthMsgHeaderName = "X-com-ibm-team-repository-web-auth-msg";

		/// <summary>
		/// The RQM server to log into (just the part that starts with "http" and ends with "/qm")
		/// </summary>
		public string ServerURL { get; private set; }

		/// <summary>
		/// The user name to log in as
		/// </summary>
		public string User { get; private set; }

		/// <summary>
		/// The password to log in with
		/// </summary>
		public string Password { get; private set; }

		/// <summary>
		/// True if we're logged in, False otherwise
		/// </summary>
		public bool LoggedIn { get { return Identity() != null; } }

		/// <summary>
		/// The object that holds cookies for use with HttpWebRequest
		/// </summary>
		private readonly CookieContainer _cookieContainer = new CookieContainer();

		private static readonly string _userAgent = "RQMDemoCodeCSharpUserAgent";

		public RQMSession(RQMLogin login) : this(login.RQMServer, login.UserName, login.Password) { }

		public RQMSession(string server, string user, string password)
		{
			if (string.IsNullOrWhiteSpace(server))
			{
				Log.Error("No URL supplied. Can not log in to RQM.");
				throw new ArgumentOutOfRangeException("server", "Must not be null or whitespace.");
			}
			if (string.IsNullOrWhiteSpace(user))
			{
				Log.Error("No user name supplied. Can not log in to RQM.");
				throw new ArgumentOutOfRangeException("user", "Must not be null or whitespace.");
			}
			if (string.IsNullOrWhiteSpace(password))
			{
				Log.Error("No password supplied. Can not log in to RQM.");
				throw new ArgumentOutOfRangeException("password", "Must not be null or whitespace.");
			}

			if (!AllowSelfSignedSSLCertificates)
			{
				AllowSelfSignedSSLCertificates = true;
			}

			ServerURL = server.Trim();
			User = user.Trim();
			Password = password.Trim();

			if (ServerURL.EndsWith("/"))
			{
				ServerURL = ServerURL.Substring(0, ServerURL.Length - 1);
			}
		}

		#region Login
		/// <summary>
		/// Logs in to RQM by simulating what the browser posts from the RQM UI login page.
		/// 
		/// </summary>
		/// <returns>True if login worked, false if not.</returns>
		public bool LogIn()
		{
			var loginURL = ServerURL + "/j_security_check";
			if (ServerURL.Contains("jazz.net"))
			{
				loginURL = "https://jazz.net/auth/login";
			}

			using (var response = GetResponse(CreatePostRequest_Form(loginURL, "j_username", User, "j_password", Password)))
			{
				var authMsg = response.Headers[RQMAuthMsgHeaderName];
				if (!string.IsNullOrWhiteSpace(authMsg))
				{
					Log.ErrorFormat("Login failed. '{0}' was present in the response. Its value was {1}.", RQMAuthMsgHeaderName, authMsg);
					return false;
				}
				Log.Debug("Login succeeded.");
				return true;
			}
		}

		/// <summary>
		/// This method accesses (Server URL)/authenticated/identity.
		/// If you're logged in, you'll get a JSON object with your user name and RQM roles in it back.
		/// This method will parse that into an instance of the <see cref="RQMIdentity"/> class.
		/// </summary>
		/// <returns>Either an instance of the <see cref="RQMIdentity"/> class or null if you're not logged or something goes wrong.</returns>
		public RQMIdentity Identity()
		{
			try
			{
				using (var response = GetResponse(CreateGetRequest(ServerURL + "/authenticated/identity")))
				{
					if (response.StatusCode > (HttpStatusCode) 399)
					{
						Log.ErrorFormat("HTTP {0} (\"{1}\") returned from the server.", response.StatusCode, response.StatusDescription);
						return null;
					}
					var authMsg = response.Headers[RQMAuthMsgHeaderName];
					if (authMsg != null)
					{
						Log.DebugFormat("We are not logged in. '{0}' was present in the response. Its value was {1}.", RQMAuthMsgHeaderName, authMsg);
						return null;
					}
					if (response.ContentType.Contains("text/json"))
					{
						Log.DebugFormat("The server returned JSON. Attempting to parse it as a RQMIdentity object.");
						using (var responseStream = response.GetResponseStream())
						{
							var dcjs = new DataContractJsonSerializer(typeof(RQMIdentity));
							if (responseStream != null)
							{
								return (RQMIdentity) dcjs.ReadObject(responseStream);
							}
							return null;
						}
					}
					Log.Error("The RQM server claimed we're logged in but did not return 'text/json' as the Content-Type header.");
					Log.DebugFormat("The RQM server returned {0} as the Content-Type header.", response.ContentType);
				}
			}
			catch (Exception e)
			{
				Log.Error("Unhandled exception accessing /authenticated/identity", e);
			}
			return null;
		}
		#endregion

		#region Custom Helper Functions
		/// <summary>
		/// This method checks to see if the given test script is referenced by the given test case, and that they both exist in the supplied project name.
		/// </summary>
		/// <param name="projectName">The **API Name** of the project to look in</param>
		/// <param name="tsid">The test script ID to check</param>
		/// <param name="tcid">The test case ID to check</param>
		/// <returns>
		/// True if the given test script and test case exist in the supplied project and if the test case references the test script		
		/// </returns>
		public bool DoesTestScriptBelongToTestCase(string projectName, int tsid, int tcid)
		{
			var tc = GetArtifactByWebID<TestCase>(projectName, tcid);
			var ts = GetArtifactByWebID<TestScript>(projectName, tsid);

			if (tc == null || ts == null)
			{
				return false;
			}
			return GetScriptsForTestCase(projectName, tcid).Any(t => t.Identifier == ts.Identifier);			
		}

		/// <summary>
		/// Gets all of the phases of a test plan. 
		/// Useful in creating a TCER...Test Plan and Test Phase are two of the things you need to make one.
		/// </summary>
		/// <param name="projectName">The **API Name** of the project to look in</param>
		/// <param name="testPlanID">The Web ID of the test plan</param>
		/// <returns>All of the phases of the test plan. Not ordered any particular way though.</returns>
		public List<TestPhase> GetPhasesForPlan(string projectName, int testPlanID)
		{
			var tp = GetArtifactByWebID<TestPlan>(projectName, testPlanID);
			// i know this looks inefficient, but in RQM resources can appear at multiple URLs.
			// things can be referenced by both their internal and web IDs.
			// so therefore we have to fetch the URL and check the ID tag in the returned XML.
			//
			// so we attempt to use LINQ to make things somewhat easier
			// get the target test plan, so we have its ID tag
			// get all the test phases in the project
			// get the distinct set of testplan URLs from the collection
			// hit all those URLs and grab the ID tag from each one, building a mapping collection
			// return all the test phases that reference a test plan with the right ID
			var phases = GetArtifacts<TestPhase>(projectName).Select(a => GetURLAs<TestPhase>(a.ID)).ToList();
			var seen = new Dictionary<string, string>();
			phases.Select(a => a.TestPlan.HREF).Distinct().ToList().ForEach(a => seen.Add(a, (GetURLAs<TestPlan>(a) ?? new TestPlan()).Identifier));
			return phases.Where(a => seen[a.TestPlan.HREF] == tp.Identifier).ToList();
		}

		/// <summary>
		/// Gets all the testscripts referenced by a test case.
		/// First it gets the testcase.
		/// Then it hits every URL specified in the HREF property of each testscript tag under the testcase and collects the returned objects into a list.
		/// </summary>
		/// <param name="projectName">The **API Name** of the project to look in</param>
		/// <param name="testCaseID">The Web ID of the test case</param>
		/// <returns>All of the test scripts associated with the testcase</returns>
		public List<TestScript> GetScriptsForTestCase(string projectName, int testCaseID)
		{
			return GetArtifactByWebID<TestCase>(projectName, testCaseID).TestScripts.Select(a => GetURLAs<TestScript>(a.HREF)).ToList();
		}
		#endregion

		#region Get Single Item
		/// <summary>
		/// Gets the specified project 
		/// </summary>
		/// <param name="projectName">The API name of the project</param>
		/// <returns>the RQM project object</returns>
		public Project GetProject(string projectName)
		{
			if (string.IsNullOrWhiteSpace(projectName))
			{
				throw new ArgumentOutOfRangeException("projectName", "Must not be null or whitespace.");
			}
			return GetURLAs<Project>(string.Format(URLTemplates.ProjectByName, ServerURL, IntegrationPath, projectName));
		}

		/// <summary>
		/// Gets the specified artifact by its web ID (number shown in the RQM UI)
		/// </summary>
		/// <typeparam name="TX">The type of the artifact you want (testcase, testscript, testplan, etc)</typeparam>
		/// <param name="projectName">The API name of the project it's under</param>
		/// <param name="webID">The web (UI) ID of the artifact you want</param>
		/// <returns>The artifact as a C# class</returns>
		public TX GetArtifactByWebID<TX>(string projectName, int webID) where TX : class
		{
			if (string.IsNullOrWhiteSpace(projectName))
			{
				throw new ArgumentOutOfRangeException("projectName", "Must not be null or whitespace.");
			}
			if (webID <= 0)
			{
				throw new ArgumentOutOfRangeException("webID", "Must be greater than 0.");
			}
			return GetURLAs<TX>(string.Format(URLTemplates.ArtifactByWebID, ServerURL, IntegrationPath, projectName, typeof(TX).Name.ToLower(), webID));
		}

		/// <summary>
		/// Gets the specified artifact by its web ID (number shown in the RQM UI)
		/// </summary>
		/// <param name="type">The type of the artifact you want (testcase, testscript, testplan, etc)</param>
		/// <param name="projectName">The API name of the project it's under</param>
		/// <param name="webID">The web (UI) ID of the artifact you want</param>
		/// <returns>The artifact as a string</returns>
		public string GetArtifactByWebID(string projectName, System.Type type, int webID)
		{
			if (string.IsNullOrWhiteSpace(projectName))
			{
				throw new ArgumentOutOfRangeException("projectName", "Must not be null or whitespace.");
			}
			if (webID <= 0)
			{
				throw new ArgumentOutOfRangeException("webID", "Must be greater than 0.");
			}

			return GetURLAsString(string.Format(URLTemplates.ArtifactByWebID, ServerURL, IntegrationPath, projectName, type.Name.ToLower(), webID));
		}

		/// <summary>
		/// Gets the specified artifact by its internal ID (string shown in some places in the REST API)
		/// </summary>
		/// <typeparam name="TX">The type of the artifact you want (testcase, testscript, testplan, etc)</typeparam>
		/// <param name="projectName">The API name of the project it's under</param>
		/// <param name="id">The internal ID of the artifact you want</param>
		/// <returns>The artifact as a C# class</returns>
		public TX GetArtifactByID<TX>(string projectName, string id) where TX : class
		{
			if (string.IsNullOrWhiteSpace(projectName))
			{
				throw new ArgumentOutOfRangeException("projectName", "Must not be null or whitespace.");
			}
			if (string.IsNullOrWhiteSpace(id))
			{
				throw new ArgumentOutOfRangeException("id", "Must not be null or whitespace.");
			}
			return GetURLAs<TX>(string.Format(URLTemplates.ArtifactByID, ServerURL, IntegrationPath, projectName, typeof(TX).Name.ToLower(), id));
		}

		/// <summary>
		/// Downloads the specified file from RQM.
		/// You can upload arbitrary files to a RQM project as "attachment" artifacts.
		/// </summary>
		/// <param name="projectName">The API name of the project it's under</param>
		/// <param name="id">The internal ID of the attachment you want</param>
		/// <returns>The file as a byte array</returns>
		public byte[] GetAttachment(string projectName, string id)
		{
			if (string.IsNullOrWhiteSpace(projectName))
			{
				throw new ArgumentOutOfRangeException("projectName", "Must not be null or whitespace.");
			}
			if (string.IsNullOrWhiteSpace(id))
			{
				throw new ArgumentOutOfRangeException("id", "Must not be null or whitespace.");
			}
			using (var response = GetResponse(CreateGetRequest(string.Format(URLTemplates.AttachmentByID, ServerURL, IntegrationPath, projectName, id))))
			{
				if (response.StatusCode == HttpStatusCode.NotFound)
				{
					return new byte[0];
				}

				using (var responseStream = response.GetResponseStream())
				using (var memoryStream = new MemoryStream())
				{
					if (responseStream != null)
					{
						responseStream.CopyTo(memoryStream);
					}
					return memoryStream.ToArray();
				}
			}
		}
		#endregion

		#region Upload
		/// <summary>
		/// Executes a HTTP PUT of the specified artifact to the specified URL.
		/// HTTP PUTs are used to update an existing artifact.
		/// </summary>
		/// <typeparam name="TX">The C# type of the artifact you're uploading (testplan, testcase, etc)</typeparam>
		/// <param name="item">The artifact you're uploading</param>
		/// <param name="url">The URL to HTTP PUT the artifact to.</param>
		/// <returns>The result of the update</returns>
		public UploadResult Put<TX>(TX item, string url) where TX : class
		{
			return Upload("PUT", item, url);
		}

		/// <summary>
		/// Executes a HTTP POST of the specified artifact to the specified URL.
		/// HTTP POSTs are used to create a new artifact.
		/// </summary>
		/// <typeparam name="TX">The C# type of the artifact you're uploading (testplan, testcase, etc)</typeparam>
		/// <param name="projectName">The RQM project to post the new artifact to</param>
		/// <param name="item">The artifact you're uploading</param>
		/// <returns>The result of the update</returns>
		public UploadResult Post<TX>(string projectName, TX item) where TX : class
		{
			return Upload("POST", item, BuildFeedURL<TX>(projectName));
		}

		/// <summary>
		/// General purpose upload helper. 
		/// Serializes the object to XML and sends it to the specified URL using the specified HTTP method.
		/// </summary>
		/// <typeparam name="TX">The C# type of the artifact you're uploading (testplan, testcase, etc)</typeparam>
		/// <param name="methodName">The HTTP method (PUT/POST/etc) you're using in the upload</param>
		/// <param name="item">The artifact you're uploading</param>
		/// <param name="url">The URL to HTTP POST the artifact to.</param>
		/// <returns>The result of the upload</returns>
		public UploadResult Upload<TX>(string methodName, TX item, string url) where TX : class
		{
			if (string.IsNullOrWhiteSpace(url))
			{
				throw new ArgumentOutOfRangeException("url", "Must not be null or whitespace.");
			}
			if (item == null)
			{
				throw new ArgumentNullException("item", "Must not be null.");
			}

			var xs = new XmlSerializer(typeof(TX));
			var buffer = new UTF8StringWriter();
			xs.Serialize(buffer, item);

			var request = (HttpWebRequest)WebRequest.Create(url);
			request.CookieContainer = _cookieContainer;
			request.Method = methodName.ToUpper();
			request.ContentType = "application/xml";
			request.UserAgent = _userAgent;

			var data = Encoding.UTF8.GetBytes(buffer.ToString());
			request.ContentLength = data.Length;

			using (var stream = request.GetRequestStream())
			{
				stream.Write(data, 0, data.Length);
			}

			using (var response = GetResponse(request))
			{
				switch (response.StatusCode)
				{
					case HttpStatusCode.InternalServerError:
					case HttpStatusCode.NotFound:
						return new UploadResult { Status = UploadResultStatus.Failure };
					case HttpStatusCode.SeeOther:
						return new UploadResult {
							Status = UploadResultStatus.Duplicate,
							URL = response.ResponseUri.ToString(),
							ContentLocation = response.Headers["Content-Location"]
						};
					default:
						return new UploadResult {
							Status = UploadResultStatus.Success,
							URL = response.ResponseUri.ToString(),
							ContentLocation = response.Headers["Content-Location"]
						};
				}
			}
		}

		/// <summary>
		/// Uploads a file to a RQM project's "attachments" artifact collection.
		/// You can send any arbitrary file.
		/// This sends the file as "multipart/form-data", which is how browsers upload files when you use an [input type="file"] html tag
		/// </summary>
		/// <param name="projectName">The API name of the project</param>
		/// <param name="id">The ID to assign to the attachment</param>
		/// <param name="fileName">The file name to assign to the attachment</param>
		/// <param name="contentMIMEType">
		/// The MIME type of the file you're uploading (e.g. text/plan, application/pdf, etc)
		/// this defaults to the generic fallback of application/octet-stream
		/// </param>
		/// <param name="file">The actual bytes of the file you're uploading</param>
		/// <returns>The result of the upload.</returns>
		public UploadResult PostAttachment(string projectName, string id, string fileName, byte[] file, string contentMIMEType = "application/octet-stream")
		{
			var boundary = "----------" + DateTime.Now.Ticks.ToString("x");

			var request = (HttpWebRequest)WebRequest.Create(string.Format(URLTemplates.AttachmentByID, ServerURL, IntegrationPath, projectName, id));
			request.UserAgent = _userAgent;
			request.CookieContainer = _cookieContainer;
			request.Method = "POST";
			request.ContentType = string.Format("multipart/form-data; boundary={0}", boundary);

			var header = Encoding.UTF8.GetBytes(string.Format("--{0}\r\n", boundary));
			request.ContentLength = header.Length;

			var contentDisposition = Encoding.UTF8.GetBytes(string.Format("Content-Disposition: form-data; name=\"{0}\"; filename=\"{0}\"\r\n", fileName));
			request.ContentLength += contentDisposition.Length;

			var contentType = Encoding.UTF8.GetBytes(string.Format("Content-Type: \"{0}\"\r\n", contentMIMEType));
			request.ContentLength += contentType.Length;

			var contentTransferEncoding = Encoding.UTF8.GetBytes("Content-Transfer-Encoding: \"binary\"\r\n\r\n");
			request.ContentLength += contentTransferEncoding.Length;

			request.ContentLength += file.Length;

			var footer = Encoding.UTF8.GetBytes(string.Format("\r\n--{0}--\r\n", boundary));
			request.ContentLength += footer.Length;

			using (var stream = request.GetRequestStream())
			{
				stream.Write(header, 0, header.Length);
				stream.Write(contentDisposition, 0, contentDisposition.Length);
				stream.Write(contentType, 0, contentType.Length);
				stream.Write(contentTransferEncoding, 0, contentTransferEncoding.Length);
				stream.Write(file, 0, file.Length);
				stream.Write(footer, 0, footer.Length);
			}

			using (var response = GetResponse(request))
			{
				switch (response.StatusCode)
				{
					case HttpStatusCode.InternalServerError:
					case HttpStatusCode.NotFound:
						return new UploadResult { Status = UploadResultStatus.Failure };
					case HttpStatusCode.SeeOther:
						return new UploadResult {
							Status = UploadResultStatus.Duplicate,
							URL = response.ResponseUri.ToString(),
							ContentLocation = response.Headers["Content-Location"]
						};
					default:
						return new UploadResult {
							Status = UploadResultStatus.Success,
							URL = response.ResponseUri.ToString(),
							ContentLocation = response.Headers["Content-Location"]
						};
				}
			}

		}
		#endregion

		#region Feeds
		/// <summary>
		/// Gets all the RQM projects on this RQM server.
		/// Once you have this list you can convert a project's UI name into its API name by looking at Project.Title (the UI name) and Project.Aliases[0].Value (the API name)
		/// </summary>
		/// <returns>All the projects on this RQM server</returns>
		public List<FeedItem<Project>> GetProjects()
		{
			var projects = GetFeedOf<Project>(string.Format("{0}{1}projects/", ServerURL, IntegrationPath), "");
			projects.ForEach(a => a.Content = GetURLAs<Project>(a.ID));
			return projects;
		}

		/// <summary>
		/// Gets all pages of the given artifact feed.
		/// (e.g. gets all the pages of the testcase feed or testscript feed, etc)
		/// 
		///The artifact types with feeds are:
		///executionresult
		///testplan
		///testphase
		///testcase
		///testscript
		///executionworkitem
		///requirement
		///datapool
		///tooladapter
		///adaptertask
		///template
		///testsuite
		///keyword
		///testsuitelog
		///suiteexecutionrecord
		///configuration
		///remotescript
		///request
		///reservation
		///resourcecollection
		///resourcegroup
		///labresource
		///job
		///jobresult
		///teamarea
		///contributor
		///workitem
		///buildrecord
		///builddefinition
		///testcell
		///objective
		///project
		///attachment
		///category
		///categoryType
		/// </summary>
		/// <typeparam name="TX">The artifact type you want (the C# class represnting one of the items above)</typeparam>
		/// <param name="projectName">Optional. The RQM project to pull the feed from. If left out it will try to hit a RQM 4.0 global feed</param>
		/// <param name="filteringOptions">
		/// Additional options to pass to the RQM server for filtering. See the wiki on jazz.net for details.
		/// They must alternate between option name and option value
		/// </param>
		/// <returns>All the pages of the feed</returns>
		public List<FeedItem<TX>> GetArtifacts<TX>(string projectName = "", params string[] filteringOptions) where TX : class
		{
			return GetFeedOf<TX>(BuildFeedURL<TX>(projectName), filteringOptions);
		}

		/// <summary>
		/// Helper method used to build feed URLs for a given artifact type and optionally project
		/// </summary>
		/// <typeparam name="TX">An artifact type with a feed. <see cref="GetArtifacts{TX}"/></typeparam>
		/// <param name="project">Optional: the API name of the project that has the feed you want</param>
		/// <returns>The full URL to the feed</returns>
		private string BuildFeedURL<TX>(string project = "") where TX : class
		{
			var artifact = typeof(TX).Name.ToLower();
			return string.Format("{0}{1}resources/{2}/", ServerURL, IntegrationPath, (!string.IsNullOrWhiteSpace(project) ? string.Format("{0}/{1}", project, artifact) : artifact));
		}

		/// <summary>
		/// This function downloads all pages of the given RQM feed.
		/// It attempts to do so using Paralle.ForEach to make the pages load in paralle.
		/// </summary>
		/// <typeparam name="TX">An artifact type with a feed. <see cref="GetArtifacts{TX}"/></typeparam>
		/// <param name="baseURL">The URL of the feed</param>
		/// <param name="queryParams">Additional parameters to append to the URL (alternate between names and values)</param>
		/// <returns>All the items on all the pages of this feed.</returns>
		private List<FeedItem<TX>> GetFeedOf<TX>(string baseURL, params string[] queryParams) where TX : class
		{
			var url = baseURL;

			if (queryParams != null && queryParams.Any())
			{
				var enc = StringFunctions.URLEncode(queryParams);
				if (!string.IsNullOrWhiteSpace(enc))
				{
					url += "?" + enc;
				}
			}

			try
			{
				#region Determine Last Page (page count)
				var initialFeed = DownloadFeedPage(url);
				if (initialFeed == null)
				{
					return new List<FeedItem<TX>>();
				}

				var urls = new List<string>();
				
				var lastPageURL = initialFeed.Links.FirstOrDefault(a => a.RelationshipType == "last");
				if (lastPageURL != null)
				{
					var lastPage = Convert.ToInt32(Regex.Match(lastPageURL.Uri.ToString(), "page=(\\d+)").Groups[1].Value);
					Log.DebugFormat("{0} was the original last page URL.", lastPageURL.GetAbsoluteUri().ToString());
					for (var i = 0; i <= lastPage; i++)
					{
						urls.Add(Regex.Replace(lastPageURL.Uri.ToString(), "page=\\d+", "page=" + i));
						Log.DebugFormat("Feed URL {0} added.", urls.Last());
					}
				}
				else
				{
					urls.Add(url);
				}
				#endregion

				#region Download Pages
				var parallelResults = new ConcurrentBag<FeedItem<TX>>();
				Parallel.ForEach(urls, (currentURL, state, currentPage) => {
					Log.DebugFormat("Downloading feed URL {0}", currentURL);
					var feed = DownloadFeedPage(currentURL);
					if (feed != null)
					{
						FeedItem<TX>.ProcessFeedPage<TX>(feed, currentPage).ForEach(parallelResults.Add);
						Log.DebugFormat("Done downloading feed URL {0}", currentURL);
					}
					else
					{
						Log.ErrorFormat("Error downloading feed URL {0}", currentURL);
					}
				});
				return parallelResults.OrderByDescending(a => a.Date).ToList();
				#endregion
			}
			catch (Exception e)
			{
				Log.Error("Unhandled Exception", e);
			}
			return new List<FeedItem<TX>>();
		}
		
		/// <summary>
		/// This function renders a URL into an instance of SyndicationFeed
		/// </summary>
		/// <param name="currentURL">The URL to download</param>
		/// <returns>a SyndicationFeed object, or null if something goes wrong </returns>
		private SyndicationFeed DownloadFeedPage(string currentURL)
		{
			using (var response = GetResponse(CreateGetRequest(currentURL)))
			{
				if (response.StatusCode == HttpStatusCode.NotFound)
				{
					Log.ErrorFormat("Unable to download {0} as an Atom feed. HTTP 404 Not Found.", currentURL);
					return null;
				}

				using (var httpStream = response.GetResponseStream())
				{
					if (httpStream == null)
					{
						Log.ErrorFormat("response.GetResponseStream() returned null for {0}", currentURL);
						return null;
					}

					#region Special Workaround because it appears that SyndicationFeed does not like empty dates.
					var sr = new StreamReader(httpStream);

					var replacement = string.Format("<updated>{0}</updated>", XmlConvert.ToString(new DateTimeOffset(DateTime.UtcNow)));
					var xml = sr.ReadToEnd().Replace("<updated/>", replacement).Replace("<updated></updated>", replacement);

					Log.Debug("Response XML: " + xml);
					#endregion
					
					return SyndicationFeed.Load(XmlReader.Create(new StringReader(xml)));
				}
			}
		}
		#endregion

		/// <summary>
		/// Downloads the specified URL and attempts to convert it into a C# object of the specified type.
		/// The download had better return XML.
		/// This is the main function used when downloading assets from RQM.
		/// </summary>
		/// <typeparam name="TX">The type you want to serialize the download into</typeparam>
		/// <param name="url">The URL to download</param>
		/// <returns>Either the XML returned from that URL serialized into a C# object, or null if something went wrong.</returns>
		public TX GetURLAs<TX>(string url) where TX : class
		{
			using (var response = GetResponse(CreateGetRequest(url)))
			{
				if (response.StatusCode == HttpStatusCode.NotFound)
				{
					Log.ErrorFormat("HTTP 404 Not Found attempting to fetch {0}", url);
					return default(TX);
				}

				using (var stream = response.GetResponseStream())
				{
					if (stream == null)
					{
						Log.ErrorFormat("response.GetResponseStream() returned null for {0}", url);
						return default(TX);
					}
					var reader = XmlReader.Create(stream);
					var xs = new XmlSerializer(typeof(TX));
					return (TX) xs.Deserialize(reader);
				}
			}
		}

		/// <summary>
		/// Downloads the specified URL.
		/// </summary>
		/// <param name="url">The URL to download</param>
		/// <returns>Either the XML returned from that URL, or null if something went wrong.</returns>
		public string GetURLAsString(string url)
		{
			using (var response = GetResponse(CreateGetRequest(url)))
			{
				if (response.StatusCode == HttpStatusCode.NotFound)
				{
					Log.ErrorFormat("HTTP 404 Not Found attempting to fetch {0}", url);
					return null;
				}
				using (var ms = new MemoryStream())
				using (var stream = response.GetResponseStream())
				{
					if (stream == null)
					{
						Log.ErrorFormat("response.GetResponseStream() returned null for {0}", url);
						return null;
					}
					stream.CopyTo(ms);
					return Encoding.UTF8.GetString(ms.ToArray());
				}
			}
		}

		/// <summary>
		/// Downloads the specified URL.
		/// </summary>
		/// <param name="url">The URL to download</param>
		/// <returns>Either the XML returned from that URL, or null if something went wrong.</returns>
		public byte[] GetURLAsBytes(string url)
		{
			using (var response = GetResponse(CreateGetRequest(url)))
			{
				if (response.StatusCode == HttpStatusCode.NotFound)
				{
					Log.ErrorFormat("HTTP 404 Not Found attempting to fetch {0}", url);
					return null;
				}
				using (var ms = new MemoryStream())
				using (var stream = response.GetResponseStream())
				{
					if (stream == null)
					{
						Log.ErrorFormat("response.GetResponseStream() returned null for {0}", url);
						return null;
					}
					stream.CopyTo(ms);
					return ms.ToArray();
				}
			}
		}

		/// <summary>
		/// Checks to see if the specified item exists in the specified project
		/// </summary>
		/// <typeparam name="TX">The type of the artifact (testplan, testcase, etc)</typeparam>
		/// <param name="projectName">The API name of the RQM project you're polling</param>
		/// <param name="webID">The UI ID of the artifact you're looking for</param>
		/// <returns>True if the artifact exists, False if not (or something went wrong)</returns>
		public bool DoesItemExist<TX>(string projectName, int webID) where TX : class
		{
			if (string.IsNullOrWhiteSpace(projectName))
			{
				throw new ArgumentOutOfRangeException("projectName", "Must not be null or whitespace.");
			}
			if (webID <= 0)
			{
				throw new ArgumentOutOfRangeException("webID", "Must be greater than 0.");
			}
			try
			{
				return GetURLAs<TX>(string.Format(URLTemplates.ArtifactByWebID, ServerURL, IntegrationPath, projectName, typeof(TX).Name.ToLower(), webID)) != null;
			}
			catch
			{
				return false;
			}
		}

		/// <summary>
		/// Checks to see if the specified item exists in the specified project
		/// </summary>
		/// <typeparam name="TX">The artifact's C# class type</typeparam>
		/// <param name="projectName">The API name of the RQM project you're polling</param>
		/// <param name="id">The internal ID of the artifact you're looking for</param>
		/// <returns>True if the artifact exists, False if not</returns>
		public bool DoesItemExist<TX>(string projectName, string id) where TX : class
		{
			if (string.IsNullOrWhiteSpace(projectName))
			{
				throw new ArgumentOutOfRangeException("projectName", "Must not be null or whitespace.");
			}
			if (string.IsNullOrWhiteSpace(id))
			{
				throw new ArgumentOutOfRangeException("id", "Must not be null or whitespace.");
			}
			try
			{
				return GetURLAs<TX>(string.Format(URLTemplates.ArtifactByWebID, ServerURL, IntegrationPath, projectName, typeof(TX).Name.ToLower(), id)) != null;
			}
			catch
			{
				return false;
			}
		}

		/// <summary>
		/// Builds a HTTP POST request from some name/value pairs.
		/// Formats the request so it resembles what a browser would produce if it were submitting a HTML Form
		/// </summary>
		/// <param name="url">The URL to POST to</param>
		/// <param name="postValues">The name/value pairs to post (alternate between name and value)</param>
		/// <returns>The HttpWebRequest object, ready to send.</returns>
		private HttpWebRequest CreatePostRequest_Form(string url, params string[] postValues)
		{
			var request = (HttpWebRequest)WebRequest.Create(url);

			request.Method = "POST";
			request.ContentType = "application/x-www-form-urlencoded";
			request.CookieContainer = _cookieContainer;
			request.UserAgent = _userAgent;

			var final = StringFunctions.URLEncode(postValues);
			if (final.EndsWith("&"))
			{
				final = final.TrimEnd('&');
			}

			var bytes = Encoding.UTF8.GetBytes(final);
			request.ContentLength = bytes.Length;

			using (var writeStream = request.GetRequestStream())
			{
				writeStream.Write(bytes, 0, bytes.Length);
			}

			return request;
		}

		/// <summary>
		/// Creates a basic HTTP GET request and associates the RQMSession's HTTP Cookie collection with it
		/// </summary>
		/// <param name="url">The URL you want to GET</param>
		/// <returns>A ready-to-send HttpWebRequest</returns>
		private HttpWebRequest CreateGetRequest(string url)
		{
			var request = (HttpWebRequest)WebRequest.Create(url);
			request.Method = "GET";
			request.CookieContainer = _cookieContainer;
			request.UserAgent = _userAgent;
			return request;
		}

		/// <summary>
		/// Executes a HttpWebRequest and gets a response.
		/// The .NET runtime tries to throw exceptions for HTTP protocol problems (300/400/500 HTTP return codes).
		/// I decided I didn't like that and only wanted exceptions for other problems.
		/// So I created this method. It catches all exceptions.
		/// If the cause was a HTTP 300/400/500 code it will return the response.
		/// If the cause was something else it will re-throw the exception.
		/// </summary>
		/// <param name="request">The HttpWebRequest to execute</param>
		/// <returns>The result of executing that request</returns>
		private HttpWebResponse GetResponse(HttpWebRequest request)
		{
			try
			{
				return request.GetResponse() as HttpWebResponse;
			}
			catch (WebException e)
			{
				if (e.Status == WebExceptionStatus.ProtocolError)
				{
					return e.Response as HttpWebResponse;
				}
				throw;
			}
		}
	}

	public static class URLTemplates
	{
		/*
		* The format of URLs used to reach an artifact by its UI ID (a number)
		*/
		public const string ArtifactByWebID = "{0}{1}resources/{2}/{3}/urn:com.ibm.rqm:{3}:{4}";

		/*
		* The format of URLs used to reach an artifact by its internal ID (a string)
		*/
		public const string ArtifactByID = "{0}{1}resources/{2}/{3}/{4}";

		/*
		* The format of URLs used to download attachments (arbitrary files) from a RQM project
		*/
		public const string AttachmentByID = "{0}{1}resources/{2}/attachment/{3}";

		/*
		* The format of URLs used to fetch RQM projects as XML files
		*/
		public const string ProjectByName = "{0}{1}/projects/{2}";
	}

	/*
	* The possible results of an effort to upload something to RQM.<br>
	* It either succeeded, failed, or ran afoul of RQM's efforts to detect "duplicate" artifacts. <br>
	* In the third scenario you received a redirection to the artifact that RQM thinks you're colliding with
	*/
	public enum UploadResultStatus
	{
		Success,
		Failure,
		Duplicate
	}

	public class UploadResult
	{
		/// <summary>
		/// The status of the upload effort
		/// </summary>
		public UploadResultStatus Status { get; set; }

		/// <summary>
		/// The URL of the artifact you've uploaded
		/// </summary>
		public string URL { get; set; }

		/// <summary>
		/// The URL returned by RQM when you run afoul of its duplicate detection code.
		/// This comes from the HTTP content-location header.
		/// </summary>
		public string ContentLocation { get; set; }
	}

	/// <summary>
	/// A class representing an RQM login. Useful if you need a model to bind something against.
	/// </summary>
	public class RQMLogin
	{
		/// <summary>
		/// The RQM server to log into (just the part that starts with "http" and ends with "/qm")
		/// </summary>
		[Required]
		[Display(Name = "RQM Server")]
		public string RQMServer { get; set; }

		/// <summary>
		/// The user name to log in as
		/// </summary>
		[Required]
		[Display(Name = "User Name")]
		public string UserName { get; set; }

		/// <summary>
		/// The password to log in with
		/// </summary>
		[Required]
		[Display(Name = "Password")]
		public string Password { get; set; }
	}

	/// <summary>
	/// This is the object returned by hitting (Server URL)/authenticated/identity in RQM.
	/// It is returned as JSON.
	/// It tells you who you are and what your roles are.
	/// </summary>
	[DataContract]
	public class RQMIdentity
	{
		/// <summary>
		/// The user ID you logged in to RQM with
		/// </summary>
		[DataMember(Name = "userId")]
		public string UserID { get; set; }

		/// <summary>
		/// The roles your user has
		/// </summary>
		[DataMember(Name = "roles")]
		public string[] Roles { get; set; }
	}

	/// <summary>
	/// A class made to simplify System.ServiceModel.SyndicationFeed feed items.
	/// </summary>
	/// <typeparam name="TX">The C# type that should be in the Content part of this class</typeparam>
	public class FeedItem<TX>
	{
		public FeedItem()
		{
			Links = new List<FeedLink>();
		}

		private static readonly ILog Log = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

		/// <summary>
		/// The ID of the feed entry
		/// </summary>
		public string ID { get; set; }

		/// <summary>
		/// The title of the feed entry
		/// </summary>
		public string Title { get; set; }

		/// <summary>
		/// The summary of the feed entry
		/// </summary>
		public string Summary { get; set; }

		/// <summary>
		/// All of the links in the feed entry
		/// </summary>
		public List<FeedLink> Links { get; set; }

		/// <summary>
		/// The page number that the feed entry was on 
		/// Not normally in an Atom or RSS feed item
		/// </summary>
		public long Page { get; set; }

		/// <summary>
		/// The index of this item on the current page.
		/// Not normally in an Atom or RSS feed item
		/// </summary>
		public int Index { get; set; }

		/// <summary>
		/// The body of the feed.
		/// This would usually be null, unless you use RQM filtering options to request part or all of the artifacts in the feed.
		/// Doing so will slow things down immensely
		/// </summary>
		public TX Content { get; set; }

		/// <summary>
		/// The date and time of the feed entry
		/// </summary>
		public DateTimeOffset Date { get; set; }

		/// <summary>
		/// A helper function that parses the current feed page into instances of FeedItem
		/// </summary>
		/// <typeparam name="TX">The type of FeedItem.Content</typeparam>
		/// <param name="feed">The SyndicationFeed object to parse</param>
		/// <param name="currentPage">What page of the RQM feed you're on</param>
		/// <returns>The parsed SyndicationFeed</returns>
		public static List<FeedItem<TX>> ProcessFeedPage<TX>(SyndicationFeed feed, long currentPage)
		{
			var results = new List<FeedItem<TX>>();
			var count = 1;
			foreach (var item in feed.Items)
			{
				var fp = new FeedItem<TX>
				{
					Title = item.Title.Text,
					ID = item.Id,
					Summary = item.Summary.Text,
					Page = currentPage,
					Index = count,
					Date = item.PublishDate,
				};

				fp.Links.AddRange(item.Links.Where(a => a.Uri != null && a.GetAbsoluteUri() != null).Select(a => new FeedLink
				{
					URL = a.GetAbsoluteUri().ToString(),
					Relation = a.RelationshipType,
					MediaType = a.MediaType
				}));

				if (item.Content is XmlSyndicationContent)
				{
					try
					{
						var xs = new XmlSerializer(typeof(TX));
						var content = item.Content as XmlSyndicationContent;
						fp.Content = content.ReadContent<TX>(xs);
					}
					catch (Exception e)
					{
						Log.Error("Unhandled Exception", e);
						fp.Content = default(TX);
					}
				}
				results.Add(fp);
				count++;
			}
			return results;
		}
	}

	/// <summary>
	/// A link inside a FeedItem
	/// </summary>
	public class FeedLink
	{
		public string URL { get; set; }

		public string Relation { get; set; }

		public string MediaType { get; set; }
	}
}