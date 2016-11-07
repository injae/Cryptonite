using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Win32;

namespace allo
{
    class Program
    {
        static void Main(string[] args)
        {

            string appPath = System.IO.Directory.GetCurrentDirectory();

            if (Registry.ClassesRoot.GetSubKeyNames().Contains(".cnec"))
                Registry.ClassesRoot.DeleteSubKeyTree(".cnec");
            if (Registry.ClassesRoot.GetSubKeyNames().Contains(".cnmc"))
                Registry.ClassesRoot.DeleteSubKeyTree(".cnmc");
            if (Registry.ClassesRoot.GetSubKeyNames().Contains("yourext.cnec.v1"))
                Registry.ClassesRoot.DeleteSubKeyTree("yourext.cnec.v1");

            Registry.ClassesRoot.CreateSubKey(".cnec").SetValue("", "youtext.cnec.v1");
            Registry.ClassesRoot.CreateSubKey(".cnmc").SetValue("", "youtext.cnec.v1");
            Registry.ClassesRoot.CreateSubKey("youtext.cnec.v1").SetValue("", "Cryptonite Encryption File");
            Registry.ClassesRoot.CreateSubKey("youtext.cnec.v1\\DefaultIcon");//.SetValue(Nothing, 확장자 아이콘의 경로)
            Registry.ClassesRoot.CreateSubKey("youtext.cnec.v1\\Shell\\Open\\Command").SetValue("", "\"" + appPath + "\\PathSender.exe\"" + " \"%1\"");




        }
    }
}
