using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApplication15
{
    class Manager
    {
        private Timer timer;
        private IOmanager io;
        private string id;

        public Manager()
        {
            timer = new Timer();
            io = new IOmanager();

            RequestId();
            if (!id.Equals("NULL")) { return; }

            while (!Login()) { }

            RequestId();
        }

        private void RequestId()
        {
            id = io.Command("id")[0];
        }

        private Boolean Login()
        {
            Console.WriteLine("<DataBase Login>");
            Console.Write("id : ");       io.Command(Console.ReadLine());
            Console.Write("password : "); io.Command(Console.ReadLine());
            
            string msg = io.Command("Event")[0];
            Console.WriteLine(msg);
            switch(msg)
            {
                case "Done": return true;
                default    : return false;
            }
        }

        public void Run()
        {
            try
            {
                Console.WriteLine("=============================");
                Console.WriteLine("* Welcome Cryptonite Server *");
                Console.WriteLine("* Version : Beta 1.0.0      *");
                Console.WriteLine("=============================");
                bool exitFlag = false;
                while (true)
                {
                    if (!exitFlag) { Console.Write("\n[" + id + "]" + " > "); }
                    else { exitFlag = false; }

                    String query = Console.ReadLine();                    
                    if (query.Length < 1) continue;

                    string[] result = io.Command(query);
                    if (result == null) { continue; }
                    else if (result[0].Equals("--yid")) { Environment.Exit(1); }

                    foreach (string msg in result)
                    {
                        Console.Write(msg);

                        if (msg.Equals("<y/n> : ")) { exitFlag = true; }
                        else { Console.WriteLine(); }
                    }
                }
            }
            catch (Exception e)
            {
                Environment.Exit(1);
            }
        }
    }
}
