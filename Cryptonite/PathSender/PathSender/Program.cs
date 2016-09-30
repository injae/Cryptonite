using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PathSender
{
    class Program
    {
        static void Main(string[] args)
        {
            IOmanager io = new IOmanager();
            io.Send(args[0]);
        }
    }
}
