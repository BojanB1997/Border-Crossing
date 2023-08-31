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
        for(int i = 0; i < brojPutnika; i++){
            this.listaPutnika.add(new Putnik());
        }
        this.listaPutnika.get(0).setJeVozac(true);
    }

    @Override
    public String toString(){
        return "Autobus - broj putnika: " + brojPutnika;
    }
}
