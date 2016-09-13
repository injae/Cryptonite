package Client;

import java.util.*;

import javax.swing.JOptionPane;

import java.io.*;
import java.lang.*;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;

/*
 * Developer : Youn Hee Seung
 * Date : 2016 - 07 - 22
 * 
 * Name : Folder Auto Scan
 * Description : When you select SecureFolder, then it can perceive file event(CREATE, DELETE, MODIFICATION)
 * 
 * */


public class Client_FolderScan extends Thread
{
	// WatchService Instance
	private boolean _passCheck  = false;
	private WatchService _watchService = null;
	private WatchKey _watchKey = null;
	private boolean _firstTime = false;
	private String _protectedFolderName = null;
	
	// Filenames, to make absolute directory, check directory
	private String _fileName = null;
	private String _address = null;
	private String _absoluteDirectory = null;
	private File _isDirectory = null;
	
	// StopFlag
	private boolean _stopFlag = false;
	
	// Another Class
	private Client_FolderSelector _cfs = null;
	private Client_AutoBackup _cab = null;
	
	// Constructors
	public Client_FolderScan() 
	{
		_firstTime = true;
	}
	
	public Client_FolderScan(String address)
	{
		this._firstTime = false;
		this._address = address;
		_cab = new Client_AutoBackup();
		this.start();
	}
	
	// Methods
	public synchronized void run()
	{
		while(!Client_Login._autoBackupList.isEmpty())
		{
			try 
			{
				sleep(1);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
		if(_firstTime)
		{
			try 
			{
				FileReader fr = new FileReader(new File("Cryptonite_Client/log/protectedlog.ser"));
				BufferedReader br = new BufferedReader(fr);
				_address = br.readLine();
				br.close();
				fr.close();
				_cab = new Client_AutoBackup(_address);
				_protectedFolderName = _cab.getProtectedName();
				_firstTime = false;
			} 
			catch (FileNotFoundException e1) 
			{
				e1.printStackTrace();
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		File firstScan = new File(_address);
		String[] fileList = firstScan.list();
		try 
		{
			for(int i = 0; i < fileList.length; i++)
			{
				File temp = new File(_address + "\\" + fileList[i]);
				Client_Login._autoBackupList.offer(temp.getAbsolutePath()); //
				if(temp.isDirectory())
				{
					System.out.println("New Folder is Created >> " + fileList[i]);
					Client_FolderScan cfs = new Client_FolderScan(_address + "\\" + fileList[i]);
					Client_Login._folderScanList.offer(cfs);
				}
				else if(temp.isFile())
				{
					System.out.println("New File is Created >> " + _address + "\\" + fileList[i]);
				}
				_cab.autoBackup(_address + "\\" + fileList[i]);
			}
		
			_watchService = FileSystems.getDefault().newWatchService();
			Path directory = Paths.get(_address);
		    directory.register(_watchService, StandardWatchEventKinds.ENTRY_CREATE);
		    
		    while(_stopFlag == false)
		    {
	        	_watchKey = _watchService.take();
	        	List<WatchEvent<?>> _list = _watchKey.pollEvents();
        
	        	for(WatchEvent _watchEvent : _list) 
	        	{
	        		Kind kind = _watchEvent.kind();
	        		Path path = (Path)_watchEvent.context();
              
	        		if(kind == StandardWatchEventKinds.ENTRY_CREATE)
	        		{
	        			_isDirectory = new File(_address + "\\" + path.getFileName().toString());
	        			Client_Login._autoBackupList.offer(_isDirectory.getAbsolutePath());
	        			if(_isDirectory.isDirectory() == true) 
	        			{
	        				_fileName = path.getFileName().toString();
	        				System.out.println("New Folder is Created >> " + _fileName);
	        				_absoluteDirectory = _isDirectory.getPath();
	        				Client_FolderScan cfs = new Client_FolderScan(_absoluteDirectory);
	        				Client_Login._folderScanList.offer(cfs);
	        				_cab.autoBackup(_absoluteDirectory);
	        			}
	        			else
	        			{
	        				_fileName = path.getFileName().toString();
	        				System.out.println("New File is Created >> " + _fileName);
	        				_absoluteDirectory = _isDirectory.getPath();
	        				File deleteTemp = new File(_absoluteDirectory + ".cnec");
	        				if(deleteTemp.exists())
	        				{
	        					deleteTemp.delete();
	        				}
	        				_cab.autoBackup(_absoluteDirectory);
	        			}
	        		}
	        		else if(kind == StandardWatchEventKinds.OVERFLOW) 
	        		{
	        			showMessage("Directory Existance Error", "Protected Folder is gone ...");
	        			_stopFlag = true;
	        			break;
	        		}
	        	}
           
	        	boolean valid = _watchKey.reset();
	        	
	        	if(!valid)
	        		break;
	        }
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		catch(ClosedWatchServiceException cwe)
		{
			System.out.println("WatchService Thread was closed successfully.");
			this.interrupt();
		}
		catch(NullPointerException e)
		{
			showMessage("ERROR","Protected Folder Was deleted.");
			_cfs = new Client_FolderSelector();
			_cfs.folderSelectorON();
			while (!_cfs.getSelectionEnd()) 
			{
				try 
				{
					Thread.sleep(1);
				} catch (InterruptedException e1) 
				{
					e1.printStackTrace();
				}
			}
			_address = _cfs.getSelectedPath();
			File save = new File("Cryptonite_Client/log/protectedlog.ser");
			try 
			{
				FileWriter fw = new FileWriter(save);
				fw.write(_address);
				fw.close();
			} 
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			showMessage("Notification","Restart the Cryptonite Please.");
			System.exit(0);
		}
	}
	
	public void stopThread()
	{
		try 
		{
			_watchService.close();
			this._stopFlag = true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void showMessage(String title, String message) 
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}