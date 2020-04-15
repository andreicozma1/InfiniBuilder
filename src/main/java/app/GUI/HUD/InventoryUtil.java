package app.GUI.HUD;

import app.player.PlayerUtil;
import app.structures.StructureProperties;
import app.structures.objects.BaseStructure;
import app.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Inventory util manages a set of base structures.
 */
public class InventoryUtil {

    // global variables
    private static final String TAG = "InventoryUtil";
    private final ArrayList<BaseStructure> inventory;
    private final HashMap<String, Integer> indexes = new HashMap<String, Integer>();
    private final HashMap<String, Integer> sizes = new HashMap<String, Integer>();
    public PlayerUtil context;
    private int inventorySize = 0;
    private int currentIndex = 0;
    private boolean isCycle = false;
    private BaseStructure currentItem = new BaseStructure();

    /**
     * Constructor takes a player to attach the inventory to and the size of the inventory.
     * @param ctx
     * @param inventorySize
     */
    public InventoryUtil(PlayerUtil ctx, int inventorySize) {
        Log.d(TAG, "CONSTRUCTOR");

        context = ctx;
        this.inventorySize = inventorySize;

        // set the inventory items to all empty items
        inventory = new ArrayList<>(inventorySize);
        for (int i = 0; i < inventorySize; i++) {
            inventory.add(i, new BaseStructure());
        }
    }

    //getters
    public int getInventorySize() { return inventorySize; }
    public BaseStructure getItem(int index) { return inventory.get(index); }
    public boolean isCurrentIndex(int index) { return (currentIndex == index); }
    public boolean isCycle() { return isCycle; }
    public int getCurrentIndex() { return currentIndex; }
    public int getItemSize(BaseStructure Base_Structure) { return sizes.getOrDefault(Base_Structure.getProps().getPROPERTY_ITEM_TAG(), 0); }
    public int getIndexSize(int index) { return getItemSize(getItem(index)); }
    public BaseStructure getCurrentItem() { return inventory.get(currentIndex); }

    //setters
    public void setIsCycle(boolean isCycle) { this.isCycle = isCycle; }
    public void setCurrentIndex(int currentIndex) {

        // if the given index is out of range set it to the closest index
        if (currentIndex < 0) currentIndex = 0;
        else if (currentIndex >= inventorySize) currentIndex = inventorySize - 1;
        this.currentIndex = currentIndex;
        currentItem = inventory.get(currentIndex);
    }


    // this will remove a single item from the current item
    public BaseStructure popCurrentItem() { return popItem(currentIndex); }

    // this will move the current index and item up or down the inventory based on the delta
    public int moveCurrIndex(int delta) {
        if (isCycle) {
            currentIndex = (currentIndex + delta) % inventorySize;
        } else {
            currentIndex += delta;
            if (currentIndex < 0) currentIndex = 0;
            else if (currentIndex >= inventorySize) currentIndex = inventorySize - 1;
        }

        currentItem = inventory.get(currentIndex);
        return currentIndex;
    }

    // this moves up the inventory by one index
    public int moveUpInventory() {
        currentIndex++;
        if (isCycle) {
            if (currentIndex == inventorySize) currentIndex = 0;
        } else {
            if (currentIndex == inventorySize) currentIndex = inventorySize - 1;
        }

        // return the new index
        currentItem = inventory.get(currentIndex);
        return currentIndex;
    }

    // this moves down the inventory by one index
    public int moveDownInventory() {
        currentIndex--;
        if (isCycle) {
            if (currentIndex == -1) currentIndex = inventorySize - 1;
        } else {
            if (currentIndex == -1) currentIndex = 0;
        }

        // return the new index
        currentItem = inventory.get(currentIndex);
        return currentIndex;
    }


    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // REMOVE ITEMS FROM THE INVENTORY

