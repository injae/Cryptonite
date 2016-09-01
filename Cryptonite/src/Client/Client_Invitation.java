package Client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.DefaultListModel;
import javax.swing.Icon;



public class Client_Invitation extends JFrame{
	
	private BufferedImage img = null;
	
	private JButton _Ok;
	
	private DefaultListModel<String> _model;
	private JList<String> _list;
	
	private String[] _groupname;
	private int _count = 1;
	private boolean _checkimage=false;
	
	private Font fontbt = new Font("SansSerif", Font.BOLD,24);
	
	private Client_Show_Group _csg;

	
	public static void main(String args[]){
		new Client_Invitation (null);
	}


	public Client_Invitation (Client_Show_Group csg){
		_csg = csg;
		
		getContentPane().setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(500,300,816,480);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 816, 480);
        layeredPane.setLayout(null);
        
        try {
            img = ImageIO.read(new File("img/초대목록.png"));
        } catch (IOException e) {
            System.out.println("이미지 불러오기 실패");
            System.exit(0);
        }
        
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 900, 500);
        
        _Ok = new JButton(new ImageIcon("img/_Ok.png"));
        _Ok.setPressedIcon(new ImageIcon("img/_Okh.png"));
        _Ok.setBounds(740, 107, 50, 50);
        _Ok.setFocusPainted(false);
        _Ok.setContentAreaFilled(false);
        _Ok.setBorderPainted(false);
        _Ok.addActionListener(new ActionListener() {     
         	public void actionPerformed(ActionEvent arg0)
         	{	

         	    
         	}
         });
        
        _groupname = _csg.getGroupName();
		if(_groupname.length == 0)
		{	
			System.out.println("아무것도 없음");
			/*layeredPane.remove(panel);
			_checkimage=true;
			layeredPane.add(panel);
			layeredPane.updateUI();
			repaint();*/
		}
		else
		{
			_model = new DefaultListModel<>();
           for(int i=0;i< _groupname.length;i++)
           {
            		_model.addElement( _groupname[i]);
           }
           _list = new JList<>(_model);
           _list.setBounds(100, 100, 100, 100);
           _list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
           layeredPane.add(_list);
		}
        
        layeredPane.add(_Ok);
        layeredPane.add(panel);
        getContentPane().add(layeredPane);
        setVisible(true);
	}
	
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(img, 0, 0, null);
       }
   }
	
}
