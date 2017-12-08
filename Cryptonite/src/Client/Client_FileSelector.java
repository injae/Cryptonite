package Client;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 29
 * 
 * Name : FileSelector system
 * Description : It can select files
 * 
 * */

public class Client_FileSelector extends Frame
{
	// Instance
	private File[] _selectedFiles = null;
	private String[] _fileNames = null;
	private String[] _filePaths = null;
	private boolean _selectionFinish = false;
	//private Button b1 = new Button("���� ����");
	
	// Constructors
	public Client_FileSelector() { }
	 /**
	  * @param args
	  */
	
	// Methods
	public void fileFinderON()
	{
		  FileDialog fd = new FileDialog( this, "파일 선택", FileDialog.LOAD);
		  fd.setMultipleMode(true);
		  fd.setVisible(true);
		  _selectedFiles = fd.getFiles();					// ���ϰ�ü �޾ƿ��°�
		  
		  if(_selectedFiles.length == 0)
		  {
			  this._selectionFinish = true;
		  }
		  else
		  {
			  _fileNames = new String[_selectedFiles.length];		// �����̸� ��Ʈ���迭 ����
			  _filePaths = new String[_selectedFiles.length];
			  
			  for(int i = 0; i < _selectedFiles.length; i++)
			  {
				  _filePaths[i] = _selectedFiles[i].getPath();
				  _fileNames[i] = _selectedFiles[i].getName();
			  }
			  
			  if(_selectedFiles.length != 0)
			  {
				  this._selectionFinish = true;
			  }  
		  }	}
	 
	public String[] getFileNames()
	{
		return _fileNames;
	}
	 
	public String[] getFilePaths()
	{
		this._selectionFinish = false;
		return _filePaths;
	}
	 
	public boolean getSelectionFinish()
	{
		return this._selectionFinish;
	}
			 
	private void showMessage(String title, String message) 
	{
		Font fontbt = new Font("SansSerif", Font.BOLD,24);
		JLabel input = new JLabel(message);
		input.setFont(fontbt);
		JOptionPane.showMessageDialog(null, input, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
