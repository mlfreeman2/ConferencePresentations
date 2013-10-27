using System;
using System.IO;
using System.Text;
using System.Xml;

namespace RQM.API
{
	public class UTF8StringWriter : StringWriter
	{
		public override Encoding Encoding
		{
			get { return Encoding.UTF8; }
		}
	}

	public static class StringFunctions
	{
		// reads a password from the keyboard and prints asterisks in its place to the console
		public static string ReadPassword()
		{
			StringBuilder password = new StringBuilder();
			ConsoleKeyInfo info = Console.ReadKey(true);
			while (info.Key != ConsoleKey.Enter)
			{
				if (info.Key != ConsoleKey.Backspace)
				{
					Console.Write("*");
					password.Append(info.KeyChar);
				}
				else
				{
					if (password != null && password.Length > 0)
					{
						// remove one character from the list of password characters
						password.Remove(password.Length - 1, 1);
						// get the location of the cursor
						int pos = Console.CursorLeft;
						// move the cursor to the left by one character
						Console.SetCursorPosition(pos - 1, Console.CursorTop);
						// replace it with space
						Console.Write(" ");
						// move the cursor to the left by one character again
						Console.SetCursorPosition(pos - 1, Console.CursorTop);
					}
				}
				info = Console.ReadKey(true);
			}

			// add a new line because user pressed enter at the end of their password
			Console.WriteLine();
			return password.ToString();
		}

		public static string ScrubCharacters(string original)
		{
			original = original.Replace('\u00A0', ' ');
			original = original.Replace('\u2013', '-');
			original = original.Replace('\u2014', '-');
			original = original.Replace('\u0092', '\'');
			original = original.Replace('\u201C', '"');
			original = original.Replace('\u201D', '"');
			return original;
		}

		public static string URLEncode(params string[] postValues)
		{
			var postData = new StringBuilder();

			var lim = postValues.Length % 2 == 0 ? postValues.Length : postValues.Length - 1;
			for (var i = 0; i < lim; i += 2)
			{
				if (string.IsNullOrWhiteSpace(postValues[i]) || string.IsNullOrWhiteSpace(postValues[i + 1]))
				{
					continue;
				}
				postData.Append(Uri.EscapeUriString(postValues[i]));
				postData.Append("=");
				postData.Append(Uri.EscapeUriString(postValues[i + 1]));
				postData.Append("&");
			}
			string final = postData.ToString();
			if (final.EndsWith("&"))
			{
				final = final.TrimEnd('&');
			}
			return final;
		}
	}
}
