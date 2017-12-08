package Client;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
 
/**An example using Google Map JavaScript widget in SWT. It shows how to access Javascript object from Java.
 *
 */
public class GoogleMaps {
	
 private static Image _img;
 private static Text text;
 private static String _id;
 private static String Name = "";
 private Client_File_ListReceiver _cfl;
 private boolean usegps = false;
 double lat =0;
 double lng =0;
 double radius =100;
 private Text text_radius;
 
 
 	public GoogleMaps(String id) {
 		_cfl = new Client_File_ListReceiver();
		_id = id;
		
		File f = new File("map.html");
	    if(!f.exists()){
	        System.out.println("file not exist! " + f.getAbsolutePath()); 
	        return;
	    }
	  
	    
	    Display display = new Display();
	    Shell shell = new Shell(display, SWT.CLOSE | SWT.MIN | SWT.TITLE);
	    shell.setBounds(449,329,449,330);
	    
	    shell.setLayout(new FillLayout(SWT.HORIZONTAL));
	    _img= new Image(display,"gui/groupname.png");
        Composite comp= new Composite(shell, SWT.NONE);
        comp.setBackgroundImage(_img);
        
        
        text = new Text(comp, SWT.CENTER);
        text.setBounds(173, 154, 200, 31);
        text.setTextLimit(15);
        
        Button btnNewButton = new Button(comp, SWT.NONE);
        btnNewButton.setBounds(70, 220, 141, 53);
        btnNewButton.setImage(new Image(display, "gui/create.png"));
        
        btnNewButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				btnNewButton.setImage(new Image(display, "gui/create.png"));
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				btnNewButton.setImage(new Image(display, "gui/createR.png"));
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				
			}
		});
        btnNewButton.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				if (text.getText().length()==0)
				{
					MessageBox m = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
					m.setText("ERROR");
					m.setMessage("Please insert GroupName!");
					m.open();
				}
				else
         		{
	         		String[] test = new String[1];
	         		test[0] = _id;
	        		String temp = text.getText();
	        		
	        		
	        		new Client_Make_Group().make(test, temp,usegps,lat,lng,radius);
	        		byte[] event = new Client_Receive_Event().getEvent();
	        		byte[] buffer = new byte[1021]; 
	        		for(int i = 0; i < buffer.length; i++)
	        		{
	        			buffer[i] = event[i+3];
	        		}
	        		System.out.println("Name ="+Name);
	        		System.out.println("GroupCode : " + new String(buffer).trim());
	        		System.out.println("GroupName : " + temp);
	         		
	        		shell.dispose();
	        		_cfl.running((byte)1, new String(buffer).trim());
	        		new Client_Group_Main(_id, new String(buffer).trim(), temp,  true, _cfl.getFileList());
         		}
			}
		});

        Button btnNewButton_1 = new Button(comp, SWT.NONE);
        btnNewButton_1.setBounds(250, 220, 141, 53);
        btnNewButton_1.setImage(new Image(display,"gui/gn_cancel.png"));
        
        btnNewButton_1.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				btnNewButton_1.setImage(new Image(display, "gui/gn_cancel.png"));
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				btnNewButton_1.setImage(new Image(display, "gui/gn_cancelR.png"));
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				
			}
		});
        btnNewButton_1.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				shell.dispose();
			}
		});
        
        Browser browser = new Browser(comp, SWT.NONE);
        browser.setBounds(452, 10, 432, 263);
        
        Label lblat = new Label(comp, SWT.NONE);
        lblat.setBounds(633, 275, 107, 20);
        lblat.setText("37.56");
        
        Label lblng = new Label(comp, SWT.NONE);
        lblng.setBounds(772, 275, 112, 20);
        lblng.setText("126.97");
        
        Button btn = new Button(comp, SWT.NONE);
        btn.setBounds(633, 0, 20, 20);
        btn.setText("1");
        btn.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				browser.execute("map.setCenter(SWLatLng);");
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        Button btn2 = new Button(comp, SWT.NONE);
        btn2.setBounds(653, 0, 20, 20);
        btn2.setText("2");
        btn2.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				browser.execute("map.setCenter(myLatLng);");
			}

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        

        browser.addListener(SWT.MouseUp, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
		        //check lat lng timer
		        TimerTask tt = new TimerTask(){
					@Override
					public void run() {    
						display.syncExec(new Runnable() {
							@Override
							public void run() {
					            double _lat = Double.valueOf(browser.evaluate("return document.getElementById('lat').value;").toString());
					            double _lng = Double.valueOf(browser.evaluate("return document.getElementById('Lng').value;").toString());
					            lblat.setText(String.valueOf(_lat));
					            lblng.setText(String.valueOf(_lng));
					            lat = _lat;
					            lng = _lng;
							}
						});
					}
		        };
		        
				Timer t = new Timer();
				t.schedule(tt, 50);
			}
		});
        
        Label lblUseGpsEncrypt = new Label(comp, SWT.NONE);
        lblUseGpsEncrypt.setBounds(200, 191, 160, 20);
        lblUseGpsEncrypt.setText("Use GPS encrypt files : ");
        
        
        Button btnCheckButton = new Button(comp, SWT.CHECK);
        btnCheckButton.setBounds(360, 193, 13, 16);
        
        Label lblSetRadius = new Label(comp, SWT.NONE);
        lblSetRadius.setBounds(450, 275, 100, 20);
        lblSetRadius.setText("Set Radius (m): ");
        
        text_radius = new Text(comp, SWT.BORDER);
        text_radius.setText("200");
        text_radius.setBounds(554, 275, 47, 20);
        text_radius.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				String _radius = ((Text)arg0.widget).getText();
				if (_radius.length()==0)
					_radius="0";
				browser.execute("setradius("+ _radius + ")");
				radius = Double.parseDouble(_radius);
			}
		});
        
        Label lblLat = new Label(comp, SWT.NONE);
        lblLat.setBounds(607, 275, 30, 20);
        lblLat.setText("lat :");
        
        Label lblLng = new Label(comp, SWT.NONE);
        lblLng.setBounds(746, 275, 30, 20);
        lblLng.setText("lng:");
        
        btnCheckButton.addSelectionListener(new SelectionAdapter() {
        	
        	@Override
            public void widgetSelected(SelectionEvent e)
            {
                Button button = (Button) e.widget;
                if (button.getSelection())
                    {
                		usegps = true;
                		browser.setUrl(f.toURI().toString());
                		shell.setSize(900,330);
                		lblat.setText("37.56");
                		lblng.setText("126.97");
                    }
                else
                    {
                		usegps = false;
                		shell.setSize(450,330);
                    }
            }
		});
        
	    shell.open();
	    shell.forceActive();
	    
	    while (!shell.isDisposed()) {
	        if (!display.readAndDispatch())
	            display.sleep();
	    }
	    display.dispose();
 	}
 
	public static void main(String [] args) throws IOException {
		new GoogleMaps("test");
	 }
	
	public static String getID()
	{
		return _id;
	}
	
	public static String GiveName(){
		return Name;
	}
	
	private void showMessage(String title, String message) 
	{
		Font fontbt = new Font("SansSerif", Font.BOLD,24);
		JLabel input = new JLabel(message);
		input.setFont(fontbt);
		JOptionPane.showMessageDialog(null, input, title, JOptionPane.INFORMATION_MESSAGE);
	}
}