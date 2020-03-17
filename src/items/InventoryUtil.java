package items;

import items.Emptyitem;
import items.Item;
import java.util.ArrayList;

public class InventoryUtil {
    private ArrayList<Item> inventory;
    private int maxInventorySize = 0;

    public InventoryUtil(int maxInventorySize){
        this.maxInventorySize = maxInventorySize;

        // set the inventory items to all empty items
        inventory = new ArrayList<Item>(maxInventorySize);
        for( int i = 0 ; i < maxInventorySize ; i++ ){
            inventory.set(i,new Emptyitem());
        }
    }

    public Item getItem(int index){ return inventory.get(index); }
    public void setItem(int index, Item item){ inventory.set(index, item); }
    public Item popItem(int index){
        Item tmp = inventory.get(index);
        inventory.set(index,new Emptyitem());
        return tmp;
    }
}
