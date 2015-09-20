package QLearning;

import static QLearning.GridWorld.Up;
import static QLearning.GridWorld.Down;
import static QLearning.GridWorld.Left;
import static QLearning.GridWorld.Right;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author Dong Yubo
 */
public class DetailController implements Initializable {

    private Algorithm algo;
    private GridWorld gw;
    private Location loc;
    private GridController gridCtrl;

    public class TimeEntry {

        public SimpleStringProperty dir;
        public SimpleDoubleProperty time;
        public SimpleDoubleProperty mean;
        public SimpleDoubleProperty stddev;

        public TimeEntry(String dir, double time, double mean, double stddev) {
            this.dir = new SimpleStringProperty(dir);
            this.time = new SimpleDoubleProperty(time);
            this.mean = new SimpleDoubleProperty(mean);
            this.stddev = new SimpleDoubleProperty(stddev);
        }

        public String getDir() {
            return dir.getValue();
        }

        public int getIntDir() {

            switch (dir.getValue()) {
                case "up":
                    return Up;
                case "down":
                    return Down;
                case "left":
                    return Left;
                case "right":
                    return Right;
                default:
                    System.out.println("Time Entry direction error");
                    return 0;

            }

        }

        public void setDir(SimpleStringProperty dir) {
            this.dir = dir;
        }

        public int getTime() {
            return (int)Math.round(time.getValue());
        }

        public void setTime(SimpleDoubleProperty time) {
            this.time = time;
        }

        public Double getMean() {
            return mean.getValue();
        }

        public void setMean(SimpleDoubleProperty mean) {
            this.mean = mean;
        }

        public Double getStddev() {
            return stddev.getValue();
        }

        public void setStddev(SimpleDoubleProperty stddev) {
            this.stddev = stddev;
        }

    }
    private ObservableList<TimeEntry> travelTimes = FXCollections.observableArrayList();

    public class QValueEntry {

        private SimpleDoubleProperty batteryLevel;
        private SimpleDoubleProperty upValue;
        private SimpleDoubleProperty downValue;
        private SimpleDoubleProperty leftValue;
        private SimpleDoubleProperty rightValue;

        public QValueEntry(Double batteryLevel, Double upValue, Double downValue, Double leftValue, Double rightValue) {
            this.batteryLevel = new SimpleDoubleProperty(batteryLevel);
            this.upValue = new SimpleDoubleProperty(upValue);
            this.downValue = new SimpleDoubleProperty(downValue);
            this.leftValue = new SimpleDoubleProperty(leftValue);
            this.rightValue = new SimpleDoubleProperty(rightValue);

        }

        public Double getBatteryLevel() {
            return batteryLevel.getValue();
        }

        public Double getUpValue() {
            return upValue.getValue();
        }

        public Double getDownValue() {
            return downValue.getValue();
        }

        public Double getLeftValue() {
            return leftValue.getValue();
        }

        public Double getRightValue() {
            return rightValue.getValue();
        }
    }
    ObservableList<QValueEntry> qvalueList = FXCollections.observableArrayList();

    public void setAlgo(Algorithm algo) {
        this.algo = algo;
    }

    public void setGridWorld(GridWorld gw) {
        this.gw = gw;
    }

    public void setGridCtrl(GridController gridCtrl) {
        this.gridCtrl = gridCtrl;
    }

    public StackPane getLocationPane() {
        return locationPane;
    }

    public VBox getDetailPane() {
        return detailPane;
    }

    public Label getLocationValueLabel() {
        return locationValueLabel;
    }

    public void setTimeLabel(double[] time, boolean isBlock) {
        if (isBlock) {
            upTimeLabel.setText(null);
            downTimeLabel.setText(null);
            leftTimeLabel.setText(null);
            rightTimeLabel.setText(null);
        } else {
            Format form = new DecimalFormat("0.##");
            upTimeLabel.setText(form.format(time[Up]));
            downTimeLabel.setText(form.format(time[Down]));
            leftTimeLabel.setText(form.format(time[Left]));
            rightTimeLabel.setText(form.format(time[Right]));
        }
    }

    public void resetTimeLabel() {
        upTimeLabel.setText(null);
        downTimeLabel.setText(null);
        leftTimeLabel.setText(null);
        rightTimeLabel.setText(null);
    }

    @FXML
    void upValueChanged(ActionEvent event) {
        gw.getLocation(loc.getRow(), loc.getCol()).
                setTravelTime(Up, Double.valueOf(upField.getText()));
        gridCtrl.repaintAll();
    }

    @FXML
    void leftValueChanged(ActionEvent event) {
        gw.getLocation(loc.getRow(), loc.getCol()).
                setTravelTime(Left, Double.valueOf(leftField.getText()));
        gridCtrl.repaintAll();
    }

    @FXML
    void downValueChanged(ActionEvent event) {
        gw.getLocation(loc.getRow(), loc.getCol()).
                setTravelTime(Down, Double.valueOf(downField.getText()));
        gridCtrl.repaintAll();
    }

