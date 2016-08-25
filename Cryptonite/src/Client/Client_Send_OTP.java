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
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.border.BevelBorder;



public class Client_Send_OTP extends JFrame{
	BufferedImage img = null;JTextField Otpfield;
	
	String Otp;
	
	JButton Send;
	JButton Cancel;
	Font font = new Font ("SansSerif", Font.BOLD,20);

	public static void main(String args[]){
		new Client_Send_OTP();
	}

	
	public Client_Send_OTP(){
		getContentPane().setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(500,300,460,550);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(null);
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 470, 550);
        layeredPane.setLayout(null);
        
        try {
            img = ImageIO.read(new File("img/Filesendbg.png"));
        } catch (IOException e) {
            System.out.println("이미지 불러오기 실패");
            System.exit(0);
        }
        
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 460, 500);
        
        Otpfield = new JTextField();
        Otpfield.setText("Set otp number");
        Otpfield.setBounds(158, 247, 254, 50);
        Otpfield.setForeground(Color.black);        
        Otpfield.setFont(font);
        Otpfield.setOpaque(false);
        Otpfield.setBorder(BorderFactory.createEmptyBorder());
        Otpfield.setHorizontalAlignment(JTextField.CENTER);
        Otpfield.addKeyListener(new KeyListener(){
     		@Override
     		public void keyPressed(KeyEvent e) {}
     		@Override
     		public void keyReleased(KeyEvent e) {
     			
     		}
     		@Override
     		public void keyTyped(KeyEvent e) {}
           });
        	Otpfield.addMouseListener(new MouseAdapter(){
         	public void mouseClicked(MouseEvent e){
         		Otpfield.setText("");
         	}
         });
        layeredPane.add(Otpfield);
        
        Send = new JButton(new ImageIcon("img/Send.png"));		
        Send.setPressedIcon(new ImageIcon("img/Sendp.png"));
        Send.setBounds(121, 359, 80, 40);
        Send.setBorderPainted(false);
        Send.setFocusPainted(false);
        Send.setContentAreaFilled(false);
        Send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Client_FileShare_Send().click();		
			}
		});
        layeredPane.add(Send);
        
        Cancel = new JButton(new ImageIcon("img/Cancel.png"));		
        Cancel.setPressedIcon(new ImageIcon("img/Cancelp.png"));
        Cancel.setBounds(251, 359, 80,40);
        Cancel.setBorderPainted(false);
        Cancel.setFocusPainted(false);
        Cancel.setContentAreaFilled(false);
        Cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();		
			}
		});
        layeredPane.add(Cancel);
        
        
        
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

