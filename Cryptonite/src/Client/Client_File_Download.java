package Client;

import java.awt.Font;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.KeyReposit;
import Function.PacketProcessor;
import Function.PacketRule;

public class Client_File_Download implements PacketRule
{
	Crypto _crypto;
	KeyReposit _reposit;
	public void requestFile(String path, String targetpath, SecretKey key)
	{
		try 
		{
			String extension = path.substring(path.length()-5,path.length());
			targetpath = targetpath.substring(0,targetpath.length() - 5);
			
			StringTokenizer st = new StringTokenizer(targetpath,"\\");
			String temp = "";
			String temp1 = "";
			String temp2 = "";
			while(st.hasMoreTokens())
			{
				temp = st.nextToken();
				temp2 += temp1;
				temp1 = "";
				temp1 += temp+"\\";
			}
			
			StringTokenizer st2 = new StringTokenizer(temp, "#");
			
			boolean temp3 = false;
			st2.nextToken();
			while(st2.hasMoreTokens())
			{
				temp = st2.nextToken() + "#";
				temp3 = true;
			}
			
			if (temp3)
				temp = temp.substring(0, temp.length()-1);
			
			targetpath = temp2 + temp;
			
			System.out.println(targetpath);
			
			_reposit = KeyReposit.getInstance();
			_crypto = new Crypto(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, key));
			//_crypto.init(Crypto_Factory.create("AES256", Cipher.DECRYPT_MODE, key));
			
			Client_Server_Connector csc = Client_Server_Connector.getInstance();
			byte[] event = new byte[1024];
			event[0] = FILE_DOWNLOAD;
			csc.send.setPacket(event).write();
			
			csc.send.setPacket(path.getBytes(), 500).write();
			
			String tmpfileSize = new String(csc.receive.setAllocate(500).read().getByte()).trim();
			System.out.println(tmpfileSize);
			if (tmpfileSize.equals("distance"))	//GPS return value
			{
				showMessage("Fail", "Unable to download (location verification failed)");
				return;
			}
			else if(tmpfileSize.equals("timeover"))	//GPS return value
			{
				showMessage("Fail", "Please resend your location.\n(10 minutes after the last location has passed.)");
				return;
			}
			
			long fileSize = Long.parseLong(tmpfileSize);
			
			RandomAccessFile raf  = new RandomAccessFile(targetpath, "rw");
			PacketProcessor p = new PacketProcessor(raf.getChannel(), false);
			
			if (extension.equals(".cnmc"))
			{
				csc.receive.setAllocate(64).read().getByte();
				csc.receive.setAllocate(fileSize-64);
				p.setAllocate(fileSize-64);
			}
			else
			{
				csc.receive.setAllocate(fileSize);
				p.setAllocate(fileSize);
			}
			while(!csc.receive.isAllocatorEmpty())
			{	
				p.setPacket(_crypto.endecription(csc.receive.read().getByte())).write();
		//		p.setPacket(csc.receive.read().getByte()).write();

			}
			p.close();
		}
		catch (NumberFormatException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void showMessage(String title, String message) 
	{
		Font fontbt = new Font("SansSerif", Font.BOLD,24);
		JLabel input = new JLabel(message);
		input.setFont(fontbt);
		JOptionPane.showMessageDialog(null, input, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
