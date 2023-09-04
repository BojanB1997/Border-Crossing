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
        }catch (Exception e){
            e.printStackTrace();
        }

        Pomjerac pomjerac = new Pomjerac(listaVozila, centarGP, redGP, mapa);
        pomjerac.start();
        do {
            try{
                FileReader read = new FileReader(currentDir + projectPath + "config.txt");
                BufferedReader reader = new BufferedReader(read);
                String line;
                while((line = reader.readLine())!= null){
                    String[] linija = line.split("-");
                    if(linija.length == 2){
                        String key = linija[0];
                        String value = linija[1];
                        Boolean radi = "True".equalsIgnoreCase(value);
                        if("PT1".equalsIgnoreCase(key)){
                            pt1.setuFunkciji(radi);
                        }else if("PT2".equalsIgnoreCase(key)){
                            pt2.setuFunkciji(radi);
                        }else if("PT3".equalsIgnoreCase(key)){
                            pt3.setuFunkciji(radi);
                        }else if("CT1".equalsIgnoreCase(key)){
                            ct1.setuFunkciji(radi);
                        }else if("CT2".equalsIgnoreCase(key)){
                            ct2.setuFunkciji(radi);
                        }
                    }
                }
                reader.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            Iterator<Vozilo> voziloIterator = listaVozila.iterator();
            while (voziloIterator.hasNext()) {
                Vozilo vozilo = voziloIterator.next();
                neMozePreciPT = false;
                neMozePreciCT = false;
                neMozePreciPutnik = false;
                System.out.println("Vozilo " + vozilo + "- Pozicija u redu:" + vozilo.getPozicijaURedu());
                System.out.println("Velicina liste: " + listaVozila.size());
                if (vozilo.getPozicijaURedu() == 51 && ct2.getuFunkciji()) { //Kamion na carinskom terminalu
                    try{
                        sleep(500);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    if (vozilo.getStvarnaMasa() > vozilo.getDeklarisanaMasa()) {
                        neMozePreciCT = true;
                        try {
                            FileWriter writer = new FileWriter(evidencijaCT2, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("m-Stvarna masa je razlicita od deklarisane.");
                            buff.newLine();
                            buff.write(String.valueOf(vozilo));
                            buff.newLine();
                            buff.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("Masa nije uredna.");
                    }
                    if (!neMozePreciCT) {
                        Iterator<Putnik> putnikIterator = vozilo.listaPutnika.iterator();
                        while (putnikIterator.hasNext()) {
                            Putnik putnik = putnikIterator.next();
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
                                    putnikIterator.remove();
                                }
                            }
                        }
                    }
                    if (!(neMozePreciCT || neMozePreciPT)) {
                        synchronized (mapa) {
                            mapa[3][51] = null;
                        }
                        try {
                            FileWriter writer = new FileWriter(evidencijaUrednih, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("u-Vozilo je uredno preslo granicu.");
                            buff.newLine();
                            buff.write(String.valueOf(vozilo));
                            buff.newLine();
                            buff.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        vozilo.kreceSe = false;
                        voziloIterator.remove();
                        System.out.println("Valjda obrisano: " + listaVozila.size());
                    } else {
                        synchronized (mapa) {
                            mapa[3][51] = null;
                        }
                        try {
                            FileWriter writer = new FileWriter(evidencijaCT2, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("kv-Vozac je imao nedozvoljene stvari u koferu.");
                            buff.newLine();
                            buff.write(String.valueOf(vozilo));
                            buff.newLine();
                            buff.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        vozilo.kreceSe = false;
                        voziloIterator.remove();
                    }
                } else if ((vozilo.getPozicijaURedu() == 54 || vozilo.getPozicijaURedu() == 55) && ct1.getuFunkciji()) { //Vozilo na CT
                    Iterator<Putnik> putnikIterator = vozilo.listaPutnika.iterator();
                    while (putnikIterator.hasNext()) {
                        Putnik putnik = putnikIterator.next();
                        if (putnik.getNedozvoljeneStvari()) {
                            if (putnik.getJeVozac()) {
                                neMozePreciPT = true;
                                System.out.println(putnik);
                                break;
                            } else {
                                try {
                                    FileWriter writer = new FileWriter(evidencijaCT1, true);
                                    BufferedWriter buff = new BufferedWriter(writer);
                                    buff.write("k-Putnik je imao nedozvoljene stvari u koferu.");
                                    buff.newLine();
                                    buff.write(String.valueOf(putnik));
                                    buff.newLine();
                                    buff.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                putnikIterator.remove();
                            }
                        }
                        try{
                            sleep(vozilo.getVrijemeProcesuiranja());
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    if (!neMozePreciPT) {
                        synchronized (mapa) {
                            mapa[1][51] = null;
                        }
                        try {
                            FileWriter writer = new FileWriter(evidencijaUrednih, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("u-Vozilo je uredno preslo granicu.");
                            buff.newLine();
                            buff.write(String.valueOf(vozilo));
                            buff.newLine();
                            buff.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        vozilo.kreceSe = false;
                        voziloIterator.remove();
                        System.out.println("Valjda obrisano: " + listaVozila.size());
                    } else {
                        synchronized (mapa) {
                            mapa[1][51] = null;
                        }
                        try {
                            FileWriter writer = new FileWriter(evidencijaCT1, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("kv-Vozac je imao nedozvoljene stvari u koferu.");
                            buff.newLine();
                            buff.write(String.valueOf(vozilo));
                            buff.newLine();
                            buff.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        vozilo.kreceSe = false;
                        voziloIterator.remove();
                    }
                } else if (vozilo.getPozicijaURedu() == 49) { //Vozilo prvo u redu za obradu
                    synchronized (mapa) {
                        if (!"K".equals(vozilo.getOznaka())) {
                            if (mapa[0][50] == null) {
                                mapa[0][50] = vozilo.getOznaka();
                                vozilo.setPozicijaURedu(52);
                                mapa[2][49] = null;
                            } else if (mapa[2][50] == null) {
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
                } else if (vozilo.getPozicijaURedu() == 50 && pt3.getuFunkciji()) { //Kamion na PT
                    Iterator<Putnik> putnikIterator = vozilo.listaPutnika.iterator();
                    while (putnikIterator.hasNext()) {
                        Putnik putnik = putnikIterator.next();
                        if (putnik.getNespravniDokumenti()) {
                            if (putnik.getJeVozac()) {
                                neMozePreciPT = true;
                                System.out.println(putnik);
                                break;
                            } else {
                                try {
                                    FileWriter writer = new FileWriter(evidencijaPT3, true);
                                    BufferedWriter buff = new BufferedWriter(writer);
                                    buff.write("p-Problem putnika sa dokumentima.");
                                    buff.newLine();
                                    buff.write(String.valueOf(putnik));
                                    buff.newLine();
                                    buff.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                putnikIterator.remove();
                            }
                        }
                        try{
                            sleep(vozilo.getVrijemeProcesuiranja());
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    if (!neMozePreciPT) {
                        synchronized (mapa) {
                            if (mapa[3][51] == null) {
                                mapa[3][51] = vozilo.getOznaka();
                                vozilo.setPozicijaURedu(51);
                                mapa[4][50] = null;
                            }
                        }
                    } else {
                        synchronized (mapa) {
                            mapa[4][50] = null;
                        }
                        try {
                            FileWriter writer = new FileWriter(evidencijaPT3, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("pv-Problem vozaca sa dokumentima.");
                            buff.newLine();
                            buff.write(String.valueOf(vozilo));
                            buff.newLine();
                            buff.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        voziloIterator.remove();
                    }
                } else if (vozilo.getPozicijaURedu() == 52 && pt2.getuFunkciji()) { //Vozilo na PT
                    Iterator<Putnik> putnikIterator = vozilo.listaPutnika.iterator();
                    while (putnikIterator.hasNext()) {
                        Putnik putnik = putnikIterator.next();
                        if (putnik.getNespravniDokumenti()) {
                            if (putnik.getJeVozac()) {
                                neMozePreciPT = true;
                                System.out.println(putnik);
                                break;
                            } else {
                                try {
                                    FileWriter writer = new FileWriter(evidencijaPT1, true);
                                    BufferedWriter buff = new BufferedWriter(writer);
                                    buff.write("p-Problem putnika sa dokumentacijom.");
                                    buff.newLine();
                                    buff.write(String.valueOf(putnik));
                                    buff.newLine();
                                    buff.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                putnikIterator.remove();
                            }
                        }
                        try{
                            sleep(vozilo.getVrijemeProcesuiranja());
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    if (!neMozePreciPT) {
                        synchronized (mapa) {
                            if (mapa[1][51] == null) {
                                mapa[1][51] = vozilo.getOznaka();
                                vozilo.setPozicijaURedu(54);
                                mapa[0][50] = null;
                            }
                        }
                    } else {
                        synchronized (mapa) {
                            mapa[0][50] = null;
                        }
                        try {
                            FileWriter writer = new FileWriter(evidencijaPT1, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("pv-Problem vozaca sa dokumentacijom.");
                            buff.newLine();
                            buff.write(String.valueOf(vozilo));
                            buff.newLine();
                            buff.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        voziloIterator.remove();
                    }
                } else if (vozilo.getPozicijaURedu() == 53 && pt1.getuFunkciji()) { //Vozilo na PT
                    Iterator<Putnik> putnikIterator = vozilo.listaPutnika.iterator();
                    while (putnikIterator.hasNext()) {
                        Putnik putnik = putnikIterator.next();
                        if(putnik.getNespravniDokumenti()) {
                            if (putnik.getJeVozac()) {
                                neMozePreciPT = true;
                                System.out.println(putnik);
                                break;
                            } else {
                                try {
                                    FileWriter writer = new FileWriter(evidencijaPT2, true);
                                    BufferedWriter buff = new BufferedWriter(writer);
                                    buff.write("p-Problem putnika sa dokumentacijom.");
                                    buff.newLine();
                                    buff.write(String.valueOf(putnik));
                                    buff.newLine();
                                    buff.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                putnikIterator.remove();
                            }
                        }
                        try{
                            sleep(vozilo.getVrijemeProcesuiranja());
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    if (!neMozePreciPT) {
                        synchronized (mapa) {
                            if (mapa[1][51] == null) {
                                mapa[1][51] = vozilo.getOznaka();
                                vozilo.setPozicijaURedu(55);
                                mapa[2][50] = null;
                            }
                        }
                    } else {
                        synchronized (mapa) {
                            mapa[2][50] = null;
                        }
                        try {
                            FileWriter writer = new FileWriter(evidencijaPT2, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("pv-Problem vozaca sa dokumentacijom.");
                            buff.newLine();
                            buff.write(String.valueOf(vozilo));
                            buff.newLine();
                            buff.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        voziloIterator.remove();
                    }
                }


                System.out.println("Izasao iz fora.");
                try {
                    sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }while(!listaVozila.isEmpty());
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
