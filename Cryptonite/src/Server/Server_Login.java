package Server;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import Client.Client_Login;

public class Server_Login extends Server_Funtion
{
	private String _password=Client_Login.getPassword();
	private String _id=Client_Login.getID();
	private static boolean _checkLogin=Client_Login.getCheckLogin_client();
	@Override
	public void Checker(byte[] packet) 
	{
		// TODO 자동 생성된 메소드 스텁
		
	}

	@Override
	public void running(Server_Client_Activity activity) {
		// TODO 자동 생성된 메소드 스텁
		
		Server_DataBase _db;
		_db=Server_DataBase.getInstance();
		_db.Init_DB("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/"+"cryptonite", "root", "yangmalalice3349!");
		_db.connect();
		
		
		Connection _con=(Connection) _db.Getcon();
		PreparedStatement _ps = null;
		ResultSet _rs = null;        
		String _sql = "select * from test";
		
		try{
			_ps = (PreparedStatement)_con.prepareStatement(_sql);
			_rs = _ps.executeQuery(); // 쿼리문이 select 로 시작되면 무조건
			System.out.println("id\tpassword\tenc_password");
			
			while(_rs.next()){
				String _get_id = _rs.getString(2);
				String _get_pwd = _rs.getString(3);// 두번째 필드의 데이터
				String _enc_pwd=Client_Login.Encode_password(_password);
				System.out.println(_get_id+"\t"+_get_pwd+"\t"+_enc_pwd);
				
				if(_get_id.equals(_id)&&_enc_pwd.equals(_get_pwd))
				{
					_checkLogin=true;
				}
			}
		}catch(SQLException e1){
			e1.printStackTrace();
		}
		
      /*  Server_DataBase _db;
        _db=Server_DataBase.getInstance();
        _db.Init_DB("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/"+"cryptonite", "root", "yangmalalice3349!");
        _db.connect();

        try
        {
        	ResultSet _rs  = _db.Query("select * from test where id like '"+ _id +"';");
        	System.out.println("id\tpassword\tenc_password");
       
        	if(!_rs.next()) { System.out.println("없는 아이디 입니다."); }
        
        	String _get_id = _rs.getString(2);
        	String _get_pwd = _rs.getString(3);// 두번째 필드의 데이터
        	String _enc_pwd = Encode_password(_password);
        	System.out.println(_get_id+"\t"+_get_pwd+"\t"+_enc_pwd);
       
        	if(_enc_pwd.equals(_get_pwd))
        	{
        		_checkLogin = true;
        		showMessage("LOGIN", "Welcome,\t"+_id); 
        	}
        }
        catch(SQLException e1)
        {
        	e1.printStackTrace();
        }  */       
		
		}  
	
	 private void showMessage(String title, String message) {
		 JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		}
	
	public static boolean getCheckLogin_server(){
    	return _checkLogin;
    }
    


}
