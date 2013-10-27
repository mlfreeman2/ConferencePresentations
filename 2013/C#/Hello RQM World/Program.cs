using System;
using System.IO;
using System.Linq;
using RQM.API;
using RQM.API.ObjectModel;
using log4net.Config;

namespace Hello.RQM.World
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

			var rqmIdentity = rs.Identity();

			var fc = Console.ForegroundColor;
			Console.ForegroundColor = ConsoleColor.Green;
			Console.WriteLine("Logged in as \"{0}\".", rqmIdentity.UserID);
			Console.WriteLine("Roles:");		
			rqmIdentity.Roles.ToList().ForEach(a => Console.WriteLine("\t{0}", a));
			
			// show the user a project list too
			Console.ForegroundColor = fc;
			
			Console.WriteLine();
			var projects = rs.GetProjects().Select(a => a.Content).ToList();

			Console.ForegroundColor = ConsoleColor.Blue;
			Console.WriteLine("Available Projects");
			for (var i = 0; i < projects.Count; i++)
			{
				var p = projects[i];
				Console.WriteLine("{0}: {1}", i, p.Title);
				Console.WriteLine("(API Name: {0})", p.Aliases[0].Value);
				Console.WriteLine();
			}
			
			Console.WriteLine();
			Console.WriteLine("Done");
			Console.ReadKey();
		}
	}
}