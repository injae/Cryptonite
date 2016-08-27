package Client;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;

import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JProgressBar;
import javax.swing.plaf.LayerUI;

/*import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class test_progressbar extends Application {
    
    @Override
    public void start(Stage primaryStage) {      
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setAlignment(Pos.CENTER);
        final Scene scene = new Scene(vBox, 300, 100);
        
        HBox hBox1 = new HBox();
        hBox1.setSpacing(4);
        //Text title1 = new Text("Task1");
        Button btn1 = new Button("start");
        final ProgressBar bar1 = new ProgressBar(0);
        final Text text1 = new Text("ready");
        hBox1.setAlignment(Pos.CENTER);
        hBox1.getChildren().addAll( btn1, bar1, text1);
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                final Task<String> task1 = getTask1();       
                bar1.progressProperty().unbind();
                bar1.progressProperty().bind(task1.progressProperty());
                text1.textProperty().bind(task1.messageProperty()); 
                final ExecutorService exe = Executors.newSingleThreadExecutor();
                exe.submit(task1);  
                task1.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
                    new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        exe.shutdown(); 
                    }
                });
            }
        });
        
      
        HBox hBox2 = new HBox();
        hBox2.setSpacing(5);
        Text title2 = new Text("Task2");
        Button btn2 = new Button("start");
        final ProgressBar bar2 = new ProgressBar(0);
        final Text text2 = new Text("ready");
        hBox2.setAlignment(Pos.CENTER);
        hBox2.getChildren().addAll(title2, btn2, bar2, text2);
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                final Task<String> task2 = getTask2();
                bar2.progressProperty().unbind();
                bar2.progressProperty().bind(task2.progressProperty());
                text2.textProperty().bind(task2.messageProperty());                        
                final ExecutorService exe = Executors.newSingleThreadExecutor();
                exe.submit(task2);
                task2.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, 
                    new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        exe.shutdown();
                    }
                });
            }
        });
        vBox.getChildren().add(hBox1);
       // vBox.getChildren().add(hBox2);
        primaryStage.setTitle("Progress Smaple");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Task<String> getTask1() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                updateMessage("start task1");
                int i;
                for (i = 1; i <= 10; i++) {
                    updateProgress(i, 10); 
                    TimeUnit.SECONDS.sleep(1);
                    updateMessage(String.format("running %d/%d", i, 10)); 
                }
                updateMessage("task1 done");
                return "Done";
            }
        };
    }

    private Task<String> getTask2() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                updateMessage("start task2");
                int i;
                for (i = 1; i <= 20; i++) {
                    updateProgress(i, 20);
                    TimeUnit.SECONDS.sleep(1);
                    updateMessage(String.format("running %d/%d", i, 20));
                }
                updateMessage("task2 done");
                return "Done";
            }
        };
    }
}
*/
//////////////////////////////////////swing
/*import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.Random;

import Client.Client_Send_OTP;
import Client.Client_Receive_OTP;
 
public class test_progressbar extends JPanel implements ActionListener, PropertyChangeListener {
 
    private JProgressBar progressBar;
    private JButton startButton;
    private JTextArea taskOutput;
    private Task task;
 
    class Task extends SwingWorker<Void, Void> {
       
         
        @Override
        public Void doInBackground() {
        	
            long filesize = 0;
            
            Random random=new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(1000);
                setProgress(Math.min(progress, 100));
            }
            return null;
        }
 
        
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            taskOutput.append("Done!\n");
        }
    }
 
    public test_progressbar() {
        super(new BorderLayout());
 
        //Create the demo's UI.
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
 
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
 
        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
 
        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(progressBar);
 
        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
    }

    public void actionPerformed(ActionEvent evt) {
        startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //Instances of javax.swing.SwingWorker are not reusuable, so
        //we create new instances as needed.
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
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
        //Create and set up the window.
        JFrame frame = new JFrame("ProgressBarDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new test_progressbar();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}*/
///////////////////////////////////////////////fx

