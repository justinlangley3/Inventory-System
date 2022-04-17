
/*
 *  Package: Model
 *  File:    Inventory.java
 *
 *  Created by: Justin A Langley
 *  Date: 2019AUG08
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory
{
    // data members for inventory, ObservableLists for parts and products
    private ObservableList<Part>    allParts    = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public void addPart (Part newPart)
    {
        this.allParts.add(newPart);
    }

    public void addProduct (Product newProduct)
    {
        this.allProducts.add(newProduct);
    }

    public void deletePart (Part selectedPart)
    {
        this.allParts.remove(selectedPart);
    }

    public void deleteProduct (Product product)
    {
        this.allProducts.remove(product);
    }

    public ObservableList<Part> getAllParts ()
    {
        return this.allParts;
    }

    public ObservableList<Product> getAllProducts ()
    {
        return this.allProducts;
    }

    public Part lookupPart (int partId)
    {
        Part part = this.allParts.get(partId);

        return part;
    }

    public Product lookupProduct (int productId)
    {
        Product product = this.allProducts.get(productId);

        return product;
    }

    // method to update a part
    public void updatePart (int index, Part selectedPart)
    {
        this.allParts.set(index, selectedPart);
    }

    public void updateProduct (int index, Product selectedProduct)
    {
        this.allProducts.set(index, selectedProduct);
    }
}
