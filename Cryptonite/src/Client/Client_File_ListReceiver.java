package Client;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Function.PacketRule;

public class Client_File_ListReceiver implements PacketRule
{
	// Instance
	private byte _mod;
	private int _fileCount;
	private ArrayList<String> _fileList = null;
	private String _downloadFolder = null;
	
	// Another Class
	private Client_Server_Connector _csc = null;
	private Client_FolderSelector _cfs = null;
	private Client_File_Download _cfd = null;
	
	// Constructors
	public Client_File_ListReceiver()
	{
		
		_csc = Client_Server_Connector.getInstance();
		_cfs = new Client_FolderSelector();
		_cfd = new Client_File_Download();
	}
	
	// Methods
	public void running(byte mod, String gpCode)
	{
		try 
		{
			_fileList = new ArrayList<String>();
			//_fileList.clear();
			
			Charset cs = Charset.forName("UTF-8");
			byte[] event = new byte[1024];
			event[0] = FILE_LIST_REQUEST;
			event[1] = mod;
			if(mod == 1)	// Group
			{
				byte[] temp = gpCode.getBytes();
				for(int i = 0; i < gpCode.getBytes().length; i++)
				{
					event[i + 2] = temp[i];
				}
			}
			_csc.send.setPacket(event).write();
			
			_fileCount = Integer.parseInt(new String(_csc.receive.setAllocate(100).read().getByte()).trim());
			for(int i = 0; i < _fileCount; i++)
			{
				_fileList.add(cs.decode(_csc.receive.setAllocate(1024).read().getByteBuf()).toString().trim());
			}
			if(_fileCount == 0)
			{
				System.out.println("읽을 파일 리스트가 존재하지 않습니다.");
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String[] getFileList()
	{
		String[] temp = new String[_fileList.size()];
		
		for(int i = 0 ; i < temp.length; i++)
		{
			temp[i] = _fileList.get(i);
		}
		
		return temp;
	}
	
	private String nameTokenizer(String target)
	{
		StringTokenizer st = new StringTokenizer(target, "\\");
		String temp = null;;
		
		while(st.hasMoreTokens())
		{
			temp = st.nextToken();
		}
		
		return temp;
	}
}
