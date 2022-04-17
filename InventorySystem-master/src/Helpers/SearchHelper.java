
/*
 *  Package: Helpers
 *  File:    SearchHelper.java
 *
 *  Created by: Justin A Langley
 *  Date: 2019AUG08
 */
package Helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.Comparator;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import Model.Inventory;
import Model.Part;
import Model.Product;

/**
 * The Search class provides functions to search Observable lists containing
 * Part or Product objects based on a user-entered search token.
 * <p>
 * Its functions take a user entered token, and searches the observable list
 * for a given data element and return the matched object, or otherwise
 * return null.
 * <p>
 * Its functions return the first match, and do not possess advanced capabilities
 * such as returning multiple matches
 *
 * @author Justin Langley
 * @version 1.0
 */
public class SearchHelper
{
    /*
     * Underlying Binary Search functions
     *
     * The first parameter for each of these functions is a sorted ObservableList
     * NOTE: It is highly important that the ObservableList is first sorted
     *       Binary Search does not work on unsorted lists, arrays, etc.
     *       See the intermediary functions above that prepare calls to these functions
     *
     * Example calls:
     * binarySearchPartId(list, token)      :where token is an integer
     * binarySearchPartName(list, token)    :where token is a string
     * binarySearchPartInv(list, token)     :where token is an integer
     * binarySearchPartPrice(list,token)    :where token is a double
     *
     * binarySearchProdId(list, token)      :where token is an integer
     * binarySearchProdName(list, token)    :where token is a string
     * binarySearchProdInv(list, token)     :where token is an integer
     * binarySearchProdPrice(list,token)    :where token is a double
     */

    /**
     * Performs binary search on the ID elements of Part Objects in a
     * given ObservableList of parts
     *
     * @param sorted a sorted ObservableList of Part objects
     * @param token an integer of the Part ID being searched
     * @return a matching Part or null
     */
    private Part binarySearchPartId (ObservableList<Part> sorted, int token)
    {
        int L = 0;
        int R = sorted.size() - 1;

        while (L <= R)
            {
            int M = L + (R - L) / 2;

            // check if token value matches the part ID at the middle value
            if (sorted.get(M).getId() == token)
                {
                // if it matches return the part
                return sorted.get(M);
                }

            // if the token value is greater, ignore the left half
            if (sorted.get(M).getId() < token)
                {
                L = M + 1;
                }

            // if the token value is smaller, ignore the right half
            else
                {
                R = M - 1;
                }
            }

        return null;
    }

    /**
     * Performs binary search on the Inv elements of Part Objects in a
     * given ObservableList of parts
     *
     * @param sorted a sorted ObservableList of Part objects
     * @param token an integer of the Part Stock being searched
     * @return a matching Part or null
     */
    private Part binarySearchPartInv (ObservableList<Part> sorted, int token)
    {
        int L = 0;
        int R = sorted.size() - 1;

        while (L <= R)
            {
            int M = L + (R - L) / 2;

            // check if token value matches the part ID at the middle value
            if (sorted.get(M).getStock() == token)
                {
                // if it matches return the part
                return sorted.get(M);
                }

            // if the token value is greater, ignore the left half
            if (sorted.get(M).getStock() < token)
                {
                L = M + 1;
                }

            // if the token value is smaller, ignore the right half
            else
                {
                R = M - 1;
                }
            }

        return null;
    }

    /**
     * Performs binary search on the Name elements of Part Objects in a
     * given ObservableList of parts
     *
     * @param sorted a sorted ObservableList of Part objects
     * @param token an string of the Part Name being searched
     * @return a matching Part or null
     */
    private Part binarySearchPartName (ObservableList<Part> sorted, String token)
    {
        int L = 0;
        int R = sorted.size() - 1;

        while (L <= R)
            {
            int M   = L + (R - L) / 2;
            int res = token.compareTo(sorted.get(M)
                                            .getName()
                                            .toLowerCase(Locale.US)
                                            .replaceAll("[^a-z0-9 ]+", "")
                                            .trim());

            // check if token is contained in a part Name at the index
            if (sorted.get(M)
                      .getName()
                      .toLowerCase(Locale.US)
                      .replaceAll("[^a-z0-9 ]+", "")
                      .trim()
                      .contains(token))
                {
                // if it matches return the part
                return sorted.get(M);
                }

            // if the token value is greater, ignore the left half
            if (res > 0)
                {
                L = M + 1;
                }

            // if the token value is smaller, ignore the right half
            else
                {
                R = M - 1;
                }
            }

        return null;
    }

