package Vozila;

import java.util.Random;

public class LicnoVozilo extends Vozilo{

    Integer brojPutnika;
    Random random = new Random();

    public LicnoVozilo(Object[][] mapa){
        super(mapa);
        brojPutnika = random.nextInt(5) + 1;
        this.setBrojPutnika(brojPutnika);
        this.setVrijemeProcesuiranja(500);
        this.setOznaka("LV");
        this.setBoja("tomato");
        for(int i = 0; i < brojPutnika; i++){
            this.listaPutnika.add(new Putnik());
        }
        this.listaPutnika.get(0).setJeVozac(true);
    }

    @Override
    public String toString(){
        return "Licno vozilo - broj putnika: " + brojPutnika;
    }
}
