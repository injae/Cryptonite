package Client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.Icon;



public class Client_group extends JFrame{
	BufferedImage img = null;
	JButton Create;
	JButton Participate;
	
	JButton Indivbt;
	JButton Groupbt;
	JButton Developerbt;
	JButton Settingbt;
	
	Font fontbt = new Font("SansSerif", Font.BOLD,24);
	
	
	
	

	
	
	public static void main(String args[]){
		new Client_group();
	}

	
	public Client_group(){
		getContentPane().setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(500,300,816,480);//500,300,816,480
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 900, 500);
        layeredPane.setLayout(null);
        
        try {
            img = ImageIO.read(new File("img/Group.png"));
        } catch (IOException e) {
            System.out.println("이미지 불러오기 실패");
            System.exit(0);
        }
        
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 900, 500);
        
        Indivbt = new JButton(new ImageIcon("img/Indivbt.png"));		
        Indivbt.setRolloverIcon(new ImageIcon("img/Indivhbt.png"));
        Indivbt.setBounds(259, 35, 80, 30);
        Indivbt.setBorderPainted(false);
        Indivbt.setFocusPainted(false);
        Indivbt.setContentAreaFilled(false);
        Indivbt.addActionListener(new ActionListener() {
         	public void actionPerformed(ActionEvent arg0) {
         		dispose();
         		new Client_main_menu().repaint();
         	}
         });
        layeredPane.add(Indivbt);
        
        Groupbt = new JButton(new ImageIcon("img/Groupbt.png"));		
        Groupbt.setRolloverIcon(new ImageIcon("img/Grouphbt.png"));
        Groupbt.setBounds(353, 34, 80, 30);
        Groupbt.setBorderPainted(false);
        Groupbt.setFocusPainted(false);
        Groupbt.setContentAreaFilled(false);
        layeredPane.add(Groupbt);
        
        
        
        Developerbt = new JButton(new ImageIcon("img/Developerbt.png"));
        Developerbt.setRolloverIcon(new ImageIcon("img/Developerhbt.png"));
        Developerbt.setBounds(447, 34, 80, 30);
        Developerbt.setBorderPainted(false);
        Developerbt.setFocusPainted(false);
        Developerbt.setContentAreaFilled(false);
        layeredPane.add(Developerbt);
        
        Settingbt = new JButton(new ImageIcon("img/Settingbt.png"));
        Settingbt.setPressedIcon(new ImageIcon("img/Settinghbt.png"));
        Settingbt.setBounds(707, 4, 60, 60);
        Settingbt.setFocusPainted(false);
        Settingbt.setContentAreaFilled(false);
        Settingbt.setBorderPainted(false);
        layeredPane.add(Settingbt);
        
        Create = new JButton(new ImageIcon("img/Create.png"));
        Create.setRolloverIcon(new ImageIcon("img/Createh.png"));
        Create.setBounds(10, 97, 390, 280);
        Create.setBorderPainted(false);
        Create.setFocusPainted(false);
        Create.setContentAreaFilled(false);
        layeredPane.add(Create);
        
        Participate = new JButton(new ImageIcon("img/Participate.png"));
        Participate.setRolloverIcon(new ImageIcon("img/Participateh.png"));
        Participate.setBounds(434, 90, 370, 280);
        Participate.setBorderPainted(false);
        Participate.setFocusPainted(false);
        Participate.setContentAreaFilled(false);
        layeredPane.add(Participate);
        
       
        
        
        
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
