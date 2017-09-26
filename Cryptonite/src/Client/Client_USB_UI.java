package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.JWindow;

public class Client_USB_UI {
	
	private JWindow _window;
	private JLabel _background;
	private JLayeredPane _layeredPane;
	
	private JButton _close;
	private JButton _register;
	private JButton _openfd;
	
	public static void main(String[] args)
	{
		new Client_USB_UI().UI_ON();
	}
	
	
	public Client_USB_UI()
	{
		_layeredPane = new JLayeredPane();
		_window = new JWindow();
		
		_background = new JLabel(new ImageIcon("img/Setting.png"));
		_background.setBounds(0, 0, 400, 224);
		_background.setVisible(true);
		
		_register = new JButton(new ImageIcon("img/register.jpg"));
		_register.setRolloverIcon(new ImageIcon("img/register.jpg"));
		_register.setBounds(50, 100, 120, 120);
		_register.setVisible(true);
		_register.setBorderPainted(false);
		_register.setFocusPainted(false);
		_register.addActionListener(new ActionListener() 
 	    {
 	       	public void actionPerformed(ActionEvent arg0) 
 	       	{
 	       		new Client_USB_register();
 	       		
 	       		
 	       	}
 	    });
		_layeredPane.add(_register);
		
		_openfd = new JButton(new ImageIcon("img/open.jpg"));
		_openfd.setRolloverIcon(new ImageIcon("img/open.jpg"));
		_openfd.setBounds(250,100, 120, 120);
		_openfd.setVisible(true);
		_openfd.setBorderPainted(false);
 	    _openfd.setFocusPainted(false);
		_openfd.addActionListener(new ActionListener() 
 	    {
 	       	public void actionPerformed(ActionEvent arg0) 
 	       	{
 	       		//mount+check 후 폴더 열기
 	       	}
 	    });
		_layeredPane.add(_openfd);
		
		_close = new JButton(new ImageIcon("img/SClose.png"));
		_close.setRolloverIcon(new ImageIcon("img/SCloseR.png"));
		_close.setBounds(170, 270, 70, 40);
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
		
		
		
		_window.getContentPane().add(_layeredPane);
		_window.setSize(400, 350);
		_window.setLocationRelativeTo(null);
		_window.setAlwaysOnTop(false);
	}
	public void UI_ON()
	{
		_window.setVisible(true);
	}
	private void UI_OFF()
	{
		_window.dispose();
	}
}
