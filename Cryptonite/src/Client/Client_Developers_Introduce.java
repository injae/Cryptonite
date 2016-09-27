package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JWindow;

public class Client_Developers_Introduce 
{
	// Instance
	private JWindow _window;
	private JLabel _background;
	private JButton _close;
	private JLayeredPane _layeredPane;
	
	public static void main(String[] args)
	{
		new Client_Developers_Introduce().UI_ON();
	}
	
	// Constructors
	public Client_Developers_Introduce()
	{
		_layeredPane = new JLayeredPane();
		_window = new JWindow();
		
		_background = new JLabel(new ImageIcon("img/Developers.png"));
		_background.setBounds(0, 0, 800, 448);
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
		_window.setSize(800, 448);
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
		_window.setVisible(false);
		_window.dispose();
	}
}