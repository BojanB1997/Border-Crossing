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
            mapa[2][i] = listaVozila.get(i).getOznaka();
        }
        for(int i = 0; i < 50; i++){
            listaVozila.get(i).start();
        }
        while(listaVozila.size() > 0){
            for(int i = 0; i < listaVozila.size(); i++){
                if(listaVozila.get(i).getPozicijaURedu() == 51){
                    if(!"K".equals(listaVozila.get(i).getOznaka())){
                        mapa[3][51] = null;
                    }
                    else{
                        mapa[1][51] = null;
                    }
                    listaVozila.remove(i);
                }
                else if(listaVozila.get(i).getPozicijaURedu() == 49){
                    if(!"K".equals(listaVozila.get(i).getOznaka())) {
                        if (mapa[0][50] == null) {
                            mapa[0][50] = listaVozila.get(i).getOznaka();
                            listaVozila.get(i).setPozicijaURedu(52);
                            mapa[2][49] = null;
                        } else if (mapa[2][50] == null){
                            mapa[2][50] = listaVozila.get(i).getOznaka();
                            listaVozila.get(i).setPozicijaURedu(53);
                            mapa[2][49] = null;
                        }
                    }
                    else{
                        if(mapa[4][50] == null){
                            mapa[4][50] = listaVozila.get(i).getOznaka();
                            listaVozila.get(i).setPozicijaURedu(50);
                            mapa[2][49] = null;
                        }
                    }
                }
                else if(listaVozila.get(i).getPozicijaURedu() == 50){
                    if(mapa[3][51] == null){
                        mapa[3][51] = listaVozila.get(i).getOznaka();
                        listaVozila.get(i).setPozicijaURedu(51);
                        mapa[4][50] = null;
                    }
                }
                else if(listaVozila.get(i).getPozicijaURedu() == 52){
                    if(mapa[1][51] == null){
                        mapa[1][51] = listaVozila.get(i).getOznaka();
                        listaVozila.get(i).setPozicijaURedu(51);
                        mapa[0][50] = null;
                    }
                }
                else if(listaVozila.get(i).getPozicijaURedu() == 53){
                    if(mapa[1][51] == null){
                        mapa[1][51] = listaVozila.get(i).getOznaka();
                        listaVozila.get(i).setPozicijaURedu(51);
                        mapa[2][50] = null;
                    }
                }
            }
        }
    }

    public void createListaVozila(){

        for(int i = 0; i < 35; i++){
            listaLicnihVozila.add(new LicnoVozilo(mapa));
        }

        for(int i = 0; i < 5; i++){
            listaAutobsa.add(new Autobus(mapa));
        }

        for(int i = 0; i < 10; i++){
            listaKamiona.add(new Kamion(mapa));
        }

        listaVozila.addAll(listaLicnihVozila);
        listaVozila.addAll(listaAutobsa);
        listaVozila.addAll(listaKamiona);

        Collections.shuffle(listaVozila);
    }

}