    /**
     * Performs binary search on the Price elements of Part Objects in a
     * given ObservableList of parts
     *
     * @param sorted a sorted ObservableList of Part objects
     * @param token an Double of the Part Price being searched
     * @return a matching Part or null
     */
    private Part binarySearchPartPrice (ObservableList<Part> sorted, double token)
    {
        // Truncate token beyond the 2nd decimal place
        token = BigDecimal.valueOf(token).setScale(2, RoundingMode.FLOOR).doubleValue();

        // Left is the start index of the list
        int L = 0;

        // Right is the length of the list - 1
        int R = sorted.size() - 1;

        while (L <= R)
            {
            int M = L + (R - L) / 2;

            // Truncate the price of current part beyond the 2nd decimal place
            Double price = BigDecimal.valueOf(sorted.get(M).getPrice())
                                     .setScale(2, RoundingMode.FLOOR)
                                     .doubleValue();

            // check if token value matches the part ID at the middle value
            if (price == token)
                {
                // if it matches return the part
                return sorted.get(M);
                }

            // if the token value is greater, ignore the left half
            if (price < token)
                {
                L = M + 1;
                }

            // if the token value is smaller, ignore the right half
            else
                {
                R = M - 1;
                }
            }

        return null;
    }

    /**
     * Performs binary search on the ID elements of Product Objects in a
     * given ObservableList of products
     *
     * @param sorted a sorted ObservableList of Product objects
     * @param token an integer of the Product ID being searched
     * @return a matching Product or null
     */
    private Product binarySearchProdId (ObservableList<Product> sorted, int token)
    {
        int L = 0;
        int R = sorted.size() - 1;

        while (L <= R)
            {
            int M = L + (R - L) / 2;

            // check if token value matches the part ID at the middle value
            if (sorted.get(M).getId() == token)
                {
                // if it matches return the part
                return sorted.get(M);
                }

            // if the token value is greater, ignore the left half
            if (sorted.get(M).getId() < token)
                {
                L = M + 1;
                }

            // if the token value is smaller, ignore the right half
            else
                {
                R = M - 1;
                }
            }

        return null;
    }

    /**
     * Performs binary search on the Inv elements of Product Objects in a
     * given ObservableList of products
     *
     * @param sorted a sorted ObservableList of Product objects
     * @param token an integer of the Product Inv being searched
     * @return a matching Product or null
     */
    private Product binarySearchProdInv (ObservableList<Product> sorted, int token)
    {
        int L = 0;
        int R = sorted.size() - 1;

        while (L <= R)
            {
            int M = L + (R - L) / 2;

            // check if token value matches the part ID at the middle value
            if (sorted.get(M).getStock() == token)
                {
                // if it matches return the part
                return sorted.get(M);
                }

            // if the token value is greater, ignore the left half
            if (sorted.get(M).getStock() < token)
                {
                L = M + 1;
                }

            // if the token value is smaller, ignore the right half
            else
                {
                R = M - 1;
                }
            }

        return null;
    }

