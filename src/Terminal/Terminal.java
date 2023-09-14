package Terminal;

import Vozila.Vozilo;
import javafx.scene.control.Label;

import java.util.ArrayList;

public abstract class Terminal extends Thread{
    private Boolean uFunkciji = true;
    public ArrayList<Vozilo> listaVozila = new ArrayList<>();
    public Object[][] mapa;
    public Integer[] pozicijaURedu = new Integer[2];
    public String file;
    public Label opis = new Label();

    public Terminal(ArrayList<Vozilo> listaVozila, Object[][] mapa, String file, Label opis){
        super();
        this.listaVozila = listaVozila;
        this.mapa = mapa;
        this.file = file;
        this.opis = opis;
    }

    public void setuFunkciji(Boolean uFunkciji) {
        this.uFunkciji = uFunkciji;
    }

    public Boolean getuFunkciji() {
        return uFunkciji;
    }
}
