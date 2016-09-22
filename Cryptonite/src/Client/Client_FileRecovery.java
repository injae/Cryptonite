package Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import java.util.Stack;
import java.util.StringTokenizer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import Crypto.KeyReposit;

public class Client_FileRecovery extends JFrame
{	
	private BufferedImage _img = null;
	private final int MAX_BTN = 18;
	private final int COLUMN = 3;
	private final int ROW = 6;
	
	private JButton _Select;
	private JButton _Download;
	private JButton _Right;
	private JButton _Left;
	private JButton _Back;
	
	private int _nowPage = 0;
	private int _page;
	
	private Container container;
	private JLayeredPane layeredPane = new JLayeredPane();

	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
            g.drawImage(_img, 0, 0, null);
            g.setColor(Color.BLACK);
			g.setFont(_precondition_font);
			g.drawString((_nowPage + 1) + "/" + _page, 307, 406);
        }
   }
	private MyPanel panel = new MyPanel();

	private Font fontbt = new Font("SansSerif", Font.BOLD,10);
	private Font _precondition_font = new Font ("Dialog", Font.BOLD,15);
	
	
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
	private String _downloadPath;
	private Stack<ArrayList<RecoveryButton>> _undo;
	
	
	public Client_FileRecovery(ArrayList<String> fileList) 
	{
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
		setBounds(0,0,656,480);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		container.setLayout(null);
		
        layeredPane.setBounds(0, 0, 656, 480);
        layeredPane.setLayout(null);
        
        try 
        {
            _img = ImageIO.read(new File("gui/File Recovery_BG.png"));
        }
        catch (IOException e)
        {
            System.out.println("Image Load Failed.");
            System.exit(0);
        }
        
        panel.setBounds(0, 0, 656, 480);
		
        allocator();
		makeBtn();
		page();
		basic();
        setVisible(true);
        
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
	
	private void makeFile(int index, int x, int y)
	{
		if(_btnList.get(index).button!=null) { return; }
		JButton btn = new JButton(_btnList.get(index).fileName, new ImageIcon("gui/file.png"));
		btn.setFont(fontbt);
		btn.setToolTipText(_btnList.get(index).noExtensionName());
		btn.setPressedIcon(new ImageIcon("gui/file.png"));
		btn.setBounds((7+x),(80+y),80,120);
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
				basic();
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
	
	private void makeFolder(int index, int x, int y)
	{
		if(_btnList.get(index).button!=null){return;}
		JButton btn = new JButton(_btnList.get(index).fileName, new ImageIcon("gui/_folder.png"));
		btn.setPressedIcon(new ImageIcon("gui/_folderR.png"));
		btn.setToolTipText(_btnList.get(index).fileName);
		btn.setFont(fontbt);
		btn.setBounds((7+x),(80+y),75,110);
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
				basic();
				layeredPane.repaint();
				
			}
		});
		_btnList.get(index).button = btn;
	}
	
	private void basic()
	{
		layeredPane.add(_Left);
		layeredPane.add(_Right);
		layeredPane.add(_Download);
       // layeredPane.add(_Select);
        layeredPane.add(_Back);
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
	
	private void allocator()
	{
		 _Select = new JButton(new ImageIcon("img/select.png"));
		 _Select.setRolloverIcon(new ImageIcon("img/selectR.png"));
		 _Select.setBounds(400, 10, 80, 40);
		 _Select.setFocusPainted(false);
		 _Select.setContentAreaFilled(false);
		 _Select.setBorderPainted(false);
		 _Select.addActionListener(new ActionListener() 
		 {     
			 public void actionPerformed(ActionEvent arg0)
			 {	
				Client_FolderSelector cfs = new Client_FolderSelector();
				cfs.folderSelectorON();
				 while(!cfs.getSelectionEnd())
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
				 _downloadPath = cfs.getSelectedPath();
			 }
		 });

		 _Download = new JButton(new ImageIcon("gui/download_icon.png"));	
		 _Download.setRolloverIcon(new ImageIcon("gui/download_iconR.png"));
		 _Download.setBounds(490, 10, 70,70);
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
				 for(int i =0; i < _btnList.size(); i++)
				 {
					 if(_btnList.get(i).isClick)
					 {
						 new Client_File_Download().requestFile(_btnList.get(i).fullPath, _downloadPath + "\\" + _btnList.get(i).fileName, KeyReposit.getInstance().get_aesKey());
					 }
				 }
			 }
		 });	
				
		 
		 _Left = new JButton(new ImageIcon("gui/Left.png"));
		 _Left.setRolloverIcon(new ImageIcon("gui/LeftR.png"));
		 _Left.setBounds(250, 380, 80, 40);
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
		 
		 _Right = new JButton(new ImageIcon("gui/Right.png"));
		 _Right.setRolloverIcon(new ImageIcon("gui/RightR.png"));
		 _Right.setBounds(305, 380, 80, 40);
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

		 _Back = new JButton(new ImageIcon("gui/back_icon.png"));
		 _Back.setRolloverIcon(new ImageIcon("gui/back_iconR.png"));
		 _Back.setBounds(550, 10, 80, 40);
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
				pageCount();
				page();
				basic();
				repaint();
			 }
		 });
		 
	}
	
	private void showMessage(String title, String message) 
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
}