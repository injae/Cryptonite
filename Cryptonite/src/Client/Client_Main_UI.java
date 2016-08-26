package Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
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
import javax.swing.JPanel;

public class Client_Main_UI extends JFrame
{
	private ArrayList<String> _gpCode;
	private ArrayList<String> _gpName;
	private String _name;
	private String _usCode;
	private String _address;
	
	private Client_FolderSelector cfs = null;
	
	private BufferedImage img = null;
	private BufferedImage img2=null;
	
	private JButton Sendbt;
	private JButton Receivebt;
	private JButton Encryptbt;
	private JButton Cloudbt;
	private JButton Indivbt;
	private JButton Groupbt;
	private JButton Developerbt;
	private JButton Settingbt;
	private JButton tab;
	
	private JButton Create;
	private JButton Participate;

	private Container container;
	
	private JLayeredPane layeredPane = new JLayeredPane();
	private MyPanel panel = new MyPanel();
	
	private boolean _checkUI=false;
	
	Font fontbt = new Font("SansSerif", Font.BOLD,24);
	
	public static void main(String args[]){
		new Client_Main_UI(new ArrayList<String>(),new ArrayList<String>(),null,null);
	}

	public Client_Main_UI(ArrayList<String> gpCode, ArrayList<String> gpname, String name, String usCode)
	{
		_gpCode = gpCode;
		_gpName = gpname;
		_name = name;
		_usCode = usCode;
		
		WindowListener exitLitsener = new WindowAdapter() {
				
			@Override
			public void windowClosing(WindowEvent e){
				dispose();
				new Client_Logout().logout();
				new Client_Login();
			}
		};
		
		container=getContentPane();
		container.setBackground(Color.WHITE);
		setTitle("Cryptonite");
		addWindowListener(exitLitsener);
		setResizable(false);
		setBounds(500,300,816,480);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		container.setLayout(null);
		
        layeredPane.setBounds(0, 0, 900, 500);
        layeredPane.setLayout(null);
        
        try {
            img = ImageIO.read(new File("img/MainFramebg.png"));
            img2 = ImageIO.read(new File("img/Group.png"));
        } catch (IOException e) {
            System.out.println("이미지 불러오기 실패");
            System.exit(0);
        }
        panel.setBounds(0, 0, 900, 500);
       
        allocator();

        individual();
	    
        setVisible(true);
	}
	public void group(){
		_checkUI=true;
 		layeredPane.removeAll();

        layeredPane.add(Indivbt);
 		layeredPane.add(Groupbt);
        layeredPane.add(Developerbt);
        layeredPane.add(Settingbt);
        
 	    layeredPane.add(Create);
 	    layeredPane.add(Participate);
 	    
 	    layeredPane.add(panel);
 	    container.add(layeredPane);
	}
	
	public void individual(){
		_checkUI=false;
 		 layeredPane.removeAll();

		 layeredPane.add(Indivbt);
		 layeredPane.add(Groupbt);
		 layeredPane.add(Developerbt);
		 layeredPane.add(Settingbt);
		 
		 layeredPane.add(Sendbt);
		 layeredPane.add(Receivebt);
		 layeredPane.add(Encryptbt); 
		 layeredPane.add(Cloudbt);
		 
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
        Groupbt.addActionListener(new ActionListener() {     
         	public void actionPerformed(ActionEvent arg0) {
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
        Indivbt.addActionListener(new ActionListener() {
         	public void actionPerformed(ActionEvent arg0) {
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
        
        
        Settingbt = new JButton(new ImageIcon("img/Settingbt.png"));
        Settingbt.setPressedIcon(new ImageIcon("img/Settinghbt.png"));
        Settingbt.setBounds(707, 4, 60, 60);
        Settingbt.setFocusPainted(false);
        Settingbt.setContentAreaFilled(false);
        Settingbt.setBorderPainted(false);
        
        //-------------------------------------------------
        
        
        Sendbt = new JButton(new ImageIcon("img/Filesend.png"));		
        Sendbt.setPressedIcon(new ImageIcon("img/Filesendh.png"));
        Sendbt.setBounds(1, 82, 396, 160);
        Sendbt.setBorderPainted(false);
        Sendbt.setFocusPainted(false);
        Sendbt.setContentAreaFilled(false);
        Sendbt.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
   				new Client_Send_OTP();		
   			}
   		});
        
        Receivebt = new JButton(new ImageIcon("img/Filereceive.png"));
        Receivebt.setPressedIcon(new ImageIcon("img/Filereceiveh.png"));
        Receivebt.setFocusPainted(false);
        Receivebt.setContentAreaFilled(false);
        Receivebt.setBorderPainted(false);
        Receivebt.setBounds(403, 82, 390, 160);
        Receivebt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Client_Receive_OTP();		
			}
		});
        
        Encryptbt = new JButton(new ImageIcon("img/protectedfolder.png"));
        Encryptbt.setPressedIcon(new ImageIcon("img/protectedfolderh.png"));
        Encryptbt.setFocusPainted(false);
        Encryptbt.setContentAreaFilled(false);
        Encryptbt.setBorderPainted(false);
        Encryptbt.setBounds(1, 245, 470, 190);
        Encryptbt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					cfs	 = new Client_FolderSelector();
					cfs.folderSelectorON();
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
					FileWriter fw;
					try {
						fw = new FileWriter(save);
						fw.write(_address);
						fw.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				new Client_FolderScan().start();		
			}
		});
        
        Cloudbt = new JButton(new ImageIcon("img/Cloud.png"));
        Cloudbt.setRolloverIcon(new ImageIcon("img/Cloudh.png"));
        Cloudbt.setBounds(478, 250, 315, 181);
        Cloudbt.setFocusPainted(false);
        Cloudbt.setBorderPainted(false);
        Cloudbt.setContentAreaFilled(false);
        Cloudbt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Client_File_ListReceiver().click((byte)1, null);
			}
		});
		//--------------------------------------------------
		
		Create = new JButton(new ImageIcon("img/Create.png"));
 		Create.setRolloverIcon(new ImageIcon("img/Createh.png"));
 		Create.setBounds(10, 97, 390, 280);
 		Create.setBorderPainted(false);
 		Create.setFocusPainted(false);
 		Create.setContentAreaFilled(false);
 		Create.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		new Client_TestGroupName();
        	}
        });
 	    Participate = new JButton(new ImageIcon("img/Participate.png"));
 	    Participate.setRolloverIcon(new ImageIcon("img/Participateh.png"));
 	    Participate.setBounds(434, 90, 370, 280);
 	    Participate.setBorderPainted(false);
 	    Participate.setFocusPainted(false);
 	    Participate.setContentAreaFilled(false);
 	    Participate.addActionListener(new ActionListener() {
 	       	public void actionPerformed(ActionEvent arg0) {
 	       		Scanner sc = new Scanner(System.in);
 	       		String code = sc.nextLine();
 	       		new Client_Group_Withdrawal().running(code);
 	       	}
 	    });
		//----------------------------------------------------------------------indivial
	}
	
	class MyPanel extends JPanel {
        public void paint(Graphics g) 
        {
        	if(_checkUI){
        		g.drawImage(img2, 0, 0, null);
        	}
        	else{
        		g.drawImage(img, 0, 0, null);
        	}
       }
   }
}
