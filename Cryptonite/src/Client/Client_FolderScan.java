package Client;

import java.util.*;
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
	private WatchService _watchService = null;
	private WatchKey _watchKey = null;
	
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
		_cab = new Client_AutoBackup();
	}
	
	// Methods
	public synchronized void run()
	{
		_cfs = new Client_FolderSelector();
		_cfs.folderSelectorON();
		while(!_cfs.getSelectionEnd())
		{
			try {
				sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		_address = _cfs.getSelectedPath();
		File firstScan = new File(_address);
		String[] fileList = firstScan.list();
		for(int i = 0; i < fileList.length; i++)
		{
			_cab.autoBackup(_address + "\\" + fileList[i]);
		}
		
		try {
			_watchService = FileSystems.getDefault().newWatchService();
			Path directory = Paths.get(_address);
		    directory.register(_watchService, StandardWatchEventKinds.ENTRY_CREATE,
		                                  StandardWatchEventKinds.ENTRY_DELETE);
		    
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
	        			if(_isDirectory.isDirectory() == true) 
	        			{
	        				_fileName = path.getFileName().toString();
	        				System.out.println("New Folder is Created >> " + _fileName);
	        				_absoluteDirectory = _isDirectory.getPath();
	        				_cab.autoBackup(_absoluteDirectory);
	        			}
	        			else
	        			{
	        				_fileName = path.getFileName().toString();
	        				System.out.println("New File is Created >> " + _fileName);
	        				_absoluteDirectory = _isDirectory.getPath();
	        				_cab.autoBackup(_absoluteDirectory);
	        			}
	        		}
	        		else if(kind == StandardWatchEventKinds.OVERFLOW) 
	        		{
	        			System.out.println("Directory is gone...");
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
}