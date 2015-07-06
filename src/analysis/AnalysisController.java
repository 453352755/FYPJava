
package analysis;

import QLearning.QLearnAlgo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;

/**
 *
 * @author Dong Yubo
 */
public class AnalysisController implements Initializable {

    private QLearnAlgo algo = null;

    public void setGridController(QLearnAlgo qLearnAlgo) {
        this.algo = qLearnAlgo;
    }

    @FXML
    private LineChart<?, ?> qLearnChart;

    @FXML
    private NumberAxis qLearnNumberAxis;

    @FXML
    private Pane analysisPane;

    @FXML
    private CategoryAxis qLearnCategoryAxis;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
