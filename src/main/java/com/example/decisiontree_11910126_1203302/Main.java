package com.example.decisiontree_11910126_1203302;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import static com.example.decisiontree_11910126_1203302.Controller.fileReader;
import static com.example.decisiontree_11910126_1203302.Controller.printTree;

public class Main extends Application {

    Data data;
    Stage stage;
    Data trainData;
    Data testData;
    Feature target;
    DecisionTree decisionTree;
    Map<String, Double> ev;
    Map<String, Double> confusion;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        Button load = new Button("Load Data");
        Button infoGain = new Button("Split by Information Gain");
        Button gainRatio = new Button("Split by Gain Ratio");


        load.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 10px 20px;" + "-fx-border-color: #BDBDBD;" +
                        "-fx-background-color: #795695;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;"
        );

        infoGain.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 8px;" +
                        "-fx-border-color: #BDBDBD;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: #FFFFFF;"
        );

        gainRatio.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 8px;" +
                        "-fx-border-color: #BDBDBD;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: #FFFFFF;"
        );


        TextField trainPerc = new TextField();
        trainPerc.setPromptText("training data percentage 0-100");
        trainPerc.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 8px;" +
                        "-fx-border-color: #BDBDBD;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: #FFFFFF;"
        );

        TextField depth = new TextField();
        depth.setPromptText("Max Depth");
        depth.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 8px;" +
                        "-fx-border-color: #BDBDBD;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: #FFFFFF;"
        );


        ComboBox<String> pos = new ComboBox<>();
        pos.getItems().addAll("EDIBLE","POISONOUS");
        pos.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 8px;" +
                        "-fx-border-color: #BDBDBD;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: #FFFFFF;"
        );

        HBox hb = new HBox(10);
        hb.setPadding(new Insets(10));
        hb.getChildren().addAll(infoGain, gainRatio);
        hb.setAlignment(Pos.CENTER);

        HBox hb2 = new HBox(10);
        hb2.setPadding(new Insets(10));
        hb2.getChildren().addAll(trainPerc, depth,pos);
        hb2.setAlignment(Pos.CENTER);


        TextArea textarea = new TextArea();
        textarea.isResizable();
        textarea.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 10px;" +
                        "-fx-border-color: #BDBDBD;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: #FFFFFF;"
        );

        TextArea textarea2 = new TextArea();
        textarea2.isResizable();
        textarea2.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 10px;" +
                        "-fx-border-color: #BDBDBD;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-color: #FFFFFF;"
        );

        HBox hb3 = new HBox();
        hb3.getChildren().addAll(textarea, textarea2);

        VBox vb = new VBox(10);
        vb.setPadding(new Insets(10));
        vb.getChildren().addAll(load, hb2, hb);
        vb.setAlignment(Pos.CENTER);

        BorderPane b = new BorderPane();
        b.setTop(vb);
        b.setCenter(hb3);

        Scene scene = new Scene(b, 1000, 600);
        stage.setScene(scene);
        stage.show();


        load.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(stage);
                data = fileReader(file.getAbsolutePath());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        infoGain.setOnAction(e -> {
            Node root = build(Integer.parseInt(trainPerc.getText()), new InformationGain(), Integer.parseInt(depth.getText()), pos.getValue().toString());
            textarea.setText(printTree(root, 0));
            int totalSize = data.getRows().size();
            int trainSize = (int) (totalSize * ((double) Integer.parseInt(trainPerc.getText()) / 100));
            int testSize = totalSize - trainSize;

            String s = testSize + "\n";
            for (Map.Entry<String, Double> entry : ev.entrySet()) {
                s += entry.getKey() + ": " + entry.getValue() + "\n";
            }

            s+= "------Confusion Matrix------" + "\n";

            for (Map.Entry<String, Double> entry : confusion.entrySet()) {
                s += entry.getKey() + ": " + entry.getValue() + "\n";
            }

            textarea2.setText(s);

        });

        gainRatio.setOnAction(e -> {
            Node root = build(Integer.parseInt(trainPerc.getText()), new GainRatio(), Integer.parseInt(depth.getText()), pos.getValue().toString());
            textarea.setText(printTree(root, 0));
            int totalSize = data.getRows().size();
            int trainSize = (int) (totalSize * ((double) Integer.parseInt(trainPerc.getText()) / 100));
            int testSize = totalSize - trainSize;

            String s = testSize + "\n";
            for (Map.Entry<String, Double> entry : ev.entrySet()) {
                s += entry.getKey() + ": " + entry.getValue() + "\n";
            }

            s+= "------Confusion Matrix------" + "\n";

            for (Map.Entry<String, Double> entry : confusion.entrySet()) {
                s += entry.getKey() + ": " + entry.getValue() + "\n";
            }
            textarea2.setText(s);

        });


    }

    Node build(int sizeData, SplitMethod splitMethod, int depth, String pos) {
        int totalSize = data.getRows().size();
        int trainSize = (int) (totalSize * ((double) sizeData / 100));

        Random r = new Random(42);
        Collections.shuffle(data.getRows(), r);

        ArrayList<String[]> trainRows = new ArrayList<>(data.getRows().subList(0, trainSize));
        ArrayList<String[]> testRows = new ArrayList<>(data.getRows().subList(trainSize, totalSize));

        trainData = new Data(data.getColumns(), trainRows);
        testData = new Data(data.getColumns(), testRows);
        target = new Feature("EDIBLE");

        decisionTree = new DecisionTree(trainData, target, splitMethod);
        decisionTree.maxDepth = depth;
        decisionTree.positiveClass = pos;

        Node root = decisionTree.build(trainData, null, 0);
        ev = decisionTree.evaluate(testData, root, target);
        confusion = decisionTree.confusion;
        return root;

    }
}
