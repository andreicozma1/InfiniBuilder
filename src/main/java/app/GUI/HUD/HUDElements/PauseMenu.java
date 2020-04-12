package app.GUI.HUD.HUDElements;

import app.GUI.HUD.HUDUtil;
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


public class PauseMenu extends HUDElement {
    private final double screenWidth;
    private final double screenHeight;
    private final double backdropBorderWidth = 5;
    private final double arcW = 0;
    private final double arcH = 0;
    private final String singleArrow = ">";
    private final String doubleArrow = ">>";
    private final GameBuilder context;
    private final MenuUtil menuUtil;
    private final Paint backdropPaint = Color.BLACK;
    private final Paint backdropBorderPaint = Color.WHITE;
    private final Paint textPaint = Color.WHITE;
    private final Color GREEN = Color.valueOf("#20C20E");
    private final InterfaceBuilder pause;
    private final Font pauseTitle = Font.font("Monospaced", FontWeight.BOLD, FontPosture.REGULAR, 20);
    private final Font pauseText = Font.font("Monospaced", FontWeight.BOLD, FontPosture.REGULAR, 15);
    private double width;
    private double height;
    private boolean isCentered = true;
    private boolean isPaused = false;

    public PauseMenu(String elementTag,
                     Point2D pos,
                     GameBuilder context,
                     double width,
                     double height,
                     double screenWidth,
                     double screenHeight) {
        super(elementTag, pos);
        this.context = context;
        this.menuUtil = context.getComponents().getMenu();
        this.width = width;
        this.height = height;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        pause = new InterfaceBuilder();
        update();
    }

    public void setCentered(boolean centered) {
        isCentered = centered;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
        update();
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void update() {
        getGroup().getChildren().clear();
        pause.getGroup().getChildren().clear();

        if (isPaused) {
            double x = getPos().getX();
            double y = getPos().getY();
            if (isCentered) {
                x = screenWidth / 2 - width / 2;
                y = screenHeight / 2 - height / 2;

            }

            // draw black backdrop
            Rectangle backdrop = pause.drawRectangle((float) x, (float) y, width, height, 0, 0, Color.BLACK);
            backdrop.setOpacity(.75);
            backdrop.setStroke(Color.WHITE);
            backdrop.setStrokeWidth(4);

            //draw title
            pause.drawText("ROOT@CS307:~Pause$",
                    (float) x + 45,
                    (float) y + 55,
                    GREEN,
                    pauseTitle);

            pause.drawText("-------------",
                    (float) x + 45,
                    (float) y + 75,
                    Color.WHITE,
                    pauseTitle);


            //**************************************************************************\
            // RESUME GAME
            Text returnArrow = pause.drawText(singleArrow, x + 45, y + 100, GREEN, pauseText);
            Text returnText = pause.drawText("./Return_To_Game", x + 70, y + 100, Color.WHITE, pauseText);
            Rectangle returnHitBox = pause.drawRectangle(x, y + 85, width, 20, 0, 0, Color.TRANSPARENT);
            returnHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            isPaused = false;
                            if(!((Crosshair) context.getComponents().getHUD().getElement(HUDUtil.CROSSHAIR)).isShowing())((Crosshair) context.getComponents().getHUD().getElement(HUDUtil.CROSSHAIR)).toggleCrosshair();
                            update();
                        }
                    });
            returnHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            returnArrow.setText(doubleArrow);
                            returnText.setFill(GREEN);
                        }
                    });
            returnHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            returnArrow.setText(singleArrow);
                            returnText.setFill(Color.WHITE);
                        }
                    });


            //**************************************************************************\
            // GOTO SETTINGS
            Text settingsArrow = pause.drawText(singleArrow, x + 45, y + 130, GREEN, pauseText);
            Text settingsText = pause.drawText("./Settings", x + 70, y + 130, Color.WHITE, pauseText);
            Rectangle settingsHitBox = pause.drawRectangle(x, y + 115, width, 20, 0, 0, Color.TRANSPARENT);
            settingsHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            menuUtil.setSettingsReturnState(MenuUtil.PAUSE);
                            context.getWindow().showScene(context.getComponents().getMenu().getScene());
                            menuUtil.activateGroup(MenuUtil.GROUP_SETTINGS);
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
            Text mainMenuArrow = pause.drawText(singleArrow, x + 40, y + 160, GREEN, pauseText);
            Text mainMenuText = pause.drawText("./Exit_To_Main_Menu", x + 70, y + 160, Color.WHITE, pauseText);
            Rectangle mainMenuHitBox = pause.drawRectangle(x, y + 145, width, 20, 0, 0, Color.TRANSPARENT);
            mainMenuHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            context.getWindow().showScene(context.getComponents().getMenu().getScene());
                            menuUtil.activateGroup(MenuUtil.GROUP_MAIN_MENU);
                            isPaused = false;
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
