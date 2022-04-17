
/*
 *  Package: Model
 *  File:    Product.java
 *
 *  Created by: Justin A Langley
 *  Date: 2019AUG08
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product
{
    /*
     * counter for auto-generating product ID, static so all product instances have access
     * if the test payload is removed, adjust this to the desired start number for auto-generating IDs
     * also be sure to check Part.java for its similar ID generation properties
     */
    private static int prodIdCount = 109;
    private String     name;
    private double     price;
    private int        id;
    private int        max;
    private int        min;
    private int        stock;

    // private data elements for Product objects
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    // constuctor overload, auto-generates product ID
    public Product (String name, double price, int stock, int min, int max)
    {
        setId(prodIdCount);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);

        // Increment the product ID counter so the same ID isn't re-used
        prodIdCount++;
    }

    // default, parameterized constructor
    public Product (int id, String name, double price, int stock, int min, int max)
    {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }

    // associate a part with a product
    public void addAssociatedPart (Part part)
    {
        // attempt to associate part
        try
            {
            // set isAdded to true if it was added to associatedParts
            boolean isAdded = this.associatedParts.add(part);

            if (isAdded)
                {
                // print to console that part was associated
                System.out.println("Part:\t" + part.getName() + ", associated with\n\t\tProduct: "
                                   + this.getName() + "\n");
                }
            }
        catch (Exception e)
            {
            // print the exception to console if the part could not be associated
            System.out.println("Part:\t" + part.getName() + ", could not be associated"
                               + "with\n\t\tProduct: " + this.getName() + " because:\n\t\t" + e);
            }
    }

    // remove an associated part from a product
    public void deleteAssociatedPart (Part associatedPart)
    {
        // attempt to disassociate part
        try
            {
            // set isRemoved to true if it was deleted from associatedParts
            boolean isRemoved = this.associatedParts.remove(associatedPart);

            if (isRemoved)
                {
                // print to console that part was disassociated
                System.out.println("Part:\t" + associatedPart.getName()
                                   + ", disassociated from\n\t\tProduct: " + this.getName());
                }
            }
        catch (Exception e)
            {
            // print the exception to console if part could not be disassociated
            System.out.println("Part:\t" + associatedPart.getName()
                               + ", could not be disassociated" + "with\n\t\tProduct: "
                               + this.getName() + " because:\n\t\t" + e);
            }
    }

    // returns an ObservableList of all parts in associatedParts
    public ObservableList<Part> getAllAssociatedParts ()
    {
        return this.associatedParts;
    }

    public final int getId ()
    {
        return this.id;
    }

    public final int getMax ()
    {
        return this.max;
    }

    public final int getMin ()
    {
        return this.min;
    }

    public final String getName ()
    {
        return this.name;
    }

    public final double getPrice ()
    {
        return this.price;
    }

    public final int getStock ()
    {
        return this.stock;
    }

    public final void setId (int id)
    {
        this.id = id;
    }

    public final void setMax (int max)
    {
        this.max = max;
    }

    public final void setMin (int min)
    {
        this.min = min;
    }

    public final void setName (String name)
    {
        this.name = name;
    }

    public final void setPrice (double price)
    {
        this.price = price;
    }

    public final void setStock (int stock)
    {
        this.stock = stock;
    }
}
