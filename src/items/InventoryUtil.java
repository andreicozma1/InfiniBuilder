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

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // REMOVE ITEMS FROM THE INVENTORY

    /**
     * This function will decrement the count of the item at the given index from the inventory. If the index is not in range or if the size is less than 1 it will return an EmptyItem.
     * @param index
     * @return
     */
    public Item popItem(int index){
        if( index<0 || index >= inventorySize ){
            System.out.println("The given index is not in range");
            return new EmptyItem();
        }

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

    /**
     * This function will decrement the count of the given item from the inventory. If the item is not in the inventory it will return -1.
     * @param item
     * @return
     */
    public int popItem(Item item){
        String itemTag = item.getItemTag();
        // if item is in the inventory
        if(indexes.containsKey(itemTag)){
            int itemIndex = indexes.get(itemTag);
            popItem(itemIndex);
            return itemIndex;
        }
        // if item is not in the inventory
        else
        {
            System.out.println("Inventory does not contain given item");
            return -1;
        }
    }

    /**
     * This function will remove the given amount of items at the given index from the inventory. If the index is not in range or if the size is less than 1 it will return an EmptyItem.
     * @param index
     * @param size
     * @return
     */
    public Item popItem(int index, int size){
        if(size<1){
            System.out.println("Size must be greater than 0");
            return new EmptyItem();
        }
        if( index<0 || index >= inventorySize ){
            System.out.println("The given index is not in range");
            return new EmptyItem();
        }
        Item tmp = inventory.get(index);
        int sizeAfterPop = sizes.get(tmp.getItemTag())-size;
        if ( sizeAfterPop < 0 ) sizeAfterPop = 0;
        // if there is no item in the inventory
        if (tmp.getItemTag() == "EMPTY"){
            System.out.println("No Item at index "+index);
        }

        // if last item in the item stack
        else if ( sizeAfterPop == 0 ){
            System.out.println("Index "+index+" is now empty");
            sizes.remove(tmp.getItemTag());
            indexes.remove(tmp.getItemTag());
            inventory.set(index,new EmptyItem());
        }

        // if there are items left over in the item stack
        else{
            System.out.println("Index " + index + " now has " + (sizeAfterPop) + " items");
            sizes.put(tmp.getItemTag(),sizeAfterPop);
        }

        return tmp;
    }

    /**
     * This function will remove the given amount of items from the inventory. If the item is not in the inventory it will return -1.
     * @param item
     * @param size
     * @return
     */
    public int popItem(Item item, int size){
        if(size<1){
            System.out.println("Size must be greater than 0");
            return -1;
        }

        String itemTag = item.getItemTag();
        // if item is in the inventory
        if(indexes.containsKey(itemTag)){
            int itemIndex = indexes.get(itemTag);
            popItem(itemIndex,size);
            return itemIndex;
        }
        // if item is not in the inventory
        else
        {
            System.out.println("Inventory does not contain given item");
            return -1;
        }
    }

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // ADD ITEMS TO THE INVENTORY

    // if the item isn't already in the inventory it will fill up the inventory from index 0 to inventorySize-1
    public void addItem(Item item){
      if(indexes.containsKey(item.getItemTag())){
          addItem(indexes.get(item.getItemTag()),item);
      }else{
          for( int i = 0 ; i < inventory.size() ; i++ ){
              if(inventory.get(i).getItemTag() == "EMPTY"){
                    addItem(i,item);
                    break;
              }
          }
      }
    }

    public void addItem(Item item, int size){
        if(indexes.containsKey(item.getItemTag())){
            addItem(indexes.get(item.getItemTag()),item,size);
        }else{
            for( int i = 0 ; i < inventory.size() ; i++ ){
                if(inventory.get(i).getItemTag() == "EMPTY"){
                    addItem(i,item,size);
                    break;
                }
            }
        }
    }

    // will place the item at the given index
    public void addItem(int index, Item item){

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

    public void addItem(int index, Item item, int size){

        String itemTag = item.getItemTag();

        // if the index is empty
        if(inventory.get(index).getItemTag() == "EMPTY" ){
            System.out.println("Added the given item to the given index");
            inventory.set(index, item);
            sizes.put(itemTag,size);
            indexes.put(itemTag,index);
        }
        //if the index contains the correct item
        else if (inventory.get(index).getItemTag() == itemTag ) {
            System.out.println("Incremented the number of items at the given index");
            sizes.put(itemTag,sizes.get(itemTag)+size);
        }
        // if the item at the index does not match the given item
        else{
            System.out.println("Given item does not match the item at the given index");
        }
    }

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // SWAP ITEMS IN THE INVENTORY

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
            if(item.getItemTag() == "EMPTY"){
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
