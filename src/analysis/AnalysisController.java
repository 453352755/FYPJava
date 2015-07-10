package analysis;

import QLearning.QLearnAlgo;
import app.GUI;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;

/**
 *
 * @author Dong Yubo
 */
public class AnalysisController implements Initializable {

    private QLearnAlgo algo = null;
    private LineChart aLineChart;
    private LineChart bLineChart;
    private LineChart cLineChart;
    private XYChart.Series rewardSeries;
    private final String chartStyle
            = "-fx-border-width: 2;"
            + "-fx-border-stroke: black;"
            + "-fx-background-color: lightyellow;"
            + "-fx-max-height: 640;"
            + "-fx-max-width: 800;"
            + "-fx-min-height: 320;"
            + "-fx-min-width: 400;"
            + "-fx-pref-height: 320;"
            + "-fx-pref-width: 400;";

    public void setGridController(QLearnAlgo qLearnAlgo) {
        this.algo = qLearnAlgo;
    }

    private void addChart(Chart c) {
        if (chartFlowPane.getChildren().size() < 4) {
            chartFlowPane.getChildren().add(c);
        } else {
            System.out.println("Max Chart number reached");
        }
    }

    private void removeChart(Chart c) {
        if (chartFlowPane.getChildren().contains(c)) {
            chartFlowPane.getChildren().remove(c);
        }
    }
    
    public void reset(){
        rewardSeries.getData().clear();
    }

    private void chartAInit() {
        if (aLineChart == null) {
            final NumberAxis stepAxis = new NumberAxis();
            final NumberAxis rewardAxis = new NumberAxis();
            stepAxis.setLabel("Number of Steps");
            //creating the chart
            aLineChart = new LineChart<Number, Number>(stepAxis, rewardAxis);
            aLineChart.setTitle("Reward");
            aLineChart.setStyle(chartStyle);
            rewardSeries = new XYChart.Series();
            rewardSeries.setName("Reward");
            aLineChart.getData().add(rewardSeries);
            
        }
    }
    
    public void addRewardData(int steps, double reward){
        if(rewardSeries == null){
            chartAInit();
        }else{
            rewardSeries.getData().add(new XYChart.Data(steps,reward));
        }
    }

    private void chartBInit() {
        if (bLineChart == null) {
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Number of Month");
            //creating the chart
            bLineChart = new LineChart<>(xAxis, yAxis);

            bLineChart.setTitle("Stock Monitoring, 2010");
            bLineChart.setStyle(chartStyle);
            XYChart.Series series = new XYChart.Series();
            series.setName("My portfolio");
            //populating the series with data
            series.getData().add(new XYChart.Data(1, 23));
            series.getData().add(new XYChart.Data(2, 14));
            series.getData().add(new XYChart.Data(3, 15));
            series.getData().add(new XYChart.Data(4, 24));
            series.getData().add(new XYChart.Data(5, 34));
            series.getData().add(new XYChart.Data(6, 36));
            series.getData().add(new XYChart.Data(7, 22));
            series.getData().add(new XYChart.Data(8, 45));
            series.getData().add(new XYChart.Data(9, 43));
            series.getData().add(new XYChart.Data(10, 17));
            series.getData().add(new XYChart.Data(11, 29));
            series.getData().add(new XYChart.Data(12, 25));
            bLineChart.getData().add(series);
        }
    }

    private void chartCInit() {
        if (cLineChart == null) {
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Number of Month");
            //creating the chart
            cLineChart = new LineChart<>(xAxis, yAxis);

            cLineChart.setTitle("Stock Monitoring, 2010");
            cLineChart.setStyle(chartStyle);
            XYChart.Series series = new XYChart.Series();
            series.setName("My portfolio");
            //populating the series with data
            series.getData().add(new XYChart.Data(1, 23));
            series.getData().add(new XYChart.Data(2, 14));
            series.getData().add(new XYChart.Data(3, 15));
            series.getData().add(new XYChart.Data(4, 24));
            series.getData().add(new XYChart.Data(5, 34));
            series.getData().add(new XYChart.Data(6, 36));
            series.getData().add(new XYChart.Data(7, 22));
            series.getData().add(new XYChart.Data(8, 45));
            series.getData().add(new XYChart.Data(9, 43));
            series.getData().add(new XYChart.Data(10, 17));
            series.getData().add(new XYChart.Data(11, 29));
            series.getData().add(new XYChart.Data(12, 25));
            cLineChart.getData().add(series);
        }
    }

