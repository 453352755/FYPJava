package QLearning;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class GridController {

    private boolean play;
    private static Thread t;

    private final QLearnAlgo algo = new QLearnAlgo(new GridWorld(10, 10));
    private GridPane grid;
    private GridPane selectedLocation;
    private TitledPane detail;
    private DetailController detailCtrl;

    public void setGridWorld(int row, int col) {
        algo.gw = new GridWorld(row, col);
        System.out.println("New grid world set");
        System.out.format("Rows: %d, Cols: %d", row, col);
    }

    public void initialize() {
//        mapCanvas.setHeight(640);
//        mapCanvas.setWidth(640);
//        gc = mapCanvas.getGraphicsContext2D();
//        drawtest();
//        graphPane.getChildren().remove(0);
        gridPaneInit();
        Label test = new Label();
        test.setText("test");
        test.setStyle("-fx-text-fill:#FFEB3B;-fx-font-size:40;");

        graphPane.getChildren().add(grid);
        rowBox.setItems(FXCollections.observableArrayList(
                10, 15, 20)
        );
        colBox.setItems(FXCollections.observableArrayList(
                10, 15, 20)
        );
        rowBox.setValue(10);
        colBox.setValue(10);
        
        //initialize deatail pane
        FXMLLoader detailLoader = new FXMLLoader();
        detailLoader.setLocation(getClass().getClassLoader().getResource("resource/detail.fxml"));
        
        try {
            detail = (TitledPane) detailLoader.load();
            detailCtrl = detailLoader.getController();
            System.out.println("Detail loaded");
        } catch (IOException ex) {
            Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Load detail fxml failed");
        }

        root.widthProperty().addListener((ObservableValue<? extends Number> ov, Number o, Number n) -> {
            if (o.doubleValue() < 1280 && n.doubleValue() >= 1280) {
                System.out.println("GUI expanded, showing node detail");
                if (selectedLocation != null) {
                    detailCtrl.location.getChildren().add(selectedLocation);
                }
                root.getChildren().add(root.getChildren().size(), detail);
            } else if (o.doubleValue() >= 1280 && n.doubleValue() < 1280) {
                if (root.getChildren().contains(detail)) {
                    root.getChildren().remove(detail);
                }
            }
        });

        repaintAll();
    }

    public void gridPaneInit() {
        grid = new GridPane();
        grid.setPrefSize(640, 640);
        grid.setMaxSize(1000, 1000);
        grid.setStyle("-fx-background-color: #C0C0C0;");
        grid.setGridLinesVisible(true);
        grid.setAlignment(Pos.CENTER);
        grid.setCenterShape(true);

//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getClassLoader().getResource("QLearning/location.fxml"));
//        System.out.println("Loading FXML from ");
//        System.out.println(loader.getLocation());
//        try {
//            location = (GridPane) loader.load();
//        } catch (Exception e) {
//            System.out.println("location fxml load failed");
//        }
        for (int i = 0; i < algo.gw.getRows(); i++) {
            for (int j = 0; j < algo.gw.getCols(); j++) {
                GridPane location = Location.newNode();
                location.setOnMouseClicked((MouseEvent t) -> {
                    System.out.print("mouse clicked: row ");
                    selectedLocation = (GridPane) t.getSource();
                    System.out.print(GridPane.getRowIndex(selectedLocation));
                    System.out.print(" col ");
                    System.out.println(GridPane.getColumnIndex(selectedLocation));
                    detailCtrl.location.getChildren().add(selectedLocation);
                });
                grid.add(location, i, j);

                ColumnConstraints col = new ColumnConstraints();
                //col.setPercentWidth(100 / gw.getCols());
                //col.setHgrow(Priority.ALWAYS);
                grid.getColumnConstraints().add(col);

                RowConstraints row = new RowConstraints();
                //row.setPercentHeight(100 / gw.getRows());
                //row.setVgrow(Priority.ALWAYS);
                grid.getRowConstraints().add(row);

            }
        }
        System.out.println("location loaded");

    }

    public void paint(GridPane loc) {
        int row = GridPane.getRowIndex(loc);
        int col = GridPane.getColumnIndex(loc);
        algo.gw.location[row][col].updateQ(loc);
        for (int i = 0; i < loc.getChildren().size(); i++) {
            try {
                Circle c = (Circle) loc.getChildren().get(i);
                if (row == algo.gw.curRow && col == algo.gw.curCol) {
                    c.setVisible(true);
                } else {
                    c.setVisible(false);
                }
            } catch (Exception e) {
            }
        }

    }

    public void repaintAll() {
        //System.out.print("Repainting...");
        GridPane loc;
        for (int i = 0; i < algo.gw.getRows(); i++) {
            for (int j = 0; j < algo.gw.getCols(); j++) {
                loc = (GridPane) getNode(i, j, grid);
                paint(loc);
            }
        }

        //System.out.println("finished");
    }

    void updatePerformance() {
        double r = algo.gw.totalReward;
        int s = algo.gw.numberOfSteps;
        totalRewards.setText(String.valueOf(r));
        totalSteps.setText(String.valueOf(s));
    }

    public Node getNode(final int row, final int column, GridPane gridPane) {
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

    void setReward(int row, int col, double reward) {
        algo.gw.location[row][col].reward = reward;
        System.out.print("New reward set to ");
        System.out.println(reward);
    }

////////////////////////////////////////////////////////////////////////////////
    @FXML
    void moveLeft(ActionEvent event) {
        algo.dostep(GridWorld.Left);
        repaintAll();
        updatePerformance();
        System.out.println("move left");
    }

    @FXML
    void moveRight(ActionEvent event) {
        algo.dostep(GridWorld.Right);
        repaintAll();
        updatePerformance();
        System.out.println("move right");
    }

    @FXML
    void moveUp(ActionEvent event) {
        algo.dostep(GridWorld.Up);
        repaintAll();
        updatePerformance();
        System.out.println("move up");
    }

    @FXML
    void moveDown(ActionEvent event) {
        algo.dostep(GridWorld.Down);
        repaintAll();
        updatePerformance();
        System.out.println("move down");
    }

    @FXML
    void updateDiscountValue(ActionEvent event) {
        double dv = Double.parseDouble(discountValue.getText());
        algo.discount = dv;

        System.out.println("discount value = ");
        System.out.println(dv);

    }

    @FXML
    void updateGreedyValue(ActionEvent event) {
        double gv = Double.parseDouble(greedyValue.getText());
        algo.greedyProb = gv;

        System.out.println("greedy value = ");
        System.out.println(gv);

    }

    @FXML
    void reset(ActionEvent event) {
        System.out.println("reset");
        autorunStatus.setText("Play to start auto run");
        totalSteps.setText(null);
        totalRewards.setText(null);
        int row, col;
        try {
            row = rowBox.getValue();
            col = colBox.getValue();
        } catch (NullPointerException e) {
            row = 10;
            col = 10;
        }
        setGridWorld(row, col);
        repaintAll();
        updatePerformance();
    }

    @FXML
    void updateSpeedValue(ActionEvent event) {
        double sv = Double.parseDouble(speedValue.getText());

        System.out.println("speed value = ");
        System.out.println(sv);

    }

    private class RunMap implements Runnable {

        @Override
        public void run() {
            System.out.println("run started");
            int interval;
            try {
                interval = 1000 / (int) Double.parseDouble(speedValue.getText());
            } catch (Exception e) {
                interval = 1000;
            }

            while (play) {
                try {
                    algo.doSteps(1);
                    if (interval > 100) {
                        long start = System.nanoTime();
                        repaintAll();
                        long end = System.nanoTime();
                        long us = (end - start) / 1000;
                        //System.out.println(us);
                        //System.out.println("Map running");
                    }
                    Thread.sleep(interval);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GridController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    @FXML
    void playClicked(ActionEvent event) {
        System.out.println("play button clicked");
        upButton.setDisable(true);
        downButton.setDisable(true);
        leftButton.setDisable(true);
        rightButton.setDisable(true);
        discountValue.setDisable(true);
        greedyValue.setDisable(true);
        speedValue.setDisable(true);
        //playButton.setDisable(true);
        resetButton.setDisable(true);
        autorunStatus.setText("Auto running");

        //run simulator
        play = true;
        t = new Thread(new RunMap());
        t.start();

    }

    @FXML
    void pauseClicked(ActionEvent event) {
        System.out.println("pause button clicked");
        upButton.setDisable(false);
        downButton.setDisable(false);
        leftButton.setDisable(false);
        rightButton.setDisable(false);
        discountValue.setDisable(false);
        greedyValue.setDisable(false);
        speedValue.setDisable(false);
        //playButton.setDisable(false);
        resetButton.setDisable(false);
        autorunStatus.setText("Paused, single step");

        play = false;
        try {
            t.join();
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
    void tracingChecked(ActionEvent event) {
        if (tracing.isSelected()) {
            System.out.println("Tracing started");
            algo.tracing = true;
        } else {
            System.out.println("Tracing Stopped");
            algo.tracing = false;
        }
    }

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
    private TextField greedyValue;
    @FXML
    private TextField speedValue;
    @FXML
    private TextField discountValue;
    @FXML
    private ChoiceBox<Integer> rowBox;
    @FXML
    private ChoiceBox<Integer> colBox;

    @FXML
    private Slider brightness;
    @FXML
    private Canvas mapCanvas;
    @FXML
    private Label brightnessValue;
    @FXML
    private Label autorunStatus;

    @FXML
    private StackPane graphPane;

    @FXML
    private CheckBox tracing;
    @FXML
    private HBox root;
}
