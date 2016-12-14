/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxdemo;

import javafx.application.Platform;
import javax.swing.SwingUtilities;

/**
 *
 * @author stevenyi
 */
public class JfxPropertyExecutors {

    public static JfxPropertyExecutor INHERIT = new JfxPropertyExecutor() {
        @Override
        public boolean runsOnCurrentThread() {
            return true;
        }

        @Override
        public void execute(Runnable command) {
            command.run();
        }
    };

    public static JfxPropertyExecutor SWING = new JfxPropertyExecutor() {
        @Override
        public boolean runsOnCurrentThread() {
            return SwingUtilities.isEventDispatchThread();
        }

        @Override
        public void execute(Runnable command) {
            SwingUtilities.invokeLater(command);
        }
    };

     public static JfxPropertyExecutor JFX = new JfxPropertyExecutor() {
        @Override
        public boolean runsOnCurrentThread() {
            return Platform.isFxApplicationThread();
        }

        @Override
        public void execute(Runnable command) {
            Platform.runLater(command);
        }
    };
}
