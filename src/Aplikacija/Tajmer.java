package Aplikacija;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.logging.Level;

public class Tajmer extends Thread{
    public Label vrijeme;
    public Integer brojac = 0;

    public Tajmer(Label vrijeme){
        this.vrijeme = vrijeme;
    }

    @Override
    public void run(){
        while(true){
            String brojSekundi = brojac + "[s]";
            Platform.runLater(() -> vrijeme.setText(brojSekundi));
            try{
                Thread.sleep(1000);
            }catch(Exception e){
                MojLogger.log(Level.SEVERE, "Greska prilikom uspavljivanja tajmera.", e);;
            }
            brojac++;
        }
    }
}
