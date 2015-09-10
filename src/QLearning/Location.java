package QLearning;

/**
 *
 * @author Dong Yubo
 */
import java.text.DecimalFormat;
import java.text.Format;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
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

    private final int row; //row
    private final int col; //column
    private double reward;

    private boolean special;
    private boolean isWall = false;
    private boolean isBlock = false;
    private boolean isStart = false;
    private boolean isCharging = false;
    private boolean isGoal = false;
    private boolean duplicated = false;

//    private double[] qvalues = new double[4];
    private double locationValue;
//    private boolean[] isOptimal = new boolean[4];
//    private int[] visits = new int[4];
    private double[] travelTime = new double[4];

    private final GridPane locPane;

    ////////////////////////////////////////////////////////////////////////////
    //for grid world
    public Location(int row, int col) {
        this.row = row;
        this.col = col;
        reward = 0.0;
        special = false;
        locPane = newNode();
    }

    public Location(int row, int col, boolean duplicated) {
        this.row = row;
        this.col = col;
        reward = 0.0;
        special = false;
        this.duplicated = duplicated;
        locPane = newNode();
    }

    public Location copy() {
        Location newLocation = new Location(this.row, this.col, true);
        newLocation.isBlock = this.isBlock;
        newLocation.isStart = this.isStart;
        newLocation.isCharging = this.isCharging;
        newLocation.isGoal = this.isGoal;

        newLocation.isWall = this.isWall;
        newLocation.setReward(this.reward);
        newLocation.special = this.special;
//        newLocation.isOptimal = this.isOptimal;
//        newLocation.qvalues = this.qvalues.clone();
//        newLocation.visits = this.visits.clone();
        newLocation.locationValue = this.locationValue;
        newLocation.duplicated = true;
        newLocation.travelTime = this.travelTime.clone();

        return newLocation;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public double getTravelTime(int direction) {
        return travelTime[direction];
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

    public boolean isStart() {
        return isStart;
    }

    public void setIsStart(boolean isStart) {
        this.isStart = isStart;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setIsCharging(boolean isCharging) {
        this.isCharging = isCharging;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public void setIsGoal(boolean isGoal) {
        this.isGoal = isGoal;
    }

    public boolean isBlock() {
        return isBlock;
    }

//    public double[] getQvalues() {
//        return qvalues;
//    }
//
//    public double getQvalue(int i) {
//        return qvalues[i];
//    }
//
    public double getLocationValue() {
        return locationValue;
    }

    public GridPane getLocPane() {
        return locPane;
    }

//    public boolean[] getIsOptimal() {
//        return isOptimal;
//    }
//
//    public int getVisit(int d) {
//        return visits[d];
//    }
    public double[] getTravelTime() {
        return travelTime;
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

//    public void setQvalue(int i, double v) {
//        this.qvalues[i] = v;
//    }
//
    public void setLocationValue(double locationValue) {
        this.locationValue = locationValue;
    }

    public void setTravelTime(int direction, double time) {
        this.travelTime[direction] = time;
    }

//    public void visited(int i) {
//        this.visits[i]++;
//    }
//    public void doreset(double initVal) {
//        for (int i = 0; i < 4; i++) {
//            qvalues[i] = initVal;
//            visits[i] = 0;
//        }
//    }
//    public void print() {
//        String s = "";
//        System.out.format("row %d col %d\n", row, col);
//        for (int i = 0; i < qvalues.length; i++) {
//            System.out.print(qvalues[i]);
//            System.out.print(" ");
//        }
//        System.out.println("");
//    }
    public final GridPane newNode() {
        final GridPane gp = new GridPane();
        String color = app.Color.Red[2];

        //newgp.getChildren().addAll(gp.getChildren());
        gp.setPrefSize(60, 60);
        gp.setMaxSize(320, 320);
        gp.setMinSize(30, 30);
        // TO DO set equal height and width
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

        Label rewardLabel = new Label();
        rewardLabel.setText(null);
//        Format form = new DecimalFormat("0");
//        if (this.reward != 0) {
//            rewardLabel.setText(form.format(0));
//        }
        if (duplicated) {
            rewardLabel.setStyle("-fx-font-size: 25px");
        } else {
            rewardLabel.setStyle("-fx-font-size: 9px");
        }
        GridPane.setHalignment(rewardLabel, HPos.CENTER);
        GridPane.setValignment(rewardLabel, VPos.CENTER);
        gp.add(rewardLabel, 4, 4);

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
    public void repaint(Algorithm algo) {
        //print();
        Text t;
        Format form = new DecimalFormat("0.##");
        Font largeFont = new Font(30);

        int sector = (int) algo.getRange() / 10 == 0 ? 1 : (int) algo.getRange() / 10;
        int index = (int) (locationValue - algo.getLowest()) / sector;

        index = index < 0 ? 0 : index;
        index = index > 9 ? 9 : index;
        try {
            if (reward > 0 && !isWall && !isStart && !isGoal) {
                locPane.setBackground(new Background(new BackgroundFill(
                        Paint.valueOf(app.Color.Green[3]), CornerRadii.EMPTY, Insets.EMPTY)));
            } else if (reward < -0.1 && !isWall && !isBlock) {
                locPane.setBackground(new Background(new BackgroundFill(
                        Paint.valueOf(app.Color.Red[3]), CornerRadii.EMPTY, Insets.EMPTY)));
            } else if (isBlock) {
                locPane.setBackground(new Background(new BackgroundFill(
                        Paint.valueOf(app.Color.Brown[3]), CornerRadii.EMPTY, Insets.EMPTY)));
            } else if (isStart || isGoal) {
                locPane.setBackground(new Background(new BackgroundFill(
                        Paint.valueOf(app.Color.Blue[3]), CornerRadii.EMPTY, Insets.EMPTY)));
            } else if (isCharging) {
                locPane.setBackground(new Background(new BackgroundFill(
                        Paint.valueOf(app.Color.Cyan[3]), CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                locPane.setBackground(new Background(new BackgroundFill(
                        Paint.valueOf(app.Color.Yellow[index]), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        } catch (ArrayIndexOutOfBoundsException a) {
            System.out.print("color index: ");
            System.out.println(index);
            System.out.println(locationValue);
            System.out.println(sector);
        }
        for (int i = 0; i < locPane.getChildren().size(); i++) {
            if (locPane.getChildren().get(i).getClass().equals(Text.class)) {
                t = (Text) locPane.getChildren().get(i);
                if (duplicated) {
                    t.setFont(largeFont);
                }
                if (this.isBlock || this.isGoal) {
                    t.setVisible(false);
                } else {
                    t.setVisible(true);
                }
                if (GridPane.getRowIndex(t) == 0) { //up
                    t.setText(form.format(algo.getQvalue(row, col, GridWorld.Up)));
                    //System.out.println(form.format(qvalues[GridWorld.Up]));
                } else if (GridPane.getRowIndex(t) == 4) { //down
                    t.setText(form.format(algo.getQvalue(row, col, GridWorld.Down)));
                    //System.out.println(form.format(qvalues[GridWorld.Down]));
                } else if (GridPane.getColumnIndex(t) == 0) { //left
                    t.setText(form.format(algo.getQvalue(row, col, GridWorld.Left)));
                    //System.out.println(form.format(qvalues[GridWorld.Left]));
                } else if (GridPane.getColumnIndex(t) == 4) { //right
                    t.setText(form.format(algo.getQvalue(row, col, GridWorld.Right)));
                    //System.out.println(form.format(qvalues[GridWorld.Right]));
                }
            } else if (locPane.getChildren().get(i).getClass().equals(Polygon.class)) {
                Polygon p = (Polygon) locPane.getChildren().get(i);
                if (this.isBlock || this.isGoal) {
                    p.setVisible(false);
                } else if (GridPane.getRowIndex(p) == 1) { //up
                    if (algo.isOptimal(row, col, GridWorld.Up)) {
                        p.setVisible(true);
                    } else {
                        p.setVisible(false);
                    }
                } else if (GridPane.getRowIndex(p) == 3) { //down
                    if (algo.isOptimal(row, col, GridWorld.Down)) {
                        p.setVisible(true);
                    } else {
                        p.setVisible(false);
                    }
                } else if (GridPane.getColumnIndex(p) == 1) { //left
                    if (algo.isOptimal(row, col, GridWorld.Left)) {
                        p.setVisible(true);
                    } else {
                        p.setVisible(false);
                    }
                } else if (GridPane.getColumnIndex(p) == 3) { //right
                    if (algo.isOptimal(row, col, GridWorld.Right)) {
                        p.setVisible(true);
                    } else {
                        p.setVisible(false);
                    }
                }
            } else if (locPane.getChildren().get(i).getClass().equals(Circle.class)) {
                Circle c = (Circle) locPane.getChildren().get(i);
                if (row == algo.getGridWorld().getCurRow() && col == algo.getGridWorld().getCurCol()) {
                    c.setVisible(true);
                } else {
                    c.setVisible(false);
                }
            }
        }
        Label rewardLabel = (Label) GridController.getNode(4, 4, locPane);
        form = new DecimalFormat("0.#");
        if (this.reward == 0) {
            rewardLabel.setText(null);
        } else {
            rewardLabel.setText(String.valueOf(form.format(this.reward)));
        }
    }

}
