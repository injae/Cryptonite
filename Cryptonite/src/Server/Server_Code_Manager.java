package Server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

/*
 * @author In Jae Lee
 * 
 */

public class Server_Code_Manager 
{
	private static Server_Code_Manager _manager;
	private Server_DataBase _db;
	
	private int _lastusCode = 0;
	private Queue<Integer> _usableUsCode;
	
	private int _lastAcCode = 0;
	private Queue<Integer> _usableAcCode;
	
	private int _lastGpCode = 0;
	private Queue<Integer> _usableGpCode;
	
	public static Server_Code_Manager getInstance()
	{
		if(_manager == null)
		{
			_manager = new Server_Code_Manager();
		}
		return _manager;
	}
	
	private Server_Code_Manager()
	{
		_db = Server_DataBase.getInstance();
		_usableUsCode = new LinkedList<Integer>();
		_usableAcCode = new LinkedList<Integer>();
		_usableGpCode = new LinkedList<Integer>();
		searchUserCode();
		searchGroupCode();
	}

	private void searchUserCode()
	{
		int oldcode = 0;
		ResultSet rs = _db.Query("select uscode from test order by uscode");
		try 
		{
			while(rs.next())
			{
				int uscode = rs.getInt(1);
				if(uscode != oldcode + 1) 
				{
					for(int i = oldcode + 1; i < uscode; i++)
					{
						_usableUsCode.add(i);
					}
				}
				oldcode = uscode;
			}
			_lastusCode = oldcode;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void searchGroupCode()
	{
		int oldcode = 0;
		ResultSet rs = _db.Query("select gpcode from grouplist order by gpcode");
		try 
		{
			while(rs.next())
			{
				int gpcode = rs.getInt(1);
				if(gpcode != oldcode + 1) 
				{
					for(int i = oldcode + 1; i < gpcode; i++)
					{
						_usableGpCode.add(i);
					}
				}
				oldcode = gpcode;
			}
			_lastGpCode = oldcode;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsCode()
	{
		String usCode = "@";
		if(_usableUsCode.isEmpty()) { usCode = usCode + ++_lastusCode; }
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
	
	public String getGpCode()
	{
		String code = "$";
		if(_usableGpCode.isEmpty()) { code = code  + ++_lastGpCode; }
		else 					    { code = code + _usableGpCode.remove(); }
		
		return code;		
	}
	
	public void removeCode(String code)
	{
		if(code.charAt(0) == '@')
		{
			Integer usCode = Integer.parseInt(code.substring(1));
			_usableUsCode.add(usCode);
		}
		else if(code.charAt(0) == '#')
		{
			Integer acCode = Integer.parseInt(code.substring(1));
			_usableAcCode.add(acCode);
		}
		else if(code.charAt(0) == '$')
		{
			Integer gpCode = Integer.parseInt(code.substring(1));
			_usableGpCode.add(gpCode);
		}
	}
}
