package Vozila;

import java.util.Random;

public class Autobus extends Vozilo{

    Random random = new Random();
    Integer brojPutnika;

    public Autobus(Object[][] mapa){
        super(mapa);
        brojPutnika = random.nextInt(52) + 1;
        this.setBrojPutnika(brojPutnika);
        this.setVrijemeProcesuiranja(100);
        this.setOznaka("A");
        this.setBoja("cadetblue");
    }

    @Override
    public String toString(){
        return "Autobus - broj putnika: " + brojPutnika;
    }
}
