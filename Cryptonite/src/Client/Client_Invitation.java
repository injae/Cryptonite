package Client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.DefaultListModel;
import javax.swing.Icon;



public class Client_Invitation extends JFrame{
	
	private String _selectedGroup;
	private String _id;
	private String _choice;
	private String _gpCode = "null";
	
	private BufferedImage img = null;
	private BufferedImage img2=null;
	
	private JLayeredPane layeredPane = new JLayeredPane();
	private MyPanel panel = new MyPanel();
	private JScrollPane scrollPane;
	
	private JButton _Ok;
	private JButton _Cancel;
	
	private Container container;
	
	private DefaultListModel<String> _model;
	private JList<String> _list;
	
	private String[] _groupname;
	private int _count = 1;
	private boolean _checkimage = false;
	private boolean _passCheck = true;
	
	private Font fontbt = new Font("SansSerif", Font.BOLD,24);
	
	private Client_Show_Group _csg;
	private Client_Find_Captain _cfc;
	private Client_File_ListReceiver _cfl;
	
	public static void main(String args[]){
		new Client_Invitation (null, null);
	}


	public Client_Invitation (Client_Show_Group csg, String id){
		
		_id = id;
		_csg = csg;
		_cfc = new Client_Find_Captain();
		_cfl = new Client_File_ListReceiver();
		
		try{
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
		setBounds(500,300,800,480);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		container.setLayout(null);
		
        layeredPane.setBounds(0, 0, 800, 480);
        layeredPane.setLayout(null);
        
        try 
        {
            img = ImageIO.read(new File("img/초대목록.png"));
            img2 = ImageIO.read(new File("img/x.png"));
        } catch (IOException e) {
            System.out.println("Image Load Failed.");
            System.exit(0);
        }
        
        panel.setBounds(0, 0, 890, 500);
        
        _groupname = _csg.getGroupName();
         
		if(_groupname.length == 0)
		{	
			_checkimage = false;
		}
		else
		{
			_checkimage = true;
			_model = new DefaultListModel<>();
			
			for(int i = 0; i <  _groupname.length; i++)
			{
				_model.addElement( _groupname[i]);
			}
		
           _list = new JList<>(_model);
           _list.setFont(fontbt);
           _list.setBounds(100, 100, 100, 100);
           _list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
          
           scrollPane = new JScrollPane(_list);
           scrollPane.setVisible(true);
           scrollPane.setBounds(45, 95, 707, 287);
		}
        
		allocator();
		
		if(!_checkimage)
		{
			nohave();
		}
		else
		{
			have();
		}
		
        setVisible(true);
	}
	
	public void have(){
		layeredPane.removeAll();
		
		layeredPane.add(_Ok);
		layeredPane.add(_Cancel);
		
		layeredPane.add(scrollPane);
		layeredPane.add(panel);
		container.add(layeredPane);
	}
	public void nohave(){
		layeredPane.removeAll();
		
		layeredPane.add(_Cancel);
		
		layeredPane.add(panel);
		container.add(layeredPane);
	}
	public void allocator()
	{
		_Ok = new JButton(new ImageIcon("img/select.png"));
		_Ok.setRolloverIcon(new ImageIcon("img/selectR.png"));
		_Ok.setBounds(347, 400, 80,40);
		_Ok.setFocusPainted(false);
		_Ok.setContentAreaFilled(false);
		_Ok.setBorderPainted(false);
		_Ok.addActionListener(new ActionListener() {     
			public void actionPerformed(ActionEvent arg0)
			{	
				_selectedGroup = _model.getElementAt(_list.getSelectedIndex());
				
				String temp = _cfc.running(_selectedGroup, _id);
				StringTokenizer st = new StringTokenizer(temp, ":");
				_choice = st.nextToken();
				_gpCode = st.nextToken();
				
				if(_gpCode.equals("null"))
				{
					showMessage("ERROR","The group that was already either delete or doesn't exsit.");
				}
				else
				{
					if(_choice.equals("TRUE"))
					{
						_cfl.running((byte)1, _gpCode);
						new Client_Group_Main(_id, _gpCode, _selectedGroup, true, _cfl.getFileList());
					}
					else
					{
						_cfl.running((byte)1, _gpCode);
						new Client_Group_Main(_id, _gpCode, _selectedGroup, false, _cfl.getFileList());
					}
				}
				close();
			}
		});
	        
		_Cancel = new JButton(new ImageIcon("img/Cancel.png"));		
		_Cancel.setPressedIcon(new ImageIcon("img/Cancelp.png"));
		_Cancel.setBounds(350, 400, 80,40);
		_Cancel.setBorderPainted(false);
		_Cancel.setFocusPainted(false);
		_Cancel.setContentAreaFilled(false);
		_Cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();		
			}
		});
		//----------------------------------------------------------------------indivial
	}
	
	private void showMessage(String title, String message) 
	{
		Font fontbt = new Font("SansSerif", Font.BOLD,24);
		JLabel input = new JLabel(message);
		input.setFont(fontbt);
		JOptionPane.showMessageDialog(null, input, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void close()
	{
		this.dispose();
	}
	
	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
            g.drawImage(img, 0, 0, null);
            if(!_checkimage)
            {
            	g.drawImage(img2, 0, 0,null);
            }
       }
   }
	
}
