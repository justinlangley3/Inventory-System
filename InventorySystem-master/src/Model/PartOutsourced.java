
/*
 *  Package: Model
 *  File:    PartOutsourced.java
 *
 *  Created by: Justin A Langley
 *  Date: 2019AUG08
 */
package Model;

public class PartOutsourced extends Part
{
    // private data element, companyName, for outsourced parts
    private String companyName;

    // constructor overload for auto-generated id
    public PartOutsourced (String name, double price, int stock, int min, int max,
                           String companyName)
    {
        this.companyName = companyName;
        this.setId(partIdCount);
        this.setName(name);
        this.setPrice(price);
        this.setStock(stock);
        this.setMin(min);
        this.setMax(max);
        partIdCount++;
        System.out.println("New part with ID: " + this.getId());
    }

    // parameterized constructor
    public PartOutsourced (int id, String name, double price, int stock, int min, int max,
                           String companyName)
    {
        this.companyName = companyName;
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
        this.setStock(stock);
        this.setMin(min);
        this.setMax(max);
        System.out.println("New part with ID: " + this.getId());
    }

    public String getCompanyName ()
    {
        return this.companyName;
    }

    public void setCompanyName (String companyName)
    {
        this.companyName = companyName;
    }
}
