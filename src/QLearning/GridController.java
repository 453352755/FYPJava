package QLearning;

/**
 *
 * @author Dong Yubo
 */
import analysis.AnalysisController;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class GridController {

    private boolean play;
    private static Thread algoThread;
    private long startTime;
    

    private GridWorld gw = new GridWorld(3, 5);
    //private Algorithm algo = new ModifiedAlgo(gw);
    private Algorithm algo = new QLearnAlgo(gw);
    private GridPane gridPane;
    private GridPane selectedLocationPane;
    private VBox detailPane;
    private DetailController detailCtrl;
    private AnalysisController analysisCtrl;

    public void setGridWorld(int row, int col) {
        gw = new GridWorld(row, col);
        algo.setGridWorld(gw);
        System.out.println("New grid world set");
        System.out.format("Rows: %d, Cols: %d\n", row, col);
    }

    public void setAnalysisController(AnalysisController a) {
        this.analysisCtrl = a;
    }

    public Algorithm getAlgo() {
        return algo;
    }

    public void initialize() {
        gridPaneInit();
        Label test = new Label();
        test.setText("test");
        test.setStyle("-fx-text-fill:#FFEB3B;-fx-font-size:40;");

        graphPane.getChildren().add(gridPane);
        rowBox.setItems(FXCollections.observableArrayList(
                3,4,5,6,7,8,9,10, 15, 20)
        );
        colBox.setItems(FXCollections.observableArrayList(
                3,4,5,6,7,8,9,10, 15, 20)
        );
        rowBox.setValue(3);
        colBox.setValue(5);
//        alphaField.setText(String.valueOf(algo.getAlpha()));
//        greedyValue.setText(String.valueOf(algo.getGreedyProb()));
//        discountValue.setText(String.valueOf(algo.getDiscount()));
//        directionProbability.setText(String.valueOf(gw.getDirectionProbability()));

        //initialize deatail pane
        FXMLLoader detailLoader = new FXMLLoader();
        detailLoader.setLocation(getClass().getClassLoader().getResource("resource/detail.fxml"));

        try {
            detailPane = (VBox) detailLoader.load();
            //System.out.println(detailPane.getChildrenUnmodifiable().size());
            detailCtrl = detailLoader.getController();
            detailCtrl.setGridWorld(gw);
            detailCtrl.setGridCtrl(this);
            //System.out.println("Detail loaded");
        } catch (IOException ex) {
            Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Load detail fxml failed");
        }

        root.widthProperty().addListener((ObservableValue<? extends Number> ov, Number o, Number n) -> {
            int bound = 1290;
            if (o.doubleValue() < bound && n.doubleValue() >= bound) {
                //System.out.println("GUI expanded, showing node detail");
                if (selectedLocationPane != null) {
                    //detailCtrl.location.getChildren().add(selectedLocation);
                    //System.out.println(detailCtrl.getLocationPane().isVisible());
                }
                root.getChildren().add(root.getChildren().size(), detailPane);
            } else if (o.doubleValue() >= bound && n.doubleValue() < bound) {
                if (root.getChildren().contains(detailPane)) {
                    root.getChildren().remove(detailPane);
                }
            }
        });
        
        checkSettings();

        repaintAll();
    }

    public void gridPaneInit() {
        gridPane = new GridPane();
        gridPane.setPrefSize(640, 640);
        gridPane.setMaxSize(1000, 1000);
        gridPane.setBackground(new Background(new BackgroundFill(
                Paint.valueOf(app.Color.White), CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.setGridLinesVisible(true);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setCenterShape(true);
        gridPane.setHgap(1);
        gridPane.setVgap(1);

//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getClassLoader().getResource("QLearning/location.fxml"));
//        System.out.println("Loading FXML from ");
//        System.out.println(loader.getLocation());
//        try {
//            location = (GridPane) loader.load();
//        } catch (Exception e) {
//            System.out.println("location fxml load failed");
//        }
        for (int i = 0; i < gw.getRows(); i++) {
            for (int j = 0; j < gw.getCols(); j++) {
                //GridPane location = Location.newNode();
                GridPane location = gw.getLocation(i, j).getLocPane();

                //location on mouse click
                location.setOnMouseClicked((MouseEvent me) -> {
                    selectedLocationPane = (GridPane) me.getSource();
                    int row = GridPane.getRowIndex(selectedLocationPane);
                    int col = GridPane.getColumnIndex(selectedLocationPane);
                    String s = "-fx-border-color: " + app.Color.Blue[5] + ";-fx-border-width:2;";
                    selectedLocationPane.setStyle(s);
//                    System.out.print("location detail: row ");
//                    System.out.print(row);
//                    System.out.print(" col ");
//                    System.out.println(col);
                    //gw.getLocation(row, col).print();
                    Location loc = gw.getLocation(row, col).copy();
                    //loc.print();
                    //GridPane loc = Location.newNode();
                    loc.repaint(algo);
                    detailCtrl.init(loc);
//                    try {
//                        detailCtrl.getLocationPane().getChildren().remove(0);
//                    } catch (Exception n) {
//
//                    }
//                    detailCtrl.getLocationPane().getChildren().add(loc.getLocPane());
//                    detailCtrl.setTimeLabel(loc.getTravelTime(), loc.isBlock());
//                    DecimalFormat form = new DecimalFormat("0.##");
//                    detailCtrl.getLocationValueLabel().setText("Location Value: " + form.format(
//                            gw.getLocation(row, col).getLocationValue()));

                });

                gridPane.add(location, j, i);

                //ColumnConstraints col = new ColumnConstraints();
                //col.setPercentWidth(100 / gw.getCols());
                //col.setHgrow(Priority.ALWAYS);
                //grid.getColumnConstraints().add(col);
                //RowConstraints row = new RowConstraints();
                //row.setPercentHeight(100 / gw.getRows());
                //row.setVgrow(Priority.ALWAYS);
                //grid.getRowConstraints().add(row);
            }
        }
        //System.out.println("location loaded");

    }

//    public void paint(GridPane loc) {
//        int row = GridPane.getRowIndex(loc);
//        int col = GridPane.getColumnIndex(loc);
//        gw.getLocation(row, col).repaint(loc);
//        for (int i = 0; i < loc.getChildren().size(); i++) {
//            try {
//                Circle c = (Circle) loc.getChildren().get(i);
//                if (row == gw.getCurRow() && col == gw.getCurCol()) {
//                    c.setVisible(true);
//                } else {
//                    c.setVisible(false);
//                }
//            } catch (Exception e) {
//            }
//        }
//
//    }
    public void repaintAll() {
        //System.out.print("Repainting...");
        for (int i = 0; i < gw.getRows(); i++) {
            for (int j = 0; j < gw.getCols(); j++) {
                gw.getLocation(i, j).repaint(algo);
            }
        }
        //detailCtrl.updateReward();

        //System.out.println("finished");
    }

    private void addDataToChart() {
        analysisCtrl.addRewardData(gw.getNumberOfSteps(),
                gw.getTotalReward());
        analysisCtrl.addTimeData(gw.getNumberOfSteps(),
                gw.getTotalTravelTime());
        //System.out.println("New data: ");
    }

    void updatePerformance() {
        DecimalFormat form = new DecimalFormat("0.#E0");
        double reward = gw.getTotalReward();
        int step = gw.getNumberOfSteps();
        double time = gw.getTotalTravelTime();
        totalRewards.setText(form.format(reward));
        totalSteps.setText(form.format(step));
        totalTravelTime.setText(form.format(time) + " mins");
        remainingStepsLabel.setText(String.valueOf(gw.getRemainingSteps()));
        addDataToChart();
    }

    public static Node getNode(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null) {
                if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                    result = node;
                    break;
                }
            }
        }
        return result;
    }

    public void stop() {
        //System.out.println("Application stopped");

    }

////////////////////////////////////////////////////////////////////////////////
    @FXML
    void moveLeft(ActionEvent event) {
        algo.moveToDir(GridWorld.Left);
        repaintAll();
        updatePerformance();
        System.out.println("move left");
    }

    @FXML
    void moveRight(ActionEvent event) {
        algo.moveToDir(GridWorld.Right);
        repaintAll();
        updatePerformance();
        System.out.println("move right");
    }

    @FXML
    void moveUp(ActionEvent event) {
        algo.moveToDir(GridWorld.Up);
        repaintAll();
        updatePerformance();
        System.out.println("move up");
    }

    @FXML
    void moveDown(ActionEvent event) {
        algo.moveToDir(GridWorld.Down);
        repaintAll();
        updatePerformance();
        System.out.println("move down");
    }

    @FXML
    void changeDiscountValue(ActionEvent event) {
        double dv = Double.parseDouble(discountValue.getText());
        algo.setDiscount(dv);

        System.out.print("discount value = ");
        System.out.println(dv);

    }

    @FXML
    void changeGreedyValue(ActionEvent event) {
        double gv = Double.parseDouble(greedyValue.getText());
        algo.setGreedyProb(gv);

        System.out.print("greedy value = ");
        System.out.println(gv);

    }

    @FXML
    void reset(ActionEvent event) {
        System.out.println("reset");
        autorunStatus.setText("Play to start auto run");
        totalSteps.setText(null);
        totalRewards.setText(null);
        totalTravelTime.setText(null);
        int row, col;
        try {
            row = rowBox.getValue();
            col = colBox.getValue();
        } catch (NullPointerException e) {
            row = 10;
            col = 10;
        }
        setGridWorld(row, col);
        checkSettings();
        graphPane.getChildren().remove(gridPane);
        detailCtrl.reset(algo,gw);
        gridPaneInit();
        graphPane.getChildren().add(gridPane);
        repaintAll();
        updatePerformance();
        analysisCtrl.reset(algo,gw);
    }

    @FXML
    void updateSpeedValue(ActionEvent event) {
        double sv;
        try {
            sv = Double.parseDouble(speedValue.getText());
        } catch (NumberFormatException e) {
            sv = -1000;
        }
        System.out.print("speed value = ");
        System.out.println(sv);

    }

    private void checkSettings() {
        //algo reset
        ActionEvent event = new ActionEvent();
        checkAlgo(event);
        checkBatteryLife(event);
        checkFixedalpha(event);
        checkRandomTravelTime(event);
        checkTracing(event);
        
        changeAlphaValue(event);
        changeDirectionProbability(event);
        changeDiscountValue(event);
        changeGreedyValue(event);
        changeDefaultTravelTime(event);
    }

    private class RunMap implements Runnable {

        @Override
        public void run() {

            int interval;
            try {
                interval = 1000 / (int) Double.parseDouble(speedValue.getText());
                System.out.print("running at speed ");
                System.out.println(speedValue.getText());
            } catch (Exception e) {
                //default speed infinity
                interval = -1000;
                System.out.println("running at speed infinity");
            }

            while (play) {
                try {
                    boolean move = algo.doSteps(1);
                    //if (move == false) play = false;
//                    play = move;
                    //algo.doSteps(1);
                    if (interval > 100) {
                        long start = System.nanoTime();

                        long end = System.nanoTime();
                        long us = (end - start) / 1000;
                        //System.out.println(us);
                        //System.out.println("Map running");
                        Thread.sleep(interval);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                repaintAll();
                                updatePerformance();
                            }
                        });

                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    @FXML
    void playClicked(ActionEvent event) {
        //System.out.println("play button clicked");
        upButton.setDisable(true);
        downButton.setDisable(true);
        leftButton.setDisable(true);
        rightButton.setDisable(true);
        discountValue.setDisable(true);
        greedyValue.setDisable(true);
        speedValue.setDisable(true);
        //playButton.setDisable(true);
        resetButton.setDisable(true);
        //directionProbability.setDisable(true);
        discountValue.setDisable(true);
        autorunStatus.setText("Auto running");

        //run simulator
        play = true;
        algoThread = new Thread(new RunMap());
        startTime = System.nanoTime();
        algoThread.start();

    }

    @FXML
    void pauseClicked(ActionEvent event) {
        //System.out.println("pause button clicked");
        upButton.setDisable(false);
        downButton.setDisable(false);
        leftButton.setDisable(false);
        rightButton.setDisable(false);
        discountValue.setDisable(false);
        greedyValue.setDisable(false);
        speedValue.setDisable(false);
        //directionProbability.setDisable(false);
        discountValue.setDisable(false);
        //playButton.setDisable(false);
        resetButton.setDisable(false);
        autorunStatus.setText("Paused, single step");

        play = false;
        try {
            algoThread.join();
            long time = System.nanoTime() - startTime;
            System.out.print("Total time taken: ");
            System.out.print(time / 1000000);
            System.out.println(" mili second");
            repaintAll();
            updatePerformance();
            //t.interrupt();
        } catch (InterruptedException ex) {
            Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException n) {
            System.out.println("Map not running");
        }

    }

    @FXML
    void checkTracing(ActionEvent event) {
        if (tracing.isSelected()) {
            System.out.println("Tracing started");
            algo.setTracing(true);
        } else {
            System.out.println("Tracing Stopped");
            algo.setTracing(false);
        }
    }

    @FXML
    void changeAlphaValue(ActionEvent event) {
        double av = Double.parseDouble(alphaField.getText());
        algo.setAlpha(av);
        System.out.print("Alpha value set to ");
        System.out.println(av);
    }

    @FXML
    void changeDirectionProbability(ActionEvent event) {
        double dv = Double.parseDouble(directionProbability.getText());
        gw.setDirectionProbability(dv);
        System.out.print("DIrection Probability set to ");
        System.out.println(dv);
    }

    @FXML
    void changeDefaultTravelTime(ActionEvent event) {
        double dv = Double.parseDouble(defaultTravelTimeField.getText());
        gw.setDefaultTraveTime(dv);
        System.out.print("Default Travel Time set to ");
        System.out.println(dv);
    }

    @FXML
    void checkFixedalpha(ActionEvent event) {
        if (alphaFixedBox.isSelected()) {
            algo.setAlphaFixed(true);
            System.out.println("Alpha fixed");
        } else {
            algo.setAlphaFixed(false);
            System.out.println("Alpha Unfixed");
        }
    }

    @FXML
    void checkRandomTravelTime(ActionEvent event) {
        if (randomTravelTimeCheckBox.isSelected()) {
            defaultTravelTimeField.setDisable(true);
            gw.setRandomTravelTime(true);
            System.out.println("Random travel time");
        } else {
            defaultTravelTimeField.setDisable(false);
            gw.setRandomTravelTime(false);
        }
    }
    
    @FXML
    void checkBatteryLife(ActionEvent event) {
        if (batteryLifeCheckBox.isSelected()) {
            gw.setBatteryEnabled(true);
            System.out.println("Battery life enabled");
        }else{
            gw.setBatteryEnabled(false);
            System.out.println("Battery life disabled");
        }
    }
    
    @FXML
    void checkAlgo(ActionEvent event) {
        if (originalRadioButton.isSelected()) {
            algo = new QLearnAlgo(gw);
            System.out.println("Original Algo selected");
        }else if (modifiedRadioButton.isSelected()) {
            algo = new ModifiedAlgo(gw);
            System.out.println("Modified Algo selected");
        }
    }

    @FXML
    private ToggleGroup Algo;

    @FXML
    private RadioButton modifiedRadioButton;

    @FXML
    private RadioButton originalRadioButton;

    @FXML
    private CheckBox alphaFixedBox;
    
    @FXML
    private CheckBox batteryLifeCheckBox;

    @FXML
    private CheckBox randomTravelTimeCheckBox;

    @FXML
    private TextField alphaField;

    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

    @FXML
    private Button upButton;

    @FXML
    private Button downButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button playButton;

    @FXML
    private Button resetButton;

    @FXML
    private ImageView leftImage;

    @FXML
    private ImageView rightImage;

    @FXML
    private ImageView downImage;

    @FXML
    private ImageView upImage;

    @FXML
    private ImageView playIcon;

    @FXML
    private ImageView pauseIcon;

    @FXML
    private Label totalSteps;

    @FXML
    private Label totalRewards;

    @FXML
    private Label totalTravelTime;
    
    @FXML
    private Label remainingStepsLabel;

    @FXML
    private TextField greedyValue;

    @FXML
    private TextField speedValue;

    @FXML
    private TextField discountValue;

    @FXML
    private TextField directionProbability;

    @FXML
    private TextField defaultTravelTimeField;

    @FXML
    private ChoiceBox<Integer> rowBox;

    @FXML
    private ChoiceBox<Integer> colBox;

    @FXML
    private Canvas mapCanvas;

    @FXML
    private Label autorunStatus;

    @FXML
    private StackPane graphPane;

    @FXML
    private CheckBox tracing;

    @FXML
    private HBox root;
}
