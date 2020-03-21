package items;

import java.util.ArrayList;
import java.util.HashMap;

public class InventoryUtil {
    private ArrayList<Item> inventory;
    private HashMap<String,Integer> indexes = new HashMap<String, Integer>();
    private HashMap<String,Integer> sizes = new HashMap<String, Integer>();
    private int inventorySize = 0;

    public InventoryUtil(int inventorySize){
        this.inventorySize = inventorySize;

        // set the inventory items to all empty items
        inventory = new ArrayList<Item>(inventorySize);
        for( int i = 0 ; i < inventorySize ; i++ ){
            inventory.add(i,new EmptyItem());
        }
    }

    public int getInventorySize(){ return inventorySize; }
    public Item getItem(int index){ return inventory.get(index); }

    public Item popItem(int index){
        Item tmp = inventory.get(index);
        // if there is no item in the inventory
        if (tmp.getItemTag() == "EMPTY"){
            System.out.println("No Item at index "+index);
        }

        // if last item in the item stack
        else if (sizes.get(tmp.getItemTag()) == 1 ){
            System.out.println("Index "+index+" is now empty");
            sizes.remove(tmp.getItemTag());
            indexes.remove(tmp.getItemTag());
            inventory.set(index,new EmptyItem());
        }

        // if there are items left over in the item stack
        else{
            System.out.println("Index " + index + " now has " + (sizes.get(tmp.getItemTag()) - 1) + " items");
            sizes.put(tmp.getItemTag(),sizes.get(tmp.getItemTag())-1);
        }

        return tmp;
    }

    public void addItem(int index,Item item){

        String itemTag = item.getItemTag();

        // if the index is empty
        if(inventory.get(index).getItemTag() == "EMPTY" ){
            System.out.println("Added the given item to the given index");
            inventory.set(index, item);
            sizes.put(itemTag,1);
            indexes.put(itemTag,index);
        }
        //if the index contains the correct item
        else if (inventory.get(index).getItemTag() == itemTag ) {
            System.out.println("Incremented the number of items at the given index");
            sizes.put(itemTag,sizes.get(itemTag)+1);
        }
        // if the item at the index does not match the given item
        else{
            System.out.println("Given item does not match the item at the given index");
        }
    }

    public void swapItems(int index1, int index2){
        if(index1<0 || index1 >=inventorySize){
            System.out.println("index1 is not within the bounds of the inventory");
            return;
        }
        if(index2<0 || index2 >=inventorySize){
            System.out.println("index2 is not within the bounds of the inventory");
            return;
        }

        // item at index1
        Item item1 = inventory.get(index1);
        String item1Tag = item1.getItemTag();

        // item at index2
        Item item2 = inventory.get(index2);
        String item2Tag = item2.getItemTag();

        // swap the items in inventory
        inventory.set(index1,item2);
        inventory.set(index2,item1);

        // change the item indexes in indexes
        indexes.put(item1Tag,index2);
        indexes.put(item2Tag,index1);
    }

    public void print(){
        System.out.println("CURRENT INVENTORY::");
        for (int i = 0 ; i < inventory.size() ; i++){
            Item item = inventory.get(i);
            int size;
            if(item.getItemTag() =="EMPTY"){
                size = 0;
            }
            else{
                size = sizes.get(item.getItemTag());
            }
            System.out.println( "Index = " +i +
                                " , Item = "+item.getItemTag() +
                                " , Size = "+size);
        }
    }

}
