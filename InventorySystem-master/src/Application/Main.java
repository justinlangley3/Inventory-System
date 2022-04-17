
/*
 *  Package: Application
 *  File:    Main.java
 *
 *  Created by: Justin A Langley
 *  Date: 2019AUG08
 */
package Application;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.stage.Stage;

public class Main extends Application
{
    // Launch the program
    public static void main (String[] args)
    {
        launch(args);
    }

    @Override

    // Load MainController()
    public void start (Stage stage) throws Exception
    {
        /*
         *  TODO:
         * Known bug:
         *   If the user has made part association changes in the modify product
         *   window, then closes the window via the "X" in the upper right corner
         *   of the window, changes will remain in effect.
         *
         *   This should be changed to behave the same as if the user selects
         *   the "cancel" button and confirms the subsequent dialog box
         */

        // Load resources
        String resourcePath = "/View_Controller/FXMLMain.fxml";
        Parent root         = FXMLLoader.load(getClass().getResource(resourcePath));

        // Create a new scene
        Scene scene = new Scene(root);

        // Adjust the stage settings and add to scene
        stage.centerOnScreen();
        stage.getIcons().add(new Image("/Images/icon.png"));
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }
}
