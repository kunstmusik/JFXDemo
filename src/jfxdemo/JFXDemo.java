/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxdemo;

import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author stevenyi
 */
public class JFXDemo extends Application {

    XDoubleProperty dVal = new XDoubleProperty(0.0);
    
    @Override
    public void start(Stage primaryStage) {
        final SwingNode swingNode = new SwingNode();
        SwingUtilities.invokeLater(() -> {
            JLabel jlabel = new JLabel();
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(jlabel, BorderLayout.CENTER);
            swingNode.setContent(panel);

            dVal.addListener(JfxPropertyExecutors.SWING, 
                    (obs, old, newVal) -> {
                jlabel.setText("SWING VAL: " + newVal);
            });
        });

        Label label = new Label();        
        dVal.addListener(JfxPropertyExecutors.JFX, 
                (obs, old, newVal) -> {
            label.setText("VAL: " + newVal);
        });

        BorderPane pane = new BorderPane();
        pane.setCenter(swingNode);
//        pane.setCenter(btn);
        pane.setBottom(label);
        
        Scene scene = new Scene(pane, 300, 250);
        scene.getStylesheets().add(
                JFXDemo.class.getResource("test.css").toExternalForm());
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnHidden(e -> System.exit(0));

        Font.getFontNames("Roboto").stream().forEach(s -> System.out.println(s));
        Font.getFontNames("Roboto Condensed").stream().forEach(s -> System.out.println(s));

        Thread t = new Thread(() -> { 
            while(true) {
                dVal.setValue(Math.random());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JFXDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
