
/*
 *  Package: View_Controller
 *  File:    ModifyProductController.java
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

import javafx.collections.FXCollections;
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

import Model.*;

public class ModifyProductController implements Initializable
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
    private Product                      prod;
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
    private ObservableList<Part>         removedParts = FXCollections.observableArrayList();
    private final DialogHelper           dialog       = new DialogHelper();
    private final SearchHelper           search       = new SearchHelper();
    private final ValidationHelper       validator    = new ValidationHelper();

    // adds a part to the current product
    @FXML
    private void associatePart ()
    {
        // get the user selected part
        Part part = partView.getSelectionModel().getSelectedItem();

        if (part == null)
            {
            // a part was not selected
            dialog.displayAddAssocPartFailed();
            }
        else
            {
            // associate the part with the current product
            prod.addAssociatedPart(part);
            }
    }

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

    // close the current window
    @FXML
    private void cancel ()
    {
        // display confirmation dialog
        if (dialog.confirmCancel())
            {
            // user confirmed
            // check if the user removed parts
            if (removedParts.size() > 0)
                {
                removedParts.forEach(
                    (removedPart) -> {
                    // put all removed parts back before exiting
                        prod.addAssociatedPart(removedPart);
                    });
                }

            // close the window
            Stage stage = (Stage) cancel.getScene().getWindow();

            stage.close();
            }
    }

    @FXML
    private void clearErrors ()
    {
        // clear all form validation errors displayed on screen
        validator.clearErrorLabels(errorLabelList);
    }

    // delete part button action
    @FXML
    private void deletePart ()
    {
        // get the user-selected part
        Part part = assocPartView.getSelectionModel().getSelectedItem();

        if (part == null)
            {
            // a part was not selected
            dialog.displayDeleteAssocPartFailed();
            }
        else
            {
            // display confirmation dialog
            if (dialog.confirmDisassociate(part.getName()))
                {
                // User confirmed
                // add the part to a list of removed parts
                removedParts.add(part);

                // disassociate the part from the current product
                prod.deleteAssociatedPart(part);
                }
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

    // validate inventory field input as the user types
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

            // validate the quantity
            validator.validateMinQty(pos, key, minInput, maxInput, errorLabelList);
            }
    }

    // validate product name input as the user types
    @FXML
    private void productNameInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // validate what is currently in the product name input field
        validator.validateProductName(ev, key, productName, errorLabelList);
    }

    // validate price input as the user types
    @FXML
    private void productPriceInput (KeyEvent ev)
    {
        // get the key pressed
        String key = ev.getCharacter();

        // validate as currency input
        int pos = productPrice.getCaretPosition();

        // validate the field against the currency filter
        validator.validateCurrency(pos, ev, key, productPrice, errorLabelList);
    }

    // performs checks on user inputs, saves if all checks pass
    @FXML
    private void save () throws ParseException
    {
        // parse user inputs
        int    curr_id = Integer.valueOf(id.getText());
        String name    = productName.getText().trim();
        int    stock   = Integer.valueOf(inv.getText());
        int    min     = Integer.valueOf(minInput.getText());
        int    max     = Integer.valueOf(maxInput.getText());
        Double price   = validator.parseCurrency(productPrice.getText());
        int    index   = inventory.getAllProducts().indexOf(prod);

        // detect if product has no parts
        if (prod.getAllAssociatedParts().size() < 1)
            {
            // product has no parts, inform the user
            dialog.displayProdHasNoParts();

            // exit this function
            return;
            }

        // compute total cost of associated parts
        double prodTotalCost = 0;

        prodTotalCost = prod.getAllAssociatedParts()
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

        // if previous checks pass, product  can be saved
        // create a temporary product to replace the current product with
        Product temp = new Product(curr_id, name, price, stock, min, max);

        // get associated parts
        ObservableList<Part> assoc = prod.getAllAssociatedParts();

        // add all associated parts to temp
        assoc.forEach(
            (part) -> {
                temp.addAssociatedPart(part);
            });

        // update current product
        inventory.updateProduct(index, temp);

        // close the current window
        Stage stage = (Stage) save.getScene().getWindow();

        stage.close();
    }

    // run the search helper to query search input
    @FXML
    private void searchParts ()
    {
        // see implementation details in the Search class in the Model package
        search.searchPart(inventory, partView, searchBar);
    }

    // set the injected inventory object
    void setInventory (Inventory inventory)
    {
        this.inventory = inventory;
    }

    // set the injected product
    void setProduct (Product prod)
    {
        // set the form fields with data from the injected product
        this.id.setText(String.valueOf(prod.getId()));
        this.productName.setText(prod.getName());
        this.productPrice.setText(NumberFormat.getCurrencyInstance(Locale.US)
                                              .format(prod.getPrice()));
        this.inv.setText(String.valueOf(prod.getStock()));
        this.minInput.setText(String.valueOf(prod.getMin()));
        this.maxInput.setText(String.valueOf(prod.getMax()));
        this.prod = prod;
    }

    // set the TableViews
    void setTableViews (Inventory inventory, Product prod)
    {
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
        assocPartView.setItems(prod.getAllAssociatedParts());
    }
}
