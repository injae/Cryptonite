package Client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import Client.Client_Testmain.MyPanel;

public class Client_Testmain_group extends JFrame{
	
public static void main(String[] args){
		
		new Client_Testmain_group();
	}
	
	BufferedImage _img = null;
	
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(_img, 0, 0, null);
        }
    }
	
	private static JFrame frame=new JFrame("CRYPTONITE");
	
	JButton _Create;
	JButton _Participate;
	//JButton _ProtectedFile;
	JButton _Ok;//Is it right?
	
	Client_TestGroupName tgn;
	Client_Testmain_group thisJFrame;
	
	public Client_Testmain_group(){
		
	/*	WindowListener exitListener = new WindowAdapter() {
	        	
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
				new Client_Login();
			}
		};*/
		
		WindowListener exitLitsener = new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e){
				thisJFrame.dispose();
				new Client_Logout().logout();
				new Client_Login();
			}
		};
		
		try{
			 Toolkit tk = Toolkit.getDefaultToolkit(); 
			 Image image = tk.getImage("gui/logo.png");
			 this.setIconImage(image);
			 thisJFrame=this;
		}
		catch(Exception e)
		{
			System.out.println("Appilcation icon not found");
		}
		thisJFrame.setTitle("Cryptonite");
		thisJFrame.addWindowListener(exitLitsener);
        thisJFrame.setBounds(710,200,456,700);
        thisJFrame.setResizable(false);
        thisJFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
       // setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        getContentPane().setLayout(null);
        JLayeredPane _layeredPane = new JLayeredPane();
        _layeredPane.setBounds(0,0, 470, 700);
        _layeredPane.setLayout(null);
        
        try {
            _img = ImageIO.read(new File("img/test_image.png"));//input image
        } catch (IOException e) {
            System.out.println("No Image");
            System.exit(0);
        }
         
        MyPanel _panel = new MyPanel();
        _panel.setBounds(0, 0, 470, 700);
        
        _Create = new JButton(new ImageIcon("img/test_create.png"));
        _Create.setBounds(30, 100, 400, 50);
        _Create.setBorderPainted(false);
        _Create.setFocusPainted(false);
        _Create.setContentAreaFilled(false);
        _Create.setPressedIcon(new ImageIcon("img/test_create_hv.png"));
        _Create.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		tgn=new Client_TestGroupName();
        		Creategroup();
        	}
        });
       
        
        _Participate = new JButton(new ImageIcon("img/test_participate.png"));
        _Participate.setBounds(30, 200, 400, 50);
        _Participate.setBorderPainted(false);
        _Participate.setFocusPainted(false);
        _Participate.setContentAreaFilled(false);
        _Participate.setPressedIcon(new ImageIcon("img/test_participate_hv.png"));
        _Participate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        	}
        });
        
       /* _ProtectedFile = new JButton(new ImageIcon("img/test_protectbt.png"));
        _ProtectedFile.setBounds(30, 300, 400, 50);
        _ProtectedFile.setBorderPainted(false);
        _ProtectedFile.setFocusPainted(false);
        _ProtectedFile.setContentAreaFilled(false);
        _ProtectedFile.setPressedIcon(new ImageIcon("img/tset_protectbt_hv.png"));
        _ProtectedFile.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        	}
        });*/
        
       /* _Ok = new JButton(new ImageIcon("img/test_cloudbt.png"));
        _Ok.setBounds(30, 400, 400, 50);
        _Ok.setBorderPainted(false);
        _Ok.setFocusPainted(false);
        _Ok.setContentAreaFilled(false);
        _Ok.setPressedIcon(new ImageIcon("img/tset_cloudbt_hv.png"));
        _Ok.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
    					
        	}
        });*/
        _layeredPane.add(_Create);
        _layeredPane.add(_Participate);
       // _layeredPane.add(_ProtectedFile);
       // _layeredPane.add(_Ok);
        _layeredPane.add(_panel);
             
        getContentPane().add(_layeredPane);          
        thisJFrame.setVisible(true);
	}
	public void Creategroup(){
		String[] test = new String[3];
		test[0] = "a";
		test[1] = "b";
		test[2] = "c";
		String temp = tgn.Name;
		new Client_Make_Group().make(test, temp);
		byte[] event = new Client_Receive_Event().getEvent();
		byte[] buffer = new byte[1021]; 
		for(int i = 0; i < buffer.length; i++)
		{
			buffer[i] = event[i+3];
		}
		System.out.println("GroupCode : "+new String(buffer).trim());
		System.out.println("GroupName : "+temp);	 
	}

}
