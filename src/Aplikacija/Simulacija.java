package Aplikacija;

import Vozila.Autobus;
import Vozila.Kamion;
import Vozila.LicnoVozilo;
import Vozila.Vozilo;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;

public class Simulacija extends Thread {
    ArrayList<Vozilo> listaVozila = new ArrayList<Vozilo>();
    ArrayList<LicnoVozilo> listaLicnihVozila = new ArrayList<LicnoVozilo>();
    ArrayList<Kamion> listaKamiona = new ArrayList<Kamion>();
    ArrayList<Autobus> listaAutobsa = new ArrayList<Autobus>();

    public Object[][] mapa = new Object[5][52];
    public GridPane centarGP = new GridPane();
    public GridPane redGP = new GridPane();

    public Simulacija(GridPane centarGP, GridPane redGP) {
        super();
        this.centarGP = centarGP;
        this.redGP = redGP;
    }

    @Override
    public void run() {
        createListaVozila();
        for (int i = 0; i < 50; i++) {
            listaVozila.get(i).setPozicijaURedu(i);
            mapa[2][i] = listaVozila.get(i).getOznaka();
        }
        for (int i = 0; i < 50; i++) {
            listaVozila.get(i).start();
        }

        Pomjerac pomjerac = new Pomjerac(listaVozila, centarGP, redGP, mapa);
        pomjerac.start();
        while (listaVozila.size() > 0) {
            int i;
            for (i = 0; i < listaVozila.size(); i++) {

                System.out.println("Vozilo " + i + " - Pozicija u redu: " + listaVozila.get(i).getPozicijaURedu());
                System.out.println("Velicina liste: " + listaVozila.size());
                System.out.println("Vozilo: " + listaVozila.get(i));
                if (listaVozila.get(i).getPozicijaURedu() == 51) { //Vozilo na carinskom terminalu
                    synchronized (mapa) {
                        mapa[3][51] = null;
                    }
                    listaVozila.get(i).kreceSe = false;
                    synchronized (listaVozila) {
                        listaVozila.remove(i);
                    }
                    System.out.println("Valjda obrisano: " + listaVozila.size());
                } else if(listaVozila.get(i).getPozicijaURedu() == 54 || listaVozila.get(i).getPozicijaURedu() == 55){
                    synchronized (mapa) {
                        mapa[1][51] = null;
                    }
                    listaVozila.get(i).kreceSe = false;
                    synchronized (listaVozila) {
                        listaVozila.remove(i);
                    }
                    System.out.println("Valjda obrisano: " + listaVozila.size());
                }else if (listaVozila.get(i).getPozicijaURedu() == 49) { //Vozilo prvo u redu za obradu
                    synchronized (mapa) {
                        if (!"K".equals(listaVozila.get(i).getOznaka())) {
                            if (mapa[0][50] == null) {
                                mapa[0][50] = listaVozila.get(i).getOznaka();
                                listaVozila.get(i).setPozicijaURedu(52);
                                mapa[2][49] = null;
                            } else if (mapa[2][50] == null) {
                                mapa[2][50] = listaVozila.get(i).getOznaka();
                                listaVozila.get(i).setPozicijaURedu(53);
                                mapa[2][49] = null;
                            }
                        } else {
                            if (mapa[4][50] == null) {
                                mapa[4][50] = listaVozila.get(i).getOznaka();
                                listaVozila.get(i).setPozicijaURedu(50);
                                mapa[2][49] = null;
                            }
                        }
                    }
                } else if (listaVozila.get(i).getPozicijaURedu() == 50) { //Kamion na PT
                    synchronized (mapa) {
                        if (mapa[3][51] == null) {
                            mapa[3][51] = listaVozila.get(i).getOznaka();
                            listaVozila.get(i).setPozicijaURedu(51);
                            mapa[4][50] = null;
                        }
                    }
                } else if (listaVozila.get(i).getPozicijaURedu() == 52) { //Vozilo na PT
                    synchronized (mapa) {
                        if (mapa[1][51] == null) {
                            mapa[1][51] = listaVozila.get(i).getOznaka();
                            listaVozila.get(i).setPozicijaURedu(54);
                            mapa[0][50] = null;
                        }
                    }
                } else if (listaVozila.get(i).getPozicijaURedu() == 53) { //Vozilo na PT
                    synchronized (mapa) {
                        if (mapa[1][51] == null) {
                            mapa[1][51] = listaVozila.get(i).getOznaka();
                            listaVozila.get(i).setPozicijaURedu(55);
                            mapa[2][50] = null;
                        }
                    }
                }


                System.out.println("Izasao iz fora.");
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Svi presli granicu.");

    }

    public void createListaVozila() {

        for (int i = 0; i < 35; i++) {
            listaLicnihVozila.add(new LicnoVozilo(mapa));
        }

        for (int i = 0; i < 5; i++) {
            listaAutobsa.add(new Autobus(mapa));
        }

        for (int i = 0; i < 10; i++) {
            listaKamiona.add(new Kamion(mapa));
        }

        listaVozila.addAll(listaLicnihVozila);
        listaVozila.addAll(listaAutobsa);
        listaVozila.addAll(listaKamiona);

        Collections.shuffle(listaVozila);
    }

}
