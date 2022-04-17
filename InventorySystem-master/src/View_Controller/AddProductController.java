
/*
 *  Package: View_Controller
 *  File:    AddProductController.java
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

import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import javafx.stage.Stage;

import Helpers.DialogHelper;
import Helpers.SearchHelper;
import Helpers.ValidationHelper;

import Model.Inventory;
import Model.Part;
import Model.Product;

public class AddProductController implements Initializable
{
    @FXML
    private Button                       add;
    @FXML
    private Button                       cancel;
    @FXML
    private Button                       delete;
    @FXML
    private Button                       save;
    @FXML
    private Button                       searchPart;
    private Inventory                    inventory;
    @FXML
    private List<Label>                  errorLabelList;
    @FXML
    private TableColumn<Part, String>    partId;
    @FXML
    private TableColumn<Part, String>    partInv;
    @FXML
    private TableColumn<Part, String>    partName;
    @FXML
    private TableColumn<Part, String>    partPrice;
    @FXML
    private TableColumn<Product, String> assocPartId;
    @FXML
    private TableColumn<Product, String> assocPartInv;
    @FXML
    private TableColumn<Product, String> assocPartName;
    @FXML
    private TableColumn<Product, String> assocPartPrice;
    @FXML
    private TableView<Part>              assocPartView;
    @FXML
    private TableView<Part>              partView;
    @FXML
    private TextField                    id;
    @FXML
    private TextField                    inv;
    @FXML
    private TextField                    maxInput;
    @FXML
    private TextField                    minInput;
    @FXML
    private TextField                    productName;
    @FXML
    private TextField                    productPrice;
    @FXML
    private TextField                    searchBar;
    private Product                      temp      = new Product(0, "", 0, 0, 0, 0);
    private final SearchHelper           search    = new SearchHelper();
    private final DialogHelper           dialog    = new DialogHelper();
    private final ValidationHelper       validator = new ValidationHelper();

    // add a part to the product being created
    @FXML
    private void associatePart ()
    {
        // get the user selected part
        Part part = partView.getSelectionModel().getSelectedItem();

        // referesh TableViews
        if (part == null)
            {
            dialog.displayAddAssocPartFailed();
            }
        else
            {
            // associate the part with the current product
            temp.addAssociatedPart(part);
            }

        // refresh
        updateViews();
    }

    // disable the save button if all fields are not filled
    @FXML
    private void bindSaveButton ()
    {
        // disallows saving unless all fields are filled out
        BooleanBinding isTextEmpty = productName.textProperty()
                                                .isEmpty()
                                                .or(inv.textProperty().isEmpty())
                                                .or(productPrice.textProperty().isEmpty())
                                                .or(minInput.textProperty().isEmpty())
                                                .or(maxInput.textProperty().isEmpty());

        save.disableProperty().bind(isTextEmpty);
    }

    // cancel current edits and close the window
    @FXML
    private void cancel ()
    {
        // display confirmation dialog
        if (dialog.confirmCancel())
            {
            // User confirmed, close the window
            Stage stage = (Stage) cancel.getScene().getWindow();

            stage.close();
            }
    }

    // clear validation text when the user changes focus
    @FXML
    private void clearErrors ()
    {
        // clears all form validation errors displayed on screen
        validator.clearErrorLabels(errorLabelList);
    }

    // remove a part from the product being created
    @FXML
    private void deletePart ()
    {
        // get the user-selected part
        Part part = assocPartView.getSelectionModel().getSelectedItem();

        if (part == null)
            {
            // a part was not selected from the view
            dialog.displayDeleteAssocPartFailed();
            }
        else
            {
            // display confirmation dialog
            if (dialog.confirmDisassociate(part.getName()))
                {
                // User confirmed, disassociate the part from the current product
                temp.deleteAssociatedPart(part);
                }
            }

        // referesh TableViews
        updateViews();
    }

    // Initialize the controller
    @Override
    public void initialize (URL url, ResourceBundle rb)
    {
        bindSaveButton();
    }

    // validate inv input as the user types, keep numeric only
    @FXML
    private void invInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // validate what is currently in the inv input field
        validator.validateNumeric(ev, key, inv, errorLabelList);
    }

    // validate max input field as the user types
    @FXML
    private void maxInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // validate what is currently in the max input field
        if (validator.validateNumeric(ev, key, maxInput, errorLabelList))
            {
            // key press was valid, compare min,max values
            int pos = maxInput.getCaretPosition();

            // validate the quantities
            validator.validateMaxQty(pos, key, minInput, maxInput, errorLabelList);
            }
    }

    // validate min input field as the user types
    @FXML
    private void minInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // validate what is currently in the min input field
        if (validator.validateNumeric(ev, key, minInput, errorLabelList))
            {
            // key press was valid, compare min,max values
            int pos = minInput.getCaretPosition();

            // validate the quantities
            validator.validateMinQty(pos, key, minInput, maxInput, errorLabelList);
            }
    }

    // validate the product name against allowed character filter
    @FXML
    private void productNameInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // validate what is currently in the product name input field
        validator.validateProductName(ev, key, productName, errorLabelList);
    }

    // validate the price input as the user types
    @FXML
    private void productPriceInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();
        int    pos = productPrice.getCaretPosition();

        // validate as currency input
        validator.validateCurrency(pos, ev, key, productPrice, errorLabelList);
    }

    // performs checks on input, saves the product if all checks pass
    @FXML
    private void save () throws ParseException
    {
        // parse user inputs
        String name  = productName.getText().trim();
        int    stock = Integer.valueOf(inv.getText());
        int    min   = Integer.valueOf(minInput.getText());
        int    max   = Integer.valueOf(maxInput.getText());
        Double price = validator.parseCurrency(productPrice.getText());

        // detect if product has no parts
        if (temp.getAllAssociatedParts().size() < 1)
            {
            // product has no parts, inform the user
            dialog.displayProdHasNoParts();

            // exit this function
            return;
            }

        // compute total cost of associated parts
        double prodTotalCost = 0;

        prodTotalCost = temp.getAllAssociatedParts()
                            .stream()
                            .map((part) -> part.getPrice())
                            .reduce(prodTotalCost, (accum, _item) -> accum + _item);

        // determine if the the entered price is less than cost of its parts
        if (prodTotalCost > price)
            {
            // price is set too low, inform the user
            dialog.displayProdPriceTooLow();

            // exit this function
            return;
            }

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

        // if previous checks pass, the product can be saved
        Product newProd = new Product(name, price, stock, min, max);

        // copy all associated parts from the view
        ObservableList<Part> assoc = temp.getAllAssociatedParts();

        // add all associated parts
        assoc.forEach(
            (part) -> {
                newProd.addAssociatedPart(part);
            });

        // add the new product to the inventory
        inventory.addProduct(newProd);

        // close the current window
        Stage stage = (Stage) save.getScene().getWindow();

        stage.close();
    }

    // run the search helper to query search input
    @FXML
    private void searchParts ()
    {
        // see implementation details in the Search class of the model package
        search.searchPart(inventory, partView, searchBar);
    }

    // set the injected inventory object
    void setInventory (Inventory inventory)
    {
        this.inventory = inventory;
    }

    // set the TableViews with injected inventory object
    void setTableViews (Inventory inventory)
    {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        // associate part view cells with part data elements
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // format price column to currency format
        partPrice.setCellFactory(new CurrencyCellFactory<>());
        partView.setItems(inventory.getAllParts());

        // associate product view cells with product data elements
        assocPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // format price column to currency format
        assocPartPrice.setCellFactory(new CurrencyCellFactory<>());
        assocPartView.setItems(temp.getAllAssociatedParts());
    }

    // refreshes TableViews
    private void updateViews ()
    {
        // force refresh of the TableViews
        partView.refresh();
        assocPartView.refresh();
    }
}
