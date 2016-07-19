package Client;

import java.util.*;
import java.io.*;
import java.lang.*;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;

public class Client_FolderScan extends Thread
{
	// WatchService Instance
	private WatchService _watchService = null;
	private WatchKey _watchKey = null;
	private Vector<String> _stringVector = null;
	
	// Filenames, to make absolute directory, check directory
	private String _fileName = null;
	private String _address = null;
	private String _absoluteDirectory = null;
	private File _isDirectory = null;
	
	// StopFlag
	private boolean _stopFlag = false;
	
	// Methods
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
	
	public synchronized void run()
	{
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
	        		Kind _kind = _watchEvent.kind();
	        		Path _path = (Path)_watchEvent.context();
              
	        		if(_kind == StandardWatchEventKinds.ENTRY_CREATE)	// New File(Directory) was created.
	        		{
	        			_isDirectory = new File(_address + "\\" + _path.getFileName().toString());
	        			if(_isDirectory.isDirectory() == true) 
	        			{
	        				// This is folder, write new work.
	        			}
	        			else // This is file
	        			{
	        				_fileName = _path.getFileName().toString();
	        				System.out.println("New File Created >> " + _fileName);
	        				_absoluteDirectory = _isDirectory.getPath();
	        				_stringVector.add(_absoluteDirectory);
	        				//CEM = new Client_EncryptManagement(realAddress, AES_Key);
	        			}
	        		}
	        		else if(_kind == StandardWatchEventKinds.ENTRY_DELETE)	// // New File(Directory) was deleted.
	        		{
	        			for(int i = 0; i < _stringVector.size(); i++) 
	        			{
	        				if(_path.getFileName().toString().equals(_stringVector.get(i)))
	        				{
	        					_stringVector.remove(i);
	        					break;
	        				}
	        			}
	        		}
	        		else if(_kind == StandardWatchEventKinds.OVERFLOW) 
	        		{
	        			System.out.println("Directory is gone...");
	        			break;
	        		}
	        	}
           
	        	boolean valid = _watchKey.reset();   // watchKey reset
                                   					 // If reset is success, then return true
	        	if(!valid)
	        		break;                     // If reset is fail, then return false and folderscan will be down.
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
}