
/*
 *  Package: Model
 *  File:    Part.java
 *
 *  Created by: Justin A Langley
 *  Date: 2019AUG08
 *
 */
package Model;

public abstract class Part
{
    /*
     *  Member for auto-generating part id,
     *  static allows it to be used by other instances of Part
     *  must be left as default package private for child classes to have access
     *
     *  if the test payload is removed from MainController.java,
     *  adjust this to desired start number for auto-generating IDs
     *  also check Product.java for it's ID generation properties
     */
    static int     partIdCount = 38;
    private String name;
    private double price;

    // private data members
    private int id;
    private int max;
    private int min;
    private int stock;

    public Part ()
    {
        // default constructor, required for program to compile
    }

    // parameterized constructor
    public Part (int id, String name, double price, int stock, int min, int max)
    {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
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
