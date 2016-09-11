package Client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.DefaultListModel;
import javax.swing.Icon;



public class Client_Group_Main extends JFrame{
	
	private BufferedImage img = null;
	private BufferedImage img2 = null;

	private JButton _Upload;
	private JButton _Select;
	private JButton _Download;
	private JButton Search;
	private JButton[] Button;
	private JButton OK;
	private JButton Withdrawal;
	private JButton Delete;
	private JTextField _idField;
	
	private boolean _passCheck = true;

	private ArrayList<String> _directoryArray;
	private ArrayList<String> _nameArray;
	
	private String[] _result;
	private String[] _fileList;
	private String[] _name;
	private String _id;
	private String _receivedID;
	private String _gpCode;
	private String _gpName;
	private String _downloadDirectory;
	private int _mod;
	private int _x=0;
	private int _y=0;
	private boolean _checkmod = true;
	
	private DefaultListModel<String> _model;
	private JList<String> _list;
	private JScrollPane scrollPane = null;
	private Container container;
	private JLayeredPane layeredPane = new JLayeredPane();
	private  MyPanel panel = new MyPanel();
	
	private int _count = 1;
	
	private Font fontbt = new Font("SansSerif", Font.BOLD,24);
	private Font _precondition_font = new Font ("Dialog", Font.BOLD,20);
	
	private Client_FolderSelector _cfs;
	private Client_Group_Search _cgs;
	private Client_Group_Invite _cgi;
	private Client_Group_Withdrawal _cgw;
	private Client_Delete_Group _cdg;
	private Client_File_ListReceiver _cfl;
	private Client_File_Download _cfd;
	private Client_File_Upload _cfu;
	private Client_Get_Group_Key _cggk;
	
	public static void main(String args[])
	{
		new Client_Group_Main(null, null, null, 0, null);
	}


