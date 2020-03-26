package app.HUD;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TextBox extends HUDElement {

    private String text = "";
    private double maxWidth = 0;
    private Paint textColor = Color.BLACK;
    private Font textFont = Font.getDefault();
    private TextAlignment textAlignment = TextAlignment.LEFT;


    public TextBox(GraphicsContext graphicsContext,
                   String elementTag,
                   Point2D pos,
                   String text,
                   double maxWidth,
                   Paint textColor){
        super(graphicsContext, elementTag, pos);
        this.text = text;
        this.maxWidth = maxWidth;
        this.textColor = textColor;

        generateTextBox();
    }

    public void setText(String text) { this.text = text; }
    public void setMaxWidth(double maxWidth) { this.maxWidth = maxWidth; }
    public void setTextColor(Paint textColor) { this.textColor = textColor; }
    public void setFont(Font font){ this.textFont = font; }
    public void setTextAlignment(TextAlignment textAlignment) { this.textAlignment = textAlignment; }

    public String getText() { return text; }
    public double getMaxWidth() { return maxWidth; }
    public Paint getTextColor() { return textColor; }
    public Font getTextFont() { return textFont; }
    public TextAlignment getTextAlignment() { return textAlignment; }

    private void generateTextBox(){
        getGraphicsContext().setFill(textColor);
        getGraphicsContext().setFont(textFont);
        getGraphicsContext().setTextAlign(textAlignment);
        getGraphicsContext().fillText(text,getPos().getX(),getPos().getY(),maxWidth);
    }
}
