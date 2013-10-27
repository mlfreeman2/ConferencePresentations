namespace Upload.Result
{
	partial class MainForm
	{
		/// <summary>
		/// Required designer variable.
		/// </summary>
		private System.ComponentModel.IContainer components = null;

		/// <summary>
		/// Clean up any resources being used.
		/// </summary>
		/// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
		protected override void Dispose(bool disposing)
		{
			if (disposing && (components != null))
			{
				components.Dispose();
			}
			base.Dispose(disposing);
		}

		#region Windows Form Designer generated code

		/// <summary>
		/// Required method for Designer support - do not modify
		/// the contents of this method with the code editor.
		/// </summary>
		private void InitializeComponent()
		{
			this.components = new System.ComponentModel.Container();
			this.txtPassword = new System.Windows.Forms.TextBox();
			this.txtUsername = new System.Windows.Forms.TextBox();
			this.lblUsername = new System.Windows.Forms.Label();
			this.lblPassword = new System.Windows.Forms.Label();
			this.lblServer = new System.Windows.Forms.Label();
			this.txtServer = new System.Windows.Forms.TextBox();
			this.btnExit = new System.Windows.Forms.Button();
			this.btnLogIn = new System.Windows.Forms.Button();
			this.cbxProject = new System.Windows.Forms.ComboBox();
			this.lblProject = new System.Windows.Forms.Label();
			this.lblTestPlan = new System.Windows.Forms.Label();
			this.cbxTestPlan = new System.Windows.Forms.ComboBox();
			this.lblTestcaseWebID = new System.Windows.Forms.Label();
			this.btnUpload = new System.Windows.Forms.Button();
			this.lblLoading = new System.Windows.Forms.Label();
			this.cbxTestPhase = new System.Windows.Forms.ComboBox();
			this.lblTestPhase = new System.Windows.Forms.Label();
			this.txtAttachmentPath = new System.Windows.Forms.TextBox();
			this.txtComment = new System.Windows.Forms.TextBox();
			this.lblAttachmentPath = new System.Windows.Forms.Label();
			this.btnBrowseAttachment = new System.Windows.Forms.Button();
			this.label1 = new System.Windows.Forms.Label();
			this.numericTestcaseWebID = new System.Windows.Forms.NumericUpDown();
			this.Tooltip = new System.Windows.Forms.ToolTip(this.components);
			this.cbxResult = new System.Windows.Forms.ComboBox();
			this.lblResult = new System.Windows.Forms.Label();
			this.numericTestscriptWebID = new System.Windows.Forms.NumericUpDown();
			this.lblTestscriptWebID = new System.Windows.Forms.Label();
			((System.ComponentModel.ISupportInitialize)(this.numericTestcaseWebID)).BeginInit();
			((System.ComponentModel.ISupportInitialize)(this.numericTestscriptWebID)).BeginInit();
			this.SuspendLayout();
			// 
			// txtPassword
			// 
			this.txtPassword.Location = new System.Drawing.Point(77, 36);
			this.txtPassword.Name = "txtPassword";
			this.txtPassword.PasswordChar = '*';
			this.txtPassword.Size = new System.Drawing.Size(228, 20);
			this.txtPassword.TabIndex = 1;
			// 
			// txtUsername
			// 
			this.txtUsername.Location = new System.Drawing.Point(77, 10);
			this.txtUsername.Name = "txtUsername";
			this.txtUsername.Size = new System.Drawing.Size(228, 20);
			this.txtUsername.TabIndex = 0;
			// 
			// lblUsername
			// 
			this.lblUsername.AutoSize = true;
			this.lblUsername.Location = new System.Drawing.Point(13, 13);
			this.lblUsername.Name = "lblUsername";
			this.lblUsername.Size = new System.Drawing.Size(58, 13);
			this.lblUsername.TabIndex = 2;
			this.lblUsername.Text = "Username:";
			this.lblUsername.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// lblPassword
			// 
			this.lblPassword.AutoSize = true;
			this.lblPassword.Location = new System.Drawing.Point(13, 39);
			this.lblPassword.Name = "lblPassword";
			this.lblPassword.Size = new System.Drawing.Size(56, 13);
			this.lblPassword.TabIndex = 3;
			this.lblPassword.Text = "Password:";
			this.lblPassword.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// lblServer
			// 
			this.lblServer.AutoSize = true;
			this.lblServer.Location = new System.Drawing.Point(28, 64);
			this.lblServer.Name = "lblServer";
			this.lblServer.Size = new System.Drawing.Size(41, 13);
			this.lblServer.TabIndex = 4;
			this.lblServer.Text = "Server:";
			this.lblServer.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// txtServer
			// 
			this.txtServer.Location = new System.Drawing.Point(77, 61);
			this.txtServer.Name = "txtServer";
			this.txtServer.Size = new System.Drawing.Size(228, 20);
			this.txtServer.TabIndex = 2;
			// 
			// btnExit
			// 
			this.btnExit.Location = new System.Drawing.Point(77, 88);
			this.btnExit.Name = "btnExit";
			this.btnExit.Size = new System.Drawing.Size(75, 23);
			this.btnExit.TabIndex = 4;
			this.btnExit.Text = "Exit";
			this.btnExit.UseVisualStyleBackColor = true;
			this.btnExit.Click += new System.EventHandler(this.btnExit_Click);
			// 
			// btnLogIn
			// 
			this.btnLogIn.Location = new System.Drawing.Point(230, 88);
			this.btnLogIn.Name = "btnLogIn";
			this.btnLogIn.Size = new System.Drawing.Size(75, 23);
			this.btnLogIn.TabIndex = 3;
			this.btnLogIn.Text = "Log In";
			this.btnLogIn.UseVisualStyleBackColor = true;
			this.btnLogIn.Click += new System.EventHandler(this.btnLogIn_Click);
			// 
			// cbxProject
			// 
			this.cbxProject.Enabled = false;
			this.cbxProject.FormattingEnabled = true;
			this.cbxProject.Location = new System.Drawing.Point(500, 10);
			this.cbxProject.Name = "cbxProject";
			this.cbxProject.Size = new System.Drawing.Size(215, 21);
			this.cbxProject.TabIndex = 5;
			this.cbxProject.SelectionChangeCommitted += new System.EventHandler(this.cbxProject_SelectionChangeCommitted);
			// 
			// lblProject
			// 
			this.lblProject.AutoSize = true;
			this.lblProject.Location = new System.Drawing.Point(451, 13);
			this.lblProject.Name = "lblProject";
			this.lblProject.Size = new System.Drawing.Size(43, 13);
			this.lblProject.TabIndex = 9;
			this.lblProject.Text = "Project:";
			this.lblProject.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// lblTestPlan
			// 
			this.lblTestPlan.AutoSize = true;
			this.lblTestPlan.Location = new System.Drawing.Point(439, 40);
			this.lblTestPlan.Name = "lblTestPlan";
			this.lblTestPlan.Size = new System.Drawing.Size(55, 13);
			this.lblTestPlan.TabIndex = 10;
			this.lblTestPlan.Text = "Test Plan:";
			this.lblTestPlan.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// cbxTestPlan
			// 
			this.cbxTestPlan.Enabled = false;
			this.cbxTestPlan.FormattingEnabled = true;
			this.cbxTestPlan.Location = new System.Drawing.Point(500, 37);
			this.cbxTestPlan.Name = "cbxTestPlan";
			this.cbxTestPlan.Size = new System.Drawing.Size(215, 21);
			this.cbxTestPlan.TabIndex = 6;
			this.cbxTestPlan.SelectionChangeCommitted += new System.EventHandler(this.cbxTestPlan_SelectionChangeCommitted);
			// 
			// lblTestcaseWebID
			// 
			this.lblTestcaseWebID.AutoSize = true;
			this.lblTestcaseWebID.Location = new System.Drawing.Point(387, 95);
			this.lblTestcaseWebID.Name = "lblTestcaseWebID";
			this.lblTestcaseWebID.Size = new System.Drawing.Size(108, 13);
			this.lblTestcaseWebID.TabIndex = 12;
			this.lblTestcaseWebID.Text = "Testcase Web UI ID:";
			this.lblTestcaseWebID.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// btnUpload
			// 
			this.btnUpload.Enabled = false;
			this.btnUpload.Location = new System.Drawing.Point(500, 282);
			this.btnUpload.Name = "btnUpload";
			this.btnUpload.Size = new System.Drawing.Size(75, 23);
			this.btnUpload.TabIndex = 14;
			this.btnUpload.Text = "Upload";
			this.btnUpload.UseVisualStyleBackColor = true;
			this.btnUpload.Click += new System.EventHandler(this.btnUpload_Click);
			// 
			// lblLoading
			// 
			this.lblLoading.Font = new System.Drawing.Font("Microsoft Sans Serif", 20.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.lblLoading.Location = new System.Drawing.Point(10, 154);
			this.lblLoading.Name = "lblLoading";
			this.lblLoading.Size = new System.Drawing.Size(376, 154);
			this.lblLoading.TabIndex = 15;
			this.lblLoading.Text = "Loading...";
			this.lblLoading.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
			this.lblLoading.Visible = false;
			// 
			// cbxTestPhase
			// 
			this.cbxTestPhase.Enabled = false;
			this.cbxTestPhase.FormattingEnabled = true;
			this.cbxTestPhase.Location = new System.Drawing.Point(500, 65);
			this.cbxTestPhase.Name = "cbxTestPhase";
			this.cbxTestPhase.Size = new System.Drawing.Size(215, 21);
			this.cbxTestPhase.TabIndex = 7;
			this.cbxTestPhase.SelectionChangeCommitted += new System.EventHandler(this.cbxTestPhase_SelectionChangeCommitted);
			// 
			// lblTestPhase
			// 
			this.lblTestPhase.AutoSize = true;
			this.lblTestPhase.Location = new System.Drawing.Point(430, 68);
			this.lblTestPhase.Name = "lblTestPhase";
			this.lblTestPhase.Size = new System.Drawing.Size(64, 13);
			this.lblTestPhase.TabIndex = 17;
			this.lblTestPhase.Text = "Test Phase:";
			this.lblTestPhase.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// txtAttachmentPath
			// 
			this.txtAttachmentPath.Enabled = false;
			this.txtAttachmentPath.Location = new System.Drawing.Point(501, 148);
			this.txtAttachmentPath.Name = "txtAttachmentPath";
			this.txtAttachmentPath.Size = new System.Drawing.Size(215, 20);
			this.txtAttachmentPath.TabIndex = 10;
			// 
			// txtComment
			// 
			this.txtComment.Enabled = false;
			this.txtComment.Location = new System.Drawing.Point(500, 201);
			this.txtComment.Multiline = true;
			this.txtComment.Name = "txtComment";
			this.txtComment.Size = new System.Drawing.Size(215, 75);
			this.txtComment.TabIndex = 13;
			// 
			// lblAttachmentPath
			// 
			this.lblAttachmentPath.AutoSize = true;
			this.lblAttachmentPath.Location = new System.Drawing.Point(406, 151);
			this.lblAttachmentPath.Name = "lblAttachmentPath";
			this.lblAttachmentPath.Size = new System.Drawing.Size(89, 13);
			this.lblAttachmentPath.TabIndex = 20;
			this.lblAttachmentPath.Text = "Attachment Path:";
			this.lblAttachmentPath.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// btnBrowseAttachment
			// 
			this.btnBrowseAttachment.Enabled = false;
			this.btnBrowseAttachment.Location = new System.Drawing.Point(722, 148);
			this.btnBrowseAttachment.Name = "btnBrowseAttachment";
			this.btnBrowseAttachment.Size = new System.Drawing.Size(75, 23);
			this.btnBrowseAttachment.TabIndex = 11;
			this.btnBrowseAttachment.Text = "Browse";
			this.btnBrowseAttachment.UseVisualStyleBackColor = true;
			this.btnBrowseAttachment.Click += new System.EventHandler(this.btnBrowseAttachment_Click);
			// 
			// label1
			// 
			this.label1.AutoSize = true;
			this.label1.Location = new System.Drawing.Point(441, 231);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(54, 13);
			this.label1.TabIndex = 22;
			this.label1.Text = "Comment:";
			this.label1.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// numericTestcaseWebID
			// 
			this.numericTestcaseWebID.Enabled = false;
			this.numericTestcaseWebID.Location = new System.Drawing.Point(501, 93);
			this.numericTestcaseWebID.Maximum = new decimal(new int[] {
            1048576,
            0,
            0,
            0});
			this.numericTestcaseWebID.Name = "numericTestcaseWebID";
			this.numericTestcaseWebID.Size = new System.Drawing.Size(214, 20);
			this.numericTestcaseWebID.TabIndex = 8;
			// 
			// cbxResult
			// 
			this.cbxResult.Enabled = false;
			this.cbxResult.FormattingEnabled = true;
			this.cbxResult.Location = new System.Drawing.Point(501, 174);
			this.cbxResult.Name = "cbxResult";
			this.cbxResult.Size = new System.Drawing.Size(214, 21);
			this.cbxResult.TabIndex = 12;
			// 
			// lblResult
			// 
			this.lblResult.AutoSize = true;
			this.lblResult.Location = new System.Drawing.Point(455, 177);
			this.lblResult.Name = "lblResult";
			this.lblResult.Size = new System.Drawing.Size(40, 13);
			this.lblResult.TabIndex = 24;
			this.lblResult.Text = "Result:";
			this.lblResult.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// numericTestscriptWebID
			// 
			this.numericTestscriptWebID.Enabled = false;
			this.numericTestscriptWebID.Location = new System.Drawing.Point(501, 120);
			this.numericTestscriptWebID.Maximum = new decimal(new int[] {
            1048576,
            0,
            0,
            0});
			this.numericTestscriptWebID.Name = "numericTestscriptWebID";
			this.numericTestscriptWebID.Size = new System.Drawing.Size(214, 20);
			this.numericTestscriptWebID.TabIndex = 25;
			// 
			// lblTestscriptWebID
			// 
			this.lblTestscriptWebID.AutoSize = true;
			this.lblTestscriptWebID.Location = new System.Drawing.Point(385, 122);
			this.lblTestscriptWebID.Name = "lblTestscriptWebID";
			this.lblTestscriptWebID.Size = new System.Drawing.Size(110, 13);
			this.lblTestscriptWebID.TabIndex = 26;
			this.lblTestscriptWebID.Text = "Testscript Web UI ID:";
			this.lblTestscriptWebID.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
			// 
			// MainForm
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(803, 317);
			this.Controls.Add(this.lblTestscriptWebID);
			this.Controls.Add(this.numericTestscriptWebID);
			this.Controls.Add(this.lblResult);
			this.Controls.Add(this.cbxResult);
			this.Controls.Add(this.numericTestcaseWebID);
			this.Controls.Add(this.label1);
			this.Controls.Add(this.btnBrowseAttachment);
			this.Controls.Add(this.lblAttachmentPath);
			this.Controls.Add(this.txtComment);
			this.Controls.Add(this.txtAttachmentPath);
			this.Controls.Add(this.lblTestPhase);
			this.Controls.Add(this.cbxTestPhase);
			this.Controls.Add(this.lblLoading);
			this.Controls.Add(this.btnUpload);
			this.Controls.Add(this.lblTestcaseWebID);
			this.Controls.Add(this.cbxTestPlan);
			this.Controls.Add(this.lblTestPlan);
			this.Controls.Add(this.lblProject);
			this.Controls.Add(this.cbxProject);
			this.Controls.Add(this.btnLogIn);
			this.Controls.Add(this.btnExit);
			this.Controls.Add(this.txtServer);
			this.Controls.Add(this.lblServer);
			this.Controls.Add(this.lblPassword);
			this.Controls.Add(this.lblUsername);
			this.Controls.Add(this.txtUsername);
			this.Controls.Add(this.txtPassword);
			this.MaximizeBox = false;
			this.MinimizeBox = false;
			this.Name = "MainForm";
			this.Text = "Upload Execution Result";
			((System.ComponentModel.ISupportInitialize)(this.numericTestcaseWebID)).EndInit();
			((System.ComponentModel.ISupportInitialize)(this.numericTestscriptWebID)).EndInit();
			this.ResumeLayout(false);
			this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.TextBox txtPassword;
		private System.Windows.Forms.TextBox txtUsername;
		private System.Windows.Forms.Label lblUsername;
		private System.Windows.Forms.Label lblPassword;
		private System.Windows.Forms.Label lblServer;
		private System.Windows.Forms.TextBox txtServer;
		private System.Windows.Forms.Button btnExit;
		private System.Windows.Forms.Button btnLogIn;
		private System.Windows.Forms.ComboBox cbxProject;
		private System.Windows.Forms.Label lblProject;
		private System.Windows.Forms.Label lblTestPlan;
		private System.Windows.Forms.ComboBox cbxTestPlan;
		private System.Windows.Forms.Label lblTestcaseWebID;
		private System.Windows.Forms.Button btnUpload;
		private System.Windows.Forms.Label lblLoading;
		private System.Windows.Forms.ComboBox cbxTestPhase;
		private System.Windows.Forms.Label lblTestPhase;
		private System.Windows.Forms.TextBox txtAttachmentPath;
		private System.Windows.Forms.TextBox txtComment;
		private System.Windows.Forms.Label lblAttachmentPath;
		private System.Windows.Forms.Button btnBrowseAttachment;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.NumericUpDown numericTestcaseWebID;
		private System.Windows.Forms.ToolTip Tooltip;
		private System.Windows.Forms.ComboBox cbxResult;
		private System.Windows.Forms.Label lblResult;
		private System.Windows.Forms.NumericUpDown numericTestscriptWebID;
		private System.Windows.Forms.Label lblTestscriptWebID;
	}
}

