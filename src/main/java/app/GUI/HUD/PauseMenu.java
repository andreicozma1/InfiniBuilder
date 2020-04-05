package app.GUI.HUD;

import app.GUI.menu.MenuUtil;
import app.GameBuilder;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class PauseMenu extends HUDElement {

    private double width;
    private double height;
    private double screenWidth;
    private double screenHeight;
    private double backdropBorderWidth = 5;
    private double arcW = 0;
    private double arcH = 0;
    private boolean isCentered = true;
    private boolean isPaused = false;
    private GameBuilder context;
    private MenuUtil menuUtil;
    private Paint backdropPaint = Color.BLACK;
    private Paint backdropBorderPaint = Color.WHITE;
    private Paint textPaint = Color.WHITE;
    private Color GREEN = Color.valueOf("#20C20E");

    private Font options = Font.font("Monospaced", FontWeight.BOLD, FontPosture.REGULAR,15);

    public PauseMenu(String elementTag,
                     Point2D pos,
                     GameBuilder context,
                     MenuUtil menuUtil,
                     double width,
                     double height,
                     double screenWidth,
                     double screenHeight){
        super(elementTag,pos);
        this.context = context;
        this.menuUtil = menuUtil;
        this.width = width;
        this.height =  height;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        update();
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
        update();
    }
    public void setHeight(double height) { this.height = height; }
    public void setWidth(double width) { this.width = width; }
    public void setCentered(boolean centered) { isCentered = centered; }

    public boolean isPaused() { return isPaused; }
    public double getHeight() { return height; }
    public double getWidth() { return width; }



    public void update(){
        getGroup().getChildren().clear();

        if(isPaused){
            double x = getPos().getX();
            double y = getPos().getY();
            if(isCentered) {
                x = screenWidth/2-width/2;
                y = screenHeight/2-height/2;

            }
            Rectangle backdrop = new Rectangle(x,y,width,height);
            backdrop.setFill(backdropPaint);
            backdrop.setStroke(backdropBorderPaint);
            backdrop.setStrokeWidth(backdropBorderWidth);
            backdrop.setArcWidth(arcW);
            backdrop.setArcHeight(arcH);
            getGroup().getChildren().add(backdrop);


            Rectangle returnToGameRect = new Rectangle(x+25,y+25,width-50,25);
            returnToGameRect.setFill(Color.BLACK);
            returnToGameRect.setStroke(Color.WHITE);
            returnToGameRect.setStrokeWidth(3);
            returnToGameRect.setArcWidth(arcW);
            returnToGameRect.setArcHeight(arcH);
            getGroup().getChildren().add(returnToGameRect);

            Text returnToGameText = new Text("Return To Game");
            returnToGameText.setFont(options);
            returnToGameText.setFill(GREEN);
            returnToGameText.setX(x+60);
            returnToGameText.setY(y+43);
            getGroup().getChildren().add(returnToGameText);


            Rectangle returnToGameHitBox = new Rectangle(x+25,y+25,width-50,25);
            returnToGameHitBox.setFill(Color.TRANSPARENT);
            returnToGameHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            menuUtil.setSettingsReturnState(MenuUtil.PAUSE);
                            context.showScene(context.getGameRootScene());
                            menuUtil.activateGroup(menuUtil.GROUP_SETTINGS);
                        }
                    });
            getGroup().getChildren().add(returnToGameHitBox);



//            Rectangle settings = new Rectangle()
        }
    }
}
