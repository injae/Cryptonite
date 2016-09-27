using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApplication15
{
    class Timer
    {
        private long fTime;
        private long lTime;

        public Timer()
        {
            fTime = DateTime.Now.TimeOfDay.Milliseconds;
            lTime = DateTime.Now.TimeOfDay.Milliseconds;
        }

        public void Start()
        {
            lTime = fTime = DateTime.Now.TimeOfDay.Milliseconds;
        }

        public void Stop()
        {
            lTime = DateTime.Now.TimeOfDay.Milliseconds;
        }

        public Boolean Alarm(long time)
        {
            Stop();

            if(lTime - fTime >= time)
            {
                Start();
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
