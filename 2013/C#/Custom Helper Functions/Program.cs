using System;
using System.IO;
using System.Linq;
using RQM.API.ObjectModel;
using log4net.Config;
using RQM.API;

namespace Custom.Helper.Functions
{
	public static class Program
	{
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

			var rqmSession = new RQMSession(server, user, password);

			if (!rqmSession.LogIn())
			{
				Console.BackgroundColor = ConsoleColor.Red;
				Console.ForegroundColor = ConsoleColor.White;
				Console.WriteLine("Unable to log in to RQM at {0} as \"{1}\".", server, user);
				Console.ReadKey();
				Console.ResetColor();
				return;
			}
			Console.WriteLine();
			
			Console.ForegroundColor = ConsoleColor.DarkGray;
			Console.BackgroundColor = ConsoleColor.Yellow;
			Console.WriteLine("The log4net logging output is sent to a file in this example.");
			
			Console.ResetColor();

			// 1. show the user a project list so they can pick a project for use with the later parts of this example.
			Console.WriteLine("Available Projects");
			var projects = rqmSession.GetProjects().Select(a => a.Content).ToList();
			for (var i = 0; i < projects.Count; i++)
			{
				var p = projects[i];
				Console.WriteLine("{0}: {1}", i, p.Title);
				Console.WriteLine("(API Name: {0})", p.Aliases[0].Value);
				Console.WriteLine();
			}

			Console.Write("Please enter the index of the project you want to work with (0-{0}):", projects.Count-1);
			var projectChoice = Convert.ToInt32(Console.ReadLine());
			var project = projects[projectChoice];
			var projectAPIName = project.Aliases[0].Value;
			var projectUIName = project.Title;
			Console.WriteLine("Chosen Project: #{0}: {1} (API Name: {2})", projectChoice, projectUIName, projectAPIName);

			var testPlans = rqmSession.GetArtifacts<TestPlan>(projectAPIName).Select(a => a.ID).ToList();
			Console.WriteLine("Test Plans:");
			for (var i = 0; i < Math.Min(testPlans.Count, 3); i++)
			{
				var testPlan = rqmSession.GetURLAs<TestPlan>(testPlans[i]);
				Console.WriteLine("{0}: {1}", i, testPlan.Title);
				Console.WriteLine("Test Phases:");
				var phases = rqmSession.GetPhasesForPlan(projectAPIName, testPlan.WebId);
				foreach (var phase in phases)
				{
					Console.WriteLine("\t{0}", phase.Title);
				}
				Console.WriteLine();
			}

			var testcases = rqmSession.GetArtifacts<TestCase>(projectAPIName).Select(a => a.ID).ToList();
			Console.WriteLine("{0} testcases in the project.", testcases.Count);
			for (var i = 0; i < Math.Min(testcases.Count, 3); i++)
			{
				var testcase = rqmSession.GetURLAs<TestCase>(testcases[i]);

				Console.WriteLine("{0}: {1}", i, testcase.Title);
				Console.WriteLine("Test Scripts under this Test Case:");
				var scripts = rqmSession.GetScriptsForTestCase(projectAPIName, testcase.WebId);
				foreach (var script in scripts)
				{
					Console.WriteLine("\t{0}", script.Title);
				}
				Console.WriteLine();
			}

			Console.Write("Please enter the UI ID of a testcase to download:");
			var tcID = Convert.ToInt32(Console.ReadLine());
			Console.WriteLine("Attempting to download Testcase #{0}", tcID);
			var tc1 = rqmSession.GetArtifactByWebID<TestCase>(projectAPIName, tcID);
			Console.WriteLine("Testcase #{0} Name: \"{1}\"", tc1.WebId, tc1.Title);
			Console.WriteLine("Testcase #{0} ID: \"{1}\"", tc1.WebId, tc1.Identifier);

			Console.WriteLine();
			Console.WriteLine("Done");
			Console.ReadKey();
		}
	}
}