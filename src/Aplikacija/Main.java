package Aplikacija;

import Vozila.Vozilo;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class Main extends Application {

    Integer startCounter = 0;
    public static Boolean pokrenut = false;
    public static Object pauza = new Object();

    @Override
    public void start(Stage primaryStage) throws Exception {
        MojLogger.setup();

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

        for (int i = 0; i < 5; i++) {
            redVozilaMain.add(new Button());
            redVozilaMain.get(i).setPrefHeight(30);
            redVozilaMain.get(i).setPrefWidth(30);
            centerGP.add(redVozilaMain.get(i), i + 100, 30);
        }

        for (int i = 0; i < 45; i++) {
            redVozilaRed.add(new Button());
            redVozilaRed.get(i).setPrefWidth(20);
            redVozilaRed.get(i).setPrefHeight(20);
            redGP.add(redVozilaRed.get(i), i + 30, 50);
        }

        policijskiTerminali.add(new Button("PT"));
        policijskiTerminali.add(new Button("PT"));
        policijskiTerminali.add(new Button("PT"));

        carinskiTerminali.add(new Button("CT"));
        carinskiTerminali.add(new Button("CT"));

        for (int i = 0; i < 3; i++) {
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


        HBox spacer = new HBox();
        spacer.setHgrow(spacer, Priority.ALWAYS);

        HBox dnoHB = new HBox(10);

        Label vrijeme = new Label("Vrijeme: ");
        vrijeme.setFont(new Font("Arial", 14));
        vrijeme.setAlignment(Pos.CENTER_RIGHT);
        vrijeme.setPadding(new Insets(0, 50, 10, 0));

        VBox opisVB = new VBox(10);
        opisVB.setAlignment(Pos.CENTER);
        Label opis = new Label("Opis dogadjaja:");
        opis.setFont(new Font("Arial", 14));
        Label opisDogadjaja = new Label("Test.");
        opisDogadjaja.setFont(new Font("Arial", 14));
        opisVB.getChildren().addAll(opis, opisDogadjaja);
        opisVB.setPadding(new Insets(0, 0, 10, 650));

        opisVB.setAlignment(Pos.CENTER);
        //BorderPane.setAlignment(opisVB, Pos.CENTER);
        //BorderPane.setAlignment(dnoHB, Pos.CENTER);

        dnoHB.getChildren().addAll(opisVB, spacer, vrijeme);
        dnoHB.setAlignment(Pos.CENTER);

        mainBP.setBottom(dnoHB);


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
            startCounter = startCounter + 1;
            pokrenut = !pokrenut;
            startButton.setText("STOP");
            if (startCounter == 1) {
                Simulacija simulacija = new Simulacija(centerGP, redGP, opisDogadjaja);
                Tajmer tajmer = new Tajmer(vrijeme);
                startButton.setText("STOP");
                startCounter = 1;
                simulacija.start();
                tajmer.start();
                red.setOnAction(f -> {
                    redStage.setScene(redScene);
                    redStage.show();
                });
            } else {
                if(startCounter % 2 == 0) {
                    startButton.setText("START");
                }else{
                    startButton.setText("STOP");
                }
                synchronized (pauza) {
                    if (pokrenut) {
                        pauza.notifyAll();
                    }
                }
            }
            incident.setOnAction(g -> {
                String currentDir = System.getProperty("user.dir");
                String projectPath = File.separator + "src" + File.separator + "Datoteke" + File.separator;

                ArrayList<String> incidentNisuProsli = new ArrayList<>();
                ArrayList<String> incidentProsliSu = new ArrayList<>();

                try{
                    FileReader read = new FileReader(currentDir + projectPath + "pt1.txt");
                    BufferedReader readBuff = new BufferedReader(read);
                    String line1, line2;
                    while (((line1 = readBuff.readLine()) != null) && ((line2 = readBuff.readLine()) != null)) {
                        String[] linija = line1.split("-");
                        if("pv".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem vozaca sa dokumentima.");
                            incidentNisuProsli.add(line2);
                        }else if("kv".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem vozaca sa koferom.");
                            incidentNisuProsli.add(line2);
                        }else if("m".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem kamiona sa masom.");
                            incidentNisuProsli.add(line2);
                        }else if("p".equalsIgnoreCase(linija[0])){
                            incidentProsliSu.add("Problem putnika sa dokumentima.");
                            incidentProsliSu.add(line2);
                        }else if("k".equalsIgnoreCase(linija[0])){
                            incidentProsliSu.add("Problem putnika sa koferom.");
                            incidentProsliSu.add(line2);
                        }
                    }

                }catch(Exception b){
                    MojLogger.log(Level.SEVERE, "Greska prilikom otvaranja pt1.txt datoteke u mainu za incident.", b);
                }
                try{
                    FileReader read = new FileReader(currentDir + projectPath + "pt2.txt");
                    BufferedReader readBuff = new BufferedReader(read);
                    String line1, line2;
                    while (((line1 = readBuff.readLine()) != null) && ((line2 = readBuff.readLine()) != null)) {
                        String[] linija = line1.split("-");
                        if("pv".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem vozaca sa dokumentima.");
                            incidentNisuProsli.add(line2);
                        }else if("kv".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem vozaca sa koferom.");
                            incidentNisuProsli.add(line2);
                        }else if("m".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem kamiona sa masom.");
                            incidentNisuProsli.add(line2);
                        }else if("p".equalsIgnoreCase(linija[0])){
                            incidentProsliSu.add("Problem putnika sa dokumentima.");
                            incidentProsliSu.add(line2);
                        }else if("k".equalsIgnoreCase(linija[0])){
                            incidentProsliSu.add("Problem putnika sa koferom.");
                            incidentProsliSu.add(line2);
                        }
                    }

                }catch(Exception b){
                    MojLogger.log(Level.SEVERE, "Greska prilikom otvaranja pt1.txt datoteke u mainu za incident.", b);
                }
                try{
                    FileReader read = new FileReader(currentDir + projectPath + "pt3.txt");
                    BufferedReader readBuff = new BufferedReader(read);
                    String line1, line2;
                    while (((line1 = readBuff.readLine()) != null) && ((line2 = readBuff.readLine()) != null)) {
                        String[] linija = line1.split("-");
                        if("pv".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem vozaca sa dokumentima.");
                            incidentNisuProsli.add(line2);
                        }else if("kv".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem vozaca sa koferom.");
                            incidentNisuProsli.add(line2);
                        }else if("m".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem kamiona sa masom.");
                            incidentNisuProsli.add(line2);
                        }else if("p".equalsIgnoreCase(linija[0])){
                            incidentProsliSu.add("Problem putnika sa dokumentima.");
                            incidentProsliSu.add(line2);
                        }else if("k".equalsIgnoreCase(linija[0])){
                            incidentProsliSu.add("Problem putnika sa koferom.");
                            incidentProsliSu.add(line2);
                        }
                    }

                }catch(Exception b){
                    MojLogger.log(Level.SEVERE, "Greska prilikom otvaranja pt1.txt datoteke u mainu za incident.", b);
                }
                try{
                    FileReader read = new FileReader(currentDir + projectPath + "ct1.txt");
                    BufferedReader readBuff = new BufferedReader(read);
                    String line1, line2;
                    while (((line1 = readBuff.readLine()) != null) && ((line2 = readBuff.readLine()) != null)) {
                        String[] linija = line1.split("-");
                        if("pv".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem vozaca sa dokumentima.");
                            incidentNisuProsli.add(line2);
                        }else if("kv".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem vozaca sa koferom.");
                            incidentNisuProsli.add(line2);
                        }else if("m".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem kamiona sa masom.");
                            incidentNisuProsli.add(line2);
                        }else if("p".equalsIgnoreCase(linija[0])){
                            incidentProsliSu.add("Problem putnika sa dokumentima.");
                            incidentProsliSu.add(line2);
                        }else if("k".equalsIgnoreCase(linija[0])){
                            incidentProsliSu.add("Problem putnika sa koferom.");
                            incidentProsliSu.add(line2);
                        }
                    }

                }catch(Exception b){
                    MojLogger.log(Level.SEVERE, "Greska prilikom otvaranja pt1.txt datoteke u mainu za incident.", b);
                }
                try{
                    FileReader read = new FileReader(currentDir + projectPath + "ct2.txt");
                    BufferedReader readBuff = new BufferedReader(read);
                    String line1, line2;
                    while (((line1 = readBuff.readLine()) != null) && ((line2 = readBuff.readLine()) != null)) {
                        String[] linija = line1.split("-");
                        if("pv".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem vozaca sa dokumentima.");
                            incidentNisuProsli.add(line2);
                        }else if("kv".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem vozaca sa koferom.");
                            incidentNisuProsli.add(line2);
                        }else if("m".equalsIgnoreCase(linija[0])){
                            incidentNisuProsli.add("Problem kamiona sa masom.");
                            incidentNisuProsli.add(line2);
                        }else if("p".equalsIgnoreCase(linija[0])){
                            incidentProsliSu.add("Problem putnika sa dokumentima.");
                            incidentProsliSu.add(line2);
                        }else if("k".equalsIgnoreCase(linija[0])){
                            incidentProsliSu.add("Problem putnika sa koferom.");
                            incidentProsliSu.add(line2);
                        }
                    }

                }catch(Exception b){
                    MojLogger.log(Level.SEVERE, "Greska prilikom otvaranja pt1.txt datoteke u mainu za incident.", b);
                }

                HBox incidentHB = new HBox();
                VBox incident1VB = new VBox();
                VBox incident2VB = new VBox();
                BorderPane incidentBP = new BorderPane();

                Label prosli = new Label("USPJESNO PROSLI");
                prosli.setFont(new Font("Ariel", 17));
                incident1VB.getChildren().add(prosli);

                Label nisuProsli = new Label("NISU PROSLI");
                nisuProsli.setFont(new Font("Arial", 17));
                incident2VB.getChildren().add(nisuProsli);

                for(String linija : incidentProsliSu){
                    incident1VB.getChildren().add(new Label(linija));
                }
                incidentProsliSu.clear();

                for(String linija : incidentNisuProsli){
                    incident2VB.getChildren().add(new Label(linija));
                }
                incidentNisuProsli.clear();

                ScrollPane scp1 = new ScrollPane(incident1VB);
                scp1.setFitToWidth(true);
                scp1.setPrefWidth(500);
                ScrollPane scp2 = new ScrollPane(incident2VB);
                scp2.setFitToWidth(true);
                scp2.setPrefWidth(500);
                incidentHB.getChildren().addAll(scp1, scp2);
                incidentHB.setPadding(new Insets(0, 0, 0, 45));

                incidentBP.setCenter(incidentHB);

                Scene incidentScene = new Scene(incidentBP);
                Stage incidentStage = new Stage();
                incidentStage.setHeight(500);
                incidentStage.setWidth(1100);
                incidentStage.setScene(incidentScene);
                incidentStage.show();
            });
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