    @FXML
    void rightValueChanged(ActionEvent event) {
        gw.getLocation(loc.getRow(), loc.getCol()).
                setTravelTime(Right, Double.valueOf(rightField.getText()));
        gridCtrl.repaintAll();
    }

    @FXML
    void changeMean(ActionEvent event) {
        System.out.println("Mean changed");
    }

    @FXML
    void rewardValueChanged(ActionEvent event) {
        gw.getLocation(loc.getRow(),
                loc.getCol()).setReward(Double.valueOf(rewardField.getText()));
        System.out.println("reward value changed");
        gridCtrl.repaintAll();
    }

    public void updateReward() {
        try {
            rewardField.setText(String.valueOf(gw.getLocation(loc.getRow(), loc.getCol()).getReward()));
        } catch (NullPointerException ne) {
            rewardField.setText(null);
            System.out.println("Null pointer catched");
        }
    }

    @FXML
    void blockChecked(ActionEvent event) {
        if (blockCheckBox.isSelected()) {
            gw.setBlock(loc.getRow(), loc.getCol(), true);
        } else {
            gw.setBlock(loc.getRow(), loc.getCol(), false);
        }
        gridCtrl.repaintAll();
        updateReward();
    }

    @FXML
    void startChecked(ActionEvent event) {
        if (startCheckBox.isSelected()) {
            gw.setStart(loc.getRow(), loc.getCol());
            System.out.println("start changed");
        }
        gridCtrl.repaintAll();
        updateReward();
    }

    @FXML
    void chargingChecked(ActionEvent event) {
        if (chargingCheckBox.isSelected()) {
            gw.setCharging(loc.getRow(), loc.getCol(), true);
            System.out.println("charging station added");
        } else {
            gw.setCharging(loc.getRow(), loc.getCol(), false);
            System.out.println("charging station removed");
        }
        gridCtrl.repaintAll();
        updateReward();
    }

    @FXML
    void goalChecked(ActionEvent event) {
        if (goalCheckBox.isSelected()) {
            gw.setGoal(loc.getRow(), loc.getCol());
            System.out.println("goal changed");
        }
        gridCtrl.repaintAll();
        updateReward();
    }

    @FXML
    private TextField rightField;

    @FXML
    private TextField upField;

    @FXML
    private TextField downField;

    @FXML
    private TextField leftField;

    @FXML
    private TextField rewardField;

    @FXML
    private StackPane locationPane;

    @FXML
    private VBox detailPane;

    @FXML
    private Label locationValueLabel;

    @FXML
    private GridPane locGridPane;

    @FXML
    private StackPane locationTimePane;

    @FXML
    private Label downTimeLabel;

    @FXML
    private Label upTimeLabel;

    @FXML
    private Label leftTimeLabel;

    @FXML
    private Label rightTimeLabel;

    @FXML
    private Label rowColLabel;

    @FXML
    private CheckBox blockCheckBox;

    @FXML
    private CheckBox startCheckBox;

    @FXML
    private CheckBox chargingCheckBox;

    @FXML
    private CheckBox goalCheckBox;

    @FXML
    private TableView travelTimeTable;

    @FXML
    private TableColumn<TimeEntry, Double> batteryLevelColumn;

    @FXML
    private TableColumn<TimeEntry, Double> upColumn;

    @FXML
    private TableColumn<TimeEntry, Double> downColumn;

    @FXML
    private TableColumn<TimeEntry, Double> leftColumn;

    @FXML
    private TableColumn<TimeEntry, Double> rightColumn;

    @FXML
    private TableView QvalueTable;

    @FXML
    private TableColumn<TimeEntry, String> dirColumn;

    @FXML
    private TableColumn<TimeEntry, Double> travelTimeColumn;

    @FXML
    private TableColumn<TimeEntry, Double> meanColumn;