    /**
     * Performs binary search on the Name elements of Product Objects in a
     * given ObservableList of products
     *
     * @param sorted a sorted ObservableList of Product objects
     * @param token a string of the Product Name being searched
     * @return a matching Product or null
     */
    private Product binarySearchProdName (ObservableList<Product> sorted, String token)
    {
        int L = 0;
        int R = sorted.size() - 1;

        while (L <= R)
            {
            int M   = L + (R - L) / 2;
            int res = token.compareTo(sorted.get(M)
                                            .getName()
                                            .toLowerCase(Locale.US)
                                            .replaceAll("[^a-z0-9 ]+", "")
                                            .trim());

            // check if token value matches the part ID at the middle value
            if (sorted.get(M)
                      .getName()
                      .toLowerCase(Locale.US)
                      .replaceAll("[^a-z0-9 ]+", "")
                      .trim()
                      .contains(token))
                {
                // if it matches return the part
                return sorted.get(M);
                }

            // if the token value is greater, ignore the left half
            if (res > 0)
                {
                L = M + 1;
                }

            // if the token value is smaller, ignore the right half
            else
                {
                R = M - 1;
                }
            }

        return null;
    }

    /**
     * Performs binary search on the Price elements of Product Objects in a
     * given ObservableList of products
     *
     * @param sorted a sorted ObservableList of Product objects
     * @param token an integer of the Product Price being searched
     * @return a matching Product or null
     */
    private Product binarySearchProdPrice (ObservableList<Product> sorted, double token)
    {
        int L = 0;
        int R = sorted.size() - 1;

        token = BigDecimal.valueOf(token).setScale(2, RoundingMode.FLOOR).doubleValue();
        while (L <= R)
            {
            int    M     = L + (R - L) / 2;
            Double price = BigDecimal.valueOf(sorted.get(M).getPrice())
                                     .setScale(2, RoundingMode.FLOOR)
                                     .doubleValue();

            // check if token value matches the part ID at the middle value
            if (price == token)
                {
                // if it matches return the part
                return sorted.get(M);
                }

            // if the token value is greater, ignore the left half
            if (price < token)
                {
                L = M + 1;
                }

            // if the token value is smaller, ignore the right half
            else
                {
                R = M - 1;
                }
            }

        return null;
    }

    /*
     * Intermediary Part Search Functions, to prep data and call the binary search functions
     */

    /**
     * Performs Binary Search on an ObservableList of Part objects
     * for a specified search token
     *
     * @param parts an ObservableList of Part objects
     * @param s a String object of the search token
     * @return a Part object
     */
    private Part partById (ObservableList<Part> parts, String s)
    {
        if ("".equals(s))
            {
            return null;
            }

        // sort the list by part Id
        Comparator<Part> id = Comparator.comparingInt((part) -> part.getId());

        FXCollections.sort(parts, id);

        // convert the search token to an integer
        int token = Integer.valueOf(s);

        // binary search for a part with a part Id same as token
        Part part = binarySearchPartId(parts, token);

        // return the binary search result
        return part;
    }

    /**
     * Performs Binary Search on an ObservableList of Part objects
     * for a specified search token
     *
     * @param parts an ObservableList of Part objects
     * @param s a string of the search token
     * @return a Part object
     */
    private Part partByInv (ObservableList<Part> parts, String s)
    {
        if ("".equals(s))
            {
            return null;
            }

        // sort the list by inv
        Comparator<Part> inv = Comparator.comparingInt((part) -> part.getStock());

        FXCollections.sort(parts, inv);

        // convert the search token to an integer
        int token = Integer.valueOf(s);

        // binary search for a part with an inv same as token
        Part part = binarySearchPartInv(parts, token);

        // return the binary search result
        return part;
    }

    /**
     * Performs Binary Search on an ObservableList of Part objects
     * for a specified search token
     *
     * @param parts an ObservableList of Part objects
     * @param token a string of the search token
     * @return a Part object
     */
    private Part partByName (ObservableList<Part> parts, String token)
    {
        if ("".equals(token))
            {
            return null;
            }

        // sort the list by part name
        Comparator<Part> name = Comparator.comparing((part) -> part.getName());

        FXCollections.sort(parts, name);

        // binary search for an object with a part name same as token
        Part part = binarySearchPartName(parts, token);

        // return binary search result
        return part;
    }