	public Client_Group_Main(String id, String gpCode, String gpName, int mod, Client_File_ListReceiver cfl){
		
		_receivedID = id;
		_gpCode = gpCode;
		_gpName = gpName;
		_mod = mod;
		_cggk = new Client_Get_Group_Key();
		_cfu = new Client_File_Upload();
		_cfs = new Client_FolderSelector();
		_cgs = new Client_Group_Search();
		_cgi = new Client_Group_Invite();
		_cgw = new Client_Group_Withdrawal();
		_cdg = new Client_Delete_Group();
		_cfd = new Client_File_Download();
		_cfl = cfl;
		_fileList = _cfl.getFileList();
		_directoryArray = new ArrayList<String>();
		_nameArray = new ArrayList<String>();
		
		
		 _name = new String[_fileList.length];
	        for(int i = 0; i < _fileList.length; i++)
	        {	
	        	StringTokenizer st=new StringTokenizer(_fileList[i], "\\");

	        	while(st.hasMoreTokens())
	        	{
	    			_name[i] = st.nextToken();
	    		}
	    	}
	
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
		
		getContentPane().setLayout(null);
        layeredPane.setBounds(0, 0, 816, 480);
        layeredPane.setLayout(null);
        
        try 
        {
            img = ImageIO.read(new File("img/초대목록화면.png"));
            img2 = ImageIO.read(new File("img/초대목록화면2.png"));
        }
        catch (IOException e)
        {
            System.out.println("이미지 불러오기 실패");
            System.exit(0);
        }
        
        panel.setBounds(0, 0, 816, 480);
        
        allocator();
        button();
       
        if(_mod == 1)
        {	
        	mod1();
        }
        else
        {
        	mod2();
        }
        
        setVisible(true);
	}
	
	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
        	if(_checkmod)
        	{
        		g.drawImage(img, 0, 0, null);
        		g.setColor(Color.BLACK);
        		g.setFont(fontbt);
        		g.drawString("Group Name : " + _gpName, 230, 35);
        	}
        	else
        	{
        		g.drawImage(img2, 0, 0, null);
        		g.setColor(Color.BLACK);
        		g.setFont(fontbt);
        		g.drawString("Group Name : " + _gpName, 230, 35);
        	}
        }
   }
	
	private void mod1()
	{
		layeredPane.add(Search);
    	layeredPane.add( _idField);
        layeredPane.add(OK);
        layeredPane.remove(Withdrawal);
        layeredPane.add(Delete);
    	layeredPane.add(panel);
        container.add(layeredPane);
	}
	
	private void mod2()
	{
		_checkmod = false;
    	layeredPane.remove(Delete);
    	layeredPane.add(Withdrawal);
    	layeredPane.add(panel);
        container.add(layeredPane);
	}
	
	private void allocator()
	{
		
		_Upload = new JButton(new ImageIcon("img/upload.png"));
        _Upload.setRolloverIcon(new ImageIcon("img/uploadR.png"));
        _Upload.setBounds(700, 8, 80, 45);
        _Upload.setFocusPainted(false);
        _Upload.setContentAreaFilled(false);
        _Upload.setBorderPainted(false);
        _Upload.addActionListener(new ActionListener() {     
        	public void actionPerformed(ActionEvent arg0)
        	{	
        		_cfu.click(_gpCode);
        		_cfl.running((byte)1, _gpCode);
        		_fileList = _cfl.getFileList();
        		_x=0;
        		_y=0;
        		
        		_name = new String[_fileList.length];
    	        for(int i = 0; i < _fileList.length; i++)
    	        {	
    	        	StringTokenizer st=new StringTokenizer(_fileList[i], "\\");

    	        	while(st.hasMoreTokens())
    	        	{
    	    			_name[i] = st.nextToken();
    	    		}
    	    	}
        		
        		layeredPane.removeAll();
        		
        		for(int i=0;i<_fileList.length;i++)
        		{
        			System.out.println(_fileList[i]);
        			if(_fileList.length==0)
        			{
        				System.out.println("아무것도 없습니다.");
        			}
        		}
        		
        	    allocator();
        	    button();
        	    
    	        if(_mod == 1)
    	        {	
    	        	mod1();
    	        }
    	        else
    	        {
    	        	mod2();
    	        }
    	       repaint();
         	}
        });
        layeredPane.add(_Upload);
        
        _Select = new JButton(new ImageIcon("img/select.png"));
        _Select.setRolloverIcon(new ImageIcon("img/selectR.png"));
        _Select.setBounds(400, 370, 80, 40);
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
        layeredPane.add(_Select);
        
        _Download = new JButton(new ImageIcon("img/DOWNLOAD.png"));	
		_Download.setRolloverIcon(new ImageIcon("img/DOWNLOADR.png"));
		_Download.setBounds(200, 370, 80,40);
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
					_cfd.requestFile(_directoryArray.get(i), _downloadDirectory + "\\" + _nameArray.get(i), _cggk.running(_gpCode));
				}
			}
		});
		layeredPane.add(_Download);
        
	    _idField = new JTextField(15);
        _idField.setBounds(640, 115, 100, 31);
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
     			_id = _idField.getText();
     		}
     		@Override
     		public void keyTyped(KeyEvent e) {}
           });
        	_idField.addMouseListener(new MouseAdapter(){
         	public void mouseClicked(MouseEvent e){
         		 _idField.setText("");
         	}
         });
        
        
        Search = new JButton(new ImageIcon("img/Search.png"));
        Search.setRolloverIcon(new ImageIcon("img/Searchh.png"));
        Search.setBounds(740, 107, 50, 50);
        Search.setFocusPainted(false);
        Search.setContentAreaFilled(false);
        Search.setBorderPainted(false);
        Search.addActionListener(new ActionListener() {     
         	public void actionPerformed(ActionEvent arg0)
         	{	
         		_cgs.setDefault();
         		_cgs.search(_id);
         		_result=_cgs.getID();
         		if(_result.length == 0)
         		{
         			_list.setVisible(false);
         			showMessage("ID Error","존재하는 ID가 없습니다.");
         		}
         		else
         		{
         			if(_count != 1)
         			{
         				layeredPane.remove(scrollPane);
         			}
         			_count = 2;
         			_model = new DefaultListModel<>();
                    for(int i=0;i<_result.length;i++)
                    {
                     	if(!_result[i].equals(_receivedID))
                     	{
                     		_model.addElement(_result[i]);
                     	}
                    }
                    _list = new JList<>(_model);
                    _list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    scrollPane = new JScrollPane(_list);
                    scrollPane.setVisible(true);
                    scrollPane.setBounds(628, 200, 115, 150);
                    layeredPane.add(scrollPane);
         		}
         	}
         });
        
        OK = new JButton(new ImageIcon("img/OK.png"));
        OK.setRolloverIcon(new ImageIcon("img/OKR.png"));
        OK.setBounds(747, 304, 45, 45);
        OK.setFocusPainted(false);
        OK.setContentAreaFilled(false);
        OK.setBorderPainted(false);
        OK.addActionListener(new ActionListener() {     
         	public void actionPerformed(ActionEvent arg0)
         	{	
         		String selectedID = _model.getElementAt(_list.getSelectedIndex());
         		String check = _cgi.running(selectedID, _gpCode);
         		if(check.equals("TRUE"))
         		{
         			showMessage("Notification","그룹원 성공적으로 추가 되었습니다.");
         		}
         		else if(check.equals("FALSE"))
         		{
         			showMessage("Notification","그룹원이 중복 됩니다.");
         		}
         	}
         });

        
        
        Withdrawal = new JButton(new ImageIcon("img/Check.png"));
        Withdrawal.setRolloverIcon(new ImageIcon("img/Checkp.png"));
        Withdrawal.setBounds(685, 380, 50, 50);
        Withdrawal.setFocusPainted(false);
        Withdrawal.setContentAreaFilled(false);
        Withdrawal.setBorderPainted(false);
        Withdrawal.addActionListener(new ActionListener() {     
         	public void actionPerformed(ActionEvent arg0)
         	{
         		_cgw.running(_gpCode);
         		dispose();
         	}
         });
        
        
        Delete = new JButton(new ImageIcon("img/Settingbt.png"));
        Delete.setPressedIcon(new ImageIcon("img/Settinghbt.png"));
        Delete.setBounds(685, 380, 50, 50);
        Delete.setFocusPainted(false);
        Delete.setContentAreaFilled(false);
        Delete.setBorderPainted(false);
        Delete.addActionListener(new ActionListener() {     
         	public void actionPerformed(ActionEvent arg0)
         	{
         		_cdg.deleteGroup(_gpCode);
         		dispose();
         	}
         });

	}
	private void button()
	{
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
					_x+=105;
				}
			}
			else
			{
				if(i>1){_x+=105;}
			}

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


}