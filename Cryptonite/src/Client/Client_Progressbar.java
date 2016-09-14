package Client;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JWindow;

public class Client_Progressbar
{
	// Instance
	private Image _fileSendingImage = Toolkit.getDefaultToolkit().getImage("img/FileSending.png");
	private Image _progressBarImage = new ImageIcon("img/progressBar.gif").getImage();
	
	private JLabel _sendingLabel;
	private JLabel _progressBarLabel;
	
	private JLayeredPane _layeredPane;
	private JWindow _window;
	
	public static void main(String[] args)
	{
		new Client_Progressbar();
	}
	
	// Constructors
	public Client_Progressbar()
	{
		_layeredPane = new JLayeredPane();
		_sendingLabel = new JLabel(new ImageIcon(_fileSendingImage));
		_progressBarLabel = new JLabel(new ImageIcon("img/progressBar.gif"));
		_window = new JWindow();
		
		_sendingLabel.setBounds(10, 10, 240, 70);
		_progressBarLabel.setBounds(10, 80, 240, 30);
		_sendingLabel.setVisible(true);
		_progressBarLabel.setVisible(true);
		_layeredPane.add(_sendingLabel);
		_layeredPane.add(_progressBarLabel);
		
		_window.getContentPane().add(_layeredPane);
		_window.setSize(260, 120);
		_window.setLocationRelativeTo(null);
	}
	
	// Methods
	public void UI_ON()
	{
		_window.setVisible(true);
	}
	
	public void UI_OFF()
	{
		_window.setVisible(false);
	}
}