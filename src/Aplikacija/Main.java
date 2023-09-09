package Aplikacija;

import Vozila.Vozilo;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    Integer startCounter = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage mainStage = new Stage();
        mainStage.setTitle("Border Crossing");
        mainStage.setHeight(700);
        mainStage.setWidth(1500);

        Stage redStage = new Stage();
        redStage.setTitle("Red cekanja");
        redStage.setHeight(500);
        redStage.setWidth(1100);

        BorderPane mainBP = new BorderPane();
        BorderPane redBP = new BorderPane();

        GridPane centerGP = new GridPane();
        GridPane redGP = new GridPane();

        Scene mainScene = new Scene(mainBP);
        Scene redScene = new Scene(redBP);

        HBox topHB = new HBox();
        topHB.setPadding(new Insets(10));
        topHB.setAlignment(Pos.TOP_RIGHT);
        topHB.setSpacing(500);

        Button startButton = new Button("START");
        ArrayList<Button> redVozilaMain = new ArrayList<Button>();
        ArrayList<Button> redVozilaRed = new ArrayList<Button>();
        ArrayList<Button> policijskiTerminali = new ArrayList<Button>();
        ArrayList<Button> carinskiTerminali = new ArrayList<Button>();

        Label naslov = new Label("BORDER CROSSING");
        naslov.setFont(new Font("Arial", 40));
        naslov.setTextFill(javafx.scene.paint.Paint.valueOf("#0f578a"));

        Label redNaslov = new Label(" Vozila u redu za cekanje:");
        redNaslov.setFont(new Font("Arial", 20));

        topHB.getChildren().addAll(naslov, startButton);
        mainBP.setTop(topHB);

        redBP.setTop(redNaslov);

        for(int i = 0; i<5; i++){
            redVozilaMain.add(new Button());
            redVozilaMain.get(i).setPrefHeight(30);
            redVozilaMain.get(i).setPrefWidth(30);
            centerGP.add(redVozilaMain.get(i), i+100, 30);
        }

        for(int i = 0; i < 45; i++){
            redVozilaRed.add(new Button());
            redVozilaRed.get(i).setPrefWidth(20);
            redVozilaRed.get(i).setPrefHeight(20);
            redGP.add(redVozilaRed.get(i), i+30, 50);
        }

        policijskiTerminali.add(new Button("PT"));
        policijskiTerminali.add(new Button("PT"));
        policijskiTerminali.add(new Button("PT"));

        carinskiTerminali.add(new Button("CT"));
        carinskiTerminali.add(new Button("CT"));

        for(int i = 0; i < 3; i++){
            policijskiTerminali.get(i).setPrefHeight(50);
            policijskiTerminali.get(i).setPrefWidth(50);
            policijskiTerminali.get(i).setStyle("-fx-background-color: steelblue");
        }

        carinskiTerminali.get(0).setPrefWidth(50);
        carinskiTerminali.get(0).setPrefHeight(50);
        carinskiTerminali.get(0).setStyle("-fx-background-color: moccasin");
        carinskiTerminali.get(1).setPrefWidth(50);
        carinskiTerminali.get(1).setPrefHeight(50);
        carinskiTerminali.get(1).setStyle("-fx-background-color: moccasin");

        centerGP.add(policijskiTerminali.get(0), 130, 20);
        centerGP.add(policijskiTerminali.get(1), 130, 30);
        centerGP.add(policijskiTerminali.get(2), 130, 40);

        centerGP.add(carinskiTerminali.get(0), 133, 25);
        centerGP.add(carinskiTerminali.get(1), 133, 35);

        Button red = new Button("RED CEKANJA");
        red.setPrefHeight(30);
        red.setPrefWidth(100);

        centerGP.add(red, 5, 30);

        Button incident = new Button("INCIDENT");
        incident.setPrefHeight(30);
        incident.setPrefWidth(100);

        centerGP.add(incident, 290, 30);

        centerGP.setVgap(3);
        centerGP.setHgap(2);
        centerGP.setPadding(new Insets(0, 150, 0, 150));
        mainBP.setCenter(centerGP);

        redGP.setVgap(3);
        redGP.setHgap(2);
        redGP.setPadding(new Insets(0, 0, 10, 10));
        redBP.setCenter(redGP);

        mainStage.setScene(mainScene);
        mainStage.show();

        Stage buttonStage = new Stage();
        BorderPane buttonBP = new BorderPane();
        VBox buttonVB = new VBox();
        Scene buttonScene = new Scene(buttonBP);

        startButton.setOnAction(e -> {
            Simulacija simulacija = new Simulacija(centerGP, redGP);
            if(startCounter == 0) {
                startButton.setText("STOP");
                startCounter = 1;
                simulacija.start();
            }
            else{
                startButton.setText("START");
                startCounter = 0;
            }
            red.setOnAction(f -> {
                redStage.setScene(redScene);
                redStage.show();
            });
            redVozilaMain.get(0).setOnAction(f -> {
                buttonVB.getChildren().clear();
                List<String> dijelovi = new ArrayList<>();
                for(Vozilo vozilo: simulacija.listaVozila){
                    if(vozilo.getPozicijaURedu() == 45){
                        String podaci = String.valueOf(vozilo);
                        dijelovi = Arrays.asList(podaci.split("-"));
                        for(String dio : dijelovi){
                            buttonVB.getChildren().add(new Label(dio));
                        }
                        break;
                    }
                }
                buttonBP.setCenter(buttonVB);
                buttonStage.setScene(buttonScene);
                buttonStage.show();
            });
            redVozilaMain.get(1).setOnAction(f -> {
                buttonVB.getChildren().clear();
                List<String> dijelovi = new ArrayList<>();
                for(Vozilo vozilo: simulacija.listaVozila){
                    if(vozilo.getPozicijaURedu() == 46){
                        String podaci = String.valueOf(vozilo);
                        dijelovi = Arrays.asList(podaci.split("-"));
                        for(String dio : dijelovi){
                            buttonVB.getChildren().add(new Label(dio));
                        }
                        break;
                    }
                }
                buttonBP.setCenter(buttonVB);
                buttonStage.setScene(buttonScene);
                buttonStage.show();
            });
            redVozilaMain.get(2).setOnAction(f -> {
                buttonVB.getChildren().clear();
                List<String> dijelovi = new ArrayList<>();
                for(Vozilo vozilo: simulacija.listaVozila){
                    if(vozilo.getPozicijaURedu() == 47){
                        String podaci = String.valueOf(vozilo);
                        dijelovi = Arrays.asList(podaci.split("-"));
                        for(String dio : dijelovi){
                            buttonVB.getChildren().add(new Label(dio));
                        }
                        break;
                    }
                }
                buttonBP.setCenter(buttonVB);
                buttonStage.setScene(buttonScene);
                buttonStage.show();
            });
            redVozilaMain.get(3).setOnAction(f -> {
                buttonVB.getChildren().clear();
                List<String> dijelovi = new ArrayList<>();
                for(Vozilo vozilo: simulacija.listaVozila){
                    if(vozilo.getPozicijaURedu() == 48){
                        String podaci = String.valueOf(vozilo);
                        dijelovi = Arrays.asList(podaci.split("-"));
                        for(String dio : dijelovi){
                            buttonVB.getChildren().add(new Label(dio));
                        }
                        break;
                    }
                }
                buttonBP.setCenter(buttonVB);
                buttonStage.setScene(buttonScene);
                buttonStage.show();
            });
            redVozilaMain.get(4).setOnAction(f -> {
                buttonVB.getChildren().clear();
                List<String> dijelovi = new ArrayList<>();
                synchronized (simulacija.listaVozila) {
                    for (Vozilo vozilo : simulacija.listaVozila) {
                        if (vozilo.getPozicijaURedu() == 49) {
                            String podaci = String.valueOf(vozilo);
                            dijelovi = Arrays.asList(podaci.split("-"));
                            for (String dio : dijelovi) {
                                buttonVB.getChildren().add(new Label(dio));
                            }
                            break;
                        }
                    }
                }
                buttonBP.setCenter(buttonVB);
                buttonStage.setScene(buttonScene);
                buttonStage.show();
            });
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
