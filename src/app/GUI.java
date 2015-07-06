package app;

import QLearning.GridController;
import analysis.AnalysisController;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Dong Yubo
 */
public class GUI extends Application {

    //private static Stage myStage;
    private static BorderPane rootLayout;
    private static HBox QLearnLayout;
    private static Pane analysisLayout;

    private static GridController QLearnCtrl;
    private static RootController rootCtrl;
    private static AnalysisController analysisCtrl;

    private static URL analysisURL;
    private static URL QLearnURL;

    @Override
    public void start(Stage myStage) throws IOException {
        System.out.println("GUI start");
        analysisURL = getClass().getClassLoader().getResource("resource/analysis.fxml");
        QLearnURL = getClass().getClassLoader().getResource("resource/grid_world.fxml");

        FXMLLoader rootLoader = new FXMLLoader();
        rootLoader.setLocation(getClass().getClassLoader().getResource("resource/root.fxml"));
        rootLayout = rootLoader.load();
        rootCtrl = rootLoader.getController();

        //load QLearning fxml 
//        FXMLLoader QLoader = new FXMLLoader();
//        QLoader.setLocation(getClass().getClassLoader().getResource("resource/grid_world.fxml"));
//        QLearnLayout = (HBox) QLoader.load();
//        QLearnCtrl = QLoader.getController();
        //QLearnCtrl.setMainApp(this);
        switchToQLearn();

        //GUI customization
        rootLayout.setCenter(QLearnLayout);
        Scene scene = new Scene(rootLayout);
        myStage.setScene(scene);
        myStage.setTitle("Multi-Vehicle Coverage Control Simulator");
        myStage.getIcons().add(new Image("file:images/car-icon.png"));
        myStage.centerOnScreen();
        myStage.setMinWidth(980);
        myStage.setMinHeight(720);
        myStage.setMaxWidth(1920);
        myStage.setMaxHeight(1080);

        myStage.setOnCloseRequest((WindowEvent t) -> {
            QLearnCtrl.stop();
            System.out.println("exit");
            Platform.exit();
            System.exit(0);
        });

        myStage.maximizedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean o, Boolean n) -> {
            //System.out.println("maximized:" + n);
        });
        myStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static void switchToAnalysis() {
        FXMLLoader ALoader = new FXMLLoader();
        ALoader.setLocation(analysisURL);
        try {
            analysisLayout = (Pane) ALoader.load();
            analysisCtrl = ALoader.getController();
            analysisCtrl.setGridController(QLearnCtrl.getAlgo());
            System.out.println("anslysis layout loaded");
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("load failed");
        }
        rootLayout.setCenter(analysisLayout);
    }

    public static void switchToQLearn() {
        FXMLLoader QLearnLoader = new FXMLLoader();
        QLearnLoader.setLocation(QLearnURL);
        try {
            QLearnLayout = (HBox) QLearnLoader.load();
            QLearnCtrl = QLearnLoader.getController();
            //System.out.println("QLearn layout loaded");
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("load failed");
        }
        rootLayout.setCenter(QLearnLayout);
    }

    public static void editRewardSetting(){
        //QLearnCtrl
    }
}
//
//    public void drawtest() {
//        double width = 600 / gw.getCols();
//        double height = 600 / gw.getRows();
//
//        for (int i = 0; i < gw.getRows(); i++) {
//            for (int j = 0; j < gw.getCols(); j++) {
//                gc.strokeRect(width * j + 20, height * i + 20, width, height);
//
//            }
//
//        }
//    }
//
//    public void drawtest1() {
//        gc.setFill(Color.GREEN);
//        gc.setStroke(Color.BLUE);
//        gc.setLineWidth(5);
//        gc.strokeLine(40, 10, 10, 40);
//        gc.fillOval(10, 60, 30, 30);
//        gc.strokeOval(60, 60, 30, 30);
//        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
//        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
//        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
//        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
//        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
//        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
//        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
//        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
//        gc.fillPolygon(new double[]{10, 40, 10, 40},
//                new double[]{210, 210, 240, 240}, 4);
//        gc.strokePolygon(new double[]{60, 90, 60, 90},
//                new double[]{210, 210, 240, 240}, 4);
//        gc.strokePolyline(new double[]{110, 140, 110, 140},
//                new double[]{210, 210, 240, 240}, 4);
//    }