    /**
     * Performs Binary Search on an ObservableList of Part objects
     * for a specified search token
     *
     * @param parts an ObservableList of Part objects
     * @param s a string of the search token
     * @return a Part object
     */
    private Part partByPrice (ObservableList<Part> parts, String s)
    {
        if (s.isEmpty())
            {
            return null;
            }

        // sort the list by part price
        Comparator<Part> price = Comparator.comparingDouble((part) -> part.getPrice());

        FXCollections.sort(parts, price);

        // convert token to double
        Double token;

        try
            {
            token = Double.valueOf(s);
            }
        catch (NumberFormatException e)
            {
            // catch exception and return null if token was not a valid price number
            return null;
            }

        // binary search for an object with a price same as token
        Part part = binarySearchPartPrice(parts, token);

        // return binary search result
        return part;
    }

    /*
     * Intermediary Product Search functions, to prep data and call the binary search functions
     */

    /**
     * Performs Binary Search on an ObservableList of Part objects
     * for a specified search token
     *
     * @param prods an ObservableList of Product objects
     * @param s a string of the search token
     * @return a Part object
     */
    private Product prodById (ObservableList<Product> prods, String s)
    {
        if ("".equals(s))
            {
            return null;
            }

        // sort products by id
        Comparator<Product> id = Comparator.comparingInt((prod) -> prod.getId());

        FXCollections.sort(prods, id);

        // convert search token to integer
        int token = Integer.valueOf(s);

        // binary search for a prod with an Id same as token
        Product prod = binarySearchProdId(prods, token);

        // return the saerch result
        return prod;
    }

    /**
     * Performs Binary Search on an ObservableList of Part objects
     * for a specified search token
     *
     * @param prods an ObservableList of Product objects
     * @param s a string of the search token, will-auto convert to integer
     * @return a Part object
     */
    private Product prodByInv (ObservableList<Product> prods, String s)
    {
        if ("".equals(s))
            {
            return null;
            }

        // sort the list by inv
        Comparator<Product> inv = Comparator.comparingInt((prod) -> prod.getStock());

        FXCollections.sort(prods, inv);

        // convert the search token to an integer
        int token = Integer.valueOf(s);

        // binary search for a product with inv same as token
        Product prod = binarySearchProdInv(prods, token);

        // return the saerch result
        return prod;
    }

    /**
     * Performs Binary Search on an ObservableList of Part objects
     * for a specified search token
     *
     * @param prods an ObservableList of Product objects
     * @param token a string of the search token
     * @return a Part object
     */
    private Product prodByName (ObservableList<Product> prods, String token)
    {
        if ("".equals(token))
            {
            return null;
            }

        // sort the list by name
        Comparator<Product> name = Comparator.comparing((prod) -> prod.getName());

        FXCollections.sort(prods, name);

        // binary search for a prod with name same as token
        Product prod = binarySearchProdName(prods, token);

        return prod;
    }

    /**
     * Performs Binary Search on an ObservableList of Part objects
     * for a specified search token
     *
     * @param prods an ObservableList of Product objects
     * @param s a string of the search token, will auto-convert to double
     * @return a Part object
     */
    private Product prodByPrice (ObservableList<Product> prods, String s)
    {
        if (s.isEmpty())
            {
            return null;
            }

        // sort the list by price
        Comparator<Product> price = Comparator.comparingDouble((prod) -> prod.getPrice());

        FXCollections.sort(prods, price);

        // convert token to double
        Double token;

        try
            {
            token = Double.valueOf(s);
            }
        catch (NumberFormatException e)
            {
            // catch exception and return null if token was not a valid currency value
            return null;
            }

        // binary search for a product with a price same as token
        Product prod = binarySearchProdPrice(prods, token);

        // return the search result
        return prod;
    }

    /*
     * Public search functions to search ObservableLists of parts and products,
     * and subsequently interact with their TableViews
     */