    @FXML
    private CheckBox qLearnCChart;

    @FXML
    public FlowPane chartFlowPane;

    @FXML
    private CheckBox qLearnBChart;

    @FXML
    private CheckBox qLearnAChart;


    @FXML
    private HBox analysisPane;

    @FXML
    private Button floatButton;

    @FXML
    private Button saveButton;

    @FXML
    void floatButtonClicked(ActionEvent event) {
        floatButton.setDisable(true);
        GUI.analysisFloat();
        Stage newStage = new Stage();
        newStage.setTitle("Simulation Analysis");
        newStage.centerOnScreen();
        newStage.setMinWidth(960);
        newStage.setMinHeight(640);
        newStage.setMaxWidth(1920);
        newStage.setMaxHeight(1080);
        newStage.setHeight(640);
        newStage.setWidth(960);
        //https://www.iconfinder.com/icons/175360/combo_icon#size=512
        newStage.getIcons().add(new Image("file:images/chart-icon.png"));
        //newStage.initStyle(StageStyle.UNIFIED);
        Scene scene = new Scene(analysisPane);
        newStage.setScene(scene);
        newStage.show();

        newStage.setOnCloseRequest((WindowEvent t) -> {
            System.out.println("float closed");
            GUI.switchToAnalysis();
            floatButton.setDisable(false);
        });

    }

    @FXML
    void saveButtonClicked(ActionEvent event) {
        System.out.print("Saving Chart ");

        //construct file name
        Format form = new SimpleDateFormat("yyyyMMdd");
        String name = "";
        for (int i = 0; i < chartFlowPane.getChildren().size(); i++) {
            name += ((Chart) chartFlowPane.getChildren().get(i)).getTitle();
        }
        name += " " + form.format(new Date()) + ".png";
        System.out.println(name);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Chart");
        fileChooser.setInitialFileName(name);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        File directory = new File("C:\\Dropbox\\FY");
        if (!directory.canRead()) {
            directory = new File("c:/");
        }
        fileChooser.setInitialDirectory(directory);
        File file = fileChooser.showSaveDialog(new Stage());

        //select file location
        if (file != null) {
            try {
                WritableImage snapshot = chartFlowPane.snapshot(new SnapshotParameters(), null);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
            } catch (IOException ex) {
                System.out.println("IO exception");
            }
        }

    }

    @FXML
    void aChartChecked(ActionEvent event) {
        if (qLearnAChart.isSelected()) {
            chartAInit();
            addChart(aLineChart);
            System.out.println("Adding aLineChart");
        } else {
            removeChart(aLineChart);
            System.out.println("aLineChart removed");
        }
    }

    @FXML
    void bChartChecked(ActionEvent event) {
        if (qLearnBChart.isSelected()) {
            chartBInit();
            addChart(bLineChart);
            System.out.println("Adding bLineChart");
        } else {
            removeChart(bLineChart);
            System.out.println("bLineChart removed");
        }
    }

    @FXML
    void cChartChecked(ActionEvent event) {
        if (qLearnCChart.isSelected()) {
            chartCInit();
            addChart(cLineChart);
            System.out.println("Adding cLineChart");
        } else {
            removeChart(cLineChart);
            System.out.println("cLineChart removed");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chartFlowPane.setOrientation(Orientation.HORIZONTAL);

        //chartFlowPane.
//        chartFlowPane.setHgap(20);
//        chartFlowPane.setVgap(20);
    }

}
