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
import Crypto.KeyReposit;

public class Client_FileRecovery extends JFrame implements DropTargetListener{
	private BufferedImage img = null;
	private final int MAX_BUTTON = 18;
	
	private JLabel _downloadArea;
	private DropTarget _dropTarget;
	private List _loadedFileList;
	private JButton[] Button;
	
	private ArrayList<String> _directoryArray;
	private ArrayList<String> _nameArray;
	private ArrayList<JButton> _list;
	private boolean _passCheck = true;
	
	private String[] _fileList;
	private String[] _name=null;
	private int _x=0;
	private int _y=0;
	private int _nowPage = 0;
	private int _page;
	
	private JButton _Select;
	private JButton _Download;
	private JButton _Right;
	private JButton _Left;

	private Container container;
	private JLayeredPane layeredPane = new JLayeredPane();
	private  MyPanel panel = new MyPanel();
	
	private Font fontbt = new Font("SansSerif", Font.BOLD,24);
	private Font _precondition_font = new Font ("Dialog", Font.BOLD,20);
	
	private Client_FolderSelector _cfs = null;
	private Client_File_Download _cfd = null;
	private Client_Folder_List _cfl = null;
	private String _downloadDirectory;
	
	private KeyReposit _reposit;

	
	public static void main(String args[])
	{
		new  Client_FileRecovery(null);
	}


	public Client_FileRecovery(String[] fileList){
		
		_reposit = KeyReposit.getInstance();
		_cfs = new Client_FolderSelector();
		_cfd = new Client_File_Download();
		_cfl = new Client_Folder_List();
		_directoryArray = new ArrayList<String>();
		_nameArray = new ArrayList<String>();
		_list = new ArrayList<JButton>();
		_fileList = fileList;
		_downloadArea = new JLabel();
		_downloadArea.setBounds(2, 69, 800, 384);
		_dropTarget = new DropTarget(_downloadArea, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);
		this.add(_downloadArea, BorderLayout.CENTER);
		
		try
		{
			 Toolkit tk = Toolkit.getDefaultToolkit(); 
			 Image image = tk.getImage("gui/logo.png");
			 this.setIconImage(image);
		}
		catch(Exception e)
		{
			System.out.println("Appilcation icon not found");
		}	
		
		container=getContentPane();
		container.setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(0,0,816,480);
		//setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		container.setLayout(null);
		
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
        
        if((_name.length % MAX_BUTTON) == 0)
        {
        	_page = (_name.length / MAX_BUTTON);
        }
        else
        {
        	_page = (_name.length / MAX_BUTTON) + 1;
        }
        
		allocator();
		button();
		page();
		basic();
        setVisible(true);
	}
	
	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
            g.drawImage(img, 0, 0, null);
            g.setColor(Color.BLACK);
			g.setFont(_precondition_font);
			g.drawString((_nowPage + 1) + "/" + _page, 705, 390);
        }
   }
	
	private void basic()
	{
		layeredPane.add(_Left);
		layeredPane.add(_Right);
		layeredPane.add(_Download);
        layeredPane.add(_Select);
        layeredPane.add(panel);
        container.add(layeredPane);
	}
	
	private void page()
	{
		int buttonCount = 0;
		if(_nowPage + 1 == _page)
		{
			buttonCount = _list.size() % MAX_BUTTON;
		}
		else
		{
			buttonCount = MAX_BUTTON;
		}
		
		for(int i = 0; i < buttonCount; i++)
		{
			layeredPane.add(_list.get(i + (MAX_BUTTON * _nowPage)));
		}
	}	
	
	private void allocator()
	{
		 _Select = new JButton(new ImageIcon("img/select.png"));
		 _Select.setRolloverIcon(new ImageIcon("img/selectR.png"));
		 _Select.setBounds(680, 120, 80, 40);
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

		 _Download = new JButton(new ImageIcon("img/DOWNLOAD.png"));	
		 _Download.setRolloverIcon(new ImageIcon("img/DOWNLOADR.png"));
		 _Download.setBounds(680, 250, 80,45);
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
					 _cfd.requestFile(_directoryArray.get(i), _downloadDirectory + "\\" + _nameArray.get(i), _reposit.get_aesKey());
				 }
			 }
		 });	
				
		 
		 _Left = new JButton(new ImageIcon("img/LEFT.png"));
		 _Left.setRolloverIcon(new ImageIcon("img/LEFTR.png"));
		 _Left.setBounds(635, 362, 80, 40);
		 _Left.setFocusPainted(false);
		 _Left.setContentAreaFilled(false);
		 _Left.setBorderPainted(false);
		 _Left.addActionListener(new ActionListener() 
		 {     
			 public void actionPerformed(ActionEvent arg0)
			 {	
				 _nowPage -= 1;
				 if(_nowPage < 0)
				 {
					 _nowPage += 1;
					 showMessage("error", "Here is the first page!");
				 }
				 else
				 {   	
					 layeredPane.removeAll();

					 page();
					 basic();
					 repaint();
				 }
			 }
		 });
		 
		 _Right = new JButton(new ImageIcon("img/RIGHT.png"));
		 _Right.setRolloverIcon(new ImageIcon("img/RIGHTR.png"));
		 _Right.setBounds(724, 362, 80, 40);
		 _Right.setFocusPainted(false);
		 _Right.setContentAreaFilled(false);
		 _Right.setBorderPainted(false);
		 _Right.addActionListener(new ActionListener() 
		 {     
			 public void actionPerformed(ActionEvent arg0)
			 {	
				 _nowPage += 1;
				 if(_nowPage + 1 > _page)
				 {
					 _nowPage -= 1;
					 showMessage("error", "Here is the last page!");
				 }
				 else
				 { 
					 layeredPane.removeAll();
			         		
					 page();
					 basic();
					 repaint();
				 } 
			 }
		 });

	}
	private void button()
	{
		Button = new JButton[_name.length];
		int number = 7;
		for(int i = 1; i < _name.length + 1; i++)
		{
			if(i > 6)
			{
				if(number == i)
				{
					if((i % 18) == 1)
					{
						_x = 0;
						_y = 0;
						number += 6;
					}
					else
					{
						_x = 0;
						_y += 120;
						number += 6;
					}
				}
				else
				{
					_x += 105;
				}
			}
			else
			{
				if(i > 1)
				{
					_x += 105;
				}
			}

			Button[i-1] = new JButton(_name[i-1],new ImageIcon("gui/logo_mini.png"));
			_list.add(Button[i-1]);
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
					int index = findIndex(e.getActionCommand());
					StringTokenizer st = new StringTokenizer(e.getActionCommand(), ".");
					if(st.countTokens() == 1)	// Folder
					{
						_cfl.running(_fileList[index]);
						String[] inFolder = _cfl.getFileList();
						for(int i = 0; i < inFolder.length; i++)
						{
							System.out.println(inFolder[i]);
						}
					}
					else	// File
					{
						_passCheck = true;
						//int index = findIndex(e.getActionCommand());
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
					}
					
					for(int k = 0 ; k < _directoryArray.size(); k++)
					{
						System.out.println(_directoryArray.get(k));
					}
					System.out.println("----------------------");
				}
			});
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
