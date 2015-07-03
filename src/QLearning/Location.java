package QLearning;

/**
 *
 * @author Dong Yubo
 */
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Location {

    private int row; //row
    private int col; //column
    private double reward;

    private boolean special;
    private boolean isWall = false;
    private boolean isBlock = false;
    private boolean duplicated = false;

    private double[] qvalues = new double[4];
    private double locationValue;
    private boolean[] isOptimal = new boolean[4];
    private int[] visits = new int[4];
    private double[] travelTime = new double[4];

    private GridPane locPane;

    ////////////////////////////////////////////////////////////////////////////
    //for grid world
    public Location(int row, int col) {
        this.row = row;
        this.col = col;
        reward = 0.0;
        special = false;
        locPane = newNode();
    }

    public Location clone() {
        Location newLocation = new Location(this.row, this.col);
        newLocation.isBlock = this.isBlock;
        newLocation.isOptimal = this.isOptimal;
        newLocation.isWall = this.isWall;
        newLocation.qvalues = this.qvalues.clone();
        newLocation.reward = this.reward;
        newLocation.visits = this.visits.clone();
        newLocation.special = this.special;
        newLocation.locationValue = this.locationValue;
        newLocation.duplicated = true;

        return newLocation;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public double getReward() {
        return reward;
    }

    public boolean isSpecial() {
        return special;
    }

    public boolean isWall() {
        return isWall;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public double[] getQvalues() {
        return qvalues;
    }

    public double getQvalue(int i) {
        return qvalues[i];
    }

    public double getLocationValue() {
        return locationValue;
    }

    public GridPane getLocPane() {
        return locPane;
    }

    public boolean[] getIsOptimal() {
        return isOptimal;
    }

    public int getVisit(int d) {
        return visits[d];
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public void setIsWall(boolean isWall) {
        this.isWall = isWall;
    }

    public void setIsBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }

    public void setQvalue(int i, double v) {
        this.qvalues[i] = v;
    }

    public void setLocationValue(double locationValue) {
        this.locationValue = locationValue;
    }

    public void visited(int i) {
        this.visits[i]++;
    }

    public void doreset(double initVal) {
        for (int i = 0; i < 4; i++) {
            qvalues[i] = initVal;
            visits[i] = 0;
        }
    }

    public void print() {
        String s = "";
        System.out.format("row %d col %d\n", row, col);
        for (int i = 0; i < qvalues.length; i++) {
            System.out.print(qvalues[i]);
            System.out.print(" ");
        }
        System.out.println("");
    }

    public static GridPane newNode() {
        final GridPane gp = new GridPane();
        String color = app.Color.Red[2];

        //newgp.getChildren().addAll(gp.getChildren());
        gp.setPrefSize(60, 60);
        gp.setMaxSize(320, 320);
        gp.setMinSize(30, 30);
        gp.setPadding(new Insets(2));
        gp.setFocusTraversable(true);
        gp.setBackground(new Background(new BackgroundFill(
                Paint.valueOf(color), CornerRadii.EMPTY, Insets.EMPTY)));
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

        Font smallFont = new Font(11);

        Text tUp = new Text("1");
        tUp.setTextAlignment(TextAlignment.CENTER);

        Text tDown = new Text("1");

        Text tLeft = new Text("1");
        tLeft.setRotate(270);

        Text tRight = new Text("1");
        tRight.setRotate(90);

        tUp.setFont(smallFont);
        tDown.setFont(smallFont);
        tLeft.setFont(smallFont);
        tRight.setFont(smallFont);

        up.setFill(Color.DODGERBLUE);
        down.setFill(Color.DODGERBLUE);
        left.setFill(Color.DODGERBLUE);
        right.setFill(Color.DODGERBLUE);

        Circle c = new Circle();
        c.setRadius(7);
        c.setFill(Color.BLACK);
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

        gp.add(up, 2, 1);
        gp.add(left, 1, 2);
        gp.add(down, 2, 3);
        gp.add(right, 3, 2);
        gp.add(tUp, 2, 0);
        gp.add(tLeft, 0, 2);
        gp.add(tDown, 2, 4);
        gp.add(tRight, 4, 2);
//        GridPane.setRowSpan(tRight, 3);
//        GridPane.setRowSpan(tLeft, 3);
//        GridPane.setColumnSpan(tUp, 3);
//        GridPane.setColumnSpan(tDown, 3);
        gp.add(c, 2, 2);

        return gp;
    }

//    public void repaint(GridWorld gw) {
//        repaint(this.locPane,gw);
//    }
    public void repaint(GridWorld gw) {
        //print();
        Text t;
        Format form = new DecimalFormat("0.##");
        Font largeFont = new Font(30);

        double localValue = computeOptimal();
        int sec = (int) gw.getRange() / 10 == 0 ? 1 : (int) gw.getRange() / 10;
        int index = (int) (localValue - gw.getLowest()) / sec;

        index = index < 0 ? 0 : index;
        index = index > 9 ? 9 : index;
        try {
            if (reward > 0 && !isWall) {
                locPane.setBackground(new Background(new BackgroundFill(
                        Paint.valueOf(app.Color.Green[3]), CornerRadii.EMPTY, Insets.EMPTY)));
            } else if (reward < 0 && !isWall) {
                locPane.setBackground(new Background(new BackgroundFill(
                        Paint.valueOf(app.Color.Red[3]), CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                locPane.setBackground(new Background(new BackgroundFill(
                        Paint.valueOf(app.Color.Yellow[index]), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        } catch (ArrayIndexOutOfBoundsException a) {
            System.out.print("color index: ");
            System.out.println(index);
            System.out.println(localValue);
            System.out.println(sec);
        }
        for (int i = 0; i < locPane.getChildren().size(); i++) {
//            if (g.getChildren().get(i).getClass().equals(Text.class)) {
//                t = (Text) g.getChildren().get(i);
//                if (GridPane.getRowIndex(t) == 1) { //up
//                    t.setText(form.format(qvalues[GridWorld.Up]));
//                } else if (GridPane.getRowIndex(t) == 3) { //down
//                    t.setText(form.format(qvalues[GridWorld.Down]));
//                } else if (GridPane.getColumnIndex(t) == 1) { //left
//                    t.setText(form.format(qvalues[GridWorld.Left]));
//                } else if (GridPane.getColumnIndex(t) == 3) { //right
//                    t.setText(form.format(qvalues[GridWorld.Right]));
//                }
//            } else if (g.getChildren().get(i).getClass().equals(Polygon.class)) {
//                Polygon p = (Polygon) g.getChildren().get(i);
//                if (GridPane.getRowIndex(p) == 0) { //up
//                    if (isOptimal[GridWorld.Up]) {
//                        p.setVisible(true);
//                    } else {
//                        p.setVisible(false);
//                    }
//                } else if (GridPane.getRowIndex(p) == 4) { //down
//                    if (isOptimal[GridWorld.Down]) {
//                        p.setVisible(true);
//                    } else {
//                        p.setVisible(false);
//                    }
//                } else if (GridPane.getColumnIndex(p) == 0) { //left
//                    if (isOptimal[GridWorld.Left]) {
//                        p.setVisible(true);
//                    } else {
//                        p.setVisible(false);
//                    }
//                } else if (GridPane.getColumnIndex(p) == 4) { //right
//                    if (isOptimal[GridWorld.Right]) {
//                        p.setVisible(true);
//                    } else {
//                        p.setVisible(false);
//                    }
//                }
            if (locPane.getChildren().get(i).getClass().equals(Text.class)) {
                t = (Text) locPane.getChildren().get(i);
                if (duplicated) {
                    t.setFont(largeFont);
                }
                if (GridPane.getRowIndex(t) == 0) { //up
                    t.setText(form.format(qvalues[GridWorld.Up]));

                    //System.out.println(form.format(qvalues[GridWorld.Up]));
                } else if (GridPane.getRowIndex(t) == 4) { //down
                    t.setText(form.format(qvalues[GridWorld.Down]));
                    //System.out.println(form.format(qvalues[GridWorld.Down]));
                } else if (GridPane.getColumnIndex(t) == 0) { //left
                    t.setText(form.format(qvalues[GridWorld.Left]));
                    //System.out.println(form.format(qvalues[GridWorld.Left]));
                } else if (GridPane.getColumnIndex(t) == 4) { //right
                    t.setText(form.format(qvalues[GridWorld.Right]));
                    //System.out.println(form.format(qvalues[GridWorld.Right]));
                }
            } else if (locPane.getChildren().get(i).getClass().equals(Polygon.class)) {
                Polygon p = (Polygon) locPane.getChildren().get(i);
                if (GridPane.getRowIndex(p) == 1) { //up
                    if (isOptimal[GridWorld.Up]) {
                        p.setVisible(true);
                    } else {
                        p.setVisible(false);
                    }
                } else if (GridPane.getRowIndex(p) == 3) { //down
                    if (isOptimal[GridWorld.Down]) {
                        p.setVisible(true);
                    } else {
                        p.setVisible(false);
                    }
                } else if (GridPane.getColumnIndex(p) == 1) { //left
                    if (isOptimal[GridWorld.Left]) {
                        p.setVisible(true);
                    } else {
                        p.setVisible(false);
                    }
                } else if (GridPane.getColumnIndex(p) == 3) { //right
                    if (isOptimal[GridWorld.Right]) {
                        p.setVisible(true);
                    } else {
                        p.setVisible(false);
                    }
                }
            } else if (locPane.getChildren().get(i).getClass().equals(Circle.class)) {
                Circle c = (Circle) locPane.getChildren().get(i);
                if (row == gw.getCurRow() && col == gw.getCurCol()) {
                    c.setVisible(true);
                } else {
                    c.setVisible(false);
                }
            }
        }
    }

    public double computeOptimal() {
        double big = qvalues[0];
        for (int i = 0; i < qvalues.length; i++) {
            if (qvalues[i] > big) {
                big = qvalues[i];
            }
        }
        for (int i = 0; i < qvalues.length; i++) {
            isOptimal[i] = qvalues[i] == big;
        }
        locationValue = big;
        return big;
    }

    ////////////////////////////////////////////////////////////////////////////
    //for simulated world
//    public Location(double x, double y) {
//        this.row = x;
//        this.col = y;
//    }
    ////////////////////////////////////////////////////////////////////////////
    //universal
}
