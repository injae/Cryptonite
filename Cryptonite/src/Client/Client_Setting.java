package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.StringTokenizer;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JWindow;

import Crypto.KeyReposit;
import Function.PacketRule;

import javax.swing.JRadioButton;
import java.awt.Color;
import java.awt.SystemColor;

public class Client_Setting  implements PacketRule
{
	// Instance
	private JWindow _window;
	private JLabel _background;
	private JButton _close;
	private JLayeredPane _layeredPane;
	
	public static void main(String[] args)
	{
		new Client_Setting().UI_ON();
	}
	
	// Constructors
	public Client_Setting()
	{
		_layeredPane = new JLayeredPane();
		_window = new JWindow();
		
		_background = new JLabel(new ImageIcon("img/Setting.png"));
		_background.setBounds(0, 0, 400, 224);
		_background.setVisible(true);
		
		_close = new JButton(new ImageIcon("img/SClose.png"));
		_close.setRolloverIcon(new ImageIcon("img/SCloseR.png"));
		_close.setBounds(260, 94, 90, 60);
		_close.setVisible(true);
		_close.setBorderPainted(false);
 	    _close.setFocusPainted(false);
		_close.addActionListener(new ActionListener() 
 	    {
 	       	public void actionPerformed(ActionEvent arg0) 
 	       	{
 	       		UI_OFF();
 	       	}
 	    });
		
		_layeredPane.add(_close);
		
		JLabel lblUsePasswordEndecryption = new JLabel("Using Password to file decryption");
		lblUsePasswordEndecryption.setBounds(12, 25, 481, 35);
		_layeredPane.add(lblUsePasswordEndecryption);
		
		JRadioButton rdbtnTrue = new JRadioButton("TRUE");
		rdbtnTrue.setBackground(SystemColor.control);
		rdbtnTrue.setBounds(10, 66, 57, 43);
		rdbtnTrue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Save pbk in Sha-256
				/*UI_OFF();
				
				String password = getPassword();
				if (password == null)
				{
					JOptionPane.showConfirmDialog(null, "Cancel", "Cancel", JOptionPane.YES_OPTION);
					return;
				}
				String pbk = getPBK(password);
				
				MessageDigest md = null;
				try {
					md = MessageDigest.getInstance("SHA-256");
				} catch (NoSuchAlgorithmException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} // 이 부분을 SHA-256, MD5로만 바꿔주면 된다.
	            md.update(pbk.getBytes()); // "세이프123"을 SHA-1으로 변환할 예정!
	            byte byteData[] = md.digest();
	            
	            StringBuffer sb = new StringBuffer(); 
	            for(int i=0; i<byteData.length; i++) {
	                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
	            }
	            pbk = sb.toString();
				System.out.println(pbk);*/
				
				
				
				File setting = new File("Cryptonite_Client/log/setting.ser");
				try {
					FileWriter fr = new FileWriter(setting);
					
					fr.write("usepassword = TRUE");
					fr.write("\r\n");
					//fr.write(pbk);
					fr.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		_layeredPane.add(rdbtnTrue);
		
		JRadioButton rdbtnFalse = new JRadioButton("FALSE");
		rdbtnFalse.setBackground(SystemColor.control);
		rdbtnFalse.setBounds(85, 66, 95, 43);
		rdbtnFalse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File setting = new File("Cryptonite_Client/log/setting.ser");
				try {
					FileWriter fr = new FileWriter(setting);
					
					fr.write("usepassword = FALSE");
					fr.write("\r\n");
					//fr.write("null");
					fr.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		_layeredPane.add(rdbtnFalse);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnTrue);
		bg.add(rdbtnFalse);
		
		_layeredPane.add(_background);
		
		File setting = new File("Cryptonite_Client/log/setting.ser");
		FileReader fr;
		try {
			
			if (!setting.exists())
			{
				setting.createNewFile();
				FileWriter fw = new FileWriter(setting);
				fw.write("usepassword = FALSE");
				fw.write("\r\n");
				//fw.write("null");
				fw.flush();
				fw.close();
			}
			
			fr = new FileReader(setting);
			BufferedReader br = new BufferedReader(fr);
			StringTokenizer st = new StringTokenizer(br.readLine(), "=");
			if(st.hasMoreTokens() && st.nextToken().equals("usepassword "))
			{
				if (st.hasMoreTokens() && st.nextToken().equals(" TRUE"))
				{
					rdbtnTrue.setSelected(true);
				}
				else rdbtnFalse.setSelected(true);
			}
			else rdbtnFalse.setSelected(true);
			
			br.close();
			fr.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		_window.getContentPane().add(_layeredPane);
		_window.setSize(400, 224);
		_window.setLocationRelativeTo(null);
		_window.setAlwaysOnTop(true);
	}
	private String getPassword() {
		// TODO 자동 생성된 메소드 스텁
		return (String) JOptionPane.showInputDialog(null, "Input Password", "Password", JOptionPane.PLAIN_MESSAGE, null, null, null);
	}
	private String getPBK (String password)
	{
		Client_Server_Connector _css = Client_Server_Connector.getInstance();
		byte[] op = new byte[1024];
		String salt = null;
		int iteration = 0;
		byte size =1;
		try {
			op[0]=GET_PBKDF2;
			op[1]=size;
			_css.send.setPacket(op).write();
			
			salt = new String(_css.receive.setAllocate(32).read().getByte());
			iteration = byteArrayToInt(_css.receive.setAllocate(4).read().getByte());
			
			System.out.println(salt +"  " + iteration);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pbkdf2(password,salt,iteration);
		
	}
	public  int byteArrayToInt(byte bytes[]) {
		return ((((int)bytes[0] & 0xff) << 24) |
				(((int)bytes[1] & 0xff) << 16) |
				(((int)bytes[2] & 0xff) << 8) |
				(((int)bytes[3] & 0xff)));
	} 
    public String pbkdf2(String password, String salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterations, 20*8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return new String(Base64.getEncoder().encode(hash));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("error on pbkdf2", e);
        }
    }
	// Methods
	public void UI_ON()
	{
		_window.setVisible(true);
	}
	
	private void UI_OFF()
	{
		_window.dispose();
	}
}