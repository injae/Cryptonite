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
        private IOmanger io;
        private string id;

        public Manager()
        {
            timer = new Timer();
            io = new IOmanger();

            id = io.Command("id")[0];
        }

        public void Run()
        {
            Console.WriteLine("=============================");
            Console.WriteLine("* Welcome Cryptonite Server *");
            Console.WriteLine("* Version : Beta 1.0.0      *");
            Console.WriteLine("=============================");
           
            while(true)
            {
                Console.Write("[" + id + "]" + " > ");
                String query = Console.ReadLine();
                if (query.Length < 1) continue;
                string[] result = io.Command(query);
                if(result == null) { continue; }

                foreach (string msg in result)
                {
                    Console.WriteLine(msg);
                }
            }
        }
    }
}
