package jcd.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class provides a splash screen at application startup time. It will
 * appear for a few seconds and then close.
 * 
 * @author McKillaGorilla
 */
public class SplashScreen {
    ImageView splashImageView;
    static Stage parentStage;
    static Stage splashDialog;
    static ProgressBar progressBar;
    
    public SplashScreen() {}

    public void start(Stage initParentStage, String imagePath) {
        parentStage = initParentStage;
        parentStage.hide();
        File splashImageFile = new File(imagePath);
        try {
            // WE'LL PUT EVERYTHING IN HERE
            VBox box = new VBox();           

            // MAKE THE IMAGE
            URL splashFileURL = splashImageFile.toURI().toURL();
            Image splashImage = new Image(splashFileURL.toExternalForm());
            splashImageView = new ImageView(splashImage);

            // MAKE THE PROGRESS BAR
            progressBar = new ProgressBar();
            progressBar.prefWidthProperty().bind(box.widthProperty());

            // LAYOUT ALL OF THE COMPONENTS
            box.getChildren().add(splashImageView);
            box.getChildren().add(progressBar);
            
            // AND BUILD THE WINDOW
            Scene splashScene = new Scene(box);
            splashDialog = new Stage();
            splashDialog.initStyle(StageStyle.UNDECORATED);
            splashDialog.setScene(splashScene);
            
            Thread progressThread = new Thread(new ProgressTask());
            progressThread.start();
            
            // AND START IT UP
            splashDialog.showAndWait();
        }
        catch(MalformedURLException murle) {
            // CLOSE THE SPLASH DIALOG SINCE WE DON'T HAVE AN IMAGE FOR IT
            if (splashDialog != null) {
                splashDialog.hide();
            }
        }
    }

    class ProgressTask extends Task<Void> {
        Stage splashStage;
        ReentrantLock progressLock = new ReentrantLock();

        public ProgressTask() {}

        @Override
        protected Void call() throws Exception {
            try {
                progressLock.lock();
                double perc;
                double max = 200;
                for (int i = 0; i <= max; i++) {
                    perc = i/max;

                    // THIS WILL BE DONE ASYNCHRONOUSLY VIA MULTITHREADING
                    Platform.runLater(new UpdateToRunLater(perc));

                    // SLEEP EACH FRAME
                    Thread.sleep(15);
                }
            }
            finally {
                // REELEASE THE LOCK
                progressLock.unlock();
            }
            return null;
        }
    }

    class UpdateToRunLater implements Runnable {
        double perc;
        
        public UpdateToRunLater(double initPerc) {
            perc = initPerc;
        }
        
        @Override
        public void run() {
            progressBar.setProgress(perc);
            if (perc >= 1.0) {
                splashDialog.hide();
                parentStage.show();
            }
        }
    }
}