    /**
     * This function takes text entered in a text field
     * and then performs various searches on a list of parts based on
     * the text the user entered in the field. It also enables a user
     * to use prefixes to narrow a search to one field
     *
     * The underlying search technique is binary search. When each search is ran
     * the Observable list of Parts contained in the inventory is sorted based on
     * the data element (column) being searched
     *
     * The implementation is very simplified
     * If a matches exist, only the first match it selected in the Part TableView.
     * If no match is found, it displays a dialog to inform the user.
     *
     * This is the action event for the searchPart button FXML object
     *
     * @param inventory The inventory object holding the parts being searched
     * @param partView The TableView displaying the parts from the inventory
     * @param partSearchBar The TextField behaving as a search bar
     */
    public void searchPart (Inventory inventory, TableView partView, TextField partSearchBar)
    {
        Part         part;
        DialogHelper dialog = new DialogHelper();
        String       token  = partSearchBar.getText()
                                           .trim()
                                           .toLowerCase(Locale.US)
                                           .replaceAll("[^a-z0-9,.$: ]+", "");

        /*
         *                *** Searches are case insensitive
         *  We need to narrow the search while providing the user the capability of
         *  telling us specifically what to search for
         *  This would be a great use for Regex to determine what the user wants
         *
         *  Let's give them some search pre-fixing options to tell us what they want
         *  We'll look for these special prefixes on each search query
         *
         *                          Search Prefixes
         *
         *      ("id:")    : The user wants to search for a part of a given ID
         *      ("inv:")   : The user wants to search for a part of given INV
         *      ("name:")  : The user wants to search for a part by name
         *      ("price:") : The user wants to search for a part of a given price
         */

        // search by Part ID matches numeric regex OR "id:" prefix
        if (token.matches("[0-9]+") || token.startsWith("id:"))
            {
            // replace any non-numeric characters, i.e. remove "id:" prefix if exists
            token = token.replaceAll("[^0-9]+", "");

            // perform the search
            part = partById(inventory.getAllParts(), token);

            // check if a match was found
            if (part != null)
                {
                // match was found, select it in the Part View
                partView.getSelectionModel().select(part);

                // notify the user of the search results
                dialog.displayPartSearchResults(part);

                return;
                }
            }

        // search by Part Inv matches only if "inv:" prefix is present
        // by default we should assume an integer is a Part ID
        else if (token.startsWith("inv:"))
            {
            // replace all non-numeric characters, i.e remove "inv:" prefix if exists
            token = token.replaceAll("[^0-9]+", "");

            // perform the search
            part = partByInv(inventory.getAllParts(), token);

            // check if a match was found
            if (part != null)
                {
                // match was found, select it in the Part View
                partView.getSelectionModel().select(part);

                // notify the user of the search results
                dialog.displayPartSearchResults(part);

                return;
                }
            }

        // search by Part Price matches currency regex OR "price:" prefix
        else if (token.startsWith("price:")
                 || token.matches(
                     "\\$?([0-9]{1,3},([0-9]{3},)*[0-9]{3}|[0-9]+).?([0-9]?[0-9]?){0,1}"))
            {
            // remove invalid characters
            token = token.replaceAll("[^0-9.]", "");

            // perform search
            part = partByPrice(inventory.getAllParts(), token);

            // check if a match was found
            if (part != null)
                {
                // match was found, select it in the Part View
                partView.getSelectionModel().select(part);

                // notify the user of the search results
                dialog.displayPartSearchResults(part);

                return;
                }
            }
        else
            {
            // Default case: search is a part name
            if (token.startsWith("name:"))
                {
                // remove the prefix if exists
                token = token.substring(5).trim();
                }

            // perform the search
            part = partByName(inventory.getAllParts(), token.replaceAll("[^a-z0-9 ]", ""));
            if (part != null)
                {
                // match was found, select it in the Part View
                partView.getSelectionModel().select(part);

                // notify the user of the search results
                dialog.displayPartSearchResults(part);

                return;
                }
            }

        // search query yielded no results
        if (part == null)
            {
            dialog.displayPartNotFound(token);
            }
    }

