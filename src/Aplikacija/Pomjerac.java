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

public class Pomjerac extends Thread{
    private ArrayList<Vozilo> listaVozila = new ArrayList<>();
    private GridPane centarGP = new GridPane();
    private GridPane redGP = new GridPane();
    private Object[][] mapa;

    public Color bojaAuta = Color.GREENYELLOW;
    public Color bojaKamiona = Color.ROSYBROWN;
    public Color bojaAutobusa = Color.HOTPINK;

    public Pomjerac(ArrayList<Vozilo> listaVozila, GridPane centarGP, GridPane redGP, Object[][] mapa){
        super();
        this.listaVozila = listaVozila;
        this.centarGP = centarGP;
        this.redGP = redGP;
        this.mapa = mapa;
    }

    @Override
    public void run(){
        Integer counter = 0;
        ObservableList<Node> getChildrenNodeC = centarGP.getChildren();
        ArrayList<Button> centerButtons = new ArrayList<Button>();

        ObservableList<Node> getChildrenNodeR = redGP.getChildren();
        ArrayList<Button> redButtons = new ArrayList<>();

        for(Node node : getChildrenNodeC){
            centerButtons.add((Button)node);
        }
        for(Node node : getChildrenNodeR){
            redButtons.add((Button)node);
        }
        for(int i = listaVozila.size() - 1; i >= 0; i--, counter++){
            if(counter > 5){

            }
            else{
                //Platform.runLater(centerButtons.get(4 - counter).setStyle("-fx-background-color: " + listaVozila.get(i).getBoja()));
            }
        }
    }
}
