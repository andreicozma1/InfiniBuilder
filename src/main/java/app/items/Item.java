package app.items;


import javafx.scene.Group;

public class Item {

    private String itemTag;
    private boolean isPlaceable = false;
    private Group group = new Group();

    public Item(String itemTag){
        this.itemTag = itemTag;
    }

    public String getItemTag() { return itemTag; }
    public void setItemTag(String itemTag) { this.itemTag = itemTag; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public boolean isPlaceable(){ return isPlaceable; }
    public void setPlaceable(boolean isPlaceable) { this.isPlaceable = isPlaceable; }
}
