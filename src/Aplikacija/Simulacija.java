package Aplikacija;

import Vozila.*;
import javafx.scene.layout.GridPane;

import java.io.*;
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
    public Boolean neMozePreciPT = false;
    public Boolean neMozePreciCT = false;
    public Boolean neMozePreciPutnik = false;

    String currentDir = System.getProperty("user.dir");
    String projectPath = File.separator + "src" + File.separator + "Datoteke" + File.separator;

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

        File evidencijaPT1 = new File(currentDir + projectPath + "pt1.txt");
        File evidencijaPT2 = new File(currentDir + projectPath + "pt2.txt");
        File evidencijaPT3 = new File(currentDir + projectPath + "pt3.txt");
        File evidencijaCT1 = new File(currentDir + projectPath + "ct1.txt");
        File evidencijaCT2 = new File(currentDir + projectPath + "ct2.txt");
        File evidencijaUrednih = new File(currentDir + projectPath + "uredni.txt");

        Pomjerac pomjerac = new Pomjerac(listaVozila, centarGP, redGP, mapa);
        //pomjerac.start();
        while (listaVozila.size() > 0) {
            int i;
            for (i = 0; i < listaVozila.size(); i++) {
                neMozePreciPT = false;
                neMozePreciCT = false;
                neMozePreciPutnik = false;
                System.out.println("Vozilo " + i + " - Pozicija u redu: " + listaVozila.get(i).getPozicijaURedu());
                System.out.println("Velicina liste: " + listaVozila.size());
                System.out.println("Vozilo: " + listaVozila.get(i));
                if (listaVozila.get(i).getPozicijaURedu() == 51) { //Kamion na carinskom terminalu
                    if(listaVozila.get(i).getStvarnaMasa() > listaVozila.get(i).getDeklarisanaMasa()){
                        neMozePreciCT = true;
                        try{
                            FileWriter writer = new FileWriter(evidencijaCT2, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("m-Stvarna masa je razlicita od deklarisane.");
                            buff.newLine();
                            buff.write(String.valueOf(listaVozila.get(i)));
                            buff.newLine();
                            buff.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        System.out.println("Masa nije uredna.");
                    }
                    if(!neMozePreciCT) {
                        Integer j = 0;
                        while(j < listaVozila.get(i).getBrojPutnika()){
                            Putnik putnik = listaVozila.get(i).listaPutnika.get(j);
                            if (putnik.getNedozvoljeneStvari()) {
                                if (putnik.getJeVozac()) {
                                    neMozePreciPT = true;
                                    System.out.println(putnik);
                                    break;
                                } else {
                                    try {
                                        FileWriter writer = new FileWriter(evidencijaCT2, true);
                                        BufferedWriter buff = new BufferedWriter(writer);
                                        buff.write("k-Putnik je imao nedozvoljene stvari u koferu.");
                                        buff.newLine();
                                        buff.write(String.valueOf(putnik));
                                        buff.newLine();
                                        buff.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    listaVozila.get(i).listaPutnika.remove(putnik);
                                }
                            }
                            j++;
                        }
                    }
                    if(!(neMozePreciCT || neMozePreciPT)) {
                        synchronized (mapa) {
                            mapa[3][51] = null;
                        }
                        try{
                            FileWriter writer = new FileWriter(evidencijaUrednih, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("u-Vozilo je uredno preslo granicu.");
                            buff.newLine();
                            buff.write(String.valueOf(listaVozila.get(i)));
                            buff.newLine();
                            buff.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        listaVozila.get(i).kreceSe = false;
                        synchronized (listaVozila) {
                            listaVozila.remove(i);
                        }
                        System.out.println("Valjda obrisano: " + listaVozila.size());
                    }else{
                        mapa[3][51] = null;
                        try{
                            FileWriter writer = new FileWriter(evidencijaCT2, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("kv-Vozac je imao nedozvoljene stvari u koferu.");
                            buff.newLine();
                            buff.write(String.valueOf(listaVozila.get(i)));
                            buff.newLine();
                            buff.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        listaVozila.get(i).kreceSe = false;
                        listaVozila.remove(i);
                    }
                } else if(listaVozila.get(i).getPozicijaURedu() == 54 || listaVozila.get(i).getPozicijaURedu() == 55){ //Vozilo na CT
                    Integer j = 0;
                    while(j < listaVozila.get(i).getBrojPutnika()){
                        Putnik putnik = listaVozila.get(i).listaPutnika.get(j);
                        if(putnik.getNedozvoljeneStvari()){
                            if(putnik.getJeVozac()) {
                                neMozePreciPT = true;
                                System.out.println(putnik);
                                break;
                            }else{
                                try{
                                    FileWriter writer = new FileWriter(evidencijaCT1, true);
                                    BufferedWriter buff = new BufferedWriter(writer);
                                    buff.write("k-Putnik je imao nedozvoljene stvari u koferu.");
                                    buff.newLine();
                                    buff.write(String.valueOf(putnik));
                                    buff.newLine();
                                    buff.close();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                listaVozila.get(i).listaPutnika.remove(putnik);
                            }
                        }
                        j++;
                    }
                    if(!neMozePreciPT) {
                        synchronized (mapa) {
                            mapa[1][51] = null;
                        }
                        try{
                            FileWriter writer = new FileWriter(evidencijaUrednih, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("u-Vozilo je uredno preslo granicu.");
                            buff.newLine();
                            buff.write(String.valueOf(listaVozila.get(i)));
                            buff.newLine();
                            buff.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        listaVozila.get(i).kreceSe = false;
                        synchronized (listaVozila) {
                            listaVozila.remove(i);
                        }
                        System.out.println("Valjda obrisano: " + listaVozila.size());
                    }else{
                        mapa[1][51] = null;
                        try{
                            FileWriter writer = new FileWriter(evidencijaCT1, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("kv-Vozac je imao nedozvoljene stvari u koferu.");
                            buff.newLine();
                            buff.write(String.valueOf(listaVozila.get(i)));
                            buff.newLine();
                            buff.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        listaVozila.get(i).kreceSe = false;
                        listaVozila.remove(i);
                    }
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
                    Integer j = 0;
                    while(j < listaVozila.get(i).getBrojPutnika()){
                        Putnik putnik = listaVozila.get(i).listaPutnika.get(j);
                        if(putnik.getNedozvoljeneStvari()){
                            if(putnik.getJeVozac()) {
                                neMozePreciPT = true;
                                System.out.println(putnik);
                                break;
                            }else{
                                try{
                                    FileWriter writer = new FileWriter(evidencijaPT3, true);
                                    BufferedWriter buff = new BufferedWriter(writer);
                                    buff.write("p-Problem putnika sa dokumentima.");
                                    buff.newLine();
                                    buff.write(String.valueOf(putnik));
                                    buff.newLine();
                                    buff.close();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                listaVozila.get(i).listaPutnika.remove(putnik);
                            }
                        }
                        j++;
                    }
                    if(!neMozePreciPT) {
                        synchronized (mapa) {
                            if (mapa[3][51] == null) {
                                mapa[3][51] = listaVozila.get(i).getOznaka();
                                listaVozila.get(i).setPozicijaURedu(51);
                                mapa[4][50] = null;
                            }
                        }
                    }else{
                        mapa[4][50] = null;
                        try{
                            FileWriter writer = new FileWriter(evidencijaPT3, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("pv-Problem vozaca sa dokumentima.");
                            buff.newLine();
                            buff.write(String.valueOf(listaVozila.get(i)));
                            buff.newLine();
                            buff.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        listaVozila.remove(i);
                    }
                } else if (listaVozila.get(i).getPozicijaURedu() == 52) { //Vozilo na PT
                    Integer j = 0;
                    while(j < listaVozila.get(i).getBrojPutnika()){
                        Putnik putnik = listaVozila.get(i).listaPutnika.get(j);
                        if(putnik.getNedozvoljeneStvari()){
                            if(putnik.getJeVozac()) {
                                neMozePreciPT = true;
                                System.out.println(putnik);
                                break;
                            }else{
                                try{
                                    FileWriter writer = new FileWriter(evidencijaPT1, true);
                                    BufferedWriter buff = new BufferedWriter(writer);
                                    buff.write("p-Problem putnika sa dokumentacijom.");
                                    buff.newLine();
                                    buff.write(String.valueOf(putnik));
                                    buff.newLine();
                                    buff.close();
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                                listaVozila.get(i).listaPutnika.remove(putnik);
                            }
                        }
                        j++;
                    }
                    if(!neMozePreciPT) {
                        synchronized (mapa) {
                            if (mapa[1][51] == null) {
                                mapa[1][51] = listaVozila.get(i).getOznaka();
                                listaVozila.get(i).setPozicijaURedu(54);
                                mapa[0][50] = null;
                            }
                        }
                    }else{
                        mapa[0][50] = null;
                        try{
                            FileWriter writer = new FileWriter(evidencijaPT1, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("pv-Problem vozaca sa dokumentacijom.");
                            buff.newLine();
                            buff.write(String.valueOf(listaVozila.get(i)));
                            buff.newLine();
                            buff.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        listaVozila.remove(i);
                    }
                } else if (listaVozila.get(i).getPozicijaURedu() == 53) { //Vozilo na PT
                    Integer j = 0;
                    while(j < listaVozila.get(i).getBrojPutnika()){
                        Putnik putnik = listaVozila.get(i).listaPutnika.get(j);
                        if(putnik.getJeVozac()) {
                            neMozePreciPT = true;
                            System.out.println(putnik);
                            break;
                        }else{
                            try{
                                FileWriter writer = new FileWriter(evidencijaPT2, true);
                                BufferedWriter buff = new BufferedWriter(writer);
                                buff.write("p-Problem putnika sa dokumentacijom.");
                                buff.newLine();
                                buff.write(String.valueOf(putnik));
                                buff.newLine();
                                buff.close();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            listaVozila.get(i).listaPutnika.remove(putnik);
                        }
                        j++;
                    }
                    if(!neMozePreciPT) {
                        synchronized (mapa) {
                            if (mapa[1][51] == null) {
                                mapa[1][51] = listaVozila.get(i).getOznaka();
                                listaVozila.get(i).setPozicijaURedu(55);
                                mapa[2][50] = null;
                            }
                        }
                    }else{
                        mapa[2][50] = null;
                        try{
                            FileWriter writer = new FileWriter(evidencijaPT2, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("pv-Problem vozaca sa dokumentacijom.");
                            buff.newLine();
                            buff.write(String.valueOf(listaVozila.get(i)));
                            buff.newLine();
                            buff.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        listaVozila.remove(i);
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
