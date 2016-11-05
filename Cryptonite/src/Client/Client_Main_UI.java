package Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Crypto.KeyReposit;

public class Client_Main_UI extends JFrame
{
	private ArrayList<String> _gpCode;
	private ArrayList<String> _gpName;
	private String _id;
	private String _name;
	private String _usCode;
	private String _address;
	
	private Client_FolderSelector cfs = null;
	private Client_Show_Group _csg = null;
	private Client_File_ListReceiver _cfl = null;
	private Client_Developers_Introduce _cdi = null;
	private Client_Setting _cst = null;
	
	private BufferedImage _backGroundImg = null;
	
	private JButton ProtectedFolderOpenbt;
	private JButton Developersbt;
	private JButton Setting;
	
	private JButton Sendbt;
	private JButton Receivebt;
	private JButton ProtectedFolderbt;
	private JButton FileRecoverybt;
	
	private JButton Create;
	private JButton Participate;

	private Container container;
	
	private JLayeredPane layeredPane = new JLayeredPane();
	private MyPanel panel = new MyPanel();
	
	private Client_FolderScan _cfs = null;
	
	Font fontbt = new Font("SansSerif", Font.BOLD,24);
	
	Client_Main_UI main;
	
	public static void main(String args[]){
		new Client_Main_UI(new ArrayList<String>(),new ArrayList<String>(),null,null,null,null);
	}
	
	public Client_Main_UI(ArrayList<String> gpCode, ArrayList<String> gpname, String name, String usCode, String id, Client_FolderScan cfs)
	{
		_cfs = cfs;
		_cfl = new Client_File_ListReceiver();
		_csg = new Client_Show_Group();
		_cst = new Client_Setting();
		_cdi = new Client_Developers_Introduce();
		main = this;
		_gpCode = gpCode;
		_gpName = gpname;
		_name = name;
		_usCode = usCode;
		_id = id;
		
		try
		{
			 Toolkit tk = Toolkit.getDefaultToolkit(); 
			 Image image = tk.getImage("gui/logo.png");
			 this.setIconImage(image);
		}
		catch(Exception e)
		{
			System.out.println("Appilcation icon not found");
		}	
		
		WindowListener exitLitsener = new WindowAdapter() 
		{
				
			@Override
			public void windowClosing(WindowEvent e){
				dispose();
				while(!Client_Login._folderScanList.isEmpty())
				{
					Client_Login._folderScanList.remove().stopThread();
				}
				new Client_Logout().logout();
				KeyReposit.getInstance().logout();
				new Client_Login();
			}
		};
		
		container = getContentPane();
		container.setBackground(Color.WHITE);
		setTitle("Cryptonite");
		addWindowListener(exitLitsener);
		setResizable(false);
		setBounds(0, 0, 645, 480);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		container.setLayout(null);
		
        layeredPane.setBounds(0, 0, 645, 480);
        layeredPane.setLayout(null);
        
        try 
        {
        	_backGroundImg = ImageIO.read(new File("img/Background.png"));
        } 
        catch (IOException e) 
        {
            System.out.println("Image Load Failed.");
            System.exit(0);
        }
        panel.setBounds(0, 0, 645, 480);
       
        allocator();
        setFirst();
        setVisible(true);
	}
	
	private void setFirst()
	{
		layeredPane.add(ProtectedFolderOpenbt);
		layeredPane.add(Setting);
		layeredPane.add(Developersbt);
		layeredPane.add(Sendbt);
		layeredPane.add(Receivebt);
		layeredPane.add(ProtectedFolderbt); 
		layeredPane.add(FileRecoverybt);
		layeredPane.add(Create);
	 	layeredPane.add(Participate);
	
		layeredPane.add(panel);
		container.add(layeredPane);
	}
	
