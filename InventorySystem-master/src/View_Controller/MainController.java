
/*
 *  Package: View_Controller
 *  File:    MainController.java
 *
 *  Created by: Justin A Langley
 *  Date: 2019AUG08
 */
package View_Controller;

import java.io.IOException;

import java.net.URL;

import java.text.NumberFormat;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.util.Callback;

import Helpers.DialogHelper;
import Helpers.SearchHelper;

import Model.Inventory;
import Model.Part;
import Model.PartInHouse;
import Model.PartOutsourced;
import Model.Product;

/*
 * Customized CellFactory class to format a TableView column to currency format.
 * Package private for re-use in other controller classes.
 */
class CurrencyCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>>
{
    public CurrencyCellFactory (){}

    @Override
    public TableCell<S, T> call (TableColumn<S, T> p)
    {
        TableCell<S, T> cell = new TableCell<S, T>()
        {
            // currency format object
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
            @Override
            protected void updateItem (Object item, boolean empty)
            {
                if (empty)
                    {
                    // empty the cell text
                    setText(null);
                    }
                else
                    {
                    // has data, replace the text with currency formatted text
                    setText(currencyFormat.format(item));
                    }
                super.updateItem((T) item, empty);
            }
        };

        return cell;
    }
}


public class MainController implements Initializable
{
    // button elements
    @FXML
    private Button                        exit;
    @FXML
    private Button                        partAdd;
    @FXML
    private Button                        partDelete;
    @FXML
    private Button                        partModify;
    @FXML
    private Button                        partSearch;
    @FXML
    private Button                        prodSearch;
    @FXML
    private Button                        productAdd;
    @FXML
    private Button                        productDelete;
    @FXML
    private Button                        productModify;
    @FXML
    private TableColumn<Part, Double>     partPriceCost;
    @FXML
    private TableColumn<Part, Integer>    partId;
    @FXML
    private TableColumn<Part, Integer>    partInv;
    @FXML
    private TableColumn<Part, String>     partName;
    @FXML
    private TableColumn<Product, Double>  prodPrice;
    @FXML
    private TableColumn<Product, Integer> prodId;
    @FXML
    private TableColumn<Product, Integer> prodInv;
    @FXML
    private TableColumn<Product, String>  prodName;

    // part TableView elements
    @FXML
    private TableView<Part> partView;

    // product TableView Elements
    @FXML
    private TableView<Product> prodView;

    // search bars
    @FXML
    private TextField    partSearchBar;
    @FXML
    private TextField    prodSearchBar;
    private final String addPartDir = "FXMLAddPart.fxml";
    private final String addProdDir = "FXMLAddProduct.fxml";

    // resource directories
    private final String       icon          = "/Images/icon.png";
    private final String       modifyPartDir = "FXMLModifyPart.fxml";
    private final String       modifyProdDir = "FXMLModifyProduct.fxml";
    
    // helper objects
    private final SearchHelper search        = new SearchHelper();
    private Inventory          inventory     = new Inventory();
    private final DialogHelper dialog        = new DialogHelper();

    // Loads the Add Part window where the user can add a new part to the inventory.
    @FXML
    private void addPart ()
    {
        // attempt to open the add part view
        try
            {
            // get resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource(addPartDir));
            Parent     root   = (Parent) loader.load();

            // get the controller
            AddPartController partAddController = loader.getController();

            // create a new stage
            Stage stage = new Stage();

            stage.centerOnScreen();

            // set the stage properties
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setHeight(500);
            stage.setWidth(460);
            stage.setResizable(false);
            stage.getIcons().add(new Image(icon));
            stage.setTitle("Add Part");
            stage.setScene(new Scene(root));

            // inject the inventory object to the new stage
            partAddController.setInventory(inventory);

            // show the add product view
            stage.show();
            }
        catch (IOException e)
            {
            Logger logger = Logger.getLogger(getClass().getName());

            logger.log(Level.SEVERE, "Failed to create new Window.\n", e);
            }
    }

