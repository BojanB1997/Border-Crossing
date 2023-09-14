package Aplikacija;

import Vozila.Putnik;
import Vozila.Vozilo;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

import static Aplikacija.Main.pauza;
import static Aplikacija.Main.pokrenut;
import static Aplikacija.Simulacija.mapa;

public class Pomjerac extends Thread {
    private ArrayList<Vozilo> listaVozila = new ArrayList<>();
    private GridPane centarGP = new GridPane();
    private GridPane redGP = new GridPane();
    //private Object[][] mapa;

    Integer counter = 0;
    Integer counterCenter = 4;
    Integer counterRed = 44;
    Integer i;
    Integer prviProlazak = 0;

    public static Object sinhro = new Object();

    public Pomjerac(ArrayList<Vozilo> listaVozila, GridPane centarGP, GridPane redGP, Object[][] mapa) {
        super();
        this.listaVozila = listaVozila;
        this.centarGP = centarGP;
        this.redGP = redGP;
        //this.mapa = mapa;
    }

    @Override
    public void run() {
        ObservableList<Node> getChildrenNodeC = centarGP.getChildren();
        ArrayList<Button> centerButtons = new ArrayList<Button>();

        ObservableList<Node> getChildrenNodeR = redGP.getChildren();
        ArrayList<Button> redButtons = new ArrayList<Button>();

        ArrayList<Button> dugmici = new ArrayList<Button>();

        while (!listaVozila.isEmpty()) {
            synchronized (pauza){
                if(!pokrenut){
                    try{
                        pauza.wait();
                    }catch(Exception e){
                        MojLogger.log(Level.SEVERE, "Pauza u pomjeracu", e);
                    }
                }
            }
            synchronized (listaVozila) {
                synchronized (sinhro) {
                    centerButtons.clear();
                    redButtons.clear();
                    dugmici.clear();
                    for (Node node : getChildrenNodeC) {
                        centerButtons.add((Button) node);
                    }
                    for (Node node : getChildrenNodeR) {
                        redButtons.add((Button) node);
                    }

                    dugmici.addAll(redButtons);
                    dugmici.addAll(centerButtons);

//                System.out.println("Duzina niza redButton: " + redButtons.size());
//                System.out.println("Duzina niza centerButton: " + centerButtons.size());
//                System.out.println("Duzina niza listaVozila: " + listaVozila.size());

//                synchronized (listaVozila) {
                    for (Button button : dugmici) {
                        Platform.runLater(() -> {
                            button.setStyle("");
                            button.setBorder(null);
                            button.setTextFill(Paint.valueOf("black"));
                        });
//                        try {
//                            sleep(1);
//                        } catch (InterruptedException e) {
//                            MojLogger.log(Level.SEVERE, "Spavanje  GUI threadu.", e);
//                        }
                    }
                    Platform.runLater(() -> {
                        if(dugmici.size() > 49) {
                            dugmici.get(50).setStyle("-fx-background-color: steelblue");
                            dugmici.get(51).setStyle("-fx-background-color: steelblue");
                            dugmici.get(52).setStyle("-fx-background-color: steelblue");

                            dugmici.get(53).setStyle("-fx-background-color: moccasin");
                            dugmici.get(54).setStyle("-fx-background-color: moccasin");
                        }
                    });
//                    try{
//                        sleep(50);
//                    }catch (InterruptedException e){
//                        MojLogger.log(Level.SEVERE, "Spavanje  GUI threadu.", e);
//                    }
                    i = listaVozila.size() - 1;
//                    System.out.println("");

                    counter = 49 - i;
                    counterCenter = 4;
                    counterRed = 44;
                    Integer j;
//                    System.out.println("I je " + i);
                    Platform.runLater(() -> {
                        for (i = listaVozila.size() - 1; i > -1; i--) {
                            if (listaVozila.get(i).getPozicijaURedu() > 49) {

                                if (listaVozila.get(i).getPozicijaURedu() == 51) {
                                    //Platform.runLater(() -> {
                                    dugmici.get(53).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
                                    dugmici.get(53).setBorder(new Border(new BorderStroke(Color.web(listaVozila.get(i).getBoja()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                                    //dugmici.get(50).setBorder(null);
                                    //dugmici.get(50).setTextFill(Paint.valueOf("black"));
                                    // });
                                } else if (listaVozila.get(i).getPozicijaURedu() == 53) {
                                    //Platform.runLater(() -> {
                                    dugmici.get(54).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
                                    dugmici.get(54).setBorder(new Border(new BorderStroke(Color.web(listaVozila.get(i).getBoja()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                                    //dugmici.get(52).setBorder(null);
                                    //dugmici.get(52).setTextFill(Paint.valueOf("black"));
                                    // });
                                    System.out.println("ZajebavM TE AL NE BOJIM");
                                } else if (listaVozila.get(i).getPozicijaURedu() == 55) {
                                    //Platform.runLater(() -> {
                                    dugmici.get(53).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
                                    dugmici.get(53).setBorder(new Border(new BorderStroke(Color.web(listaVozila.get(i).getBoja()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                                    //dugmici.get(52).setBorder(null);
                                    //dugmici.get(52).setTextFill(Paint.valueOf("black"));
                                    //});
                                } else if (listaVozila.get(i).getPozicijaURedu() == 50) {
                                    // Platform.runLater(() -> {
                                    dugmici.get(50).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
                                    dugmici.get(50).setBorder(new Border(new BorderStroke(Color.web(listaVozila.get(i).getBoja()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                                    //dugmici.get(49).setStyle("");
                                    //});
                                } else if (listaVozila.get(i).getPozicijaURedu() == 52) {
                                    //Platform.runLater(() -> {
                                    dugmici.get(51).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
                                    dugmici.get(51).setBorder(new Border(new BorderStroke(Color.web(listaVozila.get(i).getBoja()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                                    //dugmici.get(49).setStyle("");
                                    //});
                                } else if (listaVozila.get(i).getPozicijaURedu() == 54) {
                                    //Platform.runLater(() -> {
                                    dugmici.get(52).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
                                    dugmici.get(52).setBorder(new Border(new BorderStroke(Color.web(listaVozila.get(i).getBoja()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                                    //dugmici.get(49).setStyle("");
                                    //});
                                }
//                            try {
//                                Thread.sleep(5);
//                            } catch (InterruptedException e) {
//                                MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
//                            }
                            } else {
//                            System.out.println("I + counter = " + (i + counter));
                                //Platform.runLater(() -> {
                                dugmici.get(listaVozila.get(i).getPozicijaURedu()).setStyle("-fx-background-color: " + listaVozila.get(i).getBoja());
                                //});
//                            try {
//                                Thread.sleep(5);
//                            } catch (InterruptedException e) {
//                                MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
//                            }
                            }


//                        try {
//                            Thread.sleep(20);
//                        } catch (InterruptedException e) {
//                            MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
//                        }
                            //i--;
//                        try {
//                            Thread.sleep(30);
//                        } catch (InterruptedException e) {
//                            MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
//                        }
                        }
                    });
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
            }
        }
    }
}