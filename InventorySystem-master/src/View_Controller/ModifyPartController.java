
/*
 *  Package: View_Controller
 *  File:    ModifyPartController.java
 *
 *  Created by: Justin A Langley
 *  Date: 2019AUG08
 */
package View_Controller;

import java.net.URL;

import java.text.NumberFormat;
import java.text.ParseException;

import java.util.List;
import java.util.Locale;
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
import Model.Part;
import Model.PartInHouse;
import Model.PartOutsourced;

public class ModifyPartController implements Initializable
{
    @FXML
    private AnchorPane             window;
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
    private TextField              id;
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
    private boolean                isInHouse;
    private PartInHouse            partInHouse    = null;
    private PartOutsourced         partOutsourced = null;
    private final ValidationHelper validator      = new ValidationHelper();
    private final DialogHelper     dialog         = new DialogHelper();

    // disable the save button if appropriate fields are not filled
    @FXML
    private void bindSaveButton ()
    {
        // disallows saving unless all required fields are filled out
        BooleanBinding isTextEmpty = partName.textProperty()
                                             .isEmpty()
                                             .or(inv.textProperty().isEmpty())
                                             .or(partPriceCost.textProperty().isEmpty())
                                             .or(minInput.textProperty().isEmpty())
                                             .or(maxInput.textProperty().isEmpty());

        save.disableProperty().bind(isTextEmpty);
    }

    // closes the stage
    @FXML
    private void cancel ()
    {
        if (dialog.confirmCancel())
            {
            // User confirmed, close the current window
            Stage stage = (Stage) cancel.getScene().getWindow();

            stage.close();
            }
    }

    // clears validation errors when the user changes focus
    @FXML
    private void clearErrors ()
    {
        validator.clearErrorLabels(errorLabelList);
    }

