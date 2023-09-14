package Aplikacija;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.logging.Level;

import static Aplikacija.Main.pauza;
import static Aplikacija.Main.pokrenut;

public class Tajmer extends Thread {
    public Label vrijeme;
    public Integer brojac = 0;

    public Tajmer(Label vrijeme) {
        this.vrijeme = vrijeme;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (pauza){
                if(!pokrenut){
                    try{
                        pauza.wait();
                    }catch(Exception e){
                        MojLogger.log(Level.SEVERE, "Pauza u simulaciji.", e);
                    }
                }
            }
            String brojSekundi = brojac + "[s]";
            Platform.runLater(() -> vrijeme.setText(brojSekundi));
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                MojLogger.log(Level.SEVERE, "Greska prilikom uspavljivanja tajmera.", e);
                ;
            }
            brojac++;
        }
    }
}
