package Vozila;

import java.util.Random;

public class Autobus extends Vozilo{

    Random random = new Random();
    Integer brojPutnika;

    public Autobus(){
        super();
        brojPutnika = random.nextInt(52) + 1;
        this.setBrojPutnika(brojPutnika);
        this.setVrijemeProcesuiranja(100);
        this.setOznaka("A");
        this.setBoja("cadetblue");
    }
}
