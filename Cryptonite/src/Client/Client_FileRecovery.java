package Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.util.List;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import Client.Client_Main_UI.MyPanel;

public class Client_FileRecovery extends JFrame implements DropTargetListener{
	private BufferedImage img = null;
	
	private JLabel _downloadArea;
	private DropTarget _dropTarget;
	private List _loadedFileList;
	private JButton[] Button;
	
	private ArrayList<String> _directoryArray;
	private ArrayList<String> _nameArray;
	private boolean _passCheck = true;
	
	private String[] _fileList;
	private String[] _name=null;
	private int _x=0;
	private int _y=0;
	private int _i=0;
	
	private JButton _Select;
	private JButton _Download;
	
	private Font fontbt = new Font("SansSerif", Font.BOLD,24);
	private Font _precondition_font = new Font ("Dialog", Font.BOLD,20);
	
	private Client_FolderSelector _cfs = null;
	private Client_File_Download _cfd = null;
	private String _downloadDirectory;

	
	public static void main(String args[])
	{
		new  Client_FileRecovery(null);
	}


	public Client_FileRecovery(String[] fileList){
		
		_cfs = new Client_FolderSelector();
		_cfd = new Client_File_Download();
		_directoryArray = new ArrayList<String>();
		_nameArray = new ArrayList<String>();
		_fileList = fileList;
		_downloadArea = new JLabel();
		_downloadArea.setBounds(2, 69, 800, 384);
		_dropTarget = new DropTarget(_downloadArea, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);
		this.add(_downloadArea, BorderLayout.CENTER);
		
		try{
			 Toolkit tk = Toolkit.getDefaultToolkit(); 
			 Image image = tk.getImage("gui/logo.png");
			 this.setIconImage(image);
		}
		catch(Exception e)
		{
			System.out.println("Appilcation icon not found");
		}	
		
		getContentPane().setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(0,0,816,480);
		//setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 816, 480);
        layeredPane.setLayout(null);
        
        try 
        {
            img = ImageIO.read(new File("img/File Recovery_BG.png"));
        }
        catch (IOException e)
        {
            System.out.println("이미지 불러오기 실패");
            System.exit(0);
        }
        
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 816, 480);

        _name = new String[_fileList.length];
        for(int i = 0; i < _fileList.length; i++)
        {	
        	StringTokenizer st=new StringTokenizer(_fileList[i], "\\");

        	while(st.hasMoreTokens())
        	{
    			_name[i] = st.nextToken();
    		}
    	}
    
        _Select = new JButton(new ImageIcon("img/select.png"));
        _Select.setRolloverIcon(new ImageIcon("img/selectR.png"));
        _Select.setBounds(685, 300, 80, 40);
        _Select.setFocusPainted(false);
        _Select.setContentAreaFilled(false);
        _Select.setBorderPainted(false);
        _Select.addActionListener(new ActionListener() 
        {     
         	public void actionPerformed(ActionEvent arg0)
         	{	
         		_cfs.folderSelectorON();
         		while(!_cfs.getSelectionEnd())
         		{
         			try 
         			{
						Thread.sleep(1);
					}
         			catch (InterruptedException e)
         			{
						e.printStackTrace();
					}
         		}
         		_downloadDirectory = _cfs.getSelectedPath();
         	}
         });

