package analysis;

import QLearning.*;
import static QLearning.GridWorld.Down;
import static QLearning.GridWorld.Left;
import static QLearning.GridWorld.Right;
import static QLearning.GridWorld.Up;
import app.GUI;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

/**
 *
 * @author Dong Yubo
 */
public class AnalysisController implements Initializable {

    double path1mean = 46, path1steps = 6, path1dev = 8.12;
    double path2mean = 56, path2steps = 8, path2dev = 2.82;

    private QLearning.Algorithm algo;
    private LineChart aLineChart;//reward 
    private LineChart bLineChart;//time
    private LineChart cLineChart;//cdf-pdf chart
    private LineChart QvalueLineChart;//Qvalue chart
    private XYChart.Series rewardSeries;
    private XYChart.Series timeSeries;
    private final String chartStyle
            = "-fx-border-width: 2;"
            + "-fx-border-color: black;"
            + "-fx-background-color: lightyellow;"
            + "-fx-pref-height: 1000;"
            + "-fx-pref-width: 1900;"
            + "-fx-max-height: 1000;"
            + "-fx-max-width: 1900;"
            + "-fx-min-height: 320;"
            + "-fx-min-width: 400;";
    private int row = 0;
    private int col = 0;

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
        if(QValueChartCheckBox.isSelected()){
            removeChart(QvalueLineChart);
            QValueChartInit();
            addChart(QvalueLineChart);
        }
    }

    public void setAlgo(Algorithm algo) {
        this.algo = algo;
    }

    private void addChart(Chart c) {

        if (chartHBox.getChildren().size() < 4) {
            chartHBox.getChildren().add(c);

        } else {
            System.out.println("Max Chart number reached");
        }
    }

    private void removeChart(Chart c) {
        if (chartHBox.getChildren().contains(c)) {
            chartHBox.getChildren().remove(c);
        }
    }

    public void reset(Algorithm algo, GridWorld gw) {
        rewardSeries.getData().clear();
    }

    private void chartAInit() {
        if (aLineChart == null) {
            final NumberAxis stepAxis = new NumberAxis();
            final NumberAxis rewardAxis = new NumberAxis();
            stepAxis.setLabel("Number of Steps");
            //creating the chart
            aLineChart = new LineChart<>(stepAxis, rewardAxis);
            aLineChart.setTitle("Reward");
            aLineChart.setStyle(chartStyle);
            rewardSeries = new XYChart.Series();
            rewardSeries.setName("Reward");
            aLineChart.getData().add(rewardSeries);

        }
    }

    public void addRewardData(int steps, double reward) {
        if (rewardSeries == null) {
            chartAInit();
        } else {
            rewardSeries.getData().add(new XYChart.Data(steps, reward));
        }
    }

    private void chartBInit() {
        if (bLineChart == null) {
            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Number of Steps");
            //creating the chart
            bLineChart = new LineChart<>(xAxis, yAxis);
            bLineChart.setManaged(true);
            bLineChart.setTitle("Total Time Taken");
            bLineChart.setStyle(chartStyle);
            timeSeries = new XYChart.Series();
            timeSeries.setName("Time");
            bLineChart.getData().add(timeSeries);
        }
    }

    public void addTimeData(int steps, double time) {
        if (timeSeries == null) {
            chartBInit();
        } else {
            timeSeries.getData().add(new XYChart.Data(steps, time));
        }
    }

    private void chartCInit() {

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Remaining Battery");
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(algo.getGridWorld().getFullBatterySteps() * 0.1);
        xAxis.setUpperBound(algo.getGridWorld().getFullBatterySteps() * 1.5);
        yAxis.setLabel("Probability");
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(1);
        //creating the chart
        cLineChart = new LineChart<>(xAxis, yAxis);
        cLineChart.setTitle("PDF - CDF of routes");
        cLineChart.setManaged(true);
        cLineChart.setStyle(chartStyle);

        //path1
        XYChart.Series PDF1 = new XYChart.Series();
        PDF1.setName("PDF1");
        XYChart.Series CDF1 = new XYChart.Series();
        CDF1.setName("CDF1");
//        try {
//            path1mean = algo.getGridWorld().getPathMean()[0];
//            path1dev = algo.getGridWorld().getPathMean()[1];
//            System.out.println(path1mean + "," + path1dev);
//        } catch (NullPointerException ne) {
//            System.out.println("No Path");
//        }

        NormalDistribution nd1 = new NormalDistribution(path1mean, path1dev);
        for (int i = 0; i < algo.getGridWorld().getFullBatterySteps() * 15; i++) {
            double p = i / 10.0;
            PDF1.getData().add(new XYChart.Data(p, nd1.density(p)));
            CDF1.getData().add(new XYChart.Data(p, nd1.cumulativeProbability(p)));
            //System.out.println(nd1.density(p));
        }

        //path2
        XYChart.Series PDF2 = new XYChart.Series();
        PDF2.setName("PDF2");
        XYChart.Series CDF2 = new XYChart.Series();
        CDF2.setName("CDF2");

        NormalDistribution nd2 = new NormalDistribution(path2mean, path2dev);
        for (int i = 0; i < algo.getGridWorld().getFullBatterySteps() * 15; i++) {
            double p = i / 10.0;
            PDF2.getData().add(new XYChart.Data(p, nd2.density(p)));
            CDF2.getData().add(new XYChart.Data(p, nd2.cumulativeProbability(p)));
            //System.out.println(p + "," + nd2.density(p));
        }

        cLineChart.getData().add(PDF1);
        cLineChart.getData().add(CDF1);
        cLineChart.getData().add(PDF2);
        cLineChart.getData().add(CDF2);
        cLineChart.setCreateSymbols(false);
    }

    private void QValueChartInit() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Remaining Battery");
        yAxis.setLabel("Q Values");
        //creating the chart
        QvalueLineChart = new LineChart<>(xAxis, yAxis);
        QvalueLineChart.setTitle("Q Values Row " + row + " Col " + col);
        QvalueLineChart.setManaged(true);
        QvalueLineChart.setStyle(chartStyle);

        XYChart.Series up = new XYChart.Series();
        up.setName("Up");
        XYChart.Series down = new XYChart.Series();
        down.setName("Down");
        XYChart.Series left = new XYChart.Series();
        left.setName("Left");
        XYChart.Series right = new XYChart.Series();
        right.setName("Right");
        if (algo.getClass() == ModifiedAlgo.class) {
            for (int i = 0; i < algo.getGridWorld().getFullBatterySteps(); i++) {
                up.getData().add(new XYChart.Data(i,algo.getQvalue(row, col)[i][Up]));
                down.getData().add(new XYChart.Data(i,algo.getQvalue(row, col) [i][Down]));
                left.getData().add(new XYChart.Data(i,algo.getQvalue(row, col) [i][Left]));
                right.getData().add(new XYChart.Data(i,algo.getQvalue(row, col) [i][Right]));
            }
            QvalueLineChart.getData().add(up);
            QvalueLineChart.getData().add(down);
            QvalueLineChart.getData().add(left);
            QvalueLineChart.getData().add(right);
        } else {
            QvalueLineChart.setTitle("Original Algo selected");
        }

    }
    @FXML
    private CheckBox qLearnCChartCheckBox;

    @FXML
    public HBox chartHBox;

    @FXML
    private CheckBox qLearnBChartCheckBox;

    @FXML
    private CheckBox qLearnAChartCheckBox;

    @FXML
    private CheckBox QValueChartCheckBox;

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
        newStage.setMaxHeight(1920);
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
        Format form = new SimpleDateFormat("yyyyMMddmmss");
        String name = "";
        for (int i = 0; i < chartHBox.getChildrenUnmodifiable().size(); i++) {
            name += ((Chart) chartHBox.getChildrenUnmodifiable().get(i)).getTitle();
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
                WritableImage snapshot = chartHBox.snapshot(new SnapshotParameters(), null);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
            } catch (IOException ex) {
                System.out.println("IO exception");
            }
        }

    }

    @FXML
    void aChartChecked(ActionEvent event) {
        if (qLearnAChartCheckBox.isSelected()) {
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
        if (qLearnBChartCheckBox.isSelected()) {
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
        if (qLearnCChartCheckBox.isSelected()) {
            chartCInit();
            addChart(cLineChart);
            System.out.println("Adding Path CDF PDF Chart");
        } else {
            removeChart(cLineChart);
            System.out.println("Chart removed");
        }
    }

    @FXML
    void checkQValueChart(ActionEvent event) {
        if (QValueChartCheckBox.isSelected()) {
            QValueChartInit();
            addChart(QvalueLineChart);
            System.out.println("Adding Q Values Chart");
        } else {
            removeChart(QvalueLineChart);
            System.out.println("Chart removed");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //chartFlowPane.
//        chartFlowPane.setHgap(20);
//        chartFlowPane.setVgap(20);
    }

}
