using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Windows.Forms;
using System.Xml;
using RQM.API;
using RQM.API.ObjectModel;
using log4net.Config;

namespace Get.Artifact.By.Web.ID
{
	public partial class MainForm : Form
	{
		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main()
		{
			// set up log4net
			XmlConfigurator.Configure(new FileInfo("log4net.config"));

			Application.EnableVisualStyles();
			Application.SetCompatibleTextRenderingDefault(false);
			Application.Run(new MainForm());
		}

		private RQMSession session = null;

		private List<Project> projects = null; 

		public MainForm()
		{
			InitializeComponent();

			var artifacts = new Dictionary<string, string> {
				{"Test Plan", "RQM.API.ObjectModel.TestPlan, RQM.API"},
				{"Test Case", "RQM.API.ObjectModel.TestCase, RQM.API"},
				{"Test Script", "RQM.API.ObjectModel.TestScript, RQM.API"},
				{"Remote Script", "RQM.API.ObjectModel.RemoteScript, RQM.API"},
				{"Keyword", "RQM.API.ObjectModel.Keyword, RQM.API"},
				{"Test Suite", "RQM.API.ObjectModel.TestSuite, RQM.API"},
				{"Test Suite Log", "RQM.API.ObjectModel.TestSuiteLog, RQM.API"},
				{"Execution Work Item", "RQM.API.ObjectModel.ExecutionWorkItem, RQM.API"},
				{"Suite Execution Record", "RQM.API.ObjectModel.SuiteExecutionRecord, RQM.API"},
				{"Execution Result", "RQM.API.ObjectModel.ExecutionResult, RQM.API"},
				{"Requirement", "RQM.API.ObjectModel.Requirement, RQM.API"},
				{"Request", "RQM.API.ObjectModel.Request, RQM.API"},
				{"Reservation", "RQM.API.ObjectModel.Reservation, RQM.API"},
				{"Work Item", "RQM.API.ObjectModel.WorkItem, RQM.API"},
				{"Tool Adapter", "RQM.API.ObjectModel.ToolAdapter, RQM.API"},
				{"Execution Sequence", "RQM.API.ObjectModel.ExecutionSequence, RQM.API"},
				{"Job Scheduler", "RQM.API.ObjectModel.JobScheduler, RQM.API"}
			};

			cbxRQMArtifacts.DataSource = new BindingSource(artifacts, null);
			cbxRQMArtifacts.DisplayMember = "Key";
			cbxRQMArtifacts.ValueMember = "Value";

			txtRQMUser.Select();
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
				cbxRQMArtifacts.Enabled = false;
				txtRQMWebID.Enabled = false;
				btnDownload.Enabled = false;
				return;
			}

			lblLoading.Show();
			loadingSpinner.Show();
			btnLogIn.Enabled = false;

			if (string.IsNullOrWhiteSpace(txtRQMUser.Text))
			{
				MessageBox.Show("Please supply a user name.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
				return;
			}

			if (string.IsNullOrWhiteSpace(txtRQMPassword.Text))
			{
				MessageBox.Show("Please supply a password.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
				return;
			}

			if (string.IsNullOrWhiteSpace(txtRQMServer.Text))
			{
				MessageBox.Show("Please supply a server.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
				return;
			}

			var server = txtRQMServer.Text;
			var user = txtRQMUser.Text;
			var password = txtRQMPassword.Text;

			ThreadPool.QueueUserWorkItem(q => {
				session = new RQMSession(server, user, password);

				if (!session.LogIn())
				{
					MessageBox.Show("Unable to log in to RQM.", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
					return;
				}

				projects = session.GetProjects().Select(a => a.Content).ToList();

				cbxRQMProjects.Invoke(new MethodInvoker(() => {
					cbxRQMProjects.Enabled = true;
					cbxRQMProjects.DataSource = new BindingSource(projects.ToDictionary(a => a.Title, a => a.Aliases[0].Value), null);
					cbxRQMProjects.DisplayMember = "Key";
					cbxRQMProjects.ValueMember = "Value";
				}));

				cbxRQMArtifacts.Invoke(new MethodInvoker(() => cbxRQMArtifacts.Enabled = true));
				txtRQMWebID.Invoke(new MethodInvoker(() => txtRQMWebID.Enabled = true));
				btnDownload.Invoke(new MethodInvoker(() => btnDownload.Enabled = true));

				btnLogIn.Invoke(new MethodInvoker(() => {
					btnLogIn.Enabled = true;
					btnLogIn.Text = "Log Out";
				}));

				lblLoading.Invoke(new MethodInvoker(() => lblLoading.Hide()));
				loadingSpinner.Invoke(new MethodInvoker(() => loadingSpinner.Hide()));
			});

		}

		private void btnDownload_Click(object sender, EventArgs e)
		{
			if (session == null || session.Identity() == null)
			{
				MessageBox.Show("Please log out and log back in to RQM.");
				return;
			}

			var project = cbxRQMProjects.SelectedValue.ToString();
			var type = System.Type.GetType(cbxRQMArtifacts.SelectedValue.ToString());
			var artifact = session.GetArtifactByWebID(project, type, Convert.ToInt32(txtRQMWebID.Text));
			if (artifact == null)
			{
				MessageBox.Show(string.Format("{0} ID {1} does not exist.", cbxRQMArtifacts.Text, txtRQMWebID.Text));
				return;
			}
			var doc = new XmlDocument();
			doc.LoadXml(artifact);

			var saveFileDialog1 = new SaveFileDialog {
				Filter = "XML Document|*.xml",
				Title = "Save As"
			};
			saveFileDialog1.ShowDialog();

			// If the file name is not an empty string open it for saving.
			if (saveFileDialog1.FileName == "")
			{
				return;
			}

			using (var fs = (FileStream) saveFileDialog1.OpenFile())
			{
				doc.Save(fs);
			}			
		}
	}
}