    @FXML
    private TableColumn<TimeEntry, Double> stdDevColumn;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        locGridPane.setVisible(false);
//        locationValueLabel.setText(null);
//        rowColLabel.setText(null);
//
//        resetTimeLabel();
    }

    public class DoubleStringConverter extends StringConverter<Double> {

        @Override
        public String toString(Double d) {
            return String.valueOf(d);
        }

        @Override
        public Double fromString(String s) {
            return Double.valueOf(s);
        }
    }

    public void init(Location location) {
        this.loc = location;
        try {
            locationPane.getChildren().remove(0);
        } catch (Exception n) {
            //System.out.println("detailcontroller init exception");
        }

        //travel time table
        travelTimes.remove(0, travelTimes.size());
        travelTimes.add(new TimeEntry("up", loc.getTravelTime(Up), loc.getMeanTravelTime(Up), loc.getStddev(Up)));
        travelTimes.add(new TimeEntry("down", loc.getTravelTime(Down), loc.getMeanTravelTime(Down), loc.getStddev(Down)));
        travelTimes.add(new TimeEntry("left", loc.getTravelTime(Left), loc.getMeanTravelTime(Left), loc.getStddev(Left)));
        travelTimes.add(new TimeEntry("right", loc.getTravelTime(Right), loc.getMeanTravelTime(Right), loc.getStddev(Right)));

        dirColumn.setCellValueFactory(new PropertyValueFactory<>("dir"));
        travelTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
//        travelTimeColumn.setCellFactory(TextFieldTableCell.<TimeEntry, Double>forTableColumn(new DoubleStringConverter()));
//        travelTimeColumn.setOnEditCommit((TableColumn.CellEditEvent<TimeEntry, Double> t) -> {
//            int row = t.getTablePosition().getRow();
//            TimeEntry time = (TimeEntry) t.getTableView().getItems().get(row);
//            time.setTime(new SimpleDoubleProperty(t.getNewValue()));
//            algo.getGridWorld().getLocation(loc.getRow(), loc.getCol()).setTravelTime(time.getIntDir(), t.getNewValue());
//        });
        meanColumn.setCellValueFactory(new PropertyValueFactory<>("mean"));
        meanColumn.setCellFactory(TextFieldTableCell.<TimeEntry, Double>forTableColumn(new DoubleStringConverter()));
        meanColumn.setOnEditCommit((TableColumn.CellEditEvent<TimeEntry, Double> t) -> {
            int row = t.getTablePosition().getRow();
            TimeEntry time = (TimeEntry) t.getTableView().getItems().get(row);
            time.setMean(new SimpleDoubleProperty(t.getNewValue()));
            algo.getGridWorld().getLocation(loc.getRow(), loc.getCol()).setMeanTravelTime(time.getIntDir(), t.getNewValue());
        });
        stdDevColumn.setCellValueFactory(new PropertyValueFactory<>("stddev"));
        stdDevColumn.setCellFactory(TextFieldTableCell.<TimeEntry, Double>forTableColumn(new DoubleStringConverter()));
        stdDevColumn.setOnEditCommit((TableColumn.CellEditEvent<TimeEntry, Double> t) -> {
            int row = t.getTablePosition().getRow();
            TimeEntry time = (TimeEntry) t.getTableView().getItems().get(row);
            time.setStddev(new SimpleDoubleProperty(t.getNewValue()));
            algo.getGridWorld().getLocation(loc.getRow(), loc.getCol()).setStddev(time.getIntDir(), t.getNewValue());
        });
        travelTimeTable.setItems(travelTimes);

        //q value table
        double[][] qvalue = algo.getQvalue(loc.getRow(), loc.getCol());
        qvalueList.remove(0, qvalueList.size());
        for (int i = 0; i < qvalue.length; i++) {
            qvalueList.add(new QValueEntry(Double.valueOf(i), qvalue[i][Up], qvalue[i][Down], qvalue[i][Left], qvalue[i][Right]));
        }
        batteryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("batteryLevel"));
        upColumn.setCellValueFactory(new PropertyValueFactory<>("upValue"));
        downColumn.setCellValueFactory(new PropertyValueFactory<>("downValue"));
        leftColumn.setCellValueFactory(new PropertyValueFactory<>("leftValue"));
        rightColumn.setCellValueFactory(new PropertyValueFactory<>("rightValue"));
        QvalueTable.setItems(qvalueList);

        locationPane.getChildren().add(loc.getLocPane());
        update();
    }

    public void update() {
        try {
            setTimeLabel(loc.getTravelTime(), loc.isBlock());
            DecimalFormat form = new DecimalFormat("0.####");
            locationValueLabel.setText("Location Value: " + form.format(loc.getLocationValue()));
            rowColLabel.setText("Row " + loc.getRow() + " - Col " + loc.getCol());
            System.out.println("showing " + "Row " + loc.getRow() + " - Col " + loc.getCol());

//            if (loc.isBlock()) {
//                upField.setText(null);
//                downField.setText(null);
//                leftField.setText(null);
//                rightField.setText(null);
//                upField.setDisable(true);
//                downField.setDisable(true);
//                leftField.setDisable(true);
//                rightField.setDisable(true);
//            } else {
//                upField.setDisable(false);
//                downField.setDisable(false);
//                leftField.setDisable(false);
//                rightField.setDisable(false);
//                upField.setText(form.format(algo.getQvalue(loc.getRow(), loc.getCol(), Up)));
//                downField.setText(form.format(algo.getQvalue(loc.getRow(), loc.getCol(), Down)));
//                leftField.setText(form.format(algo.getQvalue(loc.getRow(), loc.getCol(), Left)));
//                rightField.setText(form.format(algo.getQvalue(loc.getRow(), loc.getCol(), Right)));
//            }
            rewardField.setText(form.format(loc.getReward()));
            blockCheckBox.setSelected(loc.isBlock());
            startCheckBox.setSelected(loc.isStart());
            chargingCheckBox.setSelected(loc.isCharging());
            goalCheckBox.setSelected(loc.isGoal());
            locGridPane.setVisible(true);
        } catch (NullPointerException ne) {
            System.out.println("Detailcontroller.update: Null pointer");
        }

    }

    public void reset(Algorithm algo, GridWorld gw) {
        if (!locationPane.getChildren().isEmpty()) {
            locationPane.getChildren().remove(0);
        }
        resetTimeLabel();
        locGridPane.setVisible(false);
        this.algo = algo;
        this.gw = gw;
    }

}
