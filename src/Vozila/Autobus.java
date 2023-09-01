package Vozila;

import java.util.Random;

public class Autobus extends Vozilo{

    private Integer aId;
    private static Integer nextId = 1;
    Random random = new Random();
    Integer brojPutnika;

    public Autobus(Object[][] mapa){
        super(mapa);
        this.aId = nextId;
        nextId++;
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

    public Integer getaId() {
        return aId;
    }

    @Override
    public String toString(){
        return "Autobus[ID:" + getaId() + "]-Broj putnika:" + brojPutnika + "-Putnici:" + listaPutnika;
    }
}
