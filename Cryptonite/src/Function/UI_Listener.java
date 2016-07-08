package Client;

import java.util.Vector;

public class UI_Listener 
{
	private Vector<Object> UI_List;
	private static UI_Listener Listener;
	
	private UI_Listener()
	{
		
	}
	
	public UI_Listener getInstance()
	{
		if(Listener == null)
		{
			Listener = new UI_Listener();
			UI_List = new Vector<Object>();
		}
		
		return Listener;
	}
}
