package QLearning;

import static QLearning.GridWorld.Up;
import static QLearning.GridWorld.Down;
import static QLearning.GridWorld.Left;
import static QLearning.GridWorld.Right;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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

    public void init(Location location) {
        this.loc = location;
        try {
            locationPane.getChildren().remove(0);
        } catch (Exception n) {

        }
        locationPane.getChildren().add(loc.getLocPane());
        update();
    }

    public void update() {
        try {
            setTimeLabel(loc.getTravelTime(), loc.isBlock());
            DecimalFormat form = new DecimalFormat("0.####");
            locationValueLabel.setText("Location Value: " + form.format(loc.getLocationValue()));
            rowColLabel.setText("Row " + loc.getRow() + " - Col " + loc.getCol());

            if (loc.isBlock()) {
                upField.setText(null);
                downField.setText(null);
                leftField.setText(null);
                rightField.setText(null);
                upField.setDisable(true);
                downField.setDisable(true);
                leftField.setDisable(true);
                rightField.setDisable(true);
            } else {
                upField.setDisable(false);
                downField.setDisable(false);
                leftField.setDisable(false);
                rightField.setDisable(false);
                upField.setText(form.format(algo.getQvalue(loc.getRow(),loc.getCol(),Up)));
                downField.setText(form.format(algo.getQvalue(loc.getRow(),loc.getCol(),Down)));
                leftField.setText(form.format(algo.getQvalue(loc.getRow(),loc.getCol(),Left)));
                rightField.setText(form.format(algo.getQvalue(loc.getRow(),loc.getCol(),Right)));
            }
            rewardField.setText(form.format(loc.getReward()));
            blockCheckBox.setSelected(loc.isBlock());
            startCheckBox.setSelected(loc.isStart());
            chargingCheckBox.setSelected(loc.isCharging());
            goalCheckBox.setSelected(loc.isGoal());
            locGridPane.setVisible(true);
        } catch (NullPointerException ne) {
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
