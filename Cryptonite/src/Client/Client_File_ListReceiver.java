package Client;

import Function.PacketRule;

public class Client_File_ListReceiver implements PacketRule
{
	// Instance
	private byte _mod;
	
	// Constructors
	
	// Methods
	public void click(byte mod, String gpName)
	{
		byte[] event = new byte[1024];
		event[0] = FILE_LIST_REQUEST;
		event[1] = mod;
		if(mod == 1)
		{
			byte[] temp = gpName.getBytes();
			for(int i = 0; i < gpName.getBytes().length; i++)
			{
				event[i + 2] = temp[i];
			}
		}
		else if(mod == 2)
		{
			
		}
	}
}
