package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JWindow;
import javax.swing.JRadioButton;

public class Client_Setting 
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
		
		_close = new JButton(new ImageIcon("img/DClose.png"));
		_close.setRolloverIcon(new ImageIcon("img/DCloseR.png"));
		_close.setBounds(555, 230, 180, 120);
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
		_layeredPane.add(_background);
		
		_window.getContentPane().add(_layeredPane);
		
		JLabel lblUsePasswordEndecryption = new JLabel("Use Password file decryption");
		lblUsePasswordEndecryption.setBounds(0, 25, 481, 35);
		_layeredPane.add(lblUsePasswordEndecryption);
		
		JRadioButton rdbtnTrue = new JRadioButton("TRUE");
		rdbtnTrue.setBounds(69, 128, 128, 43);
		_layeredPane.add(rdbtnTrue);
		
		JRadioButton rdbtnFalse = new JRadioButton("FALSE");
		rdbtnFalse.setBounds(220, 128, 142, 43);
		_layeredPane.add(rdbtnFalse);
		_window.setSize(400, 224);
		_window.setLocationRelativeTo(null);
		_window.setAlwaysOnTop(true);
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