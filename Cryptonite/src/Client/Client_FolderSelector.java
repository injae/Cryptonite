package Client;

import java.io.File;

import javax.swing.JFileChooser;

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
	
	public void folderSelectorON()
	{
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showOpenDialog(chooser);
	  	File dir = chooser.getSelectedFile();
	  	selectedPath = dir.getPath();
	  	selectionEnd = true;
	}
	
	public String getSelectedPath()
	{
		return this.selectedPath;
	}
	
	public boolean getSelectionEnd()
	{
		return selectionEnd;
	}
}
