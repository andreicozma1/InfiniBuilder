package HUD;

public class HUDElement {
    private String elementTag;
    public HUDElement(String elementTag){
        this.elementTag = elementTag;
    }

    public String getElementTag() { return elementTag; }
    public void setElementTag(String elementTag) { this.elementTag = elementTag; }
}