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
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class Pomjerac extends Thread {
    private ArrayList<Vozilo> listaVozila = new ArrayList<>();
    private GridPane centarGP = new GridPane();
    private GridPane redGP = new GridPane();
    private Object[][] mapa;

    Integer counter = 0;
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
        ArrayList<Button> redButtons = new ArrayList<Button>();

        ArrayList<Button> dugmici = new ArrayList<Button>();

        while (true) {

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

            System.out.println("Duzina niza redButton: " + redButtons.size());
            System.out.println("Duzina niza centerButton: " + centerButtons.size());
            System.out.println("Duzina niza listaVozila: " + listaVozila.size());

            synchronized (listaVozila) {
                i = listaVozila.size() - 1;
            }

            counter = 49 - i;
            counterCenter = 4;
            counterRed = 44;
            System.out.println("I je " + i);

            while (i > -1) {
                synchronized (listaVozila) {
//                    if (listaVozila.get(i).getPozicijaURedu() > 49) {
//                        Runnable update = () -> {
//                            if (listaVozila.get(i).getPozicijaURedu() == 50) {
//                                dugmici.get(52).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
//                                dugmici.get(49).setStyle("");
//                            } else if (listaVozila.get(i).getPozicijaURedu() == 53) {
//                                dugmici.get(50).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
//                                dugmici.get(49).setStyle("");
//                            } else if (listaVozila.get(i).getPozicijaURedu() == 52) {
//                                dugmici.get(51).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
//                                dugmici.get(49).setStyle("");
//                            } else if (listaVozila.get(i).getPozicijaURedu() == 51) {
//                                dugmici.get(54).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
//                                dugmici.get(52).setTextFill(Paint.valueOf(""));
//                            } else if (listaVozila.get(i).getPozicijaURedu() == 54) {
//                                dugmici.get(53).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
//                                dugmici.get(51).setTextFill(Paint.valueOf(""));
//                            }else if(listaVozila.get(i).getPozicijaURedu() == 55){
//                                dugmici.get(53).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
//                                dugmici.get(50).setTextFill(Paint.valueOf(""));
//                            }
//                        };
//                        Platform.runLater(update);
//                    }
                    try {
                        sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("I + counter = " + (i + counter));
                    Runnable update = () -> {
                        dugmici.get(i + counter).setStyle("-fx-background-color: " + listaVozila.get(i).getBoja());
                    };

                    Platform.runLater(update);

                }
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;

            }
            while (counter > 0) {
                Runnable clear = () -> {
                    dugmici.get(counter - 1).setStyle("");
                };
                Platform.runLater(clear);
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter--;
            }
            try {
                sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
