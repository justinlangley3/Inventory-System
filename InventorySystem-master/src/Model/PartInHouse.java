
/*
 *  Package: Model
 *  File:    PartInHouse.java
 *
 *  Created by: Justin A Langley
 *  Date: 2019AUG08
 */
package Model;

public class PartInHouse extends Part
{
    // additional data element for InHouse parts
    private int machineId;

    // constructor overload for auto-generated id
    public PartInHouse (String name, double price, int stock, int min, int max, int machineId)
    {
        this.setId(partIdCount);
        this.setName(name);
        this.setPrice(price);
        this.setStock(stock);
        this.setMin(min);
        this.setMax(max);
        setMachineId(machineId);

        // increment the ID counter so the same ID is not re-used
        partIdCount++;
        System.out.println("New part with ID: " + this.getId());
    }

    // parameterized constructor
    public PartInHouse (int id, String name, double price, int stock, int min, int max,
                        int machineId)
    {
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
        this.setStock(stock);
        this.setMin(min);
        this.setMax(max);
        setMachineId(machineId);
        System.out.println("New part with ID: " + this.getId());
    }

    public final int getMachine ()
    {
        return this.machineId;
    }

    public final void setMachineId (int machineId)
    {
        this.machineId = machineId;
    }
}
