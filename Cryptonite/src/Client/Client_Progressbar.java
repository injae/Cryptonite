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
	private Image _stateImage;
	
	private JLabel _sendingLabel;
	private JLabel _progressBarLabel;
	
	private JLayeredPane _layeredPane;
	private JWindow _window;
	
	public static void main(String[] args)
	{
		new Client_Progressbar(4);
	}
	
	// Constructors
	public Client_Progressbar(int mod)	// (mod == 1) : File_Send, (mod == 2) : File_Download, (mod == 3) : File_Upload
	{
		switch(mod)
		{
		case 1:
			_stateImage = Toolkit.getDefaultToolkit().getImage("img/FileSending.png");
			break;
		case 2:
			_stateImage = Toolkit.getDefaultToolkit().getImage("img/FileDownloading.png");
			break;
		case 3:
			_stateImage = Toolkit.getDefaultToolkit().getImage("img/FileUploading.png");
			break;
		case 4:
			_stateImage = Toolkit.getDefaultToolkit().getImage("img/FileDecrypting.png");
			break;
		default:
			break;
		}
		
		_layeredPane = new JLayeredPane();
		_sendingLabel = new JLabel(new ImageIcon(_stateImage));
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
		_window.setAlwaysOnTop(true);
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