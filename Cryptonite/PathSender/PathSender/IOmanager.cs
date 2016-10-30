using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace PathSender
{
    class IOmanager
    {
        Socket socket;
        IPEndPoint endPoint;

        public IOmanager()
        {
            try
            {
                socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

                endPoint = new IPEndPoint(IPAddress.Parse("127.0.0.1"), 9999);
                socket.Connect(endPoint);
            }
            catch (SocketException e)
            {
                Console.WriteLine("Please Server Open");
                Environment.Exit(0);
            }
        }

        public string Receive()
        {
            byte[] rcvLenBytes = new byte[4];
            socket.Receive(rcvLenBytes);
            int rcvLen = System.BitConverter.ToInt32(rcvLenBytes, 0);

            byte[] rcvBytes = new byte[rcvLen];
            socket.Receive(rcvBytes);
            socket.Close();

            String rcvString = System.Text.Encoding.UTF8.GetString(rcvBytes);
            return rcvString;
        }

        public void Send(string sendMsg)
        {
            int toSendLen = System.Text.Encoding.UTF8.GetByteCount(sendMsg);
            byte[] toSendLenBytes = System.BitConverter.GetBytes(toSendLen);
            socket.Send(toSendLenBytes);

            byte[] toSendBytes = System.Text.Encoding.UTF8.GetBytes(sendMsg);
            socket.Send(toSendBytes);
            socket.Close();
        }
    }
}
