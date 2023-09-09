package Vozila;

import java.util.Random;

public class LicnoVozilo extends Vozilo{

    private Integer lVid;
    private static Integer nextId = 1;
    Integer brojPutnika;
    Random random = new Random();

    public LicnoVozilo(Object[][] mapa){
        super(mapa);
        this.lVid = nextId;
        nextId++;
        brojPutnika = random.nextInt(5) + 1;
        this.setBrojPutnika(brojPutnika);
        this.setVrijemeProcesuiranja(500);
        this.setOznaka("LV");
        this.setBoja("tomato");
        for(int i = 0; i < brojPutnika; i++){
            this.listaPutnika.add(new Putnik(putnikId));
            putnikId++;
        }
        this.listaPutnika.get(0).setJeVozac(true);
    }


    public Integer getLVId() {
        return lVid;
    }

    @Override
    public Integer vrijemeCekanjaNaCarini(){
        return 2000 + this.brojPutnika*this.getVrijemeProcesuiranja();
    }

    @Override
    public String toString(){
        return "Licno vozilo[ID:" + getLVId() + "]-Broj putnika:" + brojPutnika + "-Putnici:" + listaPutnika;
    }
}