    /**
     * This function takes text entered in a text field
     * and then performs various searches on a list of parts based on
     * the text the user entered in the field. It also enables a user
     * to use prefixes to narrow a search to one field
     *
     * The underlying search technique is binary search. When each search is ran
     * the Observable list of Products contained in the inventory is sorted based on
     * the data element (column) being searched
     *
     * The implementation is very simplified
     * If a matches exist, only the first match it selected in the Product TableView.
     * If no match is found, it displays a dialog to inform the user.
     *
     * This is the action event for the searchPart button FXML object
     *
     * @param inventory The inventory object holding the products being searched
     * @param prodView The TableView displaying the products from the inventory
     * @param prodSearchBar The TextField behaving as a search bar
     */
    public void searchProd (Inventory inventory, TableView prodView, TextField prodSearchBar)
    {
        Product      prod;
        DialogHelper dialog = new DialogHelper();
        String       token  = prodSearchBar.getText()
                                           .trim()
                                           .toLowerCase(Locale.US)
                                           .replaceAll("[^a-z0-9,.$: ]+", "");

        /*
         *                *** Searches are case insensitive
         *  We need to narrow the search while providing the user the capability of
         *  telling us specifically what to search for
         *  This would be a great use for Regex to determine what the user wants
         *
         *  Let's give them some search pre-fixing options to tell us what they want
         *  We'll look for these special prefixes on each search query
         *
         *                          Search Prefixes
         *
         *      ("id:")    : The user wants to search for a product of a given ID
         *      ("inv:")   : The user wants to search for a product of given INV
         *      ("name:")  : The user wants to search for a product by name
         *      ("price:") : The user wants to search for a product of a given price
         */

        // search by Product ID matches numeric regex OR "id:" prefix
        if (token.matches("[0-9]+") || token.startsWith("id:"))
            {
            // replace any non-numeric characters, i.e. remove "id:" prefix if exists
            token = token.replaceAll("[^0-9]+", "");

            // perform the search
            prod = prodById(inventory.getAllProducts(), token);

            // check if a match was found
            if (prod != null)
                {
                // match was found, select it in the Product View
                prodView.getSelectionModel().select(prod);

                // notify the user of the search results
                dialog.displayProdSearchResults(prod);

                return;
                }
            }

        // search by Product Inv matches only if "inv:" prefix is present
        // by default we should assume an integer is a Product ID
        else if (token.startsWith("inv:"))
            {
            // replace all non-numeric characters, i.e remove "inv:" prefix if exists
            token = token.replaceAll("[^0-9]+", "");

            // perform the search
            prod = prodByInv(inventory.getAllProducts(), token);

            // check if a match was found
            if (prod != null)
                {
                // match was found, select it in the Product View
                prodView.getSelectionModel().select(prod);

                // notify the user of the search results
                dialog.displayProdSearchResults(prod);

                return;
                }
            }

        // search by Product Price matches currency regex OR "price:" prefix
        else if (token.startsWith("price:")
                 || token.matches(
                     "\\$?([0-9]{1,3},([0-9]{3},)*[0-9]{3}|[0-9]+).?([0-9]?[0-9]?){0,1}"))
            {
            // remove invalid characters
            token = token.replaceAll("[^0-9.]", "");

            // perform search
            prod = prodByPrice(inventory.getAllProducts(), token);

            // check if a match was found
            if (prod != null)
                {
                // match was found, select it in the Product View
                prodView.getSelectionModel().select(prod);

                // notify the user of the search results
                dialog.displayProdSearchResults(prod);

                return;
                }
            }
        else
            {
            // Default case: search is a product name
            if (token.startsWith("name:"))
                {
                // remove the prefix if exists
                token = token.substring(5).trim();
                }

            // Perform the search
            prod = prodByName(inventory.getAllProducts(), token.replaceAll("[^a-z0-9 ]", ""));
            if (prod != null)
                {
                // match was found, select it in the Product View
                prodView.getSelectionModel().select(prod);

                // notify the user of the search results
                dialog.displayProdSearchResults(prod);

                return;
                }
            }

        // search query yielded no results
        if (prod == null)
            {
            dialog.displayProdNotFound(token);
            }
    }
}
