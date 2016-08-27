package Client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.Random;

import Client.Client_FileShare_Send;
 
public class Client_Progressbar extends JPanel implements PropertyChangeListener 
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
                    Thread.sleep(_filesize);
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += _filesize;
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
 
    public Client_Progressbar(int flag) {
    	
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			createAndShowGUI();
    		}
    	});
    	
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
 
        JComponent newContentPane = new test_progressbar();
        newContentPane.setOpaque(true); //content panes must be opaque
        _frame.setContentPane(newContentPane);
 
        _frame.setVisible(true);
    }
    public static boolean Progress_Check(){
    	return _progressbarcheck;
    }
    public static void main(String[] args){
    	new Client_Progressbar(1);
    }
}