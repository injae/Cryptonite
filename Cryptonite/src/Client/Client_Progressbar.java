package Client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.mysql.fabric.xmlrpc.Client;

import java.beans.*;
import java.util.Random;

import Client.Client_FileShare_Send;
 

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;


import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;


import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class Client_Progressbar {
  public static void main(String[] args) {
    JFrame parentFrame = new JFrame();
    parentFrame.setSize(500, 150);
    JLabel jl = new JLabel();
    jl.setText("Count : 0");

    parentFrame.add(BorderLayout.CENTER, jl);
    parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    parentFrame.setVisible(true);

    final JDialog dlg = new JDialog(parentFrame, "Progress Dialog", true);
    JProgressBar dpb = new JProgressBar(0, 500);
    dlg.add(BorderLayout.CENTER, dpb);
    dlg.add(BorderLayout.NORTH, new JLabel("Progress..."));
    dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    dlg.setSize(300, 75);
    dlg.setLocationRelativeTo(parentFrame);

    Thread t = new Thread(new Runnable() {
      public void run() {
        dlg.setVisible(true);
      }
    });
    t.start();
    for (int i = 0; i <= 500; i++) {
      jl.setText("Count : " + i);
      dpb.setValue(i);
      if(dpb.getValue() == 500){
        dlg.setVisible(false);
        System.exit(0);
        
      }
      try {
        Thread.sleep(25);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    dlg.setVisible(true);
  }
}

/*public class Client_Progressbar extends JPanel implements PropertyChangeListener 
{
	private static JFrame _frame=new JFrame();
	
    private JProgressBar progressBar;
    private JTextArea taskOutput;
    
    static Task task;
    
    private static boolean _progressbarcheck=false;
    private long _filesize;
    
    class Task extends SwingWorker<Void, Void> {
    	
    	
    	@Override
        public Void doInBackground() {
        	
            int progress = 0;
            
            setProgress(0);
            while (progress < 100) {
                
                try {
                    Thread.sleep(10000_filesize);
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress +=10000 _filesize;
                setProgress(Math.min(progress, 100));
            }
            return null;
        }
 

        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null); //turn off the wait cursor
            taskOutput.append("Done!\n");
        }
    }
 
    public Client_Progressbar() {
    	
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			createAndShowGUI();
    		}
    	});
    	
        int flag = 0;
        if(Client_Send_OTP.send_flag()){
        	flag=1;
        }
        if(Client_Receive_OTP.send_flag()){
        	flag=2;
        }
        
        if(flag==1)
        {
        	_filesize=Client_FileShare_Send.send_filesize();
        }
        if(flag==2)
        {
        	_filesize=Client_FileShare_Receive.receive_filesize();
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
        if(task.isDone())
        {	
        	_frame.dispose();
        	_progressbarcheck=true;
        }
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
 
        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
 
        JPanel panel = new JPanel();
        panel.add(progressBar);
 
        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
    }
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            taskOutput.append(String.format(
                    "Completed %d%% of task.\n", task.getProgress()));
        } 
    }
    private static void createAndShowGUI() {
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
        JComponent newContentPane = new Client_Progressbar();
        newContentPane.setOpaque(true); //content panes must be opaque
        _frame.setContentPane(newContentPane);
 
        _frame.setVisible(true);
    }
    public static boolean Progress_Check(){
    	return _progressbarcheck;
    }
    public static void main(String[] args){
    	new Client_Progressbar();
    }
}*/