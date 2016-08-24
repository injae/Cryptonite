package Client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
 
public class Client_Test_tabmenu extends JFrame
{
    private JFrame frm;
    
    private JTabbedPane tabbedPane;
    
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JLabel label1;
//    private JLabel label2;
//    private JLabel label3;
    
    public Client_Test_tabmenu()
    {
        //생성 및 초기화
        frm = new JFrame();
        
        tabbedPane = new JTabbedPane();
        
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        
        label1 = new JLabel("I'm the First");
       // label2 = new JLabel("I'm Second");
       // label3 = new JLabel("I'm number Three");
        
        //패널에 컴포넌트 추가
        panel1.add(label1);
       // panel2.add(label2);
       // panel3.add(label3);
       

        tabbedPane.add("personal", panel1);
        tabbedPane.add("group", panel2);
        tabbedPane.add("director", panel3);
        tabbedPane.setTabPlacement(JTabbedPane.NORTH);
        
        //탭이름 재지정 메소드
        //tabbedPane.setTitleAt(0, "첫째");
        //tabbedPane.setTitleAt(1, "둘째");
        //tabbedPane.setTitleAt(2, "셋째");
        
        //탭위치 설정 메소드
        //tabbedPane.setTabPlacement(JTabbedPane.TOP); //상단위치
        //tabbedPane.setTabPlacement(JTabbedPane.BOTTOM); //하단위치                                    
        //tabbedPane.setTabPlacement(JTabbedPane.LEFT); //좌측위치
        //tabbedPane.setTabPlacement(JTabbedPane.RIGHT); //우측위치
 
        //프래임 기본 설정
        frm.add(tabbedPane);
        frm.setSize(700,500);
        frm.setTitle("탭 예제");
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setVisible(true);
    }
 
    public static void main(String[] args) 
    {
        new Client_Test_tabmenu();
    }
}

/*import javax.swing.*;
import java.awt.*;

public class Client_Test_tabmenu extends JPanel {
    public Client_Test_tabmenu() {
        initializeUI();
    }

    public static void showFrame() {
        JPanel panel = new Client_Test_tabmenu();
        panel.setOpaque(true);

        JFrame frame = new JFrame("Tabbed Pane Tab Placement Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Client_Test_tabmenu.showFrame();
            }
        });
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(400, 200));

        //
        // Creates a JTabbedPane with tabs at the bottom.
        //
        JTabbedPane pane = new JTabbedPane(JTabbedPane.HEIGHT);
        pane.addTab("Tab 1", createPanel("Panel 1"));
        pane.addTab("Tab 1", createPanel("Panel 2"));
        pane.addTab("Tab 3", createPanel("Panel 3"));

        this.add(pane, BorderLayout.CENTER);
    }

    public JPanel createPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JLabel(title), BorderLayout.NORTH);
        return panel;
    }
}*/
/*import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Client_Test_tabmenu extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tabs");
        Group root = new Group();
        Scene scene = new Scene(root, 400, 250, Color.WHITE);

        TabPane tabPane = new TabPane();

        BorderPane borderPane = new BorderPane();
        for (int i = 0; i < 5; i++) {
            Tab tab = new Tab();
            tab.setText("Tab" + i);
            HBox hbox = new HBox();
            hbox.getChildren().add(new Label("Tab" + i));
            hbox.setAlignment(Pos.CENTER);
            tab.setContent(hbox);
            tabPane.getTabs().add(tab);
            
        }
        // bind to take available space
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        
        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}*/