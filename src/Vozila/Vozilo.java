package Vozila;

import java.util.ArrayList;
import java.util.Random;

public abstract class Vozilo extends Thread {
    private Integer brojPutnika;
    private Integer pozicijaURedu;
    private Integer vrijemeProcesuiranja;
    private String oznaka;
    private String boja;
    public Object[][] mapa;
    public Boolean kreceSe = true;
    public ArrayList<Putnik> listaPutnika = new ArrayList<Putnik>();

    public Vozilo(Object[][] mapa) {
        super();
        this.mapa = mapa;
    }

    public void setBrojPutnika(Integer brojPutnika) {
        this.brojPutnika = brojPutnika;
    }

    public Integer getBrojPutnika() {
        return brojPutnika;
    }

    public void setPozicijaURedu(Integer pozicijaURedu) {
        this.pozicijaURedu = pozicijaURedu;
    }

    public Integer getPozicijaURedu() {
        return pozicijaURedu;
    }

    public Boolean isVjerovatnoca(Integer vjerovatnoca) {
        Random random = new Random();
        Integer broj = random.nextInt(100) + 1;
        if (broj <= vjerovatnoca) {
            return true;
        } else {
            return false;
        }
    }

    public void setVrijemeProcesuiranja(Integer vrijemeProcesuiranja) {
        this.vrijemeProcesuiranja = vrijemeProcesuiranja;
    }

    public Integer getVrijemeProcesuiranja() {
        return vrijemeProcesuiranja;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setBoja(String boja) {
        this.boja = boja;
    }

    public String getBoja() {
        return boja;
    }

    public float getDeklarisanaMasa()
    {
        return 0;
    }

    public float getStvarnaMasa(){
        return 0;
    }

    @Override
    public void run() {

        while (kreceSe) {
            if (getPozicijaURedu() < 49) {
                synchronized (mapa) {
                    if (mapa[2][getPozicijaURedu() + 1] == null) {
                        mapa[2][getPozicijaURedu() + 1] = this;
                        mapa[2][getPozicijaURedu()] = null;
                        setPozicijaURedu(getPozicijaURedu() + 1);
                    }
                }
            }
            try {
                sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
