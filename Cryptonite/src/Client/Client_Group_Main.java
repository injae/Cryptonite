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



public class Client_Group_Main extends JFrame{
	BufferedImage img = null;

	JButton Settingbt;
	JButton Search;
	
	Font fontbt = new Font("SansSerif", Font.BOLD,24);
	Font _precondition_font = new Font ("Dialog", Font.BOLD,20);
	
	public static void main(String args[]){
		new Client_Group_Main();
	}


	public Client_Group_Main(){
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

        Settingbt = new JButton(new ImageIcon("img/Settingbt.png"));
        Settingbt.setPressedIcon(new ImageIcon("img/Settinghbt.png"));
        Settingbt.setBounds(706, 0, 60, 60);
        Settingbt.setFocusPainted(false);
        Settingbt.setContentAreaFilled(false);
        Settingbt.setBorderPainted(false);
        layeredPane.add(Settingbt);
                
        Search = new JButton(new ImageIcon("img/Search.png"));
        Search.setPressedIcon(new ImageIcon("img/Searchh.png"));
        Search.setBounds(740, 107, 50, 50);
        Search.setFocusPainted(false);
        Search.setContentAreaFilled(false);
        Search.setBorderPainted(false);
        layeredPane.add(Search);
        
        
        layeredPane.add(panel);
        getContentPane().add(layeredPane);
        setVisible(true);
        
        
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
