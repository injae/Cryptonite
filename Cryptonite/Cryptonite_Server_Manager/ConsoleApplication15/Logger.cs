using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApplication15
{
    class Logger
    {
        private LinkedList<string> logList;
        private DateTime time;
      
        private string title;
        public string Title { get { return title; } set { title = value; } }

        public int Size { get { return logList.Count; } }

        public Logger(string title)
        {
            logList = new LinkedList<string>();
            time = DateTime.Now;
            Title = title;
        }
        
        public void Update(string log)
        {
            logList.AddLast("[Time]: " + DateTime.Now.ToString() + ", Log: " + log);
        }

        public List<string> Log { get { return logList.ToList(); } }
        
        public void PrintLog()
        {
            Console.WriteLine("\n <Title: " + title + " >");
            foreach(string log in logList)
            {
                Console.WriteLine(logList);
            }
        }
    }
}