        _Download = new JButton(new ImageIcon("img/OK.png"));	
		_Download.setRolloverIcon(new ImageIcon("img/OKR.png"));
		_Download.setBounds(700, 100, 45,45);
		_Download.setVerticalTextPosition ( SwingConstants.BOTTOM ) ;
		_Download.setVerticalAlignment    ( SwingConstants.TOP ) ;
		_Download.setHorizontalTextPosition( SwingConstants.CENTER ) ;
		_Download.setBorderPainted(false);
		_Download.setFocusPainted(false);
		_Download.setContentAreaFilled(false);
		_Download.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				for(int i = 0; i < _directoryArray.size(); i++)
				{
					_cfd.requestFile(_directoryArray.get(i), _downloadDirectory + "\\" + _nameArray.get(i));
				}
			}
		});

		Button = new JButton[_name.length];
		int number=7;
		for(int i = 1; i < _name.length + 1; i++)
		{
			if(i>6){
				if(number==i){
					_x=0;
					_y+=120;
					number+=6;
				}
				else
				{
					_x+=100;
				}
			}
			else
			{
				if(i>1){_x+=100;}
			}

			/*if((i % 6) == 0){
				_y+=120;
				Button[i - 1] = new JButton(_name[i - 1],new ImageIcon("gui/logo_mini.png"));		
				Button[i - 1].setPressedIcon(new ImageIcon("gui/logo_mini.png"));
				Button[i - 1].setBounds(10,(70 + _y), 92, 120);
				Button[i - 1].setVerticalTextPosition ( SwingConstants.BOTTOM ) ;
				Button[i - 1].setVerticalAlignment    ( SwingConstants.TOP ) ;
				Button[i - 1].setHorizontalTextPosition( SwingConstants.CENTER ) ;
				Button[i - 1].setBorderPainted(false);
				Button[i - 1].setFocusPainted(false);
				Button[i - 1].setContentAreaFilled(false);
				Button[i - 1].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						_passCheck = true;
						int index = findIndex(e.getActionCommand());
						if(!_directoryArray.isEmpty())
						{
							for(int j = 0; j < _directoryArray.size(); j++)
							{
								if(_fileList[index].equals(_directoryArray.get(j)))
								{
									_directoryArray.remove(j);
									_nameArray.remove(j);
									_passCheck = false;
									break;
								}
							}
							if(_passCheck)
							{
								_directoryArray.add(_fileList[index]);
								_nameArray.add(e.getActionCommand());
							}
						}
						else
						{
							_directoryArray.add(_fileList[index]);
							_nameArray.add(e.getActionCommand());
						}
						Button[index].setBackground(Color.BLACK);
						
						for(int k = 0 ; k < _directoryArray.size(); k++)
						{
							System.out.println(_directoryArray.get(k));
						}
						System.out.println("----------------------");
					}
				});
				_x=120;
			}
			else{
				Button[i-1] = new JButton(_name[i-1],new ImageIcon("gui/logo_mini.png"));		
				Button[i-1].setPressedIcon(new ImageIcon("gui/logo_mini.png"));
				Button[i-1].setBounds((10+_x),(70+_y),92,120);
				Button[i-1].setVerticalTextPosition ( SwingConstants.BOTTOM ) ;
				Button[i-1].setVerticalAlignment    ( SwingConstants.TOP ) ;
				Button[i-1].setHorizontalTextPosition( SwingConstants.CENTER ) ;
				Button[i-1].setBorderPainted(false);
				Button[i-1].setFocusPainted(false);
				Button[i-1].setContentAreaFilled(false);
				Button[i-1].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						_passCheck = true;
						int index = findIndex(e.getActionCommand());
						if(!_directoryArray.isEmpty())
						{
							for(int j = 0; j < _directoryArray.size(); j++)
							{
								if(_fileList[index].equals(_directoryArray.get(j)))
								{
									_directoryArray.remove(j);
									_nameArray.remove(j);
									_passCheck = false;
									break;
								}
							}
							if(_passCheck)
							{
								_directoryArray.add(_fileList[index]);
								_nameArray.add(e.getActionCommand());
							}
						}
						else
						{
							_directoryArray.add(_fileList[index]);
							_nameArray.add(e.getActionCommand());
						}
						Button[index].setBackground(Color.BLACK);
						
						for(int k = 0 ; k < _directoryArray.size(); k++)
						{
							System.out.println(_directoryArray.get(k));
						}
						System.out.println("----------------------");
					}
				});
				_x+=120;
			}*/
			Button[i-1] = new JButton(_name[i-1],new ImageIcon("gui/logo_mini.png"));		
			Button[i-1].setPressedIcon(new ImageIcon("gui/logo_mini.png"));
			Button[i-1].setBounds((10+_x),(70+_y),92,120);
			Button[i-1].setVerticalTextPosition ( SwingConstants.BOTTOM ) ;
			Button[i-1].setVerticalAlignment    ( SwingConstants.TOP ) ;
			Button[i-1].setHorizontalTextPosition( SwingConstants.CENTER ) ;
			Button[i-1].setBorderPainted(false);
			Button[i-1].setFocusPainted(false);
			Button[i-1].setContentAreaFilled(false);
			Button[i-1].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					_passCheck = true;
					int index = findIndex(e.getActionCommand());
					if(!_directoryArray.isEmpty())
					{
						for(int j = 0; j < _directoryArray.size(); j++)
						{
							if(_fileList[index].equals(_directoryArray.get(j)))
							{
								_directoryArray.remove(j);
								_nameArray.remove(j);
								_passCheck = false;
								break;
							}
						}
						if(_passCheck)
						{
							_directoryArray.add(_fileList[index]);
							_nameArray.add(e.getActionCommand());
						}
					}
					else
					{
						_directoryArray.add(_fileList[index]);
						_nameArray.add(e.getActionCommand());
					}
					Button[index].setBackground(Color.BLACK);
					
					for(int k = 0 ; k < _directoryArray.size(); k++)
					{
						System.out.println(_directoryArray.get(k));
					}
					System.out.println("----------------------");
				}
			});
			layeredPane.add(Button[i-1]);
		}
		
		
		
		layeredPane.add(_Download);
        layeredPane.add(_Select);
        layeredPane.add(panel);
        getContentPane().add(layeredPane);
        setVisible(true);
	}
	
	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
            g.drawImage(img, 0, 0, null);
        }
   }
	private void showMessage(String title, String message) 
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private int findIndex(String text)
	{
		int temp = 0;
		
		for(int i = 0; i < _name.length; i++)
		{
			if(_name[i].equals(text))
			{
				temp = i;
			}
		}
		
		return temp;
	}


	@Override
	public void dragEnter(DropTargetDragEvent dtde) 
	{
		//System.out.println("dropEnter");
	}


	@Override
	public void dragExit(DropTargetEvent dte) 
	{
		//System.out.println("dragExit");
	}


	@Override
	public void dragOver(DropTargetDragEvent dtde) 
	{
		//System.out.println("dragOver");
	}


	@Override
	public void drop(DropTargetDropEvent dtde) 
	{
		System.out.println("dragDrop");
		if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0)
        {
            dtde.acceptDrop(dtde.getDropAction());
            Transferable tr = dtde.getTransferable();
            try
            {
                //파일명 얻어오기
            	_loadedFileList = (List) tr.getTransferData(DataFlavor.javaFileListFlavor);
             
                //파일명 출력
                for(int i = 0; i < _loadedFileList.size(); i++)
                {
                	System.out.println(_loadedFileList.get(i).toString());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}


	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) 
	{
		//System.out.println("dragActionChanged");
	}
}
