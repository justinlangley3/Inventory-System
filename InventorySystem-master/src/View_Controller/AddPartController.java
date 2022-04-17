
/*
 *  Package: View_Controller
 *  File:    AddPartController.java
 *
 *  Created by: Justin A Langley
 *  Date: 2019AUG08
 */
package View_Controller;

import java.net.URL;

import java.text.ParseException;

import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.binding.BooleanBinding;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import javafx.stage.Stage;

import Helpers.DialogHelper;
import Helpers.ValidationHelper;

import Model.Inventory;
import Model.PartInHouse;
import Model.PartOutsourced;

public class AddPartController implements Initializable
{
    @FXML
    private AnchorPane window;

    // Screen elements
    @FXML
    private Button                 cancel;
    @FXML
    private Button                 save;
    private Inventory              inventory;
    @FXML
    private Label                  labelCompanyOrMachine;
    @FXML
    private List<Label>            errorLabelList;
    @FXML
    private RadioButton            inHouse;
    @FXML
    private RadioButton            outsourced;
    @FXML
    private TextField              inv;
    @FXML
    private TextField              maxInput;
    @FXML
    private TextField              minInput;
    @FXML
    private TextField              partName;
    @FXML
    private TextField              partPriceCost;
    @FXML
    private TextField              textCompanyOrMachine;
    private final DialogHelper     dialog    = new DialogHelper();
    private final ValidationHelper validator = new ValidationHelper();

    // disables the save button if specific fields are not filled
    @FXML
    private void bindSaveButton ()
    {
        // require all fields to be filled out
        BooleanBinding isTextEmpty = partName.textProperty()
                                             .isEmpty()
                                             .or(inv.textProperty().isEmpty())
                                             .or(partPriceCost.textProperty().isEmpty())
                                             .or(minInput.textProperty().isEmpty())
                                             .or(maxInput.textProperty().isEmpty());

        save.disableProperty().bind(isTextEmpty);
    }

    // closes the window if the user selects cancel
    @FXML
    private void cancel ()
    {
        if (dialog.confirmCancel())
            {
            // user confirmed, get the stage and close the window
            Stage stage = (Stage) cancel.getScene().getWindow();

            stage.close();
            }
    }

    // clears validation errors when the user switches focus
    @FXML
    private void clearErrors ()
    {
        validator.clearErrorLabels(errorLabelList);
    }

    // runs a validator on the Company Name/Machine ID field
    // the validator that runs depends on the type of part selected
    @FXML
    private void companyOrMachineInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // check the part type
        if (inHouse.isSelected())
            {
            // inHouse, validate as a numeric value
            validator.validateNumeric(ev, key, textCompanyOrMachine, errorLabelList);
            }
        else
            {
            // outsourced, validate against allowed characters
            validator.validateCompanyName(ev, key, textCompanyOrMachine, errorLabelList);
            }
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize (URL url, ResourceBundle rb)
    {
        bindSaveButton();
    }

    // runs a numeric-only validator while the user types
    @FXML
    private void invInput (KeyEvent ev)
    {
        // get the key press
        String key = ev.getCharacter();

        // filter that removes non-numeric entries
        validator.validateNumeric(ev, key, inv, errorLabelList);
    }

    // runs a cutomized validator for qty fields while the user types
    @FXML
    private void maxValidateInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // first allow only numbers
        if (validator.validateNumeric(ev, key, maxInput, errorLabelList))
            {
            // the key press was valid
            int pos = maxInput.getCaretPosition();

            // validate the quantities
            validator.validateMaxQty(pos, key, minInput, maxInput, errorLabelList);
            }
    }

    // runs a cutomized validator for qty fields while the user types
    @FXML
    private void minValidateInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // first allow only numbers
        if (validator.validateNumeric(ev, key, minInput, errorLabelList))
            {
            // the key press was valid
            int pos = minInput.getCaretPosition();

            // validate the quantities
            validator.validateMinQty(pos, key, minInput, maxInput, errorLabelList);
            }
    }

    // runs a name validator while the user types
    @FXML
    private void partNameInput (KeyEvent ev)
    {
        // get the key press
        String key = ev.getCharacter();

        // validates against allowed characters
        validator.validatePartName(ev, key, partName, errorLabelList);
    }

    // runs a currency validator while the user types
    @FXML
    private void partPriceCostInput (KeyEvent ev)
    {
        // get the key press
        String key = ev.getCharacter();
        int    pos = partPriceCost.getCaretPosition();

        /*
         *  validate what is currently in the field
         *  checks against custom filter for allowed chars
         *  additionally, uses regex to validate formatting
         */
        validator.validateCurrency(pos, ev, key, partPriceCost, errorLabelList);
    }

    // form control for user-selections of InHouse/Outsourced
    @FXML
    private void partSourceSelection ()
    {
        if (inHouse.isSelected())
            {
            // change form elements to match
            labelCompanyOrMachine.setText("Machine ID");
            textCompanyOrMachine.setPromptText("e.g. (Numeric Value)");
            textCompanyOrMachine.setText("");
            }
        else
            {
            // change form elements to match
            labelCompanyOrMachine.setText("Company Name");
            textCompanyOrMachine.setPromptText("e.g. Lumber Supply, Inc.");
            textCompanyOrMachine.setText("");
            }
    }

    // gets user-input, performs checks, saves if the checks pass
    @FXML
    private void save () throws ParseException
    {
        // parse user inputs
        String name  = this.partName.getText().trim();
        int    stock = Integer.valueOf(this.inv.getText());
        int    min   = Integer.valueOf(minInput.getText());
        int    max   = Integer.valueOf(maxInput.getText());
        Double price = validator.parseCurrency(partPriceCost.getText());

        // determine if the max inventory is too low
        if (max < min)
            {
            // inform the user
            dialog.displayMaxInvTooLow();

            // exit this function
            return;
            }

        // determine if inv is within the range of min, max
        if ((stock < min) || (stock > max))
            {
            // not in range, inform the user
            dialog.displayInvNotInRange();

            // exit this function
            return;
            }

        // if the previous checks passed, the part can be saved
        // first, check which type of part to generate
        if (inHouse.isSelected())
            {
            int machineId = 0;

            if (!textCompanyOrMachine.getText().isEmpty())
                {
                // inHouse, get the machineId also
                machineId = Integer.valueOf(textCompanyOrMachine.getText());
                }

            // create a new PartInHouse by calling the PartInHouse constructor
            PartInHouse newPart = new PartInHouse(name, price, stock, min, max, machineId);

            // add/save the new part to the inventory
            inventory.addPart(newPart);
            }
        else
            {
            // outsourced, get the company name also
            String company = textCompanyOrMachine.getText().trim();

            // create a new PartOutsourced by calling the PartOutsource constructor
            PartOutsourced newPart = new PartOutsourced(name, price, stock, min, max, company);

            // add/save the new part to the inventory
            inventory.addPart(newPart);
            }

        // close the current window
        Stage stage = (Stage) cancel.getScene().getWindow();

        stage.close();
    }

    // sets the injected inventory object
    public void setInventory (Inventory inventory)
    {
        this.inventory = inventory;
    }
}