	public void allocator()
	{
        
        ProtectedFolderOpenbt = new JButton(new ImageIcon("img/protectedFolderOpen.png"));
        ProtectedFolderOpenbt.setRolloverIcon(new ImageIcon("img/protectedFolderOpenR.png"));
        ProtectedFolderOpenbt.setBounds(560, 45, 30, 30);
        ProtectedFolderOpenbt.setFocusPainted(false);
        ProtectedFolderOpenbt.setContentAreaFilled(false);
        ProtectedFolderOpenbt.setBorderPainted(false);
        ProtectedFolderOpenbt.addActionListener(new ActionListener() 
        {
   			public void actionPerformed(ActionEvent e) 
   			{
				try 
				{
					FileReader fr = new FileReader(new File("Cryptonite_Client/log/protectedlog.ser"));
					BufferedReader br = new BufferedReader(fr);
					String address = br.readLine();
					Runtime rt = Runtime.getRuntime();
					rt.exec("explorer.exe " + address);
				} 
				catch (FileNotFoundException e1) 
				{
					e1.printStackTrace();
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
   			}
   		});
        
        Setting = new JButton(new ImageIcon("img/Settingbt.png"));
        Setting.setRolloverIcon(new ImageIcon("img/Settinghbt.png"));
        Setting.setBounds(470,45,30,30);
        Setting.setFocusPainted(false);
        Setting.setContentAreaFilled(false);
        Setting.setBorderPainted(false);
        Setting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 자동 생성된 메소드 스텁
				_cst.UI_ON();
			}
		});
        
        Developersbt = new JButton(new ImageIcon("img/Developersbt.png"));
        Developersbt.setRolloverIcon(new ImageIcon("img/DevelopersRbt.png"));
        Developersbt.setBounds(510, 45, 30, 30);
        Developersbt.setFocusPainted(false);
        Developersbt.setContentAreaFilled(false);
        Developersbt.setBorderPainted(false);
        Developersbt.addActionListener(new ActionListener() 
        {
   			public void actionPerformed(ActionEvent e) 
   			{
   				_cdi.UI_ON();
   			}
   		});
        
        //-------------------------------------------------
        
        
        Sendbt = new JButton(new ImageIcon("img/FileSend.png"));		
        Sendbt.setRolloverIcon(new ImageIcon("img/FileSendR.png"));
        Sendbt.setBounds(50, 88, 160, 160);
        Sendbt.setBorderPainted(false);
        Sendbt.setFocusPainted(false);
        Sendbt.setContentAreaFilled(false);
        Sendbt.addActionListener(new ActionListener() 
        {
   			public void actionPerformed(ActionEvent e) 
   			{
   				new Client_Send_OTP().fileSelect();	
   			}
   		});
        
        Receivebt = new JButton(new ImageIcon("img/FileReceive.png"));
        Receivebt.setRolloverIcon(new ImageIcon("img/FileReceiveR.png"));
        Receivebt.setBounds(222, 88, 160, 160);
        Receivebt.setFocusPainted(false);
        Receivebt.setContentAreaFilled(false);
        Receivebt.setBorderPainted(false);
        Receivebt.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent e) 
			{
				new Client_Receive_OTP();		
			}
		});
        
        ProtectedFolderbt = new JButton(new ImageIcon("img/ProtectedFolder.png"));
        ProtectedFolderbt.setRolloverIcon(new ImageIcon("img/ProtectedFolderR.png"));
        ProtectedFolderbt.setBounds(50, 260, 160, 160);
        ProtectedFolderbt.setFocusPainted(false);
        ProtectedFolderbt.setContentAreaFilled(false);
        ProtectedFolderbt.setBorderPainted(false);
        ProtectedFolderbt.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent e) 
			{
					cfs	 = new Client_FolderSelector();
					if(cfs.folderSelectorON())
					{
						while(!cfs.getSelectionEnd())
						{
							try 
							{
								Thread.sleep(1);
							} 
							catch (InterruptedException e1) 
							{
								e1.printStackTrace();
							}
						}	
						_address = cfs.getSelectedPath();
						
						File save = new File("Cryptonite_Client/log/protectedlog.ser");
						try {
							FileReader fr = new FileReader(save);
							BufferedReader br = new BufferedReader(fr);
							Client_Icon_Change.change(_address);
							Client_Icon_Change.restore(br.readLine());
							br.close();
							fr.close();
							
							FileWriter fw = new FileWriter(save);
							fw.write(_address);
							fw.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						while(!Client_Login._folderScanList.isEmpty())
						{
							Client_Login._folderScanList.remove().stopThread();
						}
						_cfs = new Client_FolderScan();
						Client_Login._folderScanList.offer(_cfs);
						_cfs.start();
					}
			}
		});
        
        FileRecoverybt = new JButton(new ImageIcon("img/FileRecovery.png"));
        FileRecoverybt.setRolloverIcon(new ImageIcon("img/FileRecoveryR.png"));
        FileRecoverybt.setBounds(222, 260, 160, 160);
        FileRecoverybt.setFocusPainted(false);
        FileRecoverybt.setBorderPainted(false);
        FileRecoverybt.setContentAreaFilled(false);
        FileRecoverybt.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
					_cfl.running((byte)2, null);
					new Client_FileRecovery(_cfl.getFileList());
				}
				catch(IndexOutOfBoundsException e1)
				{
					showMessage("ERROR","There are no backup files.");
				}
			}
		});
		//--------------------------------------------------
		
		Create = new JButton(new ImageIcon("img/CreateGroup.png"));
 		Create.setRolloverIcon(new ImageIcon("img/CreateGroupR.png"));
 		Create.setBounds(428, 88, 160, 160);
 		Create.setBorderPainted(false);
 		Create.setFocusPainted(false);
 		Create.setContentAreaFilled(false);
 		Create.addActionListener(new ActionListener() 
 		{
        	public void actionPerformed(ActionEvent arg0)
        	{
        		new GoogleMaps(_id);
        	}
        });
 		
 	    Participate = new JButton(new ImageIcon("img/Participate.png"));
 	    Participate.setRolloverIcon(new ImageIcon("img/ParticipateR.png"));
 	    Participate.setBounds(428, 260, 160, 160);
 	    Participate.setBorderPainted(false);
 	    Participate.setFocusPainted(false);
 	    Participate.setContentAreaFilled(false);
 	    Participate.addActionListener(new ActionListener() 
 	    {
 	       	public void actionPerformed(ActionEvent arg0) 
 	       	{
 	       		_csg.running(_id);
 	       		new Client_Invitation(_csg, _id);
 	       	}
 	    });
	}
	
	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
        	g.drawImage(_backGroundImg, 0, 0, null);
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
