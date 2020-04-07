package app.GUI.HUD.HUDElements;

import app.GUI.HUD.HUDElements.HUDElement;
import app.GUI.menu.InterfaceBuilder;
import app.GUI.menu.MenuUtil;
import app.GameBuilder;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class DeathMenu extends HUDElement {
    private static final String TAG = "DeathMenu";

    private double width;
    private double height;
    private double screenWidth;
    private double screenHeight;
    private double backdropBorderWidth = 5;
    private double arcW = 0;
    private double arcH = 0;
    private boolean isCentered = true;
    private boolean isDead = false;
    private String singleArrow = ">";
    private String doubleArrow = ">>";
    private GameBuilder context;
    private MenuUtil menuUtil;
    private Paint backdropPaint = Color.BLACK;
    private Paint backdropBorderPaint = Color.WHITE;
    private Paint textPaint = Color.WHITE;
    private Color GREEN = Color.valueOf("#20C20E");
    private InterfaceBuilder pause;

    private Font pauseTitle = Font.font("Monospaced", FontWeight.BOLD, FontPosture.REGULAR,20);

    private Font pauseText = Font.font("Monospaced", FontWeight.BOLD, FontPosture.REGULAR,15);

    public DeathMenu(String elementTag,
                     Point2D pos,
                     GameBuilder context,
                     double width,
                     double height,
                     double screenWidth,
                     double screenHeight){
        super(elementTag,pos);
        this.context = context;
        this.menuUtil = context.getComponents().getMenu();
        this.width = width;
        this.height =  height;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        pause = new InterfaceBuilder();
        update();
    }

    public void setDead(boolean dead) {
        isDead = dead;
        update();
    }
    public void setHeight(double height) { this.height = height; }
    public void setWidth(double width) { this.width = width; }
    public void setCentered(boolean centered) { isCentered = centered; }

    public boolean isDead() { return isDead; }
    public double getHeight() { return height; }
    public double getWidth() { return width; }


    public void update(){
        getGroup().getChildren().clear();

        if(isDead){
            double x = getPos().getX();
            double y = getPos().getY();
            if(isCentered) {
                x = screenWidth/2-width/2;
                y = screenHeight/2-height/2;

            }

            // draw black backdrop
            Rectangle backdrop = pause.drawRectangle((float)x,(float)y,width,height,0,0, Color.BLACK);
            backdrop.setStroke(Color.WHITE);
            backdrop.setStrokeWidth(4);

            //draw title
            pause.drawText("PLAYER_HAS_DIED",
                    (float)x+20,
                    (float)y+35,
                    GREEN,
                    pauseTitle);

            pause.drawText("-------------",
                    (float)x+20,
                    (float)y+55,
                    Color.WHITE,
                    pauseTitle);


            //**************************************************************************\
            // RESUME GAME
            Text respawnArrow = pause.drawText(singleArrow, x+20, y+80, GREEN, pauseText);
            Text respawnText = pause.drawText("./Respawn", x+45, y+80, Color.WHITE, pauseText);
            Rectangle respawnHitBox = pause.drawRectangle(x,y+65,width,20,0,0,Color.TRANSPARENT);
            respawnHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            isDead = false;
                            context.getComponents().getPlayer().reset();
                            update();
                        }
                    });
            respawnHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            respawnArrow.setText(doubleArrow);
                            respawnText.setFill(GREEN);
                        }
                    });
            respawnHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            respawnArrow.setText(singleArrow);
                            respawnText.setFill(Color.WHITE);
                        }
                    });


            //**************************************************************************\
            // GOTO SETTINGS
            Text settingsArrow = pause.drawText(singleArrow, x+20, y+110, GREEN, pauseText);
            Text settingsText = pause.drawText("./New_Game", x+45, y+110, Color.WHITE, pauseText);
            Rectangle settingsHitBox = pause.drawRectangle(x,y+95,width,20,0,0,Color.TRANSPARENT);
            settingsHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            isDead = false;
                            context.getComponents().getPlayer().reset();

                            context.getComponents().getEnvironment().reset();
                            update();
                        }
                    });
            settingsHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            settingsArrow.setText(doubleArrow);
                            settingsText.setFill(GREEN);
                        }
                    });
            settingsHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            settingsArrow.setText(singleArrow);
                            settingsText.setFill(Color.WHITE);
                        }
                    });

            //**************************************************************************\
            // GOTO MAIN MENU
            Text mainMenuArrow = pause.drawText(singleArrow, x+20, y+140, GREEN, pauseText);
            Text mainMenuText = pause.drawText("./Exit_To_Main_Menu", x+45, y+140, Color.WHITE, pauseText);
            Rectangle mainMenuHitBox = pause.drawRectangle(x,y+125,width,20,0,0,Color.TRANSPARENT);
            mainMenuHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            context.showScene(context.getComponents().getMenu().getScene());
                            menuUtil.activateGroup(MenuUtil.GROUP_MAIN_MENU);
                            isDead = false;
                            update();
                        }
                    });
            mainMenuHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            mainMenuArrow.setText(doubleArrow);
                            mainMenuText.setFill(GREEN);
                        }
                    });
            mainMenuHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            mainMenuArrow.setText(singleArrow);
                            mainMenuText.setFill(Color.WHITE);
                        }
                    });


            // add the interface builder to the pause menu group
            getGroup().getChildren().add(pause.getGroup());

        }
    }
}