package Client;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 29
 * 
 * Name : FileShare
 * Description : It can share file
 * 
 * */

public class Client_FolderSelector
{
	private JFileChooser chooser = new JFileChooser();
	private String selectedPath = null;
	private boolean selectionEnd = false;
	
	public Client_FolderSelector()
	{
		
	}
	
	public boolean folderSelectorON()
	{
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showOpenDialog(chooser);
	  	File dir = chooser.getSelectedFile();
	  	try
	  	{
	  		selectedPath = dir.getPath();
		  	selectionEnd = true;
		  	return true;
	  	}
	  	catch(NullPointerException e)
	  	{
	  		showMessage("Error", "Folder is not selected.");
	  		selectionEnd = true;
	  		return false;
	  	}
	}
	
	public String getSelectedPath()
	{
		return this.selectedPath;
	}
	
	public boolean getSelectionEnd()
	{
		return selectionEnd;
	}
	
	private void showMessage(String title, String message) 
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
