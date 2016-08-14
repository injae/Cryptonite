package Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class Server_UserCode_Manager 
{
	private static Server_UserCode_Manager _manager;
	private Server_DataBase _db;
	
	private int _lastuscode = 0;
	private int _oldcode = 0;
	private Queue<Integer> _usableUsCode;
	
	private int _lastAcCode = 0;
	private Queue<Integer> _usableAcCode;
	
	public static Server_UserCode_Manager getInstance()
	{
		if(_manager == null)
		{
			_manager = new Server_UserCode_Manager();
		}
		return _manager;
	}
	
	private Server_UserCode_Manager()
	{
		_db = Server_DataBase.getInstance();
		_usableUsCode = new LinkedList<Integer>();
		_usableAcCode = new LinkedList<Integer>();
		searchUsercode();
	}

	private void searchUsercode()
	{
		ResultSet rs = _db.Query("select uscode from test order by uscode");
		try 
		{
			while(rs.next())
			{
				int uscode = rs.getInt(1);
				if(uscode != _oldcode + 1) 
				{
					for(int i = _oldcode + 1; i < uscode; i++)
					{
						_usableUsCode.add(i);
					}
				}
				_oldcode = uscode;
			}
			_lastuscode = _oldcode +1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsCode()
	{
		String usCode = "@";
		if(_usableUsCode.isEmpty()) { usCode = usCode + _lastuscode; }
		else 						{ usCode = usCode + _usableUsCode.remove(); }
		
		return usCode;
	}
	
	public String getAcCode()
	{
		String code = "#";
		if(_usableAcCode.isEmpty()) { code = code  + ++_lastAcCode; }
		else 					    { code = code + _usableAcCode.remove(); }
		
		return code;		
	}
	
	public void removeUsCode(String uscode)
	{
		if(uscode.charAt(0) == '@')
		{
			Integer code = Integer.parseInt(uscode.substring(1));
			_usableUsCode.add(code);
		}
		else if(uscode.charAt(0) == '#')
		{
			Integer code = Integer.parseInt(uscode.substring(1));
			_usableAcCode.add(code);
		}
	}
}
