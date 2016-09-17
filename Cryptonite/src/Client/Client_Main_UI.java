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
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
	
	private BufferedImage img = null;
	private BufferedImage img2 = null;
	private BufferedImage img3 = null;
	
	private JButton Sendbt;
	private JButton Receivebt;
	private JButton ProtectedFolderbt;
	private JButton FileRecoverybt;
	private JButton Indivbt;
	private JButton Groupbt;
	private JButton Developerbt;
	private JButton ProtectedFolderOpenbt;
	private JButton tab;
	
	private JButton Create;
	private JButton Participate;

	private Container container;
	
	private JLayeredPane layeredPane = new JLayeredPane();
	private MyPanel panel = new MyPanel();
	
	private Client_FolderScan _cfs = null;
	
	private int _checkUI = 0;
	
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
				new Client_Login();
			}
		};
		
		container=getContentPane();
		container.setBackground(Color.WHITE);
		setTitle("Cryptonite");
		addWindowListener(exitLitsener);
		setResizable(false);
		setBounds(500,300,807,480);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		container.setLayout(null);
		
        layeredPane.setBounds(0, 0, 900, 500);
        layeredPane.setLayout(null);
        
        try 
        {
            img = ImageIO.read(new File("img/MainFramebg.png"));
            img2 = ImageIO.read(new File("img/Group.png"));
            img3 = ImageIO.read(new File("img/Developers.png"));
        } 
        catch (IOException e) 
        {
            System.out.println("Image Load Failed.");
            System.exit(0);
        }
        panel.setBounds(0, 0, 900, 500);
       
        allocator();
        individual();
        setVisible(true);
	}
	
	public void individual()
	{
		_checkUI = 1;
 		 layeredPane.removeAll();

		 layeredPane.add(Indivbt);
		 layeredPane.add(Groupbt);
		 layeredPane.add(Developerbt);
		 layeredPane.add(ProtectedFolderOpenbt);
		 
		 layeredPane.add(Sendbt);
		 layeredPane.add(Receivebt);
		 layeredPane.add(ProtectedFolderbt); 
		 layeredPane.add(FileRecoverybt);
		 
		 layeredPane.add(panel);
		 container.add(layeredPane);
	}
	
	public void group()
	{
		_checkUI = 2;
 		layeredPane.removeAll();

        layeredPane.add(Indivbt);
 		layeredPane.add(Groupbt);
        layeredPane.add(Developerbt);
        layeredPane.add(ProtectedFolderOpenbt);
        
 	    layeredPane.add(Create);
 	    layeredPane.add(Participate);
 	    
 	    layeredPane.add(panel);
 	    container.add(layeredPane);
	}
	
	public void developer()
	{
		_checkUI = 3;
 		layeredPane.removeAll();

        layeredPane.add(Indivbt);
 		layeredPane.add(Groupbt);
        layeredPane.add(Developerbt);
        layeredPane.add(ProtectedFolderOpenbt);
 	    
 	    layeredPane.add(panel);
 	    container.add(layeredPane);
	}
	
	public void allocator()
	{

        Groupbt = new JButton(new ImageIcon("img/Groupbt.png"));		
        Groupbt.setRolloverIcon(new ImageIcon("img/Grouphbt.png"));
        Groupbt.setBounds(353, 34, 80, 30);
        Groupbt.setBorderPainted(false);
        Groupbt.setFocusPainted(false);
        Groupbt.setContentAreaFilled(false);
        Groupbt.addActionListener(new ActionListener() 
        {     
         	public void actionPerformed(ActionEvent arg0) 
         	{
         		group();
         		repaint();
         	}
        });
        
		Indivbt = new JButton(new ImageIcon("img/Indivbt.png"));		
        Indivbt.setRolloverIcon(new ImageIcon("img/Indivhbt.png"));
        Indivbt.setBounds(259, 35, 80, 30);
        Indivbt.setBorderPainted(false);
        Indivbt.setFocusPainted(false);
        Indivbt.setContentAreaFilled(false);
        Indivbt.addActionListener(new ActionListener() 
        {
         	public void actionPerformed(ActionEvent arg0) 
         	{
         		individual();
         		repaint();
         	}
        });
        
		
        Developerbt = new JButton(new ImageIcon("img/Developerbt.png"));
        Developerbt.setRolloverIcon(new ImageIcon("img/Developerhbt.png"));
        Developerbt.setBounds(447, 34, 80, 30);
        Developerbt.setBorderPainted(false);
        Developerbt.setFocusPainted(false);
        Developerbt.setContentAreaFilled(false);
        Developerbt.addActionListener(new ActionListener() 
        {
         	public void actionPerformed(ActionEvent arg0)
         	{
         		developer();
         		repaint();
         	}
        });
        
        
        ProtectedFolderOpenbt = new JButton(new ImageIcon("img/protectedFolderOpen.png"));
        ProtectedFolderOpenbt.setPressedIcon(new ImageIcon("img/protectedFolderOpenR.png"));
        ProtectedFolderOpenbt.setBounds(740, 20, 30, 30);
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
        
        //-------------------------------------------------
        
        
        Sendbt = new JButton(new ImageIcon("img/Filesend.png"));		
        Sendbt.setRolloverIcon(new ImageIcon("img/Filesendh.png"));
        Sendbt.setBounds(1, 82, 396, 160);
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
        
        Receivebt = new JButton(new ImageIcon("img/Filereceive.png"));
        Receivebt.setRolloverIcon(new ImageIcon("img/Filereceiveh.png"));
        Receivebt.setFocusPainted(false);
        Receivebt.setContentAreaFilled(false);
        Receivebt.setBorderPainted(false);
        Receivebt.setBounds(403, 82, 390, 160);
        Receivebt.addActionListener(new ActionListener() 
        {
			public void actionPerformed(ActionEvent e) 
			{
				new Client_Receive_OTP();		
			}
		});
        
        ProtectedFolderbt = new JButton(new ImageIcon("img/protectedfolder.png"));
        ProtectedFolderbt.setRolloverIcon(new ImageIcon("img/protectedfolderh.png"));
        ProtectedFolderbt.setFocusPainted(false);
        ProtectedFolderbt.setContentAreaFilled(false);
        ProtectedFolderbt.setBorderPainted(false);
        ProtectedFolderbt.setBounds(1, 245, 470, 190);
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
        
        FileRecoverybt = new JButton(new ImageIcon("img/Cloud.png"));
        FileRecoverybt.setRolloverIcon(new ImageIcon("img/Cloudh.png"));
        FileRecoverybt.setBounds(478, 250, 315, 181);
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
					e1.printStackTrace();
					showMessage("ERROR","There are no backup files.");
				}
			}
		});
		//--------------------------------------------------
		
		Create = new JButton(new ImageIcon("img/Create.png"));
 		Create.setRolloverIcon(new ImageIcon("img/Createh.png"));
 		Create.setBounds(10, 97, 390, 280);
 		Create.setBorderPainted(false);
 		Create.setFocusPainted(false);
 		Create.setContentAreaFilled(false);
 		Create.addActionListener(new ActionListener() 
 		{
        	public void actionPerformed(ActionEvent arg0)
        	{
        		new Client_Group_Name(_id);
        	}
        });
 	    Participate = new JButton(new ImageIcon("img/Participate.png"));
 	    Participate.setRolloverIcon(new ImageIcon("img/Participateh.png"));
 	    Participate.setBounds(434, 90, 370, 280);
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
        	switch(_checkUI)
        	{
        	case 1:
        		g.drawImage(img, 0, 0, null);
        		break;
        	case 2:
        		g.drawImage(img2, 0, 0, null);
        		break;
        	case 3:
        		g.drawImage(img3, 0, 0, null);
        		break;
        	default:
        		break;
        	}
        }
   }
	
	private void showMessage(String title, String message) 
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
