package app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Dong Yubo
 */
public class RootController implements Initializable {

    GUI gui;

    @FXML
    private Button qLearnButton;

    @FXML
    private Button analysisButton;

    @FXML
    private HBox top;

    @FXML
    private Pane bottom;

    @FXML
    private Label status;

    @FXML
    private Button rewardSettingButton;

    @FXML
    void changeRewardSetting(ActionEvent event) {
        GUI.editRewardSetting();
        System.out.println("changing reward setting");
    }

    @FXML
    void qlearnClicked(ActionEvent event) {
        GUI.switchToQLearn();
        System.out.println("Switch to QLeaning simulation");
        status.setText("QLearning Simulation");
    }

    @FXML
    void analysisClicked(ActionEvent event) {
        GUI.switchToAnalysis();
        System.out.println("Switch to analysis, showing performance detail");
        status.setText("Analysis");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //top.setSpacing((top.getWidth()-top.getChildren().size()*qLearnButton.getWidth())/(top.getChildren().size()+1));

    }

}
