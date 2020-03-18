package items;

import java.util.ArrayList;

public class InventoryUtil {
    private ArrayList<Item> inventory;
    private int inventorySize = 0;

    public InventoryUtil(int inventorySize){
        this.inventorySize = inventorySize;

        // set the inventory items to all empty items
        inventory = new ArrayList<Item>(inventorySize);
        for( int i = 0 ; i < inventorySize ; i++ ){
            inventory.set(i,new EmptyItem());
        }
    }

    public int getInventorySize(){ return inventorySize; }
    public Item getItem(int index){ return inventory.get(index); }
    public void setItem(int index, Item item){ inventory.set(index, item); }
    public Item popItem(int index){
        Item tmp = inventory.get(index);
        inventory.set(index,new EmptyItem());
        return tmp;
    }
}
