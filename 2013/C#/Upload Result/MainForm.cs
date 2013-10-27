using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading;
using System.Windows.Forms;
using System.Xml;
using RQM.API;
using RQM.API.ObjectModel;

namespace Upload.Result
{
	public partial class MainForm : Form
	{
		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main()
		{
			Application.EnableVisualStyles();
			Application.SetCompatibleTextRenderingDefault(false);
			Application.Run(new MainForm());
		}

		private RQMSession session;

		private List<Project> projects;

		public MainForm()
		{
			InitializeComponent();

			txtUsername.Select();

			cbxResult.DataSource = new BindingSource(new Dictionary<string, string> { {"Pass", "true"}, {"Fail", "false"} }, null);
			cbxResult.DisplayMember = "Key";
			cbxResult.ValueMember = "Value";
		}

		private void btnExit_Click(object sender, EventArgs e)
		{
			Environment.Exit(0);
		}

		private void btnLogIn_Click(object sender, EventArgs e)
		{
			if (session != null)
			{
				btnLogIn.Text = "Log In";
				session = null;
				
				cbxProject.Enabled = false;
				cbxTestPlan.Enabled = false;
				cbxTestPhase.Enabled = false;

				numericTestcaseWebID.Enabled = false;
				
				txtAttachmentPath.Enabled = false;
				txtAttachmentPath.Text = "";

				txtComment.Enabled = false;
				txtComment.Text = "";

				btnBrowseAttachment.Enabled = false;
				btnUpload.Enabled = false;
				return;
			}

			lblLoading.Show();
			btnLogIn.Enabled = false;

			if (string.IsNullOrWhiteSpace(txtUsername.Text))
			{
				MessageBox.Show("Please supply a user name.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
				return;
			}

			if (string.IsNullOrWhiteSpace(txtPassword.Text))
			{
				MessageBox.Show("Please supply a password.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
				return;
			}

			if (string.IsNullOrWhiteSpace(txtServer.Text))
			{
				MessageBox.Show("Please supply a server.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
				return;
			}			

			session = new RQMSession(txtServer.Text, txtUsername.Text, txtPassword.Text);

			if (!session.LogIn())
			{
				MessageBox.Show("Unable to log in to RQM.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
				return;
			}

			ThreadPool.QueueUserWorkItem(q => {
				projects = session.GetProjects().Select(a => a.Content).ToList();
				

				cbxProject.Invoke(new MethodInvoker(() => {
					cbxProject.Enabled = true;
					cbxProject.DataSource = new BindingSource(projects.ToDictionary(a => a.Title, a => a.Aliases[0].Value), null);
					cbxProject.DisplayMember = "Key";
					cbxProject.ValueMember = "Value";
				}));

				btnLogIn.Invoke(new MethodInvoker(() => {
					btnLogIn.Enabled = true;
					btnLogIn.Text = "Log Out";
				}));

				lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
			});
		}

		private void cbxProject_SelectionChangeCommitted(object sender, EventArgs e)
		{
			if (string.IsNullOrWhiteSpace(cbxProject.Text))
			{
				return;
			}

			lblLoading.Show();
			
			var project = projects.First(a => a.Aliases[0].Value == cbxProject.SelectedValue.ToString());			
			
			ThreadPool.QueueUserWorkItem(q => {
				var testplans = session.GetArtifacts<TestPlan>(project.Aliases[0].Value).Select(a => session.GetURLAs<TestPlan>(a.ID)).ToList();

				cbxTestPlan.Invoke(new MethodInvoker(() => {
					cbxTestPlan.Enabled = true;
					cbxTestPlan.DataSource = new BindingSource(testplans.ToDictionary(a => a.Title, a => a.WebId), null);
					cbxTestPlan.DisplayMember = "Key";
					cbxTestPlan.ValueMember = "Value";
				}));

				lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
			});
		}

		private void cbxTestPlan_SelectionChangeCommitted(object sender, EventArgs e)
		{
			lblLoading.Show();
			
			var project = projects.First(a => a.Aliases[0].Value == cbxProject.SelectedValue.ToString());
			
			var planID = Convert.ToInt32(cbxTestPlan.SelectedValue);

			ThreadPool.QueueUserWorkItem(q => {
				var phases = session.GetPhasesForPlan(project.Aliases[0].Value, planID);
				cbxTestPhase.Invoke(new MethodInvoker(() => {
					cbxTestPhase.Enabled = true;
					cbxTestPhase.DataSource = new BindingSource(phases.ToDictionary(a => a.Title, a => a.Identifier), null);
					cbxTestPhase.DisplayMember = "Key";
					cbxTestPhase.ValueMember = "Value";
				}));

				lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
			});
		}

		private void cbxTestPhase_SelectionChangeCommitted(object sender, EventArgs e)
		{
			numericTestcaseWebID.Invoke(new MethodInvoker(() => numericTestcaseWebID.Enabled = true));

			numericTestscriptWebID.Invoke(new MethodInvoker(() => numericTestscriptWebID.Enabled = true));
			
			btnBrowseAttachment.Invoke(new MethodInvoker(() => btnBrowseAttachment.Enabled = true));

			cbxResult.Invoke(new MethodInvoker(() => cbxResult.Enabled = true));

			txtComment.Invoke(new MethodInvoker(() => txtComment.Enabled = true));
			
			btnUpload.Invoke(new MethodInvoker(() => btnUpload.Enabled = true));
		}

		private void btnBrowseAttachment_Click(object sender, EventArgs e)
		{
			var openFileDialog1 = new OpenFileDialog { InitialDirectory = "c:\\", RestoreDirectory = true };
			
			if (openFileDialog1.ShowDialog() != DialogResult.OK)
			{
				return;
			}

			txtAttachmentPath.Text = openFileDialog1.FileName;
			Tooltip.SetToolTip(txtAttachmentPath, openFileDialog1.FileName);
		}

		private void btnUpload_Click(object sender, EventArgs e)
		{			
			var project = projects.First(a => a.Aliases[0].Value == cbxProject.SelectedValue.ToString());
			var attachmentFileName = Path.GetFileName(txtAttachmentPath.Text);
			var bytes = File.ReadAllBytes(txtAttachmentPath.Text);

			var result = Convert.ToBoolean(cbxResult.SelectedValue);

			var testPlanID = Convert.ToInt32(cbxTestPlan.SelectedValue);
			var testPhaseID = cbxTestPhase.SelectedValue.ToString();
			var testCaseID = Convert.ToInt32(numericTestcaseWebID.Value);
			var testScriptID = Convert.ToInt32(numericTestcaseWebID.Value);

			var commentText = txtComment.Text;

			lblLoading.Show();
			ThreadPool.QueueUserWorkItem(q => {
				#region Step 1 - Create / Find Configuration (environment)
				var configuration = new Configuration { Title = "QM-1845 Sample Environment", Name = "Innovate 2013 QM-1845 Environment", Summary = "Upload Execution Result demo from QM-1845" };

				var environments = session.GetArtifacts<Configuration>(project.Aliases[0].Value).Select(a => a.Content = session.GetURLAs<Configuration>(a.ID)).ToList();
				if (!environments.Any(a => a.Name == configuration.Name && a.Title == configuration.Title && a.Summary == configuration.Summary))
				{
					var r = session.Post(project.Aliases[0].Value, configuration);
					if (r.Status != UploadResultStatus.Success)
					{
						MessageBox.Show("Unable to create configuration on RQM server. Can not continue.");
						lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
						return;
					}
					configuration = session.GetURLAs<Configuration>(r.URL);
				}
				else
				{
					configuration = environments.First(a => a.Name == configuration.Name && a.Title == configuration.Title && a.Summary == configuration.Summary);
				}
				#endregion

				#region Step 2 - Upload Attachment
				var attachmentUpload = session.PostAttachment(project.Aliases[0].Value, attachmentFileName, attachmentFileName, bytes);
				if (attachmentUpload.Status != UploadResultStatus.Success)
				{
					MessageBox.Show(string.Format("Unable to upload {0} to RQM server. Can not continue.", attachmentFileName));
					lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
					return;
				}
				#endregion

				#region Step 3 - Create TestCase Execution Record (ExecutionWorkItem)
				var tplan = session.GetArtifactByWebID<TestPlan>(project.Aliases[0].Value, testPlanID);
				var tcase = session.GetArtifactByWebID<TestCase>(project.Aliases[0].Value, testCaseID);
				var tscript = session.GetArtifactByWebID<TestScript>(project.Aliases[0].Value, testScriptID);

				if (tplan == null)
				{
					MessageBox.Show("Unable to find selected test plan. Can not continue.");
					lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
					return;
				}

				if (tcase == null)
				{
					MessageBox.Show("Unable to find selected test case. Can not continue.");
					lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
					return;
				}

				if (tscript == null)
				{
					MessageBox.Show("Unable to find selected test script. Can not continue.");
					lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
					return;
				}

				var ewi = new ExecutionWorkItem {
					Title = "Sample TCER created via the API on " + DateTime.Now,
					Description = "QM-1845 Innovate 2013 sample TCER",
					CreationDate = DateTime.Now,
					Updated = DateTime.Now,
					Creator = new Creator {Text = session.User},
					Owner = new Owner {Text = session.User},
					TestCase = new HrefAndID {HREF = tcase.Identifier},
					Item = new ExecutionWorkItem_TestScript { HREF = tscript.Identifier },
					Configuration = new List<HrefAndID> { new HrefAndID { HREF = configuration.Identifier } },
					TestPhase = new HrefAndID { HREF = testPhaseID },
					TestPlan = new HrefAndID { HREF = tplan.Identifier }
				};

				var ewiUpload = session.Post(project.Aliases[0].Value, ewi);
				if (ewiUpload.Status != UploadResultStatus.Success)
				{
					if (ewiUpload.Status == UploadResultStatus.Duplicate)
					{
						ewi = session.GetURLAs<ExecutionWorkItem>(ewiUpload.ContentLocation);
						MessageBox.Show("RQM thinks this TCER is a duplicate of another. Using that one.");
					}
					else
					{
						MessageBox.Show("Unable to create TCER on RQM server. Can not continue.");
						lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
						return;
					}
				}
				else
				{
					ewi = session.GetURLAs<ExecutionWorkItem>(ewiUpload.URL);
				}
				#endregion

				#region Step 4 - Create TestCase Execution Result 
				var link = new XmlDocument();
				link.LoadXml("<div xmlns=\"http://www.w3.org/1999/xhtml\"><div><a href=\"" + attachmentUpload.URL + "\">Download the RFT log.</a></div><div><pre>" +commentText + "</pre></div></div>");

				var er = new ExecutionResult {
					Title = "QM-1845 sample testcase execution result.",
					Description = "QM-1845 Innovate 2013",
					CreationDate = DateTime.Now,
					Updated = DateTime.Now,
					StartTime = DateTime.Now,
					EndTime = DateTime.Now,
					Details = new Details { Any = new List<XmlNode> { link } },
					Creator = ewi.Creator,
					Owner = ewi.Owner,
					Testcase = ewi.TestCase,
					Item = new ExecutionResult_TestScript { HREF = ((ExecutionWorkItem_TestScript)ewi.Item).HREF },
					TestPhase = ewi.TestPhase,
					TestPlan = ewi.TestPlan,
					ApprovalState = "com.ibm.rqm.planning.common.approved",
					State = "com.ibm.rqm.execution.common.state." + (result ? "passed" : "failed"),
					Attachment = new List<HrefAndID> { new HrefAndID { HREF = attachmentUpload.URL } },
					ExecutionWorkItem = new HrefAndID { HREF = ewi.Identifier }
				};


				UploadResult erUpload = session.Post(project.Aliases[0].Value, er);
				if (erUpload.Status != UploadResultStatus.Success)
				{
					MessageBox.Show("Unable to create test case execution result on RQM server. Can not continue.");
					lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
					return;
				}
				#endregion

				#region Step 5 - Link TestCase Execution Record and Test Case Execution Result
				// reload the two before linking them
				er = session.GetURLAs<ExecutionResult>(erUpload.URL);
				ewi = session.GetURLAs<ExecutionWorkItem>(ewiUpload.URL);

				ewi.CurrentExecutionResult = new HrefAndID { HREF = er.Identifier };
				ewi.ExecutionResult = new List<HrefAndID>() { new HrefAndID { HREF = er.Identifier } };
				UploadResult ewiUpdate = session.Put(ewi, ewi.Identifier);
				if (ewiUpdate.Status != UploadResultStatus.Success)
				{
					MessageBox.Show("Unable to update test case execution record on RQM server. Can not continue.");
					lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
					return;
				}
				#endregion

				MessageBox.Show("Success! Go look in RQM!");
				lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
			});
			
		}
	}
}
