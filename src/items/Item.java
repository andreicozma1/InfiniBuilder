package items;


public class Item {

    private String itemTag;
    private boolean isPlaceable = false;

    public Item(String itemTag){
        this.itemTag = itemTag;
    }

    public String getItemTag() { return itemTag; }
    public void setItemTag(String itemTag) { this.itemTag = itemTag; }
    public boolean isPlaceable(){ return isPlaceable; }
    public void setPlaceable(boolean isPlaceable) { this.isPlaceable = isPlaceable; }
}
