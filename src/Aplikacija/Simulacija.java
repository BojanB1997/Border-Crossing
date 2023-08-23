package Aplikacija;

import Vozila.Autobus;
import Vozila.Kamion;
import Vozila.LicnoVozilo;
import Vozila.Vozilo;

import java.util.ArrayList;
import java.util.Collections;

public class Simulacija extends Thread{
    ArrayList<Vozilo> listaVozila = new ArrayList<Vozilo>();
    ArrayList<LicnoVozilo> listaLicnihVozila = new ArrayList<LicnoVozilo>();
    ArrayList<Kamion> listaKamiona = new ArrayList<Kamion>();
    ArrayList<Autobus> listaAutobsa = new ArrayList<Autobus>();

    public Object[][] mapa = new Object[5][52];

    @Override
    public void run(){
        createListaVozila();
        for(int i = 0; i < 50; i++){
            listaVozila.get(i).setPozicijaURedu(i);
        }
        while(listaVozila.size() > 0){

        }
    }

    public void createListaVozila(){

        for(int i = 0; i < 35; i++){
            listaLicnihVozila.add(new LicnoVozilo());
        }

        for(int i = 0; i < 5; i++){
            listaAutobsa.add(new Autobus());
        }

        for(int i = 0; i < 10; i++){
            listaKamiona.add(new Kamion());
        }

        listaVozila.addAll(listaLicnihVozila);
        listaVozila.addAll(listaAutobsa);
        listaVozila.addAll(listaKamiona);

        Collections.shuffle(listaVozila);
    }

}
