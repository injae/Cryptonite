package Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import Client.Client_Main_UI.MyPanel;

public class Client_FileRecovery extends JFrame{
	private BufferedImage img = null;

	private JButton _Cancel;
	private JButton OK;
	
	private Font fontbt = new Font("SansSerif", Font.BOLD,24);
	private Font _precondition_font = new Font ("Dialog", Font.BOLD,20);

	
	public static void main(String args[])
	{
		new  Client_FileRecovery();
	}


	public Client_FileRecovery(){
		
		getContentPane().setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(0,0,816,480);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 816, 480);
        layeredPane.setLayout(null);
        
        try 
        {
            img = ImageIO.read(new File("img/File Recovery_BG.png"));
        }
        catch (IOException e)
        {
            System.out.println("이미지 불러오기 실패");
            System.exit(0);
        }
        
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 816, 480);

        OK = new JButton(new ImageIcon("img/OK.png"));
        OK.setRolloverIcon(new ImageIcon("img/OKR.png"));
        OK.setBounds(600, 360, 45, 45);
        OK.setFocusPainted(false);
        OK.setContentAreaFilled(false);
        OK.setBorderPainted(false);
        OK.addActionListener(new ActionListener() {     
         	public void actionPerformed(ActionEvent arg0)
         	{	
         		
         	}
         });
        
        _Cancel = new JButton(new ImageIcon("img/Cancel.png"));		
		_Cancel.setPressedIcon(new ImageIcon("img/Cancelp.png"));
		_Cancel.setBounds(350, 360, 80,40);
		_Cancel.setBorderPainted(false);
		_Cancel.setFocusPainted(false);
		_Cancel.setContentAreaFilled(false);
		_Cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();		
			}
		});
		
		layeredPane.add(_Cancel);
        layeredPane.add(OK);
        layeredPane.add(panel);
        getContentPane().add(layeredPane);
        setVisible(true);
	}
	
	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
            g.drawImage(img, 0, 0, null);
        }
   }
	private void showMessage(String title, String message) 
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
