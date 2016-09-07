package Client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

	private JButton Search;
	private JButton Search2;
	private JButton Withdrawal;
	private JButton Delete;
	private JTextField _idField;
	
	private String[] _result;
	private String _id;
	private String _receivedID;
	private String _gpCode;
	private String _gpName;
	private int _mod;
	
	private DefaultListModel<String> _model;
	private JList<String> _list;
	JScrollPane scrollPane = null;
	
	private int _count = 1;
	
	private Font fontbt = new Font("SansSerif", Font.BOLD,24);
	private Font _precondition_font = new Font ("Dialog", Font.BOLD,20);
	
	private Client_Group_Search _cgs;
	private Client_Group_Invite _cgi;
	private Client_Group_Withdrawal _cgw;
	private Client_Delete_Group _cdg;
	
	public static void main(String args[])
	{
		new Client_Group_Main(null, null, null, 0);
	}


	public Client_Group_Main(String id, String gpCode, String gpName, int mod){
		_receivedID = id;
		_gpCode = gpCode;
		_gpName = gpName;
		_mod = mod;
		_cgs = new Client_Group_Search();
		_cgi = new Client_Group_Invite();
		_cgw = new Client_Group_Withdrawal();
		_cdg = new Client_Delete_Group();
		
		getContentPane().setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(0,0,816,480);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 816, 480);
        layeredPane.setLayout(null);
        
        try 
        {
            img = ImageIO.read(new File("img/초대목록화면.png"));
        }
        catch (IOException e)
        {
            System.out.println("이미지 불러오기 실패");
            System.exit(0);
        }
        
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 816, 480);
        
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
        layeredPane.add( _idField);
        
        
        Search = new JButton(new ImageIcon("img/Search.png"));
        Search.setPressedIcon(new ImageIcon("img/Searchh.png"));
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
        layeredPane.add(Search);
        
        Search2 = new JButton(new ImageIcon("img/OK.png"));
        Search2.setRolloverIcon(new ImageIcon("img/OKR.png"));
        Search2.setBounds(747, 304, 45, 45);
        Search2.setFocusPainted(false);
        Search2.setContentAreaFilled(false);
        Search2.setBorderPainted(false);
        Search2.addActionListener(new ActionListener() {     
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
        layeredPane.add(Search2);
        
        Withdrawal = new JButton(new ImageIcon("img/Check.png"));
        Withdrawal.setPressedIcon(new ImageIcon("img/Checkp.png"));
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
       
        
        if(_mod == 1)
        {	
        	layeredPane.remove(Withdrawal);
        	layeredPane.add(Delete);
        	
        }
        else
        {
        	layeredPane.remove(Delete);
        	layeredPane.add(Withdrawal);
        }
        
        layeredPane.add(panel);
        getContentPane().add(layeredPane);
        setVisible(true);
	}
	
	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
            g.drawImage(img, 0, 0, null);
            g.setColor(Color.BLACK);
        	g.setFont(fontbt);
        	g.drawString("Group Name : " + _gpName, 230, 35);
        }
   }
	private void showMessage(String title, String message) 
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
}