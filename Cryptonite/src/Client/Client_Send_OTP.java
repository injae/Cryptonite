package Client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
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

//import Client.Client_Progressbar.Task;



public class Client_Send_OTP extends JFrame{
	
	private BufferedImage img = null;

	private Container container;
	
	private JButton Select;
	private JButton Send;
	private JButton Cancel;
	
	private String Otp;
	private boolean _checkotp=false;
	private static boolean _flag=false;
	
	private JLayeredPane layeredPane = new JLayeredPane();
	private Client_FileShare_Send _cfs = null;
	
	Font font = new Font ("SansSerif", Font.BOLD,20);
	Font _precondition_font = new Font ("Dialog", Font.BOLD,20);

	public static void main(String args[]){
		new Client_Send_OTP();
	}

	 private void showMessage(String title, String message) {
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		}
	
	public Client_Send_OTP(){
		try{
			 Toolkit tk = Toolkit.getDefaultToolkit(); 
			 Image image = tk.getImage("gui/logo.png");
			 this.setIconImage(image);
		}
		catch(Exception e)
		{
			System.out.println("Appilcation icon not found");
		}	
		_cfs = new Client_FileShare_Send();
		container=getContentPane();
		container.setBackground(Color.WHITE);
		setTitle("Cryptonite");
		setBounds(500,300,460,550);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		container.setLayout(null);
		
		getContentPane().setLayout(null);
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
        
       /* Otpfield = new JLabel();
        Otpfield.setText(_cfs.getOTP());
        Otpfield.setBounds(158, 247, 254, 50);
        Otpfield.setForeground(Color.black);        
        Otpfield.setFont(font);
        Otpfield.setOpaque(false);
        Otpfield.setBorder(BorderFactory.createEmptyBorder());
        Otpfield.setHorizontalAlignment(JTextField.CENTER);
        layeredPane.add(Otpfield);*/
        
        Select = new JButton(new ImageIcon("img/select.png"));		
        Select.setPressedIcon(new ImageIcon("img/selectp.png"));
        Select.setBounds(185, 330, 80, 40);
        Select.setBorderPainted(false);
        Select.setFocusPainted(false);
        Select.setContentAreaFilled(false);
        Select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_cfs.fileSelect();
			}
		});
        //layeredPane.add(Select);
        
        Send = new JButton(new ImageIcon("img/SENDP.png"));		
        Send.setRolloverIcon(new ImageIcon("img/SENDR.png"));
        Send.setBounds(121, 400, 80, 40);
        Send.setBorderPainted(false);
        Send.setFocusPainted(false);
        Send.setContentAreaFilled(false);
        Send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean check=false;
				//boolean progresscheck=false;
				check=_cfs.sendFile();
				_flag=true;
				_checkotp=true;
				//new Client_Progressbar();
				//progresscheck=Client_Progressbar.Progress_Check();
				layeredPane.removeAll();
				
				layeredPane.add(Send);
				//layeredPane.add(Select);
				layeredPane.add(Cancel);
				
				layeredPane.add(panel);
				container.add(layeredPane);
				layeredPane.updateUI();
				repaint();
				if(check/*&&progresscheck*/){
					showMessage("Success", "Send Complete!");
				}
				else{
					showMessage("Failed", "Send failed");
				}
			}
		});
        layeredPane.add(Send);
        
        Cancel = new JButton(new ImageIcon("img/_cancel.png"));		
        Cancel.setRolloverIcon(new ImageIcon("img/_cancelR.png"));
        Cancel.setBounds(251, 400, 80,40);
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
	
	public static boolean send_flag()
	{
		return _flag;
	}
	
	public void fileSelect()
	{
		_cfs.fileSelect();
	}
	
	class MyPanel extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(img, 0, 0, null);
            if(_checkotp){
            	g.setColor(Color.BLACK);
            	g.setFont(_precondition_font);
            	g.drawString(_cfs.getOTP(), 240, 275);
            }
       }
   }

}

