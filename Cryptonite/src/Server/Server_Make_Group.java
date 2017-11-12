package Server;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.omg.stub.java.rmi._Remote_Stub;

import Crypto.Crypto;
import Crypto.Crypto_Factory;
import Crypto.aesKeyGenerator;
import Function.PacketProcessor;

public class Server_Make_Group extends Server_Funtion
{
	public Server_Make_Group(Server_Client_Activity activity) {
		super(activity);
		_db = Server_DataBase.getInstance();
		_manager = Server_Client_Manager.getInstance();
		_members = new ArrayList<String>();
	}

	private int _groupMemberSize = 0;
	private Server_Client_Manager _manager = null;
	private Server_DataBase _db = null;
	private ArrayList<String> _members = null;
	private byte _usegps;

	@Override
	public void Checker(byte[] packet) 
	{
		System.out.println("packet[1] : " + packet[1]);
		_packetMaxCount = packet[1];
		_groupMemberSize = packet[1] - 5;
		_usegps = packet[2];
		
		
		for(int i = 0; i < _groupMemberSize; i++)		//membername
		{
			_activity.receive.setAllocate(500);
		}
		_activity.receive.setAllocate(8);	//lat
		_activity.receive.setAllocate(8);	//lng
		_activity.receive.setAllocate(8);	//radius
		_activity.receive.setAllocate(500);	//groupname

		
	}

	@Override
	public void running(int count) throws IOException 
	{
		if(count == 1) { Checker(_activity.getReceiveEvent()); }
		else
		{
			System.out.println("in");
			String gpCode = _manager._code_manager.getGpCode();
			String memberSet = new String();
			int code = Integer.parseInt(gpCode.substring(1));
			int ccount = 0;
				
			for(int i = 0; i < _groupMemberSize; i++)
			{
				try 
				{
					ResultSet rs = _db.Query("select * from test where id like'" + new String(_activity.receive.getByte()).trim() + "';");
					
					if(rs.next())
					{
						_members.add("@" + rs.getInt(6));
						if(ccount == 0)
						{
							memberSet = _members.get(ccount);
						}
						else
						{
							memberSet = memberSet + ":" + _members.get(ccount);
						}
						ccount++;
					}
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
			}
			
			double lat =0;
			double lng =0;
			double radius =0;

			lat = ByteBuffer.wrap(_activity.receive.getByte()).getDouble();
			lng = ByteBuffer.wrap(_activity.receive.getByte()).getDouble();
			radius = ByteBuffer.wrap(_activity.receive.getByte()).getDouble();
			
			String gpName = new String(_activity.receive.getByte()).trim();
			
			aesKeyGenerator ukg = new aesKeyGenerator();
			ukg.init();
			String aeskey= ukg.getAesKeyToString();
			String iteration= ukg.getIterationCountToString();
			String salt= ukg.getSaltToString();
			
			
			_db.Update("insert into grouplist values(" + code + ",'" + memberSet + "','" + gpName +"','" +aeskey+"','" + iteration +"','" + salt+"','" + _usegps + "','" + lat + "','" + lng + "','" + radius +"','" + 1 + "');");
			
			for(int i = 0; i < _members.size(); i++)
			{
				ResultSet rs = _db.Query("select *from test where uscode = '" +  Server_Code_Manager.codeCutter(_members.get(i)) + "';");
				String groupList = null;
				try 
				{
					rs.next();

					String buf = rs.getString(10);
					if(!buf.equals("NULL"))
					{
						groupList =  buf + ":" + gpCode;
					}
					else
					{
						groupList = gpCode;
					}
					
					String pubkey = rs.getString(14);
					byte[] pubKey = Base64.getDecoder().decode(pubkey);
					
					PublicKey pk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey));
					
					byte[] groupkey = Crypto_Factory.create("RSA1024", Cipher.ENCRYPT_MODE, pk).doFinal(Base64.getDecoder().decode(aeskey));
					
					int num=1;
				
					//System.out.println("insert into groupkey values('" + gpCode + "'," + num + ",'" + Server_Code_Manager.codeCutter(_members.get(i)) + "','" + Base64.getEncoder().encodeToString(groupkey) + "');");
					
					_db.Update("insert into groupkey values('" + Server_Code_Manager.codeCutter(gpCode) + "'," + num + ",'" + Server_Code_Manager.codeCutter(_members.get(i)) + "','" + Base64.getEncoder().encodeToString(groupkey) + "');");
					
				
					

				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				} catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				_db.Update("update test set mygrouplist = '" + groupList + "' where uscode = " + Server_Code_Manager.codeCutter(_members.get(i)) + ";");
			}
			
			File newFolder = new File("Server_Folder/Backup/" + gpCode);
			if(!newFolder.exists())
			{
				newFolder.mkdir();
			}
			
			byte[] event = new byte[1024];
			event[0] = 0;
			event[1] = 0;
			event[2] = 1;
			byte[] bgpcode = gpCode.getBytes();
			for(int i =0; i < bgpcode.length; i++)
			{
				event[i+3] = bgpcode[i];
			}
			for(int i =0; i < _members.size(); i++)
			{
				_manager.setEvent(_members.get(i), event);
			}
		}
	}
}
