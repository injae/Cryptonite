package Client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.util.List;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import Client.Client_Main_UI.MyPanel;

public class Client_FileRecovery extends JFrame implements DropTargetListener{
	private BufferedImage img = null;
	
	private JLabel _downloadArea;
	private DropTarget _dropTarget;
	private List _loadedFileList;
	private JButton[] Button;
	
	private String[] _fileList;
	private String[] _name=null;
	private int _x=0;
	private int _y=0;
	private int _i=0;
	
	private JButton _Cancel;
	private JButton _OK;
	
	private Font fontbt = new Font("SansSerif", Font.BOLD,24);
	private Font _precondition_font = new Font ("Dialog", Font.BOLD,20);

	
	public static void main(String args[])
	{
		new  Client_FileRecovery(null);
	}


	public Client_FileRecovery(String[] fileList){
		
		_fileList = fileList;
		_downloadArea = new JLabel();
		_downloadArea.setBounds(2, 69, 800, 384);
		_dropTarget = new DropTarget(_downloadArea, DnDConstants.ACTION_COPY_OR_MOVE, this, true, null);
		this.add(_downloadArea, BorderLayout.CENTER);
		
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
            img = ImageIO.read(new File("img/File Recovery_BG.png"));
        }
        catch (IOException e)
        {
            System.out.println("이미지 불러오기 실패");
            System.exit(0);
        }
        
        MyPanel panel = new MyPanel();
        panel.setBounds(0, 0, 816, 480);

        _name=new String[_fileList.length];
        for(int i=0;i<_fileList.length;i++)
        {	
        	StringTokenizer st=new StringTokenizer(_fileList[i], "\\");

        	while(st.hasMoreTokens()){
    			_name[i]=st.nextToken();
    		}
    	}
    
        _OK = new JButton(new ImageIcon("img/OK.png"));
        _OK.setRolloverIcon(new ImageIcon("img/OKR.png"));
        _OK.setBounds(600, 360, 45, 45);
        _OK.setFocusPainted(false);
        _OK.setContentAreaFilled(false);
        _OK.setBorderPainted(false);
        _OK.addActionListener(new ActionListener() {     
         	public void actionPerformed(ActionEvent arg0)
         	{	
         		
         	}
         });

        _Cancel = new JButton("cancel",new ImageIcon("img/Cancel.png"));	
		_Cancel.setPressedIcon(new ImageIcon("img/Cancelp.png"));
		_Cancel.setBounds(350, 360, 100,100);
		_Cancel.setVerticalTextPosition ( SwingConstants.BOTTOM ) ;
		_Cancel.setVerticalAlignment    ( SwingConstants.TOP ) ;
		_Cancel.setHorizontalTextPosition( SwingConstants.CENTER ) ;
		_Cancel.setBorderPainted(false);
		_Cancel.setFocusPainted(false);
		_Cancel.setContentAreaFilled(false);
		_Cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();		
			}
		});


		for(int i=1;i<_name.length+1;i++)
		{
			_i=i;
			
			if((i%7)==0){
				Button[i-1] = new JButton(_name[i-1],new ImageIcon("gui/logo_mini.png"));		
				Button[i-1].setPressedIcon(new ImageIcon("gui/logo_mini.png"));
				Button[i-1].setBounds((5-_x),(70+_y),200,200);
				Button[i-1].setVerticalTextPosition ( SwingConstants.BOTTOM ) ;
				Button[i-1].setVerticalAlignment    ( SwingConstants.TOP ) ;
				Button[i-1].setHorizontalTextPosition( SwingConstants.CENTER ) ;
				Button[i-1].setBorderPainted(false);
				Button[i-1].setFocusPainted(false);
				Button[i-1].setContentAreaFilled(false);
				Button[i-1].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						Button[_i-1].setBackground(Color.BLACK);
					}
				});
				_y+=150;
				_x=0;
			}
			else{
				Button[i-1] = new JButton(_name[i-1],new ImageIcon("gui/logo_mini.png"));		
				Button[i-1].setPressedIcon(new ImageIcon("gui/logo_mini.png"));
				Button[i-1].setBounds((5+_x),70,200,200);
				Button[i-1].setVerticalTextPosition ( SwingConstants.BOTTOM ) ;
				Button[i-1].setVerticalAlignment    ( SwingConstants.TOP ) ;
				Button[i-1].setHorizontalTextPosition( SwingConstants.CENTER ) ;
				Button[i-1].setBorderPainted(false);
				Button[i-1].setFocusPainted(false);
				Button[i-1].setContentAreaFilled(false);
				Button[i-1].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						Button[_i-1].setBackground(Color.BLACK);
					}
				});
				_x+=150;
			}
			layeredPane.add(Button[i-1]);
		}
		
		//layeredPane.add(_Cancel);
        //layeredPane.add(_OK);
        layeredPane.add(panel);
        getContentPane().add(layeredPane);
        setVisible(true);
	}
	
	class MyPanel extends JPanel 
	{
        public void paint(Graphics g) 
        {
            g.drawImage(img, 0, 0, null);
        }
   }
	private void showMessage(String title, String message) 
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}


	@Override
	public void dragEnter(DropTargetDragEvent dtde) 
	{
		//System.out.println("dropEnter");
	}


	@Override
	public void dragExit(DropTargetEvent dte) 
	{
		//System.out.println("dragExit");
	}


	@Override
	public void dragOver(DropTargetDragEvent dtde) 
	{
		//System.out.println("dragOver");
	}


	@Override
	public void drop(DropTargetDropEvent dtde) 
	{
		System.out.println("dragDrop");
		if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0)
        {
            dtde.acceptDrop(dtde.getDropAction());
            Transferable tr = dtde.getTransferable();
            try
            {
                //파일명 얻어오기
            	_loadedFileList = (List) tr.getTransferData(DataFlavor.javaFileListFlavor);
             
                //파일명 출력
                for(int i = 0; i < _loadedFileList.size(); i++)
                {
                	System.out.println(_loadedFileList.get(i).toString());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}


	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) 
	{
		//System.out.println("dragActionChanged");
	}
}
