package test;

import javafx.application.Application;
import javafx.stage.Stage;
import jcd.gui.SplashScreen;

/**
 *
 * @author McKillaGorilla
 */
public class SplashScreenTester extends Application {
    @Override
    public void start(Stage primaryStage) {
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.start(null, "./images/SplashScreen.png");
        System.out.println("HELLO");        
    }
    public static void main(String[] args) {
        launch(args);
    } 
}
