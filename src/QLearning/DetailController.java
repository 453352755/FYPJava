package QLearning;

import static QLearning.GridWorld.Up;
import static QLearning.GridWorld.Down;
import static QLearning.GridWorld.Left;
import static QLearning.GridWorld.Right;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
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

    private QLearnAlgo algo;
    private Location loc;

    public void setAlgo(QLearnAlgo algo) {
        this.algo = algo;
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
        algo.getGridWorld().getLocation(loc.getRow(), loc.getCol()).
                setTravelTime(Up, Double.valueOf(upField.getText()));
    }

    @FXML
    void leftValueChanged(ActionEvent event) {
        algo.getGridWorld().getLocation(loc.getRow(), loc.getCol()).
                setTravelTime(Left, Double.valueOf(leftField.getText()));

    }

    @FXML
    void downValueChanged(ActionEvent event) {
        algo.getGridWorld().getLocation(loc.getRow(), loc.getCol()).
                setTravelTime(Down, Double.valueOf(downField.getText()));

    }

    @FXML
    void rightValueChanged(ActionEvent event) {
        algo.getGridWorld().getLocation(loc.getRow(), loc.getCol()).
                setTravelTime(Right, Double.valueOf(rightField.getText()));

    }

    @FXML
    void rewardValueChanged(ActionEvent event) {
        algo.getGridWorld().getLocation(loc.getRow(),
                loc.getCol()).setReward(Double.valueOf(rewardField.getText()));
    }

    @FXML
    void blockChecked(ActionEvent event) {
        if (blockCheckBox.isSelected()) {
            algo.getGridWorld().setBlock(loc.getRow(), loc.getCol(),true);
        } else {
            algo.getGridWorld().setBlock(loc.getRow(), loc.getCol(),false);
        }
    }

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
        setTimeLabel(loc.getTravelTime(), loc.isBlock());

        DecimalFormat form = new DecimalFormat("0.##");
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
            upField.setText(form.format(loc.getTravelTime(Up)));
            downField.setText(form.format(loc.getTravelTime(Down)));
            leftField.setText(form.format(loc.getTravelTime(Left)));
            rightField.setText(form.format(loc.getTravelTime(Right)));
        }
        rewardField.setText(form.format(loc.getReward()));
        blockCheckBox.setSelected(loc.isBlock());
        locGridPane.setVisible(true);

    }

    public void reset() {
        if (!locationPane.getChildren().isEmpty()) {
            locationPane.getChildren().remove(0);
        }
        resetTimeLabel();
        locGridPane.setVisible(false);
    }

}
