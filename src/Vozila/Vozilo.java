package Vozila;

import java.util.Random;

public class Vozilo extends Thread {
    private Integer brojPutnika;
    private Integer pozicijaURedu;
    private Integer vrijemeProcesuiranja;
    private String oznaka;
    private String boja;
    public Object[][] mapa;
    public Boolean kreceSe = true;

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

    @Override
    public void run() {

        while (kreceSe) {
            synchronized (mapa) {
                if (getPozicijaURedu() < 49) {
                    if (mapa[2][getPozicijaURedu() + 1] == null) {
                        mapa[2][getPozicijaURedu() + 1] = getOznaka();
                        mapa[2][getPozicijaURedu()] = null;
                        setPozicijaURedu(getPozicijaURedu() + 1);
                    }

                    try {
                        sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
