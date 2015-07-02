package QLearning;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Dong Yubo
 */
public class DetailController implements Initializable {

    @FXML
    public StackPane locationPane;

    @FXML
    private VBox detailPane;

    @FXML
    private Label label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        assert locationPane != null : "fx:id=\"location\" was not injected: check your FXML file 'detail.fxml'.";
        assert detailPane != null : "fx:id=\"detail\" was not injected: check your FXML file 'detail.fxml'.";
        assert label != null : "fx:id=\"label\" was not injected: check your FXML file 'detail.fxml'.";
    }

}