    /**
     * This function will decrement the count of the item  at the given index from the inventory. If the index is not in range or if the size is less than 1 it will return an EmptyItem.
     *
     * @param index
     * @return
     */
    public BaseStructure popItem(int index) {
        if (index < 0 || index >= inventorySize) {
            Log.d(TAG,"The given index is not in range");
            return new BaseStructure();
        }

        BaseStructure tmp = inventory.get(index);
        Log.d(TAG,"POP ITEM " + tmp.getScaleX() + " " + tmp.getScaleY() + " " + tmp.getScaleZ());

        Log.d(TAG,"/////////// " + tmp.getShape());
        if (tmp.getProps().getPROPERTY_ITEM_TAG() == StructureProperties.UNDEFINED_TAG) {
            // if there is no item in the inventory
            Log.d(TAG,"No Item at index " + index);
        } else if (sizes.get(tmp.getProps().getPROPERTY_ITEM_TAG()) == 1) {
            // if last item in the item stack
            Log.d(TAG,"Index " + index + " is now empty");
            sizes.remove(tmp.getProps().getPROPERTY_ITEM_TAG());
            indexes.remove(tmp.getProps().getPROPERTY_ITEM_TAG());
            inventory.set(index, new BaseStructure());
        } else {
            // if there are items left over in the item stack
            Log.d(TAG,"Index " + index + " now has " + (sizes.get(tmp.getProps().getPROPERTY_ITEM_TAG()) - 1) + " items");
            sizes.put(tmp.getProps().getPROPERTY_ITEM_TAG(), sizes.get(tmp.getProps().getPROPERTY_ITEM_TAG()) - 1);
        }

        return tmp;
    }

    /**
     * This function will decrement the count of the given item from the inventory. If the item is not in the inventory it will return -1.
     *
     * @param item
     * @return
     */
    public int popItem(BaseStructure item) {
        String itemTag = item.getProps().getPROPERTY_ITEM_TAG();
        // if item is in the inventory
        if (indexes.containsKey(itemTag)) {
            int itemIndex = indexes.get(itemTag);
            popItem(itemIndex);
            return itemIndex;
        }
        // if item is not in the inventory
        else {
            Log.d(TAG,"Inventory does not contain given item");
            return -1;
        }
    }

    /**
     * This function will remove the given amount of items at the given index from the inventory. If the index is not in range or if the size is less than 1 it will return an EmptyItem.
     *
     * @param index
     * @param size
     * @return
     */
    public BaseStructure popItem(int index, int size) {
        if (size < 1) {
            Log.d(TAG,"Size must be greater than 0");
            return new BaseStructure();
        }
        if (index < 0 || index >= inventorySize) {
            Log.d(TAG,"The given index is not in range");
            return new BaseStructure();
        }
        BaseStructure tmp = inventory.get(index);
        int sizeAfterPop = sizes.get(tmp.getProps().getPROPERTY_ITEM_TAG()) - size;
        if (sizeAfterPop < 0) sizeAfterPop = 0;
        // if there is no item in the inventory
        if (tmp.getProps().getPROPERTY_ITEM_TAG() == StructureProperties.UNDEFINED_TAG) {
            Log.d(TAG,"No Item at index " + index);
        }

        // if last item in the item stack
        else if (sizeAfterPop == 0) {
            Log.d(TAG,"Index " + index + " is now empty");
            sizes.remove(tmp.getProps().getPROPERTY_ITEM_TAG());
            indexes.remove(tmp.getProps().getPROPERTY_ITEM_TAG());
            inventory.set(index, new BaseStructure());
            inventory.set(index, new BaseStructure());
        }

        // if there are items left over in the item stack
        else {
            Log.d(TAG,"Index " + index + " now has " + (sizeAfterPop) + " items");
            sizes.put(tmp.getProps().getPROPERTY_ITEM_TAG(), sizeAfterPop);
        }

        return tmp;
    }

    /**
     * This function will remove the given amount of items from the inventory. If the item is not in the inventory it will return -1.
     *
     * @param item
     * @param size
     * @return
     */
    public int popItem(BaseStructure item, int size) {
        if (size < 1) {
            Log.d(TAG,"Size must be greater than 0");
            return -1;
        }

        String itemTag = item.getProps().getPROPERTY_ITEM_TAG();
        // if item is in the inventory
        if (indexes.containsKey(itemTag)) {
            int itemIndex = indexes.get(itemTag);
            popItem(itemIndex, size);
            return itemIndex;
        }
        // if item is not in the inventory
        else {
            Log.d(TAG,"Inventory does not contain given item");
            return -1;
        }
    }

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // ADD ITEMS TO THE INVENTORY

