package QLearning;

import java.text.DecimalFormat;
import java.text.Format;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Dong Yubo
 */
public class Location {

    public double x; //row
    public double y; //column
    public double reward;
    public boolean special;
    public boolean isWall = false;
    public boolean isBlock = false;
    public double[] qvalues = new double[4];
    public boolean[] isOptimal = new boolean[4];
    public int[] visits = new int[4];

    ////////////////////////////////////////////////////////////////////////////
    //for grid world
    public Location(int row, int col) {
        this.x = row;
        this.y = col;
        reward = 0.0;
        special = false;
    }

    public Location(int row, int col, double reward, boolean special) {
        this.x = row;
        this.y = col;
        this.reward = reward;
        this.special = special;
    }

    public int getRow() {
        return (int) x;
    }

    public int getCol() {
        return (int) y;
    }

    public void doreset(double initVal) {
        for (int i = 0; i < 4; i++) {
            qvalues[i] = initVal;
            visits[i] = 0;
        }
    }

    public static GridPane newNode() {
        final GridPane gp = new GridPane();
        String style = "";

        //newgp.getChildren().addAll(gp.getChildren());
        gp.setPrefSize(60, 60);
        gp.setMaxSize(100, 100);
        gp.setMinSize(40, 40);
        gp.setPadding(new Insets(2));
        gp.setFocusTraversable(true);
        GridPane.setHgrow(gp, Priority.ALWAYS);
        GridPane.setVgrow(gp, Priority.ALWAYS);
        //gp.setGridLinesVisible(true);
        //
//        final ContextMenu cm = new ContextMenu();
//        MenuItem cmItem1 = new MenuItem("Change Reward");
//        cmItem1.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent e) {
//
//            }
//        });
//
//        cm.getItems().add(cmItem1);
//        gp.addEventHandler(MouseEvent.MOUSE_CLICKED,
//                new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent e) {
//                        if (e.getButton() == MouseButton.SECONDARY) {
//                            cm.show(gp, e.getScreenX(), e.getScreenY());
//                        }
//                    }
//                });

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(20);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(20);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(20);
        ColumnConstraints col5 = new ColumnConstraints();
        col5.setPercentWidth(20);
        gp.getColumnConstraints().addAll(col1, col2, col3, col4, col5);
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(20);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(20);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(20);
        RowConstraints row4 = new RowConstraints();
        row4.setPercentHeight(20);
        RowConstraints row5 = new RowConstraints();
        row5.setPercentHeight(20);

        gp.getRowConstraints().addAll(row1, row2, row3, row4, row5);

        Polygon up = new Polygon(new double[]{
            6, 0, 0, 12, 12, 12
        });
        Polygon right = new Polygon(new double[]{
            0, 0, 0, 12, 12, 6
        });
        Polygon down = new Polygon(new double[]{
            0, 0, 12, 0, 6, 12
        });
        Polygon left = new Polygon(new double[]{
            0, 6, 12, 0, 12, 12
        });
        
        

        Font font = new Font(11);
        Text tUp = new Text("1");
        tUp.setTextAlignment(TextAlignment.CENTER);

        Text tDown = new Text("1");

        Text tLeft = new Text("1");
        tLeft.setRotate(270);

        Text tRight = new Text("1");
        tRight.setRotate(90);

        tUp.setFont(font);
        tDown.setFont(font);
        tLeft.setFont(font);
        tRight.setFont(font);

        up.setFill(Color.DODGERBLUE);
        down.setFill(Color.DODGERBLUE);
        left.setFill(Color.DODGERBLUE);
        right.setFill(Color.DODGERBLUE);

        Circle c = new Circle();
        c.setRadius(7);
        c.setFill(Color.YELLOW);
        c.setVisible(false);

        /**
         * ********** arrow *********************
         */
        GridPane.setHalignment(left, HPos.LEFT);
        GridPane.setValignment(left, VPos.CENTER);
        GridPane.setHgrow(left, Priority.ALWAYS);
        GridPane.setVgrow(left, Priority.ALWAYS);
//        left.scaleXProperty().bind(gp.widthProperty().divide(gp.getColumnConstraints().size()).divide(10));
//        left.setLayoutX(0);

        GridPane.setHalignment(right, HPos.RIGHT);
        GridPane.setValignment(right, VPos.CENTER);
        GridPane.setHgrow(right, Priority.ALWAYS);
        GridPane.setVgrow(right, Priority.ALWAYS);

        GridPane.setHalignment(up, HPos.CENTER);
        GridPane.setValignment(up, VPos.TOP);
        GridPane.setHgrow(up, Priority.ALWAYS);
        GridPane.setVgrow(up, Priority.ALWAYS);

        GridPane.setHalignment(down, HPos.CENTER);
        GridPane.setValignment(down, VPos.BOTTOM);
        GridPane.setHgrow(down, Priority.ALWAYS);
        GridPane.setVgrow(down, Priority.ALWAYS);

        /**
         * ********** circle *********************
         */
        GridPane.setHalignment(c, HPos.CENTER);
        GridPane.setValignment(c, VPos.CENTER);
        GridPane.setHgrow(c, Priority.ALWAYS);
        GridPane.setVgrow(c, Priority.ALWAYS);

        /**
         * ********** text *********************
         */
        GridPane.setHalignment(tLeft, HPos.CENTER);
        GridPane.setValignment(tLeft, VPos.CENTER);
        GridPane.setHgrow(tLeft, Priority.ALWAYS);
        GridPane.setVgrow(tLeft, Priority.ALWAYS);

        GridPane.setHalignment(tRight, HPos.CENTER);
        GridPane.setValignment(tRight, VPos.CENTER);
        GridPane.setHgrow(tRight, Priority.ALWAYS);
        GridPane.setVgrow(tRight, Priority.ALWAYS);
        //tRight.setX(5);

        GridPane.setHalignment(tUp, HPos.CENTER);
        GridPane.setValignment(tUp, VPos.CENTER);
        GridPane.setHgrow(tUp, Priority.ALWAYS);
        GridPane.setVgrow(tUp, Priority.ALWAYS);

        GridPane.setHalignment(tDown, HPos.CENTER);
        GridPane.setValignment(tDown, VPos.CENTER);
        GridPane.setHgrow(tDown, Priority.ALWAYS);
        GridPane.setVgrow(tDown, Priority.ALWAYS);

        gp.add(up, 2, 0);
        gp.add(left, 0, 2);
        gp.add(down, 2, 4);
        gp.add(right, 4, 2);
        gp.add(tUp, 2, 1);
        gp.add(tLeft, 1, 2);
        gp.add(tDown, 2, 3);
        gp.add(tRight, 3, 2);
        gp.add(c, 2, 2);

        return gp;
    }

