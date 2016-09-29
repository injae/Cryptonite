package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

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

import Client.Client_FileRecovery.RecoveryButton;
import Client.Client_Group_Main.MyPanel;
import Crypto.KeyReposit;



public class Client_Group_Main extends JFrame
{
	private BufferedImage _img = null;
	private BufferedImage _img2 = null;
	
	private final int MAX_BTN = 18;
	private final int COLUMN = 3;
	private final int ROW = 6;
	
	private JButton _Back;
	private JButton _Download;
	private JButton _Right;
	private JButton _Left;
	private JButton _Search;
	private JTextField _idField;
	private JButton _OK;
	private JButton _Withdrawal;
	private JButton _Delete;
	private JButton _Upload;
	
	private int _nowPage = 0;
	private int _page;
	
	private Container container;
	private JLayeredPane layeredPane = new JLayeredPane();
	
	public class RecoveryButton
	{
		public RecoveryButton(String fullpath)
		{
			fullPath = fullpath;
			
			StringTokenizer st = new StringTokenizer(fullpath, "\\");
			
			while(st.hasMoreTokens())
			{
				fileName = st.nextToken();
			}
			
			isClick = false;
			
			st = new StringTokenizer(fileName, ".");
			if(st.countTokens() == 1) { isDir = true; }
			else 					  { isDir = false;}
		}
		
		private boolean isClick;
		public void click()
		{
			if(isClick)  { isClick = false; }
			else 		 { isClick = true;  }
		}
		public String noExtensionName() { return fileName.substring(0, fileName.length() - 5); }
		
		public boolean isDir;
		public JButton button;
		public String fullPath;
		public String fileName;
	}
	private ArrayList<RecoveryButton> _btnList;
	
	private String _downloadPath = "NULL";
	private Stack<ArrayList<RecoveryButton>> _undo;
	
	private Font fontbt = new Font("SansSerif", Font.BOLD,10);
	private Font precondition_font = new Font ("Dialog", Font.BOLD,20);

