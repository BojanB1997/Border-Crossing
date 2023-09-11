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
import java.util.logging.Level;

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

        while (!listaVozila.isEmpty()) {

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
                System.out.println("");

                counter = 49 - i;
                counterCenter = 4;
                counterRed = 44;
                System.out.println("I je " + i);

                while (i > -1) {
                    synchronized (listaVozila) {
//                        try {
//                            sleep(15);
//                        } catch (InterruptedException e) {
//                            MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
//                        }
                    if (listaVozila.get(i).getPozicijaURedu() > 49) {
                        Runnable update = () -> {
                            if (listaVozila.get(i).getPozicijaURedu() == 51) {
                                dugmici.get(54).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
                                dugmici.get(54).setBorder(new Border(new BorderStroke(Color.web(listaVozila.get(i).getBoja()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                                dugmici.get(52).setBorder(null);
                                dugmici.get(52).setTextFill(null);
                            } else if (listaVozila.get(i).getPozicijaURedu() == 54) {
                                dugmici.get(53).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
                                dugmici.get(53).setBorder(new Border(new BorderStroke(Color.web(listaVozila.get(i).getBoja()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                                dugmici.get(51).setBorder(null);
                                dugmici.get(51).setTextFill(null);
                            }else if(listaVozila.get(i).getPozicijaURedu() == 55) {
                                dugmici.get(53).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
                                dugmici.get(53).setBorder(new Border(new BorderStroke(Color.web(listaVozila.get(i).getBoja()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                                dugmici.get(50).setBorder(null);
                                dugmici.get(50).setTextFill(null);
                            } else if (listaVozila.get(i).getPozicijaURedu() == 50) {
                                dugmici.get(52).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
                                dugmici.get(52).setBorder(new Border(new BorderStroke(Color.web(listaVozila.get(i).getBoja()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                                //dugmici.get(49).setStyle(null);
                            } else if (listaVozila.get(i).getPozicijaURedu() == 53) {
                                dugmici.get(50).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
                                dugmici.get(50).setBorder(new Border(new BorderStroke(Color.web(listaVozila.get(i).getBoja()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                                //dugmici.get(49).setStyle(null);
                            } else if (listaVozila.get(i).getPozicijaURedu() == 52) {
                                dugmici.get(51).setTextFill(Paint.valueOf(listaVozila.get(i).getBoja()));
                                dugmici.get(51).setBorder(new Border(new BorderStroke(Color.web(listaVozila.get(i).getBoja()), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
                                //dugmici.get(49).setStyle(null);
                            }
                        };
                        Platform.runLater(update);
                    }

//                        try {
//                            sleep(15);
//                        } catch (InterruptedException e) {
//                            MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
//                        }

                        //System.out.println("I + counter = " + (i + counter));
                        Runnable update = () -> {
                            dugmici.get(i + counter).setStyle("-fx-background-color: " + listaVozila.get(i).getBoja());
                        };

                        Platform.runLater(update);


                        try {
                            sleep(10);
                        } catch (InterruptedException e) {
                            MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
                        }
                        i--;
//                        try {
//                            sleep(10);
//                        } catch (InterruptedException e) {
//                            MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
//                        }
                    }
                }
//                try {
//                    sleep(11);
//                } catch (InterruptedException e) {
//                    MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
//                }
                while (counter > 0) {
                    Runnable clear = () -> {
                        dugmici.get(counter - 1).setStyle("");
                    };
                    Platform.runLater(clear);
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
                    }
                    counter--;
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
                    }
                }
            }
            try {
                sleep(300);
            } catch (InterruptedException e) {
                MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Pomjerac.", e);
            }
        }
    }
}
