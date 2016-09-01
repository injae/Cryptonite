package Client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JTextField;


public class Client_Group_Name extends JFrame{
	public static void main(String[] args){
		new Client_Group_Name(null);
	}
	
	private static String _id;
	
	BufferedImage _img = null;
	
	JButton _create;  
	JButton _cancel;
	
	JTextField _NameField;
	
	Client_Main_UI main;
/*	Client_Testmain test=new Client_Testmain();*/
	
	static String Name;
	
	public Client_Group_Name(String id){
		_id = id;
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
        setBounds(449,329,449,329);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        getContentPane().setLayout(null);
        JLayeredPane _layeredPane = new JLayeredPane();
        _layeredPane.setBounds(0,0, 600, 300);
        _layeredPane.setLayout(null);


        try {
            _img = ImageIO.read(new File("gui/groupname.png"));//input image
        } catch (IOException e) {
            System.out.println("No Image");
            System.exit(0);
        }
        
        MyPanel _panel = new MyPanel();
        _panel.setBounds(0, 0, 600, 300);
        
        _NameField = new JTextField(15);
        _NameField.setBounds(173, 150, 200, 31);
        _NameField.setOpaque(true);
        _NameField.setForeground(Color.BLACK);
        _NameField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        _NameField.setHorizontalAlignment(JTextField.CENTER);
        _NameField.addKeyListener(new KeyListener(){
     		@Override
     		public void keyPressed(KeyEvent e) {}
     		@Override
     		public void keyReleased(KeyEvent e) {
     			Name = _NameField.getText();
     		}
     		@Override
     		public void keyTyped(KeyEvent e) {}
           });
        	_NameField.addMouseListener(new MouseAdapter(){
         	public void mouseClicked(MouseEvent e){
         		 _NameField.setText("");
         	}
         });
        _layeredPane.add( _NameField);
        
        _create = new JButton(new ImageIcon("gui/create.png"));//input buttonimage
        _create.setBounds(70, 220, 141, 53);
        _create.setBorderPainted(false);
        _create.setFocusPainted(false);
        _create.setContentAreaFilled(false);
        _create.setRolloverIcon(new ImageIcon("gui/createR.png"));
        //_create.setPressedIcon(new ImageIcon("gui/create.png"));
        _create.addMouseListener(new MouseAdapter(){
         	public void mouseClicked(MouseEvent e){
         		System.out.println("마우스 클릭됨");
         		if(Name=="")
         		{
         			showMessage("ERROR", "Please insert GroupName!");
         		}
         		else
         		{
	         		String[] test = new String[1];
	         		test[0] = _id;
	        		String temp = Name;
	        		
	        		new Client_Make_Group().make(test, temp);
	        		byte[] event = new Client_Receive_Event().getEvent();
	        		byte[] buffer = new byte[1021]; 
	        		for(int i = 0; i < buffer.length; i++)
	        		{
	        			buffer[i] = event[i+3];
	        		}
	        		System.out.println("Name ="+Name);
	        		System.out.println("GroupCode : " + new String(buffer).trim());
	        		System.out.println("GroupName : " + temp);
	         		
	        		dispose();
	        		//main.dispose();
	        		new Client_Group_Main(_id, new String(buffer).trim(), 1);
         		}
         	}
        });
        
        
        _cancel = new JButton(new ImageIcon("gui/gn_cancel.png"));//input buttonimage
        _cancel.setBounds(250, 220, 141, 53);
        _cancel.setBorderPainted(false);
        _cancel.setFocusPainted(false);
        _cancel.setContentAreaFilled(false);
        _cancel.setRolloverIcon(new ImageIcon("gui/gn_cancelR.png"));
        _cancel.setPressedIcon(new ImageIcon("gui/gn_cancel.png"));
       	_cancel.addActionListener(new ActionListener() {     
         	public void actionPerformed(ActionEvent arg0) {
         		dispose();
         	}
         });
        
        _layeredPane.add(_cancel);
        _layeredPane.add(_create);
        _layeredPane.add(_panel);
        
        getContentPane().add(_layeredPane);          
        setVisible(true);
	}
	public static String getID()
	{
		return _id;
	}
	
	public static String GiveName(){
		return Name;
	}
	
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(_img, 0, 0, null);
        }
    }
	
	 private void showMessage(String title, String message) {
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		}
}