    // if the item isn't already in the inventory it will fill up the inventory from index 0 to inventorySize-1
    public void addItem(BaseStructure item) {

        if (indexes.containsKey(item.getProps().getPROPERTY_ITEM_TAG())) {
            addItem(indexes.get(item.getProps().getPROPERTY_ITEM_TAG()), item);
        } else {
            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.get(i).getProps().getPROPERTY_ITEM_TAG() == StructureProperties.UNDEFINED_TAG) {
                    addItem(i, item);
                    break;
                }
            }
        }
    }

    // added an item with the given size to the inventory
    public void addItem(BaseStructure item, int size) {

        if (indexes.containsKey(item.getProps().getPROPERTY_ITEM_TAG())) {
            addItem(indexes.get(item.getProps().getPROPERTY_ITEM_TAG()), item, size);
        } else {
            for (int i = 0; i < inventory.size(); i++) {
                if (inventory.get(i).getProps().getPROPERTY_ITEM_TAG() == StructureProperties.UNDEFINED_TAG) {
                    addItem(i, item, size);
                    break;
                }
            }
        }
    }

    // will place the item at the given index
    public void addItem(int index, BaseStructure item) {
        Log.d(TAG,"Adding item " + item.getProps().getPROPERTY_ITEM_TAG() + " to index " + index);

        if (index < 0 || index >= inventorySize) {
            Log.d(TAG,"The given index is not in range");
        }

        String itemTag = item.getProps().getPROPERTY_ITEM_TAG();

        // if the index is empty
        if (inventory.get(index).getProps().getPROPERTY_ITEM_TAG() == StructureProperties.UNDEFINED_TAG) {
            Log.d(TAG,"Added the given item to the given index");
            inventory.set(index, item);
            sizes.put(itemTag, 1);
            indexes.put(itemTag, index);
        }
        //if the index contains the correct item
        else if (inventory.get(index).getProps().getPROPERTY_ITEM_TAG() == itemTag) {
            Log.d(TAG,"Incremented the number of items at the given index");
            sizes.put(itemTag, sizes.get(itemTag) + 1);
        }
        // if the item at the index does not match the given item
        else {
            Log.d(TAG,"Given item does not match the item at the given index");
        }
    }

    // adds an item to the given index with the given size
    public void addItem(int index, BaseStructure item, int size) {

        Log.d(TAG,"Adding item " + item.getProps().getPROPERTY_ITEM_TAG() + " of size " + size + " at index " + index);

        if (size < 1) {
            Log.d(TAG,"Size must be greater than 0");
        }
        if (index < 0 || index >= inventorySize) {
            Log.d(TAG,"The given index is not in range");
        }

        String itemTag = item.getProps().getPROPERTY_ITEM_TAG();

        // if the index is empty
        if (inventory.get(index).getProps().getPROPERTY_ITEM_TAG() == StructureProperties.UNDEFINED_TAG) {
            Log.d(TAG,"Added the given item to the given index");
            inventory.set(index, item);
            sizes.put(itemTag, size);
            indexes.put(itemTag, index);
        } else if (inventory.get(index).getProps().getPROPERTY_ITEM_TAG() == itemTag) {
            //if the index contains the correct item
            Log.d(TAG,"Incremented the number of items at the given index");
            sizes.put(itemTag, sizes.get(itemTag) + size);
        } else {
            // if the item at the index does not match the given item
            Log.d(TAG,"Given item does not match the item at the given index");
        }
    }

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // SWAP ITEMS IN THE INVENTORY

    public void swapItems(int index1, int index2) {
        if (index1 < 0 || index1 >= inventorySize) {
            Log.d(TAG,"index1 is not within the bounds of the inventory");
            return;
        }
        if (index2 < 0 || index2 >= inventorySize) {
            Log.d(TAG,"index2 is not within the bounds of the inventory");
            return;
        }

        // item at index1
        BaseStructure item1 = inventory.get(index1);
        String item1Tag = item1.getProps().getPROPERTY_ITEM_TAG();

        // item at index2
        BaseStructure item2 = inventory.get(index2);
        String item2Tag = item2.getProps().getPROPERTY_ITEM_TAG();

        // swap the items in inventory
        inventory.set(index1, item2);
        inventory.set(index2, item1);

        // change the item indexes in indexes
        indexes.put(item1Tag, index2);
        indexes.put(item2Tag, index1);
    }

    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------
    // PRINT OUT THE CURRENT INVENTORY

    public void print() {
        Log.d(TAG,"CURRENT INVENTORY::");
        for (int i = 0; i < inventory.size(); i++) {
            BaseStructure item = inventory.get(i);
            int size;

            if (item.getProps().getPROPERTY_ITEM_TAG() == StructureProperties.UNDEFINED_TAG) size = 0;
            else size = sizes.get(item.getProps().getPROPERTY_ITEM_TAG());

            Log.d(TAG,"Index = " + i +
                    " , Item = " + item.getProps().getPROPERTY_ITEM_TAG() +
                    " , Size = " + size +
                    " , CurrItem = " + isCurrentIndex(i));
        }
    }

}
