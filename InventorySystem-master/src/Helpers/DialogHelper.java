
/*
 *  Package: Helpers
 *  File:    DialogHelper.java
 *
 *  Created by: Justin A Langley
 *  Date: 2019AUG08
 */
package Helpers;

import java.text.NumberFormat;

import java.util.Locale;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

import javafx.stage.Stage;

import Model.Part;
import Model.Product;

/**
 * This class provides functions to display preset dialog boxes
 * to inform the user about something which occurred during an action
 * the user took (usually an error, but not always so)
 *
 * As a rule of user interface design, a dialog should not be used
 * unless the user did something first
 *
 * @author Justin Lanlgey
 */
public class DialogHelper
{
    // location for program icon resource
    public final String icon = "/Images/icon.png";

    /**
     * Displays a confirmation dialog asking the user if they wish to cancel.
     * @return True if user confirmed, False if canceled
     */
    public boolean confirmCancel ()
    {
        // create an alert dialog
        Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Do you wish to cancel?");
        alert.showAndWait();

        return alert.getResult() == ButtonType.YES;
    }

    /**
     * Displays a confirmation dialog asking the user if they wish to delete
     * the selected part.
     * @param partName Selected part the user is requesting deletion
     * @return True if confirmed, False if canceled
     */
    public boolean confirmDeletePart (String partName)
    {
        // create an alert dialog
        Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Delete part: \"" + partName + "\" ?");
        alert.showAndWait();

        return alert.getResult() == ButtonType.YES;
    }

    /**
     * Displays a confirmation dialog asking the user if they wish to delete
     * the selected product.
     * @param prodName Selected product the user is requesting deletion
     * @return True if confirmed, false if canceled
     */
    public boolean confirmDeleteProd (String prodName)
    {
        // create an alert dialog
        Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Delete product: \"" + prodName + "\" ?");
        alert.showAndWait();

        return alert.getResult() == ButtonType.YES;
    }

    /**
     * Displays a confirmation dialog asking the user if they wish to remove the
     * selected part from the current product.
     *
     * @param partName Selected part the user is requesting removal from the current product
     * @return True if confirmed, False if canceled
     */
    public boolean confirmDisassociate (String partName)
    {
        // create an alert dialog
        Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Remove part: \"" + partName + "\" ?");
        alert.showAndWait();

        return alert.getResult() == ButtonType.YES;
    }

    /**
     * Displays a dialog informing the user that a part cannot be added to the product
     * if one is not selected
     */
    public void displayAddAssocPartFailed ()
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("To add a part to this product, one must be selected.\n"
                             + "Please select one and try again.");
        alert.getButtonTypes().set(0, ButtonType.CLOSE);
        alert.showAndWait();
    }

    /**
     * Displays a dialog informing the user that a part cannot be removed from the product
     * if one is not selected
     */
    public void displayDeleteAssocPartFailed ()
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("To remove a part from this product, one must be selected.\n"
                             + "Please select one and try again.");
        alert.getButtonTypes().set(0, ButtonType.CLOSE);
        alert.showAndWait();
    }

    /**
     * Displays a dialog informing the user that a part cannot be deleted if one is not selected
     */
    public void displayDeletePartFailed ()
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("To delete a part, one must be selected.\n"
                             + "Please select one and try again.\n");
        alert.getButtonTypes().set(0, ButtonType.CLOSE);
        alert.showAndWait();
    }

    /**
     * Displays a dialog informing the user that a product cannot be deleted if one is not selected
     */
    public void displayDeleteProdFailed ()
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("To delete a product, one must be selected.\n"
                             + "Please select one and try again.\n");
        alert.getButtonTypes().set(0, ButtonType.CLOSE);
        alert.showAndWait();
    }

    public void displayInvNotInRange ()
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Inventory value not in range:\n"
                             + "Inventory amount must be within min and max values.");
        alert.showAndWait();
    }

    public void displayMaxInvTooLow ()
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Max inventory is less than minimum inventory:\n"
                             + "Max inventory must be greater.");
        alert.showAndWait();
    }

    /**
     * Displays a dialog informing the user that a part search failed
     * and did not return a part containing the term queried
     *
     * @param query string of a user-entered search term
     */
    public void displayPartNotFound (String query)
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Part not found.\n" + "A search for \"" + query
                             + "\" yielded no results.\n");
        alert.getButtonTypes().set(0, ButtonType.OK);
        alert.showAndWait();
    }

    public void displayPartSearchResults (Part part)
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Match Found.\n\n"
                             + "The following part has been selected in the view:\n\n" + "ID:\t\t"
                             + part.getId() + "\n" + "Name:\t" + part.getName() + "\n" + "Inv:\t\t"
                             + part.getStock() + "\n" + "Price:\t"
                             + NumberFormat.getCurrencyInstance(
                                 Locale.US).format(part.getPrice()));
        alert.showAndWait();
    }

    public void displayProdHasNoParts ()
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Product has no associated parts.\n"
                             + "Please add at least one part before saving.");
        alert.showAndWait();
    }

    /**
     * Displays a dialog informing the user that a product search failed
     * and did not return a product containing the term queried
     *
     * @param query string of a user-entered search term
     */
    public void displayProdNotFound (String query)
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Product not found.\n" + "A search for \"" + query
                             + "\" yielded no results.\n");
        alert.getButtonTypes().set(0, ButtonType.OK);
        alert.showAndWait();
    }

    public void displayProdPriceTooLow ()
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("This product price is too low.\n"
                             + "The price is less than the total cost of its parts.");
        alert.showAndWait();
    }

    public void displayProdSearchResults (Product product)
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("Match Found.\n\n"
                             + "The following product has been selected in the view:\n\n"
                             + "ID:\t\t" + product.getId() + "\n" + "Name:\t" + product.getName()
                             + "\n" + "Inv:\t\t" + product.getStock() + "\n" + "Price:\t"
                             + NumberFormat.getCurrencyInstance(
                                 Locale.US).format(product.getPrice()));
        alert.showAndWait();
    }

    /**
     * Displays a dialog informing the user that a part cannot be modified if one is not selected.
     */
    public void modifyPartFailed ()
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("To modify a part, one must be selected.\n"
                             + "Please select one and try again.\n");
        alert.getButtonTypes().set(0, ButtonType.CLOSE);
        alert.showAndWait();
    }

    /**
     * Displays a dialog informing the user that a part cannot be modified if one is not selected.
     */
    public void modifyProdFailed ()
    {
        // create an alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // get the stage of the alert dialog
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        stage.centerOnScreen();

        // set the dialog icon to match the rest of the application
        stage.getIcons().add(new Image(this.getClass().getResource(icon).toString()));

        // set dialog information
        alert.setTitle("");
        alert.setHeaderText(null);
        alert.setContentText("To modify a product, one must be selected.\n"
                             + "Please select one and try again.\n");
        alert.getButtonTypes().set(0, ButtonType.CLOSE);
        alert.showAndWait();
    }
}
