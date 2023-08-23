package Vozila;

import java.util.Random;

public class LicnoVozilo extends Vozilo{

    Integer brojPutnika;
    Random random = new Random();

    public LicnoVozilo(){
        super();
        brojPutnika = random.nextInt(5) + 1;
        this.setBrojPutnika(brojPutnika);
        this.setVrijemeProcesuiranja(500);
        this.setOznaka("LV");
        this.setBoja("tomato");
    }
}