    // validates the input of this field, depending on the type of part
    @FXML
    private void companyOrMachineInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // validate the field depending which type of part is selected
        if (inHouse.isSelected())
            {
            // inhouse, validate as a numeric value
            validator.validateNumeric(ev, key, textCompanyOrMachine, errorLabelList);
            }
        else
            {
            /*
             * outsourced,
             * validate what is currently in the field
             * checks against custom filter for allowed chars
             */
            validator.validateCompanyName(ev, key, textCompanyOrMachine, errorLabelList);
            }
    }

    // initializes the controller
    @Override
    public void initialize (URL url, ResourceBundle rb)
    {
        // bind the save button
        bindSaveButton();
    }

    // validates the inv field as the user types
    @FXML
    private void invInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // run the validator to allow numeric inputs only
        validator.validateNumeric(ev, key, inv, errorLabelList);
    }

    // validates the max field as the user types
    @FXML
    private void maxValidateInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // validate what is currently in the max input field
        if (validator.validateNumeric(ev, key, maxInput, errorLabelList))
            {
            // value was numeric
            int pos = maxInput.getCaretPosition();

            // run the validator to check quantities
            validator.validateMaxQty(pos, key, minInput, maxInput, errorLabelList);
            }
    }

    // validates the min field as the user types
    @FXML
    private void minValidateInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // first, allow only numeric values
        if (validator.validateNumeric(ev, key, minInput, errorLabelList))
            {
            // value was numeric
            int pos = minInput.getCaretPosition();

            // run validator to check quantites
            validator.validateMinQty(pos, key, minInput, maxInput, errorLabelList);
            }
    }

    // validates part name, allows only certain characters
    @FXML
    private void partNameInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // run the validator with the part name filter
        validator.validatePartName(ev, key, partName, errorLabelList);
    }

    // runs as the user types to validate price input
    @FXML
    private void partPriceCostInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();
        int    pos = partPriceCost.getCaretPosition();

        // validate what is currently in the price input field
        validator.validateCurrency(pos, ev, key, partPriceCost, errorLabelList);
    }

    // runs when the user changes the selection of the part source, updates form controls
    @FXML
    private void partSourceSelection ()
    {
        // clear any existing errors displayed
        clearErrors();

        // check which radio button is selected
        if (inHouse.isSelected())
            {
            // inHouse, change label, set (Company Name / MachineId) field to empty
            labelCompanyOrMachine.setText("Machine ID");
            textCompanyOrMachine.setPromptText("e.g. (Numeric Value)");
            textCompanyOrMachine.setText("");
            }
        else
            {
            // outsourced, change label, set (Company Name / MachineId) field to empty
            labelCompanyOrMachine.setText("Company Name");
            textCompanyOrMachine.setPromptText("e.g. Lumber Supply, Inc.");
            textCompanyOrMachine.setText("");
            }
    }

    // performs various checks, and if they pass, saves the edited part
    @FXML
    private void save () throws ParseException
    {
        // parse user inputs
        int    curr_id = Integer.valueOf(id.getText());
        String name    = this.partName.getText().trim();
        int    stock   = Integer.valueOf(this.inv.getText());
        int    min     = Integer.valueOf(minInput.getText());
        int    max     = Integer.valueOf(maxInput.getText());
        Double price   = validator.parseCurrency(partPriceCost.getText());

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
            // inform the user
            dialog.displayInvNotInRange();

            // exit this function
            return;
            }
        if (isInHouse)                   // part is currently of type PartInHouse
            {
            if (inHouse.isSelected())    // user wishes to keep as inHouse
                {
                int machineId = 0;
                int index     = inventory.getAllParts().indexOf(partInHouse);

                // check if text is empty, if not update machine Id
                if (!textCompanyOrMachine.getText().isEmpty())
                    {
                    // inHouse, get the machineId also
                    machineId = Integer.valueOf(textCompanyOrMachine.getText());
                    }

                // create a temporary part to update the current part with
                PartInHouse temp = new PartInHouse(curr_id,
                                                   name,
                                                   price,
                                                   stock,
                                                   min,
                                                   max,
                                                   machineId);

                // update part
                inventory.updatePart(index, temp);
                }
            else    // user wishes to change to outsourced
                {
                int index = inventory.getAllParts().indexOf(partInHouse);

                // create a temporary object to update the current part with
                PartOutsourced temp = new PartOutsourced(curr_id,
                                                         name,
                                                         price,
                                                         stock,
                                                         min,
                                                         max,
                                                         textCompanyOrMachine.getText());

                // update part
                inventory.updatePart(index, temp);
                }
            }
        else                             // part is currently of type PartOutsourced
            {
            if (inHouse.isSelected())    // user wishes to change to inHouse
                {
                int machineId = 0;
                int index     = inventory.getAllParts().indexOf(partInHouse);

                // check if text is empty, if not update machine Id
                if (!textCompanyOrMachine.getText().isEmpty())
                    {
                    // inHouse, get the machineId also
                    machineId = Integer.valueOf(textCompanyOrMachine.getText());
                    }

                // create a temporary object to update the current part with
                PartInHouse temp = new PartInHouse(curr_id,
                                                   name,
                                                   price,
                                                   stock,
                                                   min,
                                                   max,
                                                   machineId);

                // update part
                inventory.updatePart(index, temp);
                }
            else    // user wishes to keep as outsourced
                {
                int index = inventory.getAllParts().indexOf(partOutsourced);

                // create a temporary object to update the current part with
                PartOutsourced temp = new PartOutsourced(curr_id,
                                                         name,
                                                         price,
                                                         stock,
                                                         min,
                                                         max,
                                                         textCompanyOrMachine.getText());

                // update part
                inventory.updatePart(index, temp);
                }
            }

        // close the current window
        Stage stage = (Stage) save.getScene().getWindow();

        stage.close();
    }

    // sets the injected inventory object
    public void setInventory (Inventory inventory)
    {
        this.inventory = inventory;
    }

    // sets the injected part from the injected inventory
    void setPart (Part part)
    {
        // check the type of the part
        isInHouse = part instanceof PartInHouse;
        if (isInHouse)
            {
            // store the part
            partInHouse = (PartInHouse) part;

            // update form elements on screen
            id.setText(String.valueOf(part.getId()));
            partName.setText(part.getName());
            inv.setText(String.valueOf(part.getStock()));

            // set price with a NumberFormat object
            partPriceCost.setText(NumberFormat.getCurrencyInstance(Locale.US)
                                              .format(part.getPrice()));

            // update min/max fields
            minInput.setText(String.valueOf(part.getMin()));
            maxInput.setText(String.valueOf(part.getMax()));

            // update radio selections
            textCompanyOrMachine.setText(String.valueOf(partInHouse.getMachine()));
            labelCompanyOrMachine.setText("Machine ID");
            inHouse.setSelected(true);
            outsourced.setSelected(false);
            }
        else
            {
            // store the part
            partOutsourced = (PartOutsourced) part;

            // update form elements on screen
            id.setText(String.valueOf(part.getId()));
            partName.setText(part.getName());
            inv.setText(String.valueOf(part.getStock()));

            // set price with a format object
            partPriceCost.setText(NumberFormat.getCurrencyInstance(Locale.US)
                                              .format(part.getPrice()));

            // update min/max fields
            minInput.setText(String.valueOf(part.getMin()));
            maxInput.setText(String.valueOf(part.getMax()));

            // update radio selections
            textCompanyOrMachine.setText(partOutsourced.getCompanyName());
            labelCompanyOrMachine.setText("Company Name");
            inHouse.setSelected(false);
            outsourced.setSelected(true);
            }
    }
}