/*import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class test_progressbar extends Application {
     
    final int MAX = 100;
     
    Thread myTaskThread;
    Thread myRunnableThread;
    Timer myTimer;
     
    MyTask myTask;
    MyRunnable myRunnable;
    MyTimerTask myTimerTask;
     
    @Override
    public void start(Stage primaryStage) {
         
        myTask = new MyTask();
        ProgressBar progressBarTask = new ProgressBar();
        progressBarTask.setProgress(0);
        progressBarTask.progressProperty().bind(myTask.progressProperty());
         
        ProgressBar progressBarRunnable = new ProgressBar();
        progressBarRunnable.setProgress(0);
        myRunnable = new MyRunnable(progressBarRunnable);
         
        ProgressBar progressBarTimerTask = new ProgressBar();
        progressBarTimerTask.setProgress(0);
        myTimerTask = new MyTimerTask(progressBarTimerTask);
         
        Button btnStart = new Button("Start Task");
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
  
            @Override
            public void handle(ActionEvent t) {
                myTaskThread = new Thread(myTask);
                myTaskThread.start();
                 
                myRunnableThread = new Thread(myRunnable);
                myRunnableThread.start();
                 
                myTimer = new Timer();
                myTimer.scheduleAtFixedRate(myTimerTask, 0, 100);
            }
        });
         
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(5, 5, 5, 5));
        vBox.setSpacing(5);
        vBox.getChildren().addAll(
                new Label("Run in Thread(Task)"),
                progressBarTask,
                new Label("Run in Thread(Runnable)"),
                progressBarRunnable,
                new Label("Run in Timer and TimerTask"),
                progressBarTimerTask,
                btnStart);
         
        StackPane root = new StackPane();
        root.getChildren().add(vBox);
  
        Scene scene = new Scene(root, 300, 250);
  
        primaryStage.setTitle("java-buddy.blogspot.com");
        primaryStage.setScene(scene);
        primaryStage.show();
 
    }
 
    public static void main(String[] args) {
        launch(args);
    }
     
    class MyTask extends Task<Void>{
         
        @Override
        protected Void call() throws Exception {
            for (int i = 1; i <= MAX; i++) {
                updateProgress(i, MAX);
                Thread.sleep(100);
            }
            return null;
        }
         
    }
     
    class MyRunnable implements Runnable{
         
        ProgressBar bar;
 
        public MyRunnable(ProgressBar b) {
            bar = b;
        }
 
        @Override
        public void run() {
            for (int i = 1; i <= MAX; i++) {
                 
                final double update_i = i;
                 
                //Not work if update JavaFX UI here!
                //bar.setProgress(i/MAX);
                 
                //Update JavaFX UI with runLater() in UI thread
                Platform.runLater(new Runnable(){
 
                    @Override
                    public void run() {
                        bar.setProgress(update_i/MAX);
                    }
                });
                 
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(test_progressbar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
         
    }
     
    class MyTimerTask extends TimerTask{
 
        ProgressBar bar;
        double count;
 
        public MyTimerTask(ProgressBar b) {
            bar = b;
            count = 0;
        }
 
        @Override
        public void run() {
 
            bar.setProgress(count++/MAX);
             
            if(count >= MAX){
                myTimer.cancel();
            }
             
        }
         
    }

}*/


/*import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
 
public class test_progressbar extends Application {
 
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Progress Controls");
 
        final Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(50);
         
        final ProgressBar pb = new ProgressBar(0);
        final ProgressIndicator pi = new ProgressIndicator(0);
 
        slider.valueProperty().addListener(
            (ObservableValue<? extends Number> ov, Number old_val, 
            Number new_val) -> {
                pb.setProgress(new_val.doubleValue()/50);
                pi.setProgress(new_val.doubleValue()/50);
        });
 
        final HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(slider, pb, pi);
        scene.setRoot(hb);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.Random;
 
public class test_progressbar extends JPanel implements ActionListener,PropertyChangeListener 
{
    private JProgressBar progressBar;
    private JButton startButton;
    private JTextArea taskOutput;
    private Task task;
 
    class Task extends SwingWorker<Void, Void> {
        
        @Override
        public Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            
            setProgress(0);
            while (progress < 100) {
                
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
            }
            return null;
        }
 

        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            taskOutput.append("Done!\n");
        }
    }
 
    public test_progressbar() {
        super(new BorderLayout());
 
        startButton = new JButton("Start");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);
 
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
 
        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
 
        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(progressBar);
 
        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
    }
 
    /**
     * Invoked when the user presses the start button.
     */
    public void actionPerformed(ActionEvent evt) {
        startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }
 
    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            taskOutput.append(String.format(
                    "Completed %d%% of task.\n", task.getProgress()));
        } 
    }
 
 
    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("ProgressBarDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        JComponent newContentPane = new test_progressbar();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}