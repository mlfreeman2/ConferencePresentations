using System;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using LocalCache.DataModel;
using log4net;
using log4net.Config;
using RQM.API;
using RQM.API.ObjectModel;

namespace LocalCache
{
	public static class Program
	{
		private static readonly ILog Log = LogManager.GetLogger("RQM_Local_Caching_Tool");

		public static void Main(string[] args)
		{
			// set up log4net
			XmlConfigurator.Configure(new FileInfo("log4net.config"));

			Console.Write("User: ");
			var user = Console.ReadLine();

			Console.Write("Password: ");
			var password = StringFunctions.ReadPassword();

			Console.Write("Server: ");
			var server = Console.ReadLine();

			var rs = new RQMSession(server, user, password);

			if (!rs.LogIn())
			{
				Console.BackgroundColor = ConsoleColor.Red;
				Console.ForegroundColor = ConsoleColor.White;
				Console.WriteLine("Unable to log in to RQM at {0} as \"{1}\".", server, user);
				Console.ReadKey();
				Console.ResetColor();
				return;
			}

			var initial = DateTime.Now;

			var projects = rs.GetProjects().Select(a => a.Content).ToList();
			Parallel.ForEach(projects, p =>
			{
				using (var cache = new RQMCache())
				{
					LocalProject lp;
					if (!cache.LocalProjects.Any(a => a.ServerID == p.Identifier))
					{
						lp = cache.LocalProjects.Add(new LocalProject {
							ServerID = p.Identifier,
							APIName = p.Aliases[0].Value,
							Name = p.Title,
							ServerURL = server
						});
					}
					else
					{
						lp = cache.LocalProjects.First(a => a.ServerID == p.Identifier);
					}


					cache.SaveChanges();

					var apiName = lp.APIName;
					var projectID = lp.ID;

					#region Testcase Feed

					var testcases = rs.GetArtifacts<TestCase>(apiName);
					foreach (var tc in testcases.Where(a => a.Content != null).Select(a => a.Content).OrderBy(b => b.WebId).ToList())
					{
						Log.DebugFormat("==========================================================================");
						Log.DebugFormat("Encountered Testcase ID {0} on {1}.", tc.WebId, server);
						Log.DebugFormat("Name: {0}", tc.Title);
						Log.DebugFormat("Description: {0}", tc.Description);

						LocalTestcase ltc;
						if (!cache.LocalTestcases.Any(a => a.ProjectID == projectID && a.WebID == tc.WebId))
						{
							Log.DebugFormat("We've never seen this test case before.");
							ltc = cache.LocalTestcases.Add(new LocalTestcase {
								Name = tc.Title,
								Description = tc.Description,
								WebID = tc.WebId,
								URL = tc.Identifier,
								ProjectID = projectID,
							});
							cache.SaveChanges();
						}
						else
						{
							ltc = cache.LocalTestcases.First(a => a.ProjectID == projectID && a.WebID == tc.WebId);
						}

						foreach (var tsReference in tc.TestScripts)
						{
							Log.DebugFormat("Attempting to download a test script from {0}.", tsReference.HREF);
							var ts = rs.GetURLAs<TestScript>(tsReference.HREF);
							if (ts == null)
							{
								Log.ErrorFormat("The URL {0} returned an object that could not be parsed as a test script.", tsReference.HREF);
								continue;
							}
							Log.DebugFormat("Successfully downloaded test script ID {0} from {1}.", ts.WebId, tsReference.HREF);
							Log.DebugFormat("Name: {0}", ts.Title);

							LocalTestscript lts;
							if (!cache.LocalTestscripts.Any(a => a.ProjectID == projectID && a.WebID == ts.WebId))
							{
								Log.DebugFormat("Testscript ID {0} was already in the system, but since it showed up here it's linked to at least one testcase.", ts.WebId);
								lts = cache.LocalTestscripts.Add(new LocalTestscript { Name = ts.Title, ProjectID = projectID, WebID = ts.WebId, URL = tsReference.HREF });
							} 
							else
							{
								lts = cache.LocalTestscripts.First(a => a.ProjectID == projectID && a.WebID == ts.WebId);
							}
							ltc.Testscripts.Add(lts);
							cache.SaveChanges();
						}

					}
					#endregion

					#region Testscript Feed
					var testscripts = rs.GetArtifacts<TestScript>(apiName).Where(a => a.Content != null).Select(a => a.Content).OrderBy(b => b.WebId).ToList();
					foreach (var ts in testscripts)
					{
						if (ts == null)
						{
							Log.ErrorFormat("Null test script encountered.");
							continue;
						}
						Log.DebugFormat("==========================================================================");
						Log.DebugFormat("Encountered Testscript ID {0} on {1}.", ts.WebId, server);
						Log.DebugFormat("Name: {0}", ts.Title);

						if (!cache.LocalTestscripts.Any(a => a.WebID == ts.WebId && a.ProjectID == projectID))
						{
							Log.DebugFormat("Testscript ID {0} was not already in the system. This means it was not associated with any test cases.", ts.WebId);
							cache.LocalTestscripts.Add(new LocalTestscript { Name = ts.Title, ProjectID = projectID, WebID = ts.WebId, URL = ts.Identifier });
						}
						cache.SaveChanges();
					}
					#endregion
				}
			});
			Console.WriteLine("Took {0} seconds.", DateTime.Now.Subtract(initial).TotalSeconds);
			Console.WriteLine("Done");
			Console.ReadKey();
		}
	}
}