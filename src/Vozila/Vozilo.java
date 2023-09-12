package Vozila;

import Aplikacija.MojLogger;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import static Aplikacija.Pomjerac.sinhro;

public abstract class Vozilo extends Thread {
    private Integer brojPutnika;
    private Integer pozicijaURedu;
    private Integer vrijemeProcesuiranja;
    private String oznaka;
    private String boja;
    public Object[][] mapa;
    public Boolean kreceSe = true;
    public ArrayList<Putnik> listaPutnika = new ArrayList<Putnik>();
    public static Integer putnikId = 0;
    public ArrayList<Integer> listaPozicija = new ArrayList<>();

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

    public Integer vrijemeCekanjaNaCarini() {
        return 0;
    }

    @Override
    public void run() {

        while (kreceSe) {
            if (getPozicijaURedu() < 49) {
                synchronized (sinhro) {
                    synchronized (mapa) {
                        if (mapa[2][getPozicijaURedu() + 1] == null) {
                            mapa[2][getPozicijaURedu() + 1] = this;
                            mapa[2][getPozicijaURedu()] = null;
                            setPozicijaURedu(getPozicijaURedu() + 1);
                            listaPozicija.add(getPozicijaURedu());
                            try {
                                sleep(10);
                            } catch (InterruptedException e) {
                                MojLogger.log(Level.SEVERE, "Spava u kretanju u redu.", e);
                            }
                        }
                    }
                }
            }
            try {
                sleep(200);
            } catch (InterruptedException e) {
                MojLogger.log(Level.SEVERE, "Uspavljivanje u klasi Vozilo.", e);
            }
        }
    }
}
