package Aplikacija;

import Terminal.CarinskiTerminal;
import Terminal.PolicijskiTerminal;
import Vozila.*;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

import static Aplikacija.Pomjerac.sinhro;

public class Simulacija extends Thread {
    ArrayList<Vozilo> listaVozila = new ArrayList<>();
    ArrayList<LicnoVozilo> listaLicnihVozila = new ArrayList<LicnoVozilo>();
    ArrayList<Kamion> listaKamiona = new ArrayList<Kamion>();
    ArrayList<Autobus> listaAutobsa = new ArrayList<Autobus>();

    public static Object[][] mapa = new Object[5][52];
    public GridPane centarGP = new GridPane();
    public GridPane redGP = new GridPane();
    public Label opis = new Label();
    public Boolean neMozePreciPT = false;
    public Boolean neMozePreciCT = false;
    public Boolean neMozePreciPutnik = false;
    public Boolean prvi = true;
    public String vrijeme;

    String currentDir = System.getProperty("user.dir");
    String projectPath = File.separator + "src" + File.separator + "Datoteke" + File.separator;

    public Simulacija(GridPane centarGP, GridPane redGP, Label opis) {
        super();
        this.centarGP = centarGP;
        this.redGP = redGP;
        this.opis = opis;
    }

    @Override
    public void run() {
        Date trenutnoVrijeme = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh_mm_ss");
        vrijeme = format.format(trenutnoVrijeme);

//        Kamion kamion = new Kamion(mapa);
//        kamion.setPozicijaURedu(45);
//        listaVozila.add(kamion);
//        listaVozila.get(0).start();

        createListaVozila();
        for (int i = 0; i < 50; i++) {
            mapa[2][i] = listaVozila.get(i);
            listaVozila.get(i).setPozicijaURedu(i);
            listaVozila.get(i).listaPozicija.add(i);
        }
        for (int i = 0; i < 50; i++) {
            System.out.println("Vozilo " + i + ", pozicija: " + listaVozila.get(i).getPozicijaURedu());
            listaVozila.get(i).start();
        }

        //Pomjerac pomjerac = new Pomjerac(listaVozila, centarGP, redGP, mapa);

        String pt1txt = currentDir + projectPath + "pt1.txt";
        String pt2txt = currentDir + projectPath + "pt2.txt";
        String pt3txt = currentDir + projectPath + "pt3.txt";
        PolicijskiTerminal pt1 = new PolicijskiTerminal(listaVozila, mapa, 50, pt1txt, vrijeme, opis);
        PolicijskiTerminal pt2 = new PolicijskiTerminal(listaVozila, mapa, 52, pt2txt, vrijeme, opis);
        PolicijskiTerminal pt3 = new PolicijskiTerminal(listaVozila, mapa, 54, pt3txt, vrijeme, opis);

        String ct1txt = currentDir + projectPath + "ct1.txt";
        String ct2txt = currentDir + projectPath + "ct2.txt";
        String uredni = currentDir + projectPath + "uredni.txt";

        CarinskiTerminal ct1 = new CarinskiTerminal(listaVozila, mapa, 51, ct1txt, uredni, opis);
        CarinskiTerminal ct2 = new CarinskiTerminal(listaVozila, mapa, 53, ct2txt, uredni, opis);

        File evidencijaPT1 = new File(currentDir + projectPath + "pt1.txt");
        File evidencijaPT2 = new File(currentDir + projectPath + "pt2.txt");
        File evidencijaPT3 = new File(currentDir + projectPath + "pt3.txt");
        File evidencijaCT1 = new File(currentDir + projectPath + "ct1.txt");
        File evidencijaCT2 = new File(currentDir + projectPath + "ct2.txt");
        File evidencijaUrednih = new File(currentDir + projectPath + "uredni.txt");

        FileWatcher fileWatcher = new FileWatcher(pt1, pt2, pt3, ct1, ct2);
        fileWatcher.start();

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
            MojLogger.log(Level.SEVERE, "Neuspjesno praznjenje fajlova za evidenciju terminala.", e);
        }

        Pomjerac pomjerac = new Pomjerac(listaVozila, centarGP, redGP, mapa);
        pomjerac.start();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Simulacija.", e);
        }
        pt1.start();
        pt2.start();
        pt3.start();
        ct1.start();
        ct2.start();


        do {
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Simulacija.", e);
            }
            synchronized (listaVozila) {
                synchronized (mapa) {
                    Iterator<Vozilo> voziloIterator = listaVozila.iterator();
                    while (voziloIterator.hasNext()) {
                        Vozilo vozilo = voziloIterator.next();
                        if (vozilo.getPozicijaURedu() == 49) { //Vozilo prvo u redu za obradu
//                        try{
//                            sleep(2000);
//                        }catch (InterruptedException e){
//                            MojLogger.log(Level.SEVERE, "Spava u simulaciji.", e);
//                        }
                            //synchronized (mapa) {
                            if (!(mapa[2][49] instanceof Kamion)) {
                                if (mapa[0][50] == null) {
                                    mapa[0][50] = vozilo;
                                    vozilo.setPozicijaURedu(50);
                                    Platform.runLater(() -> {
                                        opis.setText(vozilo + " \ndolazi na policijski terminal PT1.");
                                    });
                                    vozilo.listaPozicija.add(50);
                                    mapa[2][49] = null;
                                } else if (mapa[2][50] == null) {
                                    mapa[2][50] = vozilo;
                                    vozilo.setPozicijaURedu(52);
                                    vozilo.listaPozicija.add(52);
                                    Platform.runLater(() -> {
                                        opis.setText(vozilo + " \ndolazi na policijski terminal PT2.");
                                    });
                                    mapa[2][49] = null;
                                }
                            } else {
                                if (mapa[4][50] == null) {
                                    mapa[4][50] = vozilo;
                                    vozilo.setPozicijaURedu(54);
                                    vozilo.listaPozicija.add(54);
                                    Platform.runLater(() -> {
                                        opis.setText(vozilo + " \ndolazi na policijski terminal PT3.");
                                    });
                                    mapa[2][49] = null;
                                }
                            }
                            //}

                        }

                    }
                }
            }
//            try {
//                sleep(400);
//            } catch (InterruptedException e) {
//                MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Simulacija.", e);
//            }
        } while (!listaVozila.isEmpty());
        System.out.println("Svi presli granicu.");

        try {
            pt1.join();
            pt2.join();
            pt3.join();
        } catch (InterruptedException e) {
            MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu Simulacija.", e);
        }
        String currentDir = System.getProperty("user.dir");
        String projectPath = File.separator + "src" + File.separator + "Serijalizacija" + File.separator;
        try {
            FileInputStream fileIn = new FileInputStream(currentDir + projectPath + vrijeme + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);

            ArrayList<Putnik> putnici = (ArrayList<Putnik>) in.readObject();

            System.out.println("Citanje prije deserijalizacije.\n Duzina liste: " + putnici.size());
            for (Putnik putnik : putnici) {
                System.out.println("Citanje.");
                System.out.println(String.valueOf(putnik));
            }
            System.out.println(putnici);
            System.out.println("Citanje gotovo.");
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            MojLogger.log(Level.SEVERE, "Neuspjesna deserijalizacija", e);
        }
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
