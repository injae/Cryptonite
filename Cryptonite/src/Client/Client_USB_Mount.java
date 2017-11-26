package Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JWindow;

import org.eclipse.swt.events.DisposeEvent;

import Client.Client_Receive_OTP.MyPanel;

public class Client_USB_Mount extends JFrame{
	
	private JLabel _background;

	private JLabel _select;
	private Font font = new Font ("SansSerif", Font.BOLD,20);
	
	private  JList _list;
	private BufferedImage img = null;
	private JTextField _size;
	private JTextField _pswd;
	private JLabel _sizelabel;
	private JLabel _pswdlabel;
	private JRadioButton rdbtnMB ;
	private JRadioButton rdbtnGB; 
	private ButtonGroup bg;
	
	private JButton _close;
	private JButton _ok;
	private boolean mb = true;
	private String _mounted;
	
	private String size;
	private File[] oldListRoot = File.listRoots();
	private Thread t;
	
	private String[] items = null;
	public static void main(String[] args)
	{
		new Client_USB_Mount();
	}
	public Client_USB_Mount()
	{
		
	    
		
		getContentPane().setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(500, 300, 405, 252);	//405, 259
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(null);
        JLayeredPane _layeredPane = new JLayeredPane();
        _layeredPane.setBounds(0, 0, 400, 224);
        _layeredPane.setLayout(null);
        
	    _list = new JList();
		_list.setBounds(20, 30, 170, 130);
		_list.setVisible(true);
		
		_select = new JLabel();
		_select.setText("보호된 USB 선택");
		_select.setBounds(20, 10, 170, 20);
		_select.setVisible(true);
		
		_layeredPane.add(_list);
		_layeredPane.add(_select);
        
        
        try {
            img = ImageIO.read(new File("img/Setting.png"));
        } catch (IOException e) 
        {
            System.out.println("Image Load Failed.");
            System.exit(0);
        }
		
		try {
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image image = tk.getImage("gui/logo.png");
			this.setIconImage(image);
		} catch (Exception e) {
			System.out.println("Appilcation icon not found");
		}
		
        
		 MyPanel panel = new MyPanel();
	     panel.setBounds(0, 0, 400, 224);
		
	/*	_background = new JLabel(new ImageIcon("img/Setting.png"));
		_background.setBounds(0, 0, 400, 224);
		_background.setVisible(true);*/
	
		_close = new JButton(new ImageIcon("img/SClose.png"));
		_close.setRolloverIcon(new ImageIcon("img/SCloseR.png"));
		_close.setBounds(200, 165, 70, 40);
		_close.setVisible(true);
		_close.setBorderPainted(false);
 	    _close.setFocusPainted(false);
		_close.addActionListener(new ActionListener() 
 	    {
 	       	public void actionPerformed(ActionEvent arg0) 
 	       	{
 	       		dispose();
 	       	}
 	    });
		_layeredPane.add(_close);
		
		_ok = new JButton(new ImageIcon("img/OK.png"));
		_ok.setRolloverIcon(new ImageIcon("img/OKR.png"));
		_ok.setBounds(100, 165, 40, 40);
		_ok.setVisible(true);
		_ok.setBorderPainted(false);
		_ok.setFocusPainted(false);
		_ok.addActionListener(new ActionListener() 
 	    {
 	       	public void actionPerformed(ActionEvent arg0) 
 	       	{
 	       		if (_pswd.getText().equals(""))
 	       		{
 	       			JOptionPane.showMessageDialog(null, "비밀번호를 입력해 주세요");
 	       			return;
 	       		}
 	       		if (_list.getSelectedValue() == null)
 	       		{
 	       			JOptionPane.showMessageDialog(null, "보호된 USB를 선택해 주세요.");
 	       			return;
 	       		}
 	       	String currentDir = System.getProperty("user.dir");
 	       		Runtime rt = Runtime.getRuntime();

 	       		try {
 	       			System.out.println(currentDir);
 	       			System.out.println(_pswd.getText());
 	       			_mounted = (String) _list.getSelectedValue();
					Process process = rt.exec(new String[]{currentDir+"\\CryptoniteMount.exe", "mount", _list.getSelectedValue() + ":\\Cryptonite", _pswd.getText()});
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 	       	//t.interrupt();
 	       	dispose();
 	       	}
 	    });
		_layeredPane.add(_ok);
		


		
	    // Get the index of all the selected items
	    int[] selectedIx = _list.getSelectedIndices();

	    // Get all the selected items using the indices
	    for (int i = 0; i < selectedIx.length; i++) {
	      Object sel = _list.getModel().getElementAt(selectedIx[i]);
	    }

	    // Get the index of the first selected item
	    int firstSelIx = _list.getSelectedIndex();

		
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
		DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
		decimalFormat.setGroupingUsed(false);
		_size = new JFormattedTextField(decimalFormat);
		// _size = new JTextField();
		 _size.setBounds(265, 30, 120, 31);
		 _size.setOpaque(true);
		 _size.setForeground(Color.BLACK);
		 _size.setFont(font);
	     _size.setBorder(javax.swing.BorderFactory.createEmptyBorder());
	     _size.setHorizontalAlignment(JTextField.CENTER);
	     _size.addKeyListener(new KeyListener(){
	     		@Override
	     		public void keyPressed(KeyEvent e) {
	     			if (e.getKeyCode() < '0' || e.getKeyCode() > '9')
	     			{
	     				JOptionPane.showMessageDialog(null, "숫자만 입력해 주세요.");
	     				e.consume();
	     				_size.setText("");
	     			}
	     			
	     		}
	     		@Override
	     		public void keyReleased(KeyEvent e) 
	     		{
	     			size = _size.getText();
	     			_size.setText(size);
	     		}
	     		@Override
	     		public void keyTyped(KeyEvent e) {}
	           });
	        	_size.addMouseListener(new MouseAdapter(){
	         	public void mouseClicked(MouseEvent e){
	         		_size.setText("");
	         	}
	         });
	     
	    //_layeredPane.add(_size);
	    
	    rdbtnMB = new JRadioButton("MB");
	    rdbtnMB.setBackground(SystemColor.control);
	    rdbtnMB.setBounds(270, 60, 50, 33);
	    rdbtnMB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

					mb = true;
			}
		});
		//_layeredPane.add(rdbtnMB);
		
	    
	    rdbtnGB = new JRadioButton("GB");
	    rdbtnGB.setBackground(SystemColor.control);
	    rdbtnGB.setBounds(320, 60, 50, 33);
	    rdbtnGB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

					mb = false;
			}
		});
		//_layeredPane.add(rdbtnGB);
		
		bg = new ButtonGroup();
		bg.add(rdbtnMB);
		bg.add(rdbtnGB);
		rdbtnMB.setSelected(true);
	    
	    _pswd = new JPasswordField();
	    _pswd.setBounds(265, 70, 120, 31);
	    _pswd.setOpaque(true);
	    _pswd.setForeground(Color.BLACK);
	    _pswd.setFont(font);
	    _pswd.setBorder(javax.swing.BorderFactory.createEmptyBorder());
	    _pswd.setHorizontalAlignment(JTextField.CENTER);
	    _pswd.addMouseListener(new MouseAdapter(){
	        public void mouseClicked(MouseEvent e){
	         _pswd.setText("");
	         }
	     });
	     
	    _layeredPane.add(_pswd);
	    
	    _sizelabel= new JLabel("SIZE : ");
	    _sizelabel.setBounds(210, 30, 35, 20);
	    
	    //_layeredPane.add(_sizelabel);
	    
	    _pswdlabel= new JLabel("PW : ");
	    _pswdlabel.setBounds(210, 70, 35, 20);
	    
	    _layeredPane.add(_pswdlabel);
	    /*
		_window.getContentPane().add(_layeredPane);
		_window.setSize(400, 350);
		_window.setLocationRelativeTo(null);
		_window.setAlwaysOnTop(false);*/
		
		 _layeredPane.add(panel);
	     getContentPane().add(_layeredPane);
	     setVisible(true);
		
	     Update();
	}
	
	/*public void UI_ON()
	{
		_window.setVisible(true);
	}
	private void UI_OFF()
	{
		_window.dispose();
	}*/
	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
            g.drawImage(img, 0, 0, null);
       }
	}
	public void Update() {
	    t = new Thread(new Runnable() {
	        public void run() {
	        	List<String> tmp = new ArrayList<>();
	        	for (int i = 0; i < File.listRoots().length ; i++)
	        	{
	        		System.out.println(File.listRoots()[i].toString().substring(0, 1));
	        		tmp.add(File.listRoots()[i].toString().substring(0, 1));
	        	}
	        	String[] temp = new String[File.listRoots().length];
	        	temp = tmp.toArray(temp);
	        	_list.setListData(temp);
	        	
	            while (true) {
	                try {
	                    Thread.sleep(100);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                    return;
	                }
	                if (File.listRoots().length > oldListRoot.length) {
	                    System.out.println("new drive detected");
	                    oldListRoot = File.listRoots();
	                    System.out.println("drive"+oldListRoot[oldListRoot.length-1]+" detected");
	                    
	                    System.out.println(oldListRoot[oldListRoot.length-1].toString().charAt(0));
	                    
	                    HDD_SerialNo(oldListRoot[oldListRoot.length-1].toString().charAt(0));
	                    tmp = new ArrayList<>();
	    	        	for (int i = 0; i < File.listRoots().length ; i++)
	    	        	{
	    	        		System.out.println(File.listRoots()[i].toString().substring(0, 1));
	    	        		tmp.add(File.listRoots()[i].toString().substring(0, 1));
	    	        	}
	    	        	temp = new String[File.listRoots().length];
	    	        	temp = tmp.toArray(temp);
	    	        	_list.setListData(temp);
	                    
	                } else if (File.listRoots().length < oldListRoot.length) {
	    System.out.println(oldListRoot[oldListRoot.length-1]+" drive removed");

	                    oldListRoot = File.listRoots();
	                    tmp = new ArrayList<>();
	                    boolean dismount = true;
	    	        	for (int i = 0; i < File.listRoots().length ; i++)
	    	        	{
	    	        		System.out.println(File.listRoots()[i].toString().substring(0, 1));
	    	        		if (File.listRoots()[i].toString().substring(0, 1).equals(_mounted))
	    	        		{
	    	        			dismount = false;
	    	        		}
	    	        		tmp.add(File.listRoots()[i].toString().substring(0, 1));
	    	        	}
	    	        	temp = new String[File.listRoots().length];
	    	        	temp = tmp.toArray(temp);
	    	        	_list.setListData(temp);
	    	        	
	    	        	if (dismount == true)
	    	        	{
	    	        		Runtime rt = Runtime.getRuntime();
	    	        		String currentDir = System.getProperty("user.dir");
	    					try {
								Process process = rt.exec(new String[]{currentDir+"\\CryptoniteMount.exe", "dismount"});
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

	    	        	}

	                }

	            }
	        }
	    });
	    t.start();
	}
	public static void HDD_SerialNo(char c){
	    List<String> HDD_Serial=new ArrayList<>();
	    String HDD_Number;
	    Runtime rt = Runtime.getRuntime();
	    try {
	        Process process=rt.exec(new String[]{"CMD", "/C", "vol "+c+":"});
	        String s = null;
	        //Reading sucessful output of the command
	        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        while ((s = reader.readLine()) != null) {
	            HDD_Serial.add(s);
	        }
	        
	        String Serial = null;
	        Iterator i = HDD_Serial.iterator();
	        while(i.hasNext())
	        {
	        	
	        	String temp = (String)i.next();
	        	if(temp.contains("-"))
	        	{
	        		Serial = temp;
	        	}
	        }
	        
	        String[] Serial1 = Serial.split(" ");
	        for(String tempp : Serial1)
	        {
	        	if(tempp.contains("-"))
	        	{
	        		Serial = tempp;
	        	}
	        }
	        System.out.println(Serial);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	}

}
