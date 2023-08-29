package Aplikacija;

import Vozila.Vozilo;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Pomjerac extends Thread {
    private ArrayList<Vozilo> listaVozila = new ArrayList<>();
    private GridPane centarGP = new GridPane();
    private GridPane redGP = new GridPane();
    private Object[][] mapa;

    Integer counterCenter = 4;
    Integer counterRed = 44;
    Integer i;
    Integer prviProlazak = 0;

    public Pomjerac(ArrayList<Vozilo> listaVozila, GridPane centarGP, GridPane redGP, Object[][] mapa) {
        super();
        this.listaVozila = listaVozila;
        this.centarGP = centarGP;
        this.redGP = redGP;
        this.mapa = mapa;
    }

    @Override
    public void run() {
        ObservableList<Node> getChildrenNodeC = centarGP.getChildren();
        ArrayList<Button> centerButtons = new ArrayList<Button>();

        ObservableList<Node> getChildrenNodeR = redGP.getChildren();
        ArrayList<Button> redButtons = new ArrayList<>();

        while (true) {

            centerButtons.clear();
            redButtons.clear();
            for (Node node : getChildrenNodeC) {
                centerButtons.add((Button) node);
            }
            for (Node node : getChildrenNodeR) {
                redButtons.add((Button) node);
            }
            System.out.println("Duzina niza redButton: " + redButtons.size());
            System.out.println("Duzina niza centerButton: " + centerButtons.size());
            System.out.println("Duzina niza listaVozila: " + listaVozila.size());


//                for (i = listaVozila.size() - 1, counter = 4; i > 0; i--, counter--) {
//                    if (counter > 0) {
//                        Runnable updateRed = () -> {
//                            centerButtons.get(counter).setStyle("-fx-background-color: " + listaVozila.get(i).getBoja());
//                        };
//                        Platform.runLater(updateRed);
//                    }
//                    else {
//                        System.out.println("Vrijednost i: " + i);
//                        Runnable updateColor = () -> {
//                            centerButtons.get(5 - counter).setStyle("-fx-background-color: " + listaVozila.get(i).getBoja());
//                            if(prviProlazak == 0) {
//                                centerButtons.get(5 - counter - 1).setStyle("");
//                            }
//                        };
//                        Platform.runLater(updateColor);
//                    }
//                }
            synchronized (listaVozila) {
                i = listaVozila.size() - 1;
            }
            counterCenter = 4;
            counterRed = 44;
            System.out.println("I je " + i);
            while (i > -1) {
                System.out.println("i : " + i + " centar: " + counterCenter + " red: " + counterRed);
                if (counterCenter > -1) {
                    Runnable updateCenter = () -> {
                        synchronized (listaVozila) {
                            centerButtons.get(counterCenter).setStyle("-fx-background-color: " + listaVozila.get(i).getBoja());
                            if (counterCenter > 0) {
                                centerButtons.get(counterCenter - 1).setStyle("");
                            }
                        }
                    };
                    try {
                        sleep(79);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(updateCenter);
                    counterCenter--;
                } else if (counterRed > -1) {
                    Runnable updateRed = () -> {
                        synchronized (listaVozila) {
                            redButtons.get(counterRed).setStyle("-fx-background-color: " + listaVozila.get(i).getBoja());
                            if (counterRed > 0) {
                                redButtons.get(counterRed - 1).setStyle("");
                            }
                        }
                    };
                    try {
                        sleep(79);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(updateRed);
                    counterRed--;
                }
                i--;
            }


            try {
                sleep(13);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