	private boolean _mod;
	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
        	if(_mod)
        	{
        		g.drawImage(_img, 0, 0, null);
        		g.setColor(Color.BLACK);
        		g.setFont(precondition_font);
        		g.drawString("Group Name : " + _gpName, 230, 35);
      			g.setFont(precondition_font);
      			g.drawString((_nowPage + 1) + "/" + _page, 300, 428);
        	}
        	else
        	{
        		g.drawImage(_img2, 0, 0, null);
        		g.setColor(Color.BLACK);
        		g.setFont(fontbt);
        		g.drawString("Group Name : " + _gpName, 230, 35);
        		g.setFont(precondition_font);
      			g.drawString((_nowPage + 1) + "/" + _page, 476, 428);
        	}
        }
   }
	private  MyPanel panel = new MyPanel();
	
	private String _gpCode;
	private String _gpName;
	private String _id;
	private String _searchID;
	
	private String[] _searchList;
	private JList<String> _list;
	private JScrollPane scrollPane;
	private DefaultListModel<String> _model;
	
	public Client_Group_Main(String id, String gpCode, String gpName, boolean mod, ArrayList<String> fileList) 
	{
		_gpCode = gpCode;
		_gpName = gpName;
		_mod = mod;
		_id = id;
		
		_btnList = new ArrayList<RecoveryButton>();
		_undo = new Stack<ArrayList<RecoveryButton>>();
		while(!fileList.isEmpty())
		{
			_btnList.add(new RecoveryButton(fileList.remove(0)));
		}
		pageCount();
		
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
		setResizable(false);
		setBounds(0,0,801,478);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		container.setLayout(null);
		
		getContentPane().setLayout(null);
        layeredPane.setBounds(0, 0, 816, 480);
        layeredPane.setLayout(null);
        
        try 
        {
        	_img = ImageIO.read(new File("gui/초대목록화면re.png"));
            _img2 = ImageIO.read(new File("img/초대목록화면re2.png"));
        }
        catch (IOException e)
        {
            System.out.println("Image Load Failed.");
            System.exit(0);
        }
        
        panel.setBounds(0, 0, 816, 480);
		
        allocator();
		makeBtn();
		page();
		setMod();
        setVisible(true);
	}

	private void setMod()
	{
		 layeredPane.add(_Upload);
		 layeredPane.add(_Download);
		 layeredPane.add(_Left);
		 layeredPane.add(_Right);
	     layeredPane.add(_Delete);
	     layeredPane.add(_Back);
	     
		if(_mod)
		{
			
			layeredPane.add(_Search);
	    	layeredPane.add( _idField);
	        layeredPane.add(_OK);
	        layeredPane.remove(_Withdrawal);
		}
		else
		{
	    	layeredPane.add(_Withdrawal);
		}
		
    	layeredPane.add(panel);
        container.add(layeredPane);
	}
	private void page() 
	{
		try
		{
			int k = _nowPage;
			for(int j = 0; j < COLUMN; j++)
			{
				for(int i = 0; i < ROW; i++)
				{		
					layeredPane.add(_btnList.get(k * MAX_BTN + (j * ROW) + i).button);
					if(_btnList.size() - 1 <= (k * MAX_BTN + (j * ROW) + i)) { return; }
				}
			}
		}
		catch(IndexOutOfBoundsException e1)
		{
		}
	}
	private void pageCount()
	{
		if((_btnList.size() % MAX_BTN) == 0)
		{
			_page = (_btnList.size() / MAX_BTN);
			if(_btnList.isEmpty()) { _page  = 1;}
		}
		else
		{
			_page = (_btnList.size() / MAX_BTN) + 1;
		}
		_nowPage = 0;
	}	
	private void makeBtn() 
	{
		try
		{
			for(int k = 0; k < _page; k++)
			{
				int x = 10 , y = 0;
				for(int j = 0; j < COLUMN; j++)
				{
					x = 15;
					for(int i = 0; i < ROW; i++)
					{		
						if(_btnList.get(k * MAX_BTN + (j * ROW) + i).isDir)
						{
							makeFolder(k * MAX_BTN + (j * ROW) + i, x, y);
						}
						else
						{
							makeFile(k * MAX_BTN + (j * ROW) + i, x, y);
						}
						if(_btnList.size() - 1 <= (k * MAX_BTN + (j * ROW) + i)) { return; }
						x += 100;
					}
					y += 100;
				}
			}
		}
		catch(IndexOutOfBoundsException e1)
		{
		}
	}

	private void makeFolder(int index, int x, int y) 
	{
		if(_btnList.get(index).button!=null){return;}
		JButton btn = new JButton(_btnList.get(index).fileName, new ImageIcon("gui/_folder.png"));
		btn.setFont(fontbt);
		btn.setToolTipText(_btnList.get(index).fileName);
		btn.setPressedIcon(new ImageIcon("gui/_folder.png"));
		btn.setBounds((6+x),(75+y),92,120);
		btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn.setVerticalAlignment(SwingConstants.TOP);
		btn.setHorizontalTextPosition(SwingConstants.CENTER);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setContentAreaFilled(false);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				for(int i = 0; i < _btnList.size(); i++)
				{
					if(_btnList.get(i).isClick)
					{
						_btnList.get(i).isClick=false;
						_btnList.get(i).button.setIcon(new ImageIcon("gui/file.png"));
					}
				}
				_undo.push(_btnList);
				String fullPath = _btnList.get(index).fullPath + "\\";
				
				ArrayList<String> files = new Client_Folder_List().running(_btnList.get(index).fullPath).getFileList();
				_btnList = new ArrayList<RecoveryButton>();
				
				for(int i =0; i < files.size(); i++)
				{
					_btnList.add(new RecoveryButton(fullPath + files.get(i)));
				}
				pageCount();
				layeredPane.removeAll();
				makeBtn();
				page();
				setMod();
				layeredPane.repaint();
			}
		});
		_btnList.get(index).button = btn;
	}
	private void makeFile(int index, int x, int y) 
	{
		if(_btnList.get(index).button!=null) {return;}
		JButton btn = new JButton(_btnList.get(index).fileName, new ImageIcon("gui/file.png"));
		btn.setFont(fontbt);
		btn.setToolTipText(_btnList.get(index).noExtensionName());
		btn.setPressedIcon(new ImageIcon("gui/file.png"));
		btn.setBounds((6+x),(75+y),92,120);
		btn.setVerticalTextPosition(SwingConstants.BOTTOM);
		btn.setVerticalAlignment(SwingConstants.TOP);
		btn.setHorizontalTextPosition(SwingConstants.CENTER);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setContentAreaFilled(false);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				_btnList.get(index).click();
				layeredPane.removeAll();
				makeBtn();
				page();
				setMod();
				layeredPane.repaint();

				if(!_btnList.get(index).isClick)
				{	
					_btnList.get(index).button.setIcon(new ImageIcon("gui/file.png"));
				}
				else
				{
					_btnList.get(index).button.setIcon(new ImageIcon("gui/file_check.png"));
				}
			}
		});
		_btnList.get(index).button = btn;
	}

	private void allocator() 
	{
		_Upload = new JButton(new ImageIcon("gui/upload.png"));
        _Upload.setRolloverIcon(new ImageIcon("gui/uploadR.png"));
        _Upload.setBounds(650, 9, 80, 45);
        _Upload.setFocusPainted(false);
        _Upload.setContentAreaFilled(false);
        _Upload.setBorderPainted(false);
        _Upload.addActionListener(new ActionListener() {     
        	public void actionPerformed(ActionEvent arg0)
        	{	
        		Client_File_Upload _cfu = new Client_File_Upload();
        		_cfu.setGpCode(_gpCode);
        		_cfu.start();

        		//pageCount();
        		//layeredPane.removeAll();
				//makeBtn();
				//page();
				//setMod();
				//layeredPane.repaint();
        	}
        });
        
        _Download = new JButton(new ImageIcon("gui/download.png"));	
		_Download.setRolloverIcon(new ImageIcon("gui/downloadR.png"));
		_Download.setBounds(600, 7, 80, 45);
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
				selectFolder();
	         	if(_downloadPath != null)
	         	{
	         		for(int i =0; i < _btnList.size(); i++)
					 {
						 if(_btnList.get(i).isClick)
						 {
							 new Client_File_Download().requestFile(_btnList.get(i).fullPath, _downloadPath + "\\" + _btnList.get(i).fileName, new Client_Get_Group_Key().running(_gpCode));
						 }
					 }
	         	}
			 }
	   });
		

		 _Back = new JButton(new ImageIcon("gui/back_icon.png"));
		 _Back.setRolloverIcon(new ImageIcon("gui/back_iconR.png"));
		 _Back.setBounds(740, 8, 60, 40);
		 _Back.setFocusPainted(false);
		 _Back.setContentAreaFilled(false);
		 _Back.setBorderPainted(false);
		 _Back.addActionListener(new ActionListener() 
		 {     
			 public void actionPerformed(ActionEvent arg0)
			 {	
				 if(_undo.isEmpty()) return;
				_btnList=_undo.pop();
				
				layeredPane.removeAll();
				makeBtn();
				page();
				setMod();
				layeredPane.repaint();
			 }
		 });
		 
		
		_idField = new JTextField(15);
        _idField.setBounds(653, 115, 85, 31);
        _idField.setOpaque(false);
        _idField.setForeground(Color.BLACK);
        _idField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        _idField.setHorizontalAlignment(JTextField.CENTER);
        _idField.addKeyListener(new KeyListener()
        {
     		@Override
     		public void keyPressed(KeyEvent e) {}
     		@Override
     		public void keyReleased(KeyEvent e) 
     		{
     			_searchID = _idField.getText();
     		}
     		@Override
     		public void keyTyped(KeyEvent e) {}
        });
    	_idField.addMouseListener(new MouseAdapter()
    	{
    		public void mouseClicked(MouseEvent e)
    		{
    			_idField.setText("");
    		}
    	});
    	
    	_Search = new JButton(new ImageIcon("gui/search.png"));
        _Search.setRolloverIcon(new ImageIcon("gui/searchR.png"));
        _Search.setBounds(755, 113, 35, 35);
        _Search.setFocusPainted(false);
        _Search.setContentAreaFilled(false);
        _Search.setBorderPainted(false);
        _Search.addActionListener(new ActionListener() {     
          	public void actionPerformed(ActionEvent arg0)
          	{	
          		Client_Group_Search cgs = new Client_Group_Search();
          		cgs.setDefault();
          		cgs.search(_searchID);
          		_searchList = cgs.getID();
          		if(_searchList.length == 0)
          		{
          			if(_list != null)
          			{
          				_list.setVisible(false);
          			}
          			showMessage("ID Error","There is no ID which exists.");          			
          		}
          		else
          		{
          			if(scrollPane!=null)
          			{
          				layeredPane.remove(scrollPane);
          			}

          			_model = new DefaultListModel<>();
                     for(int i = 0; i < _searchList.length; i++)
                     {
                      	if(!_searchList[i].equals(_id))
                      	{
                      		_model.addElement(_searchList[i]);
                      	}
                     }
                     _list = new JList<>(_model);
                     _list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                     scrollPane = new JScrollPane(_list);
                     scrollPane.setVisible(true);
                     scrollPane.setBounds(636, 200, 115, 150);
                     layeredPane.add(scrollPane);
          		}
          	}
          });
        
        _OK = new JButton(new ImageIcon("gui/check.png"));
        _OK.setRolloverIcon(new ImageIcon("gui/checkR.png"));
        _OK.setBounds(750, 310, 45, 45);
        _OK.setFocusPainted(false);
        _OK.setContentAreaFilled(false);
        _OK.setBorderPainted(false);
        _OK.addActionListener(new ActionListener() {     
         	public void actionPerformed(ActionEvent arg0)
         	{	
         		String selectedID = _model.getElementAt(_list.getSelectedIndex());
         		String check = new Client_Group_Invite().running(selectedID, _gpCode);
         		if(check.equals("TRUE"))
         		{
         			showMessage("Notification","Member was added completely.");
         		}
         		else if(check.equals("FALSE"))
         		{
         			showMessage("Notification","Meber was duplicated.");
         		}
         	}
         });
        
        _Withdrawal = new JButton(new ImageIcon("gui/delete_me.png"));
        _Withdrawal.setRolloverIcon(new ImageIcon("gui/delete_meR.png"));
        _Withdrawal.setBounds(685, 388, 80, 40);
        _Withdrawal.setFocusPainted(false);
        _Withdrawal.setContentAreaFilled(false);
        _Withdrawal.setBorderPainted(false);
        _Withdrawal.addActionListener(new ActionListener() {     
         	public void actionPerformed(ActionEvent arg0)
         	{
         		new Client_Group_Withdrawal().running(_gpCode);
         		dispose();
         	}
         });
        
        _Delete = new JButton(new ImageIcon("gui/group_delete.png"));
        _Delete.setRolloverIcon(new ImageIcon("gui/group_deleteR.png"));
        _Delete.setBounds(670, 398, 80, 40);
        _Delete.setFocusPainted(false);
        _Delete.setContentAreaFilled(false);
        _Delete.setBorderPainted(false);
        _Delete.addActionListener(new ActionListener() {     
         	public void actionPerformed(ActionEvent arg0)
         	{
         		new Client_Delete_Group().deleteGroup(_gpCode);
         		dispose();
         	}
         });
        
        _Left = new JButton(new ImageIcon("gui/Left.png"));
		_Left.setRolloverIcon(new ImageIcon("gui/LeftR.png"));
		_Left.setBounds(244, 400, 80, 40);
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
					 setMod();
					 repaint();
				 }
			 }
		 });
		 
		_Right = new JButton(new ImageIcon("gui/Right.png"));
		_Right.setRolloverIcon(new ImageIcon("gui/RightR.png"));
		_Right.setBounds(304, 400, 80, 40);
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
					 setMod();
					 repaint();
				 } 
			 }
		 });
 
	}
	
	private void selectFolder()
	{
		Client_FolderSelector cfs = new Client_FolderSelector();
     	cfs.folderSelectorON();
     	while(!cfs.getSelectionEnd())
     	{
     		try 
     		{
				Thread.sleep(1);
			}
    		catch (InterruptedException e1)
     		{
				e1.printStackTrace();
			}
     	}
     	_downloadPath = cfs.getSelectedPath();
	}
	
	private void showMessage(String title, String message) 
	{
		Font fontbt = new Font("SansSerif", Font.BOLD,24);
		JLabel input = new JLabel(message);
		input.setFont(fontbt);
		JOptionPane.showMessageDialog(null, input, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
}