    public void updateQ(GridPane g) {
        Text t;
        Format form = new DecimalFormat("0.##");
        computeOptimal();
        for (int i = 0; i < g.getChildren().size(); i++) {
            if (g.getChildren().get(i).getClass().equals(Text.class)) {
                t = (Text) g.getChildren().get(i);
                if (GridPane.getRowIndex(t) == 1) { //up
                    t.setText(form.format(qvalues[GridWorld.Up]));
                } else if (GridPane.getRowIndex(t) == 3) { //down
                    t.setText(form.format(qvalues[GridWorld.Down]));
                } else if (GridPane.getColumnIndex(t) == 1) { //left
                    t.setText(form.format(qvalues[GridWorld.Left]));
                } else if (GridPane.getColumnIndex(t) == 3) { //right
                    t.setText(form.format(qvalues[GridWorld.Right]));
                }
            } else if (g.getChildren().get(i).getClass().equals(Polygon.class)) {
                Polygon p = (Polygon) g.getChildren().get(i);
                p.autosize();
                if (GridPane.getRowIndex(p) == 0) { //up
                    if (isOptimal[GridWorld.Up]) {
                        p.setVisible(true);
                    } else {
                        p.setVisible(false);
                    }
                } else if (GridPane.getRowIndex(p) == 4) { //down
                    if (isOptimal[GridWorld.Down]) {
                        p.setVisible(true);
                    } else {
                        p.setVisible(false);
                    }
                } else if (GridPane.getColumnIndex(p) == 0) { //left
                    if (isOptimal[GridWorld.Left]) {
                        p.setVisible(true);
                    } else {
                        p.setVisible(false);
                    }
                } else if (GridPane.getColumnIndex(p) == 4) { //right
                    if (isOptimal[GridWorld.Right]) {
                        p.setVisible(true);
                    } else {
                        p.setVisible(false);
                    }
                }
            } else {
            }
        }
    }

    public void computeOptimal() {
        double big = qvalues[0];
        for (int i = 0; i < qvalues.length; i++) {
            if (qvalues[i] > big) {
                big = qvalues[i];
            }
        }
        for (int i = 0; i < qvalues.length; i++) {
            isOptimal[i] = qvalues[i] == big;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //for simulated world
    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    ////////////////////////////////////////////////////////////////////////////
    //universal
}
