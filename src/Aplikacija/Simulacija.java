package Aplikacija;

import Terminal.CarinskiTerminal;
import Terminal.PolicijskiTerminal;
import Vozila.*;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

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
    public Boolean prvi = true;

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

        String pt1txt = currentDir + projectPath + "pt1.txt";
        String pt2txt = currentDir + projectPath + "pt2.txt";
        String pt3txt = currentDir + projectPath + "pt3.txt";
        PolicijskiTerminal pt1 = new PolicijskiTerminal(listaVozila, mapa, 53, pt1txt);
        PolicijskiTerminal pt2 = new PolicijskiTerminal(listaVozila, mapa, 52, pt2txt);
        PolicijskiTerminal pt3 = new PolicijskiTerminal(listaVozila, mapa, 50, pt3txt);

        String ct1txt = currentDir + projectPath + "ct1.txt";
        String ct2txt = currentDir + projectPath + "ct2.txt";
        String uredni = currentDir + projectPath + "uredni.txt";

        CarinskiTerminal ct1 = new CarinskiTerminal(listaVozila, mapa, 54, ct1txt, uredni);
        CarinskiTerminal ct2 = new CarinskiTerminal(listaVozila, mapa, 51, ct2txt, uredni);

        File evidencijaPT1 = new File(currentDir + projectPath + "pt1.txt");
        File evidencijaPT2 = new File(currentDir + projectPath + "pt2.txt");
        File evidencijaPT3 = new File(currentDir + projectPath + "pt3.txt");
        File evidencijaCT1 = new File(currentDir + projectPath + "ct1.txt");
        File evidencijaCT2 = new File(currentDir + projectPath + "ct2.txt");
        File evidencijaUrednih = new File(currentDir + projectPath + "uredni.txt");

        try {
            FileWriter isprazniPT1 = new FileWriter(evidencijaPT1);
            FileWriter isprazniPT2 = new FileWriter(evidencijaPT2);
            FileWriter isprazniPT3 = new FileWriter(evidencijaPT3);
            FileWriter isprazniCT1 = new FileWriter(evidencijaCT1);
            FileWriter isprazniCT2 = new FileWriter(evidencijaCT2);
            FileWriter isprazniUredne = new FileWriter(evidencijaUrednih);

            isprazniPT1.write("");
            isprazniPT2.write("");
            isprazniPT3.write("");
            isprazniCT1.write("");
            isprazniCT2.write("");
            isprazniUredne.write("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Pomjerac pomjerac = new Pomjerac(listaVozila, centarGP, redGP, mapa);
        pomjerac.start();
        try{
            sleep(1000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        pt1.start();
        pt2.start();
        pt3.start();
        ct1.start();
        ct2.start();
        do {
            try {
                FileReader read = new FileReader(currentDir + projectPath + "config.txt");
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] linija = line.split("-");
                    if (linija.length == 2) {
                        String key = linija[0];
                        String value = linija[1];
                        Boolean radi = "True".equalsIgnoreCase(value);
                        if ("PT1".equalsIgnoreCase(key)) {
                            pt1.setuFunkciji(radi);
                        } else if ("PT2".equalsIgnoreCase(key)) {
                            pt2.setuFunkciji(radi);
                        } else if ("PT3".equalsIgnoreCase(key)) {
                            pt3.setuFunkciji(radi);
                        } else if ("CT1".equalsIgnoreCase(key)) {
                            ct1.setuFunkciji(radi);
                        } else if ("CT2".equalsIgnoreCase(key)) {
                            ct2.setuFunkciji(radi);
                        }
                    }
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (listaVozila) {
                Iterator<Vozilo> voziloIterator = listaVozila.iterator();
                while (voziloIterator.hasNext()) {
                    Vozilo vozilo = voziloIterator.next();
                    //System.out.println("Vozilo " + vozilo + "- Pozicija u redu:" + vozilo.getPozicijaURedu());
                    //System.out.println("Velicina liste: " + listaVozila.size());
                    if (vozilo.getPozicijaURedu() == 49) { //Vozilo prvo u redu za obradu
                        synchronized (mapa) {
                            if (!(mapa[2][vozilo.getPozicijaURedu()] instanceof Kamion)) {
//                                if(mapa[0][50] == null || mapa[2][50] == null){
//                                    if(prvi){
//                                        if(mapa[0][50] == null) {
//                                            mapa[0][50] = vozilo.getOznaka();
//                                            vozilo.setPozicijaURedu(52);
//                                            mapa[2][49] = null;
//                                        }
//                                        prvi = false;
//                                    }
//                                    else{
//                                        if(mapa[2][50] == null) {
//                                            mapa[2][50] = vozilo.getOznaka();
//                                            vozilo.setPozicijaURedu(53);
//                                            mapa[2][49] = null;
//                                        }
//                                        prvi = true;
//                                    }
//                                }
                                if (mapa[0][50] == null) {
                                    mapa[0][50] = vozilo.getOznaka();
                                    vozilo.setPozicijaURedu(52);
                                    mapa[2][49] = null;
                                }
                                else if (mapa[2][50] == null) {
                                    mapa[2][50] = vozilo.getOznaka();
                                    vozilo.setPozicijaURedu(53);
                                    mapa[2][49] = null;
                                }
                            } else {
                                if (mapa[4][50] == null) {
                                    mapa[4][50] = vozilo.getOznaka();
                                    vozilo.setPozicijaURedu(50);
                                    mapa[2][49] = null;
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
            }
        } while (!listaVozila.isEmpty());
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
