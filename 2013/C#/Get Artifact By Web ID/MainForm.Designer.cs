namespace Get.Artifact.By.Web.ID
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainForm));
            this.txtRQMPassword = new System.Windows.Forms.TextBox();
            this.txtRQMUser = new System.Windows.Forms.TextBox();
            this.lblRQMUser = new System.Windows.Forms.Label();
            this.lblRQMPassword = new System.Windows.Forms.Label();
            this.lblRQMServer = new System.Windows.Forms.Label();
            this.txtRQMServer = new System.Windows.Forms.TextBox();
            this.btnExit = new System.Windows.Forms.Button();
            this.btnLogIn = new System.Windows.Forms.Button();
            this.cbxRQMProjects = new System.Windows.Forms.ComboBox();
            this.lblRQMProject = new System.Windows.Forms.Label();
            this.lblRQMArtifactType = new System.Windows.Forms.Label();
            this.cbxRQMArtifacts = new System.Windows.Forms.ComboBox();
            this.lblRQMWebID = new System.Windows.Forms.Label();
            this.txtRQMWebID = new System.Windows.Forms.TextBox();
            this.btnDownload = new System.Windows.Forms.Button();
            this.lblLoading = new System.Windows.Forms.Label();
            this.loadingSpinner = new System.Windows.Forms.PictureBox();
            ((System.ComponentModel.ISupportInitialize)(this.loadingSpinner)).BeginInit();
            this.SuspendLayout();
            // 
            // txtRQMPassword
            // 
            this.txtRQMPassword.Location = new System.Drawing.Point(77, 36);
            this.txtRQMPassword.Name = "txtRQMPassword";
            this.txtRQMPassword.PasswordChar = '*';
            this.txtRQMPassword.Size = new System.Drawing.Size(228, 20);
            this.txtRQMPassword.TabIndex = 1;
            // 
            // txtRQMUser
            // 
            this.txtRQMUser.Location = new System.Drawing.Point(77, 10);
            this.txtRQMUser.Name = "txtRQMUser";
            this.txtRQMUser.Size = new System.Drawing.Size(228, 20);
            this.txtRQMUser.TabIndex = 0;
            // 
            // lblRQMUser
            // 
            this.lblRQMUser.AutoSize = true;
            this.lblRQMUser.Location = new System.Drawing.Point(13, 13);
            this.lblRQMUser.Name = "lblRQMUser";
            this.lblRQMUser.Size = new System.Drawing.Size(58, 13);
            this.lblRQMUser.TabIndex = 2;
            this.lblRQMUser.Text = "Username:";
            this.lblRQMUser.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            // 
            // lblRQMPassword
            // 
            this.lblRQMPassword.AutoSize = true;
            this.lblRQMPassword.Location = new System.Drawing.Point(13, 39);
            this.lblRQMPassword.Name = "lblRQMPassword";
            this.lblRQMPassword.Size = new System.Drawing.Size(56, 13);
            this.lblRQMPassword.TabIndex = 3;
            this.lblRQMPassword.Text = "Password:";
            this.lblRQMPassword.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            // 
            // lblRQMServer
            // 
            this.lblRQMServer.AutoSize = true;
            this.lblRQMServer.Location = new System.Drawing.Point(28, 64);
            this.lblRQMServer.Name = "lblRQMServer";
            this.lblRQMServer.Size = new System.Drawing.Size(41, 13);
            this.lblRQMServer.TabIndex = 4;
            this.lblRQMServer.Text = "Server:";
            this.lblRQMServer.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            // 
            // txtRQMServer
            // 
            this.txtRQMServer.Location = new System.Drawing.Point(77, 61);
            this.txtRQMServer.Name = "txtRQMServer";
            this.txtRQMServer.Size = new System.Drawing.Size(228, 20);
            this.txtRQMServer.TabIndex = 5;
            // 
            // btnExit
            // 
            this.btnExit.Location = new System.Drawing.Point(77, 88);
            this.btnExit.Name = "btnExit";
            this.btnExit.Size = new System.Drawing.Size(75, 23);
            this.btnExit.TabIndex = 6;
            this.btnExit.Text = "Exit";
            this.btnExit.UseVisualStyleBackColor = true;
            this.btnExit.Click += new System.EventHandler(this.btnExit_Click);
            // 
            // btnLogIn
            // 
            this.btnLogIn.Location = new System.Drawing.Point(230, 88);
            this.btnLogIn.Name = "btnLogIn";
            this.btnLogIn.Size = new System.Drawing.Size(75, 23);
            this.btnLogIn.TabIndex = 7;
            this.btnLogIn.Text = "Log In";
            this.btnLogIn.UseVisualStyleBackColor = true;
            this.btnLogIn.Click += new System.EventHandler(this.btnLogIn_Click);
            // 
            // cbxRQMProjects
            // 
            this.cbxRQMProjects.Enabled = false;
            this.cbxRQMProjects.FormattingEnabled = true;
            this.cbxRQMProjects.Location = new System.Drawing.Point(500, 10);
            this.cbxRQMProjects.Name = "cbxRQMProjects";
            this.cbxRQMProjects.Size = new System.Drawing.Size(215, 21);
            this.cbxRQMProjects.TabIndex = 8;
            // 
            // lblRQMProject
            // 
            this.lblRQMProject.AutoSize = true;
            this.lblRQMProject.Location = new System.Drawing.Point(451, 13);
            this.lblRQMProject.Name = "lblRQMProject";
            this.lblRQMProject.Size = new System.Drawing.Size(43, 13);
            this.lblRQMProject.TabIndex = 9;
            this.lblRQMProject.Text = "Project:";
            this.lblRQMProject.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            // 
            // lblRQMArtifactType
            // 
            this.lblRQMArtifactType.AutoSize = true;
            this.lblRQMArtifactType.Location = new System.Drawing.Point(424, 39);
            this.lblRQMArtifactType.Name = "lblRQMArtifactType";
            this.lblRQMArtifactType.Size = new System.Drawing.Size(70, 13);
            this.lblRQMArtifactType.TabIndex = 10;
            this.lblRQMArtifactType.Text = "Artifact Type:";
            this.lblRQMArtifactType.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            // 
            // cbxRQMArtifacts
            // 
            this.cbxRQMArtifacts.Enabled = false;
            this.cbxRQMArtifacts.FormattingEnabled = true;
            this.cbxRQMArtifacts.Location = new System.Drawing.Point(500, 37);
            this.cbxRQMArtifacts.Name = "cbxRQMArtifacts";
            this.cbxRQMArtifacts.Size = new System.Drawing.Size(215, 21);
            this.cbxRQMArtifacts.TabIndex = 11;
            // 
            // lblRQMWebID
            // 
            this.lblRQMWebID.AutoSize = true;
            this.lblRQMWebID.Location = new System.Drawing.Point(433, 64);
            this.lblRQMWebID.Name = "lblRQMWebID";
            this.lblRQMWebID.Size = new System.Drawing.Size(61, 13);
            this.lblRQMWebID.TabIndex = 12;
            this.lblRQMWebID.Text = "Web UI ID:";
            this.lblRQMWebID.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
            // 
            // txtRQMWebID
            // 
            this.txtRQMWebID.Enabled = false;
            this.txtRQMWebID.Location = new System.Drawing.Point(500, 61);
            this.txtRQMWebID.Name = "txtRQMWebID";
            this.txtRQMWebID.Size = new System.Drawing.Size(215, 20);
            this.txtRQMWebID.TabIndex = 13;
            // 
            // btnDownload
            // 
            this.btnDownload.Enabled = false;
            this.btnDownload.Location = new System.Drawing.Point(500, 88);
            this.btnDownload.Name = "btnDownload";
            this.btnDownload.Size = new System.Drawing.Size(75, 23);
            this.btnDownload.TabIndex = 14;
            this.btnDownload.Text = "Download";
            this.btnDownload.UseVisualStyleBackColor = true;
            this.btnDownload.Click += new System.EventHandler(this.btnDownload_Click);
            // 
            // lblLoading
            // 
            this.lblLoading.AutoSize = true;
            this.lblLoading.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblLoading.Location = new System.Drawing.Point(108, 118);
            this.lblLoading.Name = "lblLoading";
            this.lblLoading.Size = new System.Drawing.Size(93, 24);
            this.lblLoading.TabIndex = 15;
            this.lblLoading.Text = "Loading...";
            this.lblLoading.Visible = false;
            // 
            // loadingSpinner
            // 
            this.loadingSpinner.Image = ((System.Drawing.Image)(resources.GetObject("loadingSpinner.Image")));
            this.loadingSpinner.Location = new System.Drawing.Point(77, 117);
            this.loadingSpinner.Name = "loadingSpinner";
            this.loadingSpinner.Size = new System.Drawing.Size(25, 25);
            this.loadingSpinner.TabIndex = 16;
            this.loadingSpinner.TabStop = false;
            this.loadingSpinner.Visible = false;
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.Window;
            this.ClientSize = new System.Drawing.Size(763, 181);
            this.Controls.Add(this.loadingSpinner);
            this.Controls.Add(this.lblLoading);
            this.Controls.Add(this.btnDownload);
            this.Controls.Add(this.txtRQMWebID);
            this.Controls.Add(this.lblRQMWebID);
            this.Controls.Add(this.cbxRQMArtifacts);
            this.Controls.Add(this.lblRQMArtifactType);
            this.Controls.Add(this.lblRQMProject);
            this.Controls.Add(this.cbxRQMProjects);
            this.Controls.Add(this.btnLogIn);
            this.Controls.Add(this.btnExit);
            this.Controls.Add(this.txtRQMServer);
            this.Controls.Add(this.lblRQMServer);
            this.Controls.Add(this.lblRQMPassword);
            this.Controls.Add(this.lblRQMUser);
            this.Controls.Add(this.txtRQMUser);
            this.Controls.Add(this.txtRQMPassword);
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "MainForm";
            this.Text = "Get RQM Artifact By Web ID";
            ((System.ComponentModel.ISupportInitialize)(this.loadingSpinner)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

		}

		#endregion

		private System.Windows.Forms.TextBox txtRQMPassword;
		private System.Windows.Forms.TextBox txtRQMUser;
		private System.Windows.Forms.Label lblRQMUser;
		private System.Windows.Forms.Label lblRQMPassword;
		private System.Windows.Forms.Label lblRQMServer;
		private System.Windows.Forms.TextBox txtRQMServer;
		private System.Windows.Forms.Button btnExit;
		private System.Windows.Forms.Button btnLogIn;
		private System.Windows.Forms.ComboBox cbxRQMProjects;
		private System.Windows.Forms.Label lblRQMProject;
		private System.Windows.Forms.Label lblRQMArtifactType;
		private System.Windows.Forms.ComboBox cbxRQMArtifacts;
		private System.Windows.Forms.Label lblRQMWebID;
		private System.Windows.Forms.TextBox txtRQMWebID;
		private System.Windows.Forms.Button btnDownload;
		private System.Windows.Forms.Label lblLoading;
        private System.Windows.Forms.PictureBox loadingSpinner;
	}
}

