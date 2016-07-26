package Function;

import java.util.HashMap;

public class UI_Listener 
{
	private HashMap<String,Object> UI_List;
	private static UI_Listener Listener;
	
	private UI_Listener()
	{
		UI_List = new HashMap<String,Object>();
	}
	
	public static UI_Listener  getInstance()
	{
		if(Listener == null)
		{
			Listener = new UI_Listener();	
		}
		return Listener;
	}
	
	public Object getUI(String name)
	{
		Object UI = UI_List.get(name);
		if(UI == null) { return null; }
		else		   { return UI; }
	}
	
	public void setUI(String name,Object UI)
	{
		UI_List.put(name, UI);
	}
}
