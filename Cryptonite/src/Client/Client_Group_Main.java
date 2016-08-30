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
import javax.swing.JPanel;
import javax.swing.JTextField;
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
	private JTextField _idField;
	
	private String[] _result;
	private static String _id;
	
	private Font fontbt = new Font("SansSerif", Font.BOLD,24);
	private Font _precondition_font = new Font ("Dialog", Font.BOLD,20);
	
	private Client_Group_Search _cgs;
	
	public static void main(String args[]){
		new Client_Group_Main();
	}


	public Client_Group_Main(){
		_cgs=new Client_Group_Search();
		getContentPane().setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(0,0,816,480);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 816, 480);
        layeredPane.setLayout(null);
        
        try {
            img = ImageIO.read(new File("img/초대목록화면.png"));
        } catch (IOException e) {
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
        _idField.addKeyListener(new KeyListener(){
     		@Override
     		public void keyPressed(KeyEvent e) {}
     		@Override
     		public void keyReleased(KeyEvent e) {
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
         		_cgs.search();
         		_result=_cgs.getID();
         		
         	     DefaultListModel<String> model = new DefaultListModel<>();
                for(int i=0;i<_result.length;i++){
                	if(!(_result[i]==Client_Group_Name.getID())){
                		model.addElement(_result[i].trim());
                	}
                }
                 JList<String> list = new JList<>(model);
                 list.setBounds(640, 200, 100, 100);
                 layeredPane.add(list);
         	}
         });
        
        layeredPane.add(Search);
        
        
        layeredPane.add(panel);
        getContentPane().add(layeredPane);
        setVisible(true);
        
        
	}
	
	public static String givename(){
		return _id;
	}
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(img, 0, 0, null);
            g.setColor(Color.BLACK);
        	g.setFont(_precondition_font);
        	g.drawString("Group Name : "+Client_Group_Name.GiveName(), 200, 50);
        }
   }
	
}
