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
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.jar.Attributes.Name;
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
import javax.swing.plaf.synth.SynthSpinnerUI;

import org.omg.PortableServer._ServantActivatorStub;

import com.mysql.fabric.xmlrpc.base.Array;

import Client.Client_Main_UI.MyPanel;
import Crypto.KeyReposit;

public class Client_FileRecovery extends JFrame implements DropTargetListener
{	
	private BufferedImage _img = null;
	private final int MAX_BTN = 15;
	private final int COLUMN = 3;
	private final int ROW = 5;
	
	private DropTarget _dropTarget;
	
	private JLabel _downloadArea;
	private List _loadedFileList;

	private JButton _Select;
	private JButton _Download;
	private JButton _Right;
	private JButton _Left;
	private JButton _Back;
	
	private int _nowPage = 0;
	private int _page;
	private int[] count;
	
	private Container container;
	private JLayeredPane layeredPane = new JLayeredPane();

	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
            g.drawImage(_img, 0, 0, null);
            g.setColor(Color.BLACK);
			g.setFont(_precondition_font);
			g.drawString((_nowPage + 1) + "/" + _page, 705, 390);
        }
   }
	private MyPanel panel = new MyPanel();

	private Font fontbt = new Font("SansSerif", Font.BOLD,24);
	private Font _precondition_font = new Font ("Dialog", Font.BOLD,20);
	
	
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
		
		public boolean isDir;
		public JButton button;
		public boolean isClick;
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

		
		count=new int[_btnList.size()];
		for(int i=0;i<_btnList.size();i++)
		{
			count[i]=1;
		}
		pageCount();
		
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
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		container.setLayout(null);
		
        layeredPane.setBounds(0, 0, 816, 480);
        layeredPane.setLayout(null);
        
        try 
        {
            _img = ImageIO.read(new File("img/File Recovery_BG.png"));
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
						x += 120;
					}
					y += 120;
				}
			}
		}
		catch(IndexOutOfBoundsException e1)
		{
		}
	}
	
	private void makeFile(int index, int x, int y)
	{
		if(_btnList.get(index).button!=null){return;}
		JButton btn = new JButton(_btnList.get(index).fileName, new ImageIcon("gui/logo_mini.png"));
		btn.setPressedIcon(new ImageIcon("gui/logo_mini.png"));
		btn.setBounds((10+x),(70+y),92,120);
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
				_btnList.get(index).isClick = true;
				layeredPane.removeAll();
				makeBtn();
				page();
				basic();
				layeredPane.repaint();

				if((count[index]%2)==0)
				{	
					_btnList.get(index).button.setIcon(new ImageIcon("gui/logo_mini.png"));
					count[index]++;
				}
				else
				{
					_btnList.get(index).button.setIcon(new ImageIcon("img/logo_mini_click.png"));
					count[index]++;
				}
				/*for(int i=0;i<count.length;i++)
				{
					System.out.println(i+" : "+count[i]);
					if((count[i]%2)==0)
					{	
						_btnList.get(i).button.setIcon(new ImageIcon("gui/logo_mini.png"));
					}
					else
					{
						_btnList.get(i).button.setIcon(new ImageIcon("img/logo_mini_click.png"));
					}
				}*/
			}
		});
		_btnList.get(index).button = btn;
	}
	
	private void makeFolder(int index, int x, int y)
	{
		if(_btnList.get(index).button!=null){return;}
		JButton btn = new JButton(_btnList.get(index).fileName, new ImageIcon("img/folder.png"));
		btn.setPressedIcon(new ImageIcon("img/folderR.png"));
		btn.setBounds((10+x),(70+y),92,120);
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
					_btnList.get(i).isClick = false;
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
        layeredPane.add(_Select);
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
		 _Select.setBounds(680, 120, 80, 40);
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
				 for(int i =0; i < _btnList.size(); i++)
				 {
					 if(_btnList.get(i).isClick)
					 {
						 new Client_File_Download().requestFile(_btnList.get(i).fileName, _downloadPath + "\\" + _btnList.get(i).fileName, KeyReposit.getInstance().get_aesKey());
					 }
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
	
		 _Back = new JButton(new ImageIcon("img/LEFT.png"));
		 _Back.setRolloverIcon(new ImageIcon("img/LEFTR.png"));
		 _Back.setBounds(700, 10, 80, 40);
		 _Back.setFocusPainted(false);
		 _Back.setContentAreaFilled(false);
		 _Back.setBorderPainted(false);
		 _Back.addActionListener(new ActionListener() 
		 {     
			 public void actionPerformed(ActionEvent arg0)
			 {	
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
	
	@Override
	public void drop(DropTargetDropEvent dtde) 
	{
	
	}


	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO 자동 생성된 메소드 스텁
		
	}


	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO 자동 생성된 메소드 스텁
		
	}


	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		// TODO 자동 생성된 메소드 스텁
		
	}


	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO 자동 생성된 메소드 스텁
		
	}

}
