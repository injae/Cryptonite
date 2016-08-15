package Client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Client.Client_Login.MyPanel;

public class Client_Testmain extends JFrame{

	public static void main(String[] args)
	{
		new Client_Testmain();
	}
	
	BufferedImage _img = null;
	
	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
            g.drawImage(_img, 0, 0, null);
        }
    }
	JButton _FileSend;
	JButton _FileReceive;
	JButton _Cloud;//Is it right?
	JButton _ProtectedFile;
	
	public Client_Testmain(){
		try{
			 Toolkit tk = Toolkit.getDefaultToolkit(); 
			 Image image = tk.getImage("gui/logo.png");
			 this.setIconImage(image);
		}
		catch(Exception e)
		{
			System.out.println("Appilcation icon not found");
		}
		setTitle("Cryptonite");
        setBounds(710,200,456,700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
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
        
        _FileSend = new JButton(new ImageIcon("img/teset_sendbt.png"));
        _FileSend.setBounds(30, 100, 400, 50);
        _FileSend.setBorderPainted(false);
        _FileSend.setFocusPainted(false);
        _FileSend.setContentAreaFilled(false);
        _FileSend.setPressedIcon(new ImageIcon("img/tset_sendbt_hv.png"));
        _FileSend.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//button event
        	}
        });
        
        _FileReceive = new JButton(new ImageIcon("img/test_receivedbt.png"));
        _FileReceive.setBounds(30, 200, 400, 50);
        _FileReceive.setBorderPainted(false);
        _FileReceive.setFocusPainted(false);
        _FileReceive.setContentAreaFilled(false);
        _FileReceive.setPressedIcon(new ImageIcon("img/test_receivebt_hv.png"));
        _FileReceive.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		new Client_TestOTP();
        	}
        });
        
        _ProtectedFile = new JButton(new ImageIcon("img/test_protectbt.png"));
        _ProtectedFile.setBounds(30, 300, 400, 50);
        _ProtectedFile.setBorderPainted(false);
        _ProtectedFile.setFocusPainted(false);
        _ProtectedFile.setContentAreaFilled(false);
        _ProtectedFile.setPressedIcon(new ImageIcon("img/tset_protectbt_hv.png"));
        _ProtectedFile.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//button event
        	}
        });
        
        _Cloud = new JButton(new ImageIcon("img/test_cloudbt.png"));
        _Cloud.setBounds(30, 400, 400, 50);
        _Cloud.setBorderPainted(false);
        _Cloud.setFocusPainted(false);
        _Cloud.setContentAreaFilled(false);
        _Cloud.setPressedIcon(new ImageIcon("img/tset_cloudbt_hv.png"));
        _Cloud.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//button event
        	}
        });
        
        _layeredPane.add(_FileSend);
        _layeredPane.add(_FileReceive);
        _layeredPane.add(_ProtectedFile);
        _layeredPane.add(_Cloud);
        _layeredPane.add(_panel);
             
        getContentPane().add(_layeredPane);          
        setVisible(true);
	}
}
