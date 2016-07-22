package Client;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class Client_TrayIcon implements ActionListener
{
	private SystemTray systemTray;
	private PopupMenu mPopup;
	private MenuItem menuOC, menuOPF, menuLogout, menuExit;
	
	public Client_TrayIcon()
	{
		try 
		{
			initSystemTrayIcon();
		} 
		catch (AWTException awte) 
		{
			System.out.println("##### Error occurred during create UI!!!");
			System.out.println(awte.toString());
			System.exit(0);
		}
	}

	private void initSystemTrayIcon() throws AWTException 
	{
		if (SystemTray.isSupported())
		{
			mPopup = new PopupMenu();
			menuOC = new MenuItem("Cryptonite execute");
			menuOPF = new MenuItem("Open Secure folder");
			menuLogout = new MenuItem("Logout");
			menuExit = new MenuItem("Exit");

			menuOC.addActionListener(this);
			menuOPF.addActionListener(this);
			menuLogout.addActionListener(this);
			menuExit.addActionListener(this);

			mPopup.add(menuOC);
			mPopup.addSeparator();
			mPopup.add(menuOPF);
			mPopup.addSeparator();
			mPopup.add(menuLogout);
			mPopup.add(menuExit);

			Image image = Toolkit.getDefaultToolkit().getImage("C:\\cryptonite\\img\\trayicon.png");
			TrayIcon trayIcon = new TrayIcon(image, "Cryptonite", mPopup);
			trayIcon.setImageAutoSize(true);

			systemTray = SystemTray.getSystemTray();
			systemTray.add(trayIcon);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == menuOC) 
		{
			
		} 
		else if (ae.getSource() == menuOPF) 
		{
			
		} 
		else if (ae.getSource() == menuLogout) 
		{
			
		} 
		else if (ae.getSource() == menuExit) 
		{
			
		}
	}

	private void showMessage(String title, String message) 
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
