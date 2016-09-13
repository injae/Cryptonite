package Client;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JWindow;

public class Client_Progressbar extends JWindow
{
	// Instance
	private Image _fileSendingImage;
	private Image _progressBarImage;
	
	private JLabel _sendingLabel;
	private JLabel _progressBarLabel;
	
	private JLayeredPane _layeredPane;
	//private JWindow _window;
	
	// Constructors
	public Client_Progressbar()
	{
		_fileSendingImage = Toolkit.getDefaultToolkit().getImage("img/FileSending.png");
		_progressBarImage = Toolkit.getDefaultToolkit().getImage("img/progressBar.gif");
		
		_layeredPane = new JLayeredPane();
		_sendingLabel = new JLabel(new ImageIcon(_fileSendingImage));
		_progressBarLabel = new JLabel(new ImageIcon(_progressBarImage));
		//_window = new JWindow();
		
		_sendingLabel.setBounds(10, 10, 240, 70);
		_progressBarLabel.setBounds(10, 80, 240, 30);
		_sendingLabel.setVisible(true);
		_progressBarLabel.setVisible(true);
		_layeredPane.add(_sendingLabel);
		_layeredPane.add(_progressBarLabel);
		
		this.getContentPane().add(_layeredPane);
		this.setSize(260, 120);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		/*_window.getContentPane().add(_layeredPane);
		_window.setSize(260, 120);
		_window.setLocationRelativeTo(null);
		_window.setVisible(true);*/
	}
	
	// Methods
	public void UI_ON()
	{
		this.setVisible(true);
	}
	
	public void UI_OFF()
	{
		this.setVisible(false);
	}
}