    // Loads the Add Product window where the user can add a new product to the inventory.
    @FXML
    private void addProduct ()
    {
        // attempt to open the add product screen
        try
            {
            // load resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource(addProdDir));
            Parent     root   = (Parent) loader.load();

            // get the controller
            AddProductController prodAddController = loader.getController();

            // create the stage
            Stage stage = new Stage();

            stage.centerOnScreen();

            // set the stage properties
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setHeight(680);
            stage.setWidth(1100);
            stage.setResizable(true);
            stage.getIcons().add(new Image(icon));
            stage.setTitle("Add Product");
            stage.setScene(new Scene(root));

            // inject the inventory object
            prodAddController.setInventory(inventory);

            // call function to set the TableViews
            prodAddController.setTableViews(inventory);

            // show the add product view
            stage.showAndWait();
            }
        catch (IOException e)
            {
            Logger logger = Logger.getLogger(getClass().getName());

            logger.log(Level.SEVERE, "Failed to create new Window.\n", e);
            }
    }

    // A function to delete a Part. It is used as the action event for the deletePart button
    @FXML
    private void deletePart ()
    {
        // get the user-selected part
        Part part = partView.getSelectionModel().getSelectedItem();

        // check if a part was selected
        if (part == null)
            {
            // part not selected, inform the user
            dialog.displayDeletePartFailed();
            }
        else
            {
            if (dialog.confirmDeletePart(part.getName()))
                {
                // user confirmed, delete the part
                inventory.deletePart(part);
                }
            }
    }

    // A function to delete a Product. It is used as the action event for the deleteProduct button
    @FXML
    private void deleteProduct ()
    {
        // get the user-selected product
        Product prod = prodView.getSelectionModel().getSelectedItem();

        // check if a product was selected
        if (prod == null)
            {
            // product not selected, inform the user
            dialog.displayDeleteProdFailed();
            }
        else
            {
            if (dialog.confirmDeleteProd(prod.getName()))
                {
                // user confirmed, delete the product
                inventory.deleteProduct(prod);
                }
            }
    }

    // Function to exit the program. Used as the action event for the exit button FXML object
    @FXML
    private void exit ()
    {
        // log to console
        System.out.println("Exiting program . . . ");

        // close the window
        Stage stage = (Stage) exit.getScene().getWindow();

        stage.close();
    }

    /*
     *  Initializes the controller
     * Begins by running testPayload() to generate a demo inventory
     * Then it refreshes the TableViews
     */
    @Override
    public void initialize (URL url, ResourceBundle rb)
    {
        setTableViews();
        test();
        updateTableViews();
    }

    // Loads the Modify Part window where the user can modify an existing part.
    @FXML
    private void modifyPart () throws IOException
    {
        // attempt to open the modify part screen
        // references for storing the user-selected part as the appropriate type
        PartInHouse    partInHouse    = null;
        PartOutsourced partOutsourced = null;

        // attempt to inject the part into the modify screen
        // throw an error if a part was not selected and inform the user
        try
            {
            // check type of the part
            boolean isInHouse;

            isInHouse = partView.getSelectionModel().getSelectedItem() instanceof PartInHouse;

            // change part to match the type of the part
            if (isInHouse)
                {
                partInHouse = (PartInHouse) partView.getSelectionModel().getSelectedItem();
                }
            else
                {
                partOutsourced = (PartOutsourced) partView.getSelectionModel().getSelectedItem();
                }

            // load resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource(modifyPartDir));
            Parent     root   = (Parent) loader.load();

            // get the controller
            ModifyPartController partModifyController = loader.getController();

            // create the stage
            Stage stage = new Stage();

            stage.centerOnScreen();

            // set the properties
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setHeight(500);
            stage.setWidth(460);
            stage.setResizable(false);
            stage.getIcons().add(new Image(icon));
            stage.setTitle("Modify Part");
            stage.setScene(new Scene(root));

            // inject the selected part, and inventory object
            if (isInHouse)
                {
                partModifyController.setPart(partInHouse);
                partModifyController.setInventory(inventory);
                }
            else
                {
                partModifyController.setPart(partOutsourced);
                partModifyController.setInventory(inventory);
                }

            // show the part modify view
            stage.showAndWait();

            // refresh the table views
            updateTableViews();
            }
        catch (NullPointerException e)
            {
            // generate an alert dialog
            dialog.modifyPartFailed();
            }
    }

    // Loads the Modify Product window where the user can modify an existing product.
    @FXML
    private void modifyProduct () throws IOException
    {
        // attempt to open the modify product screen
        try
            {
            // get selected product
            Product prod = prodView.getSelectionModel().getSelectedItem();

            // create product view loader
            FXMLLoader loader = new FXMLLoader(getClass().getResource(modifyProdDir));
            Parent     root   = (Parent) loader.load();

            // get the controller
            ModifyProductController prodModifyController = loader.getController();

            // create a new stage
            Stage stage = new Stage();

            stage.centerOnScreen();

            // set the stage properties
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setHeight(680);
            stage.setWidth(1100);
            stage.setResizable(true);
            stage.getIcons().add(new Image(icon));
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(root));

            // inject the selected product and inventory
            prodModifyController.setInventory(inventory);
            prodModifyController.setProduct(prod);
            prodModifyController.setTableViews(inventory, prod);

            // show the modify product view
            stage.showAndWait();

            // refresh
            updateTableViews();
            }
        catch (NullPointerException e)
            {
            // generate an alert dialog
            dialog.modifyProdFailed();
            }
    }

    /*
     * This function takes text entered in a text field
     * and then performs various searches on a list of parts based on
     * the text the user entered in the field.
     *
     * If a match is found, it selects it in the Part TableView.
     * If no match is found, it displays a dialog to inform the user.
     *
     * This is the action event for the searchPart button FXML object
     */
    @FXML
    private void searchPart ()
    {
        // implementation details can be found in Search class in the models package
        search.searchPart(inventory, partView, partSearchBar);
    }

    /*
     * This function takes text entered in a text field
     * and then performs various searches on a list of products based on
     * the text the user entered in the field.
     *
     * If a match is found, it selects it in the Product TableView.
     * If no match is found, it displays a dialog to inform the user.
     *
     * This is the action event for the searchProd button FXML object
     */
    @FXML
    private void searchProd ()
    {
        // implementation details can be found in Search class in the models package
        search.searchProd(inventory, prodView, prodSearchBar);
    }

    /*
     * A function to set how the partView and prodView TableViews will
     * be displayed, data to be populated in each column, and finally,
     * populates partView and prodView
     */
    private void setTableViews ()
    {
        // currency formatter
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

        // associate part view cells with part data elements
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        // format the price column to currency format using our custom currency cell factory class
        partPriceCost.setCellFactory(new CurrencyCellFactory<>());

        // populate the table
        partView.setItems(inventory.getAllParts());

        // associate product view cells with product data elements
        prodId.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // format the price column to currency format
        prodPrice.setCellFactory(new CurrencyCellFactory<>());

        // populate the table
        prodView.setItems(inventory.getAllProducts());
    }

    // test data to be injected into the program for demonstrating functionality
    private void test ()
    {
        Part p1  = new PartOutsourced(1, "nut", 0.10, 380, 100, 500, "Metal Machining Co.");
        Part p2  = new PartOutsourced(2, "bolt", 0.14, 200, 100, 500, "Metal Machining Co.");
        Part p3  = new PartOutsourced(3, "screw", 0.13, 260, 80, 480, "Metal Machining Co.");
        Part p4  = new PartOutsourced(4, "bearings, sealed", 1.20, 100, 80, 300, "Spiffy Bearings, Inc.");
        Part p5  = new PartInHouse(5, "small gear", 1.67, 90, 80, 300, 360);
        Part p6  = new PartInHouse(6, "medium gear", 1.98, 90, 80, 300, 360);
        Part p7  = new PartInHouse(7, "large gear", 2.21, 90, 80, 300, 360);
        Part p8  = new PartInHouse(8, "sprocket", 6.46, 80, 60, 300, 365);
        Part p9  = new PartOutsourced(9, "drive chain", 3.20, 60, 50, 95, "Tough Chains Manufacturing");
        Part p10 = new PartOutsourced(10, "stem cap", 0.30, 300, 50, 400, "The Wheel Parts Company");
        Part p11 = new PartOutsourced(11, "spokes", 4.00, 60, 50, 95, "The Wheel Parts Company");
        Part p12 = new PartOutsourced(12, "spoke nipple", 0.05, 300, 200, 800, "The Wheel Parts Company");
        Part p13 = new PartOutsourced(13, "wheel hub", 7.00, 90, 80, 200, "The Wheel Parts Company");
        Part p14 = new PartOutsourced(14, "rim", 12.95, 90, 60, 100, "The Wheel Parts Company");
        Part p15 = new PartInHouse(15, "inner tube", 4.01, 90, 60, 100, 107);
        Part p16 = new PartInHouse(16, "tread", 5.23, 90, 60, 100, 111);
        Part p17 = new PartInHouse(17, "wheel", 35.49, 40, 20, 100, 52);
        Part p18 = new PartOutsourced(18, "aluminum, tubing", 18.01, 30, 25, 60, "All-American Aluminum");
        Part p19 = new PartOutsourced(19, "steel, tubing", 7.46, 30, 25, 60, "Real Steel Corporation");
        Part p20 = new PartInHouse(20, "tube, seat", 4.67, 40, 25, 60, 21);
        Part p21 = new PartInHouse(21, "foam grips (x2)", 2.00, 40, 25, 60, 31);
        Part p22 = new PartInHouse(22, "handle bar caps", 0.30, 80, 50, 120, 32);
        Part p23 = new PartInHouse(23, "handle bars, road", 5.20, 40, 25, 60, 33);
        Part p24 = new PartInHouse(24, "handle bars, bmx", 4.08, 37, 25, 60, 33);
        Part p25 = new PartInHouse(25, "handle bars, mountain", 3.89, 51, 25, 60, 33);
        Part p26 = new PartInHouse(26, "seat, road", 2.96, 40, 25, 60, 40);
        Part p27 = new PartInHouse(27, "seat, mountain", 2.91, 40, 25, 60, 40);
        Part p28 = new PartInHouse(28, "seat, bmx", 2.90, 40, 25, 60, 40);
        Part p29 = new PartInHouse(29, "bike frame, road", 47.01, 32, 25, 58, 96);
        Part p30 = new PartInHouse(30, "bike frame, bmx", 42.31, 37, 25, 48, 96);
        Part p31 = new PartInHouse(31, "brake lever", 1.28, 120, 90, 200, 206);
        Part p32 = new PartOutsourced(32, "brake cable", 3.00, 127, 80, 250, "BicycleWorks");
        Part p33 = new PartInHouse(33, "brake pad", 2.66, 200, 110, 320, 208);
        Part p34 = new PartOutsourced(34, "aluminum, rod", 7.00, 90, 80, 160, "All-American Aluminum");
        Part p35 = new PartOutsourced(35, "steel, rod", 4.00, 90, 80, 150, "Real Steel Corporation");
        Part p36 = new PartInHouse(36, "side pull brake", 28.00, 300, 110, 380, 210);
        Part p37 = new PartInHouse(37, "gear assembly", 13.92, 46, 30, 70, 368);

        inventory.addPart(p1);
        inventory.addPart(p2);
        inventory.addPart(p3);
        inventory.addPart(p4);
        inventory.addPart(p5);
        inventory.addPart(p6);
        inventory.addPart(p7);
        inventory.addPart(p8);
        inventory.addPart(p9);
        inventory.addPart(p10);
        inventory.addPart(p17);
        inventory.addPart(p18);
        inventory.addPart(p19);
        inventory.addPart(p20);
        inventory.addPart(p21);
        inventory.addPart(p22);
        inventory.addPart(p23);
        inventory.addPart(p24);
        inventory.addPart(p25);
        inventory.addPart(p26);
        inventory.addPart(p27);
        inventory.addPart(p28);
        inventory.addPart(p29);
        inventory.addPart(p31);
        inventory.addPart(p32);
        inventory.addPart(p33);
        inventory.addPart(p34);
        inventory.addPart(p35);
        inventory.addPart(p36);
        inventory.addPart(p37);
        Product pt100 = new Product(100, "road bike", 780.00, 30, 18, 40);

        pt100.addAssociatedPart(p8);
        pt100.addAssociatedPart(p8);
        pt100.addAssociatedPart(p9);
        pt100.addAssociatedPart(p10);
        pt100.addAssociatedPart(p11);
        pt100.addAssociatedPart(p12);
        pt100.addAssociatedPart(p13);
        pt100.addAssociatedPart(p14);
        pt100.addAssociatedPart(p15);
        pt100.addAssociatedPart(p16);
        pt100.addAssociatedPart(p17);
        pt100.addAssociatedPart(p18);
        pt100.addAssociatedPart(p19);
        pt100.addAssociatedPart(p20);
        pt100.addAssociatedPart(p21);
        pt100.addAssociatedPart(p22);
        pt100.addAssociatedPart(p23);
        pt100.addAssociatedPart(p26);
        pt100.addAssociatedPart(p29);
        pt100.addAssociatedPart(p30);
        pt100.addAssociatedPart(p31);
        pt100.addAssociatedPart(p32);
        pt100.addAssociatedPart(p33);
        pt100.addAssociatedPart(p34);
        pt100.addAssociatedPart(p36);
        pt100.addAssociatedPart(p37);
        inventory.addProduct(pt100);
        Product pt101 = new Product(101, "mountain bike", 490.00, 17, 12, 42);

        inventory.addProduct(pt101);
        Product pt102 = new Product(102, "bmx bike", 348.00, 13, 12, 30);

        inventory.addProduct(pt102);
        Product pt103 = new Product(103, "wheel", 43.00, 20, 14, 46);

        pt103.addAssociatedPart(p10);
        pt103.addAssociatedPart(p11);
        pt103.addAssociatedPart(p12);
        pt103.addAssociatedPart(p13);
        pt103.addAssociatedPart(p14);
        pt103.addAssociatedPart(p15);
        pt103.addAssociatedPart(p16);
        inventory.addProduct(pt103);
        Product pt104 = new Product(104, "brake cable (x2)", 8.00, 50, 30, 60);

        inventory.addProduct(pt104);
        Product pt105 = new Product(105, "brake pads (x2)", 11.00, 50, 30, 60);

        inventory.addProduct(pt105);
        Product pt106 = new Product(106, "seat, road", 22.00, 20, 10, 30);

        pt106.addAssociatedPart(p20);
        pt106.addAssociatedPart(p26);
        inventory.addProduct(pt106);
        Product pt107 = new Product(107, "seat, mountain", 18.00, 18, 10, 30);

        pt107.addAssociatedPart(p20);
        pt107.addAssociatedPart(p27);
        inventory.addProduct(pt107);
        Product pt108 = new Product(108, "seat, bmx", 16.00, 26, 10, 30);

        pt108.addAssociatedPart(p20);
        pt108.addAssociatedPart(p28);
        inventory.addProduct(pt108);
    }

    // A function to force refreshing of the partView and prodView TableViews
    private void updateTableViews ()
    {
        // refresh the part,product TableViews
        partView.refresh();
        prodView.refresh();
    }
}
