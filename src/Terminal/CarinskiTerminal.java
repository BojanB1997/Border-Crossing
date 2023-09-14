package Terminal;

import Aplikacija.MojLogger;
import Aplikacija.Pomjerac;
import Vozila.LicnoVozilo;
import Vozila.MasaIDokumentacija;
import Vozila.Putnik;
import Vozila.Vozilo;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

import static Aplikacija.Main.pauza;
import static Aplikacija.Main.pokrenut;
import static Aplikacija.Pomjerac.sinhro;

public class CarinskiTerminal extends Terminal {
    public Boolean neMozePreciKamion = false;
    public Boolean neMozePreciPutnik = false;
    public String uredni;

    public CarinskiTerminal(ArrayList<Vozilo> listaVozila, Object[][] mapa, Integer pozicijaURedu, String file, String uredni, Label opis) {
        super(listaVozila, mapa, file, opis);
        this.pozicijaURedu[0] = pozicijaURedu;
        if (this.pozicijaURedu[0] == 51) {
            this.pozicijaURedu[1] = 55;
        } else {
            this.pozicijaURedu[1] = 53;
        }
        this.uredni = uredni;
    }

    @Override
    public void run() {
        do {
            try {
                sleep(9001);
            } catch (InterruptedException e) {
                MojLogger.log(Level.SEVERE, "Uspavljivanje treda.", e);
            }
            synchronized (pauza){
                if(!pokrenut){
                    try{
                        pauza.wait();
                    }catch(Exception e){
                        MojLogger.log(Level.SEVERE, "Pauza u simulaciji.", e);
                    }
                }
            }
            synchronized (listaVozila) {
                synchronized (mapa) {
                    Iterator<Vozilo> voziloIterator = listaVozila.iterator();

                    while (voziloIterator.hasNext()) {
                        Vozilo vozilo = voziloIterator.next();
                        neMozePreciKamion = false;
                        neMozePreciPutnik = false;
                        if (((vozilo.getPozicijaURedu() == pozicijaURedu[0]) || (vozilo.getPozicijaURedu() == pozicijaURedu[1])) && getuFunkciji()) {
                            if (vozilo instanceof MasaIDokumentacija) {
                                MasaIDokumentacija kamion = (MasaIDokumentacija) vozilo;
                                if (!kamion.isImaDokumentaciju() && kamion.isPotrebnaDokumentacija()) {
                                    kamion.setImaDokumentaciju(true);
                                }
                                if (kamion.getStvarnaMasa() > kamion.getDeklarisanaMasa()) {
                                    neMozePreciKamion = true;
                                    Platform.runLater(() -> {
                                        opis.setText( vozilo + "\nima vecu stvarnu masu od deklarisane.");
                                    });
                                    System.out.println("Masu provjerim oke.");
                                    try {
                                        FileWriter writer = new FileWriter(file, true);
                                        BufferedWriter buff = new BufferedWriter(writer);
                                        buff.write("m-Stvarna masa je razlicita od deklarisane.");
                                        buff.newLine();
                                        buff.write(String.valueOf(vozilo));
                                        buff.newLine();
                                        buff.close();
                                    } catch (Exception e) {
                                        MojLogger.log(Level.SEVERE, "Neuspjesan upis u evidenciju carinskog terminala.", e);
                                    }
                                }
                                else{
                                    neMozePreciKamion = false;
                                }
                            }
                            if (!neMozePreciKamion) {
                                Iterator<Putnik> putnikIterator = vozilo.listaPutnika.iterator();
                                while (putnikIterator.hasNext()) {
                                    Putnik putnik = putnikIterator.next();
                                    if (putnik.getNedozvoljeneStvari()) {
                                        if (putnik.getJeVozac()) {
                                            neMozePreciPutnik = true;
                                            Platform.runLater(() -> {
                                                opis.setText(putnik + "\nje vozac vozila \n" + vozilo + "\ni ima nedozvoljene stvari u koferu.");
                                            });
                                            System.out.println(putnik);
                                            break;
                                        } else {
                                            Platform.runLater(() -> {
                                                opis.setText(putnik + "\nje putnik vozila \n" + vozilo + "\ni ima nedozvoljene stvari.");
                                            });
                                            try {
                                                FileWriter writer = new FileWriter(file, true);
                                                BufferedWriter buff = new BufferedWriter(writer);
                                                buff.write("k-Putnik je imao nedozvoljene stvari u koferu.");
                                                buff.newLine();
                                                buff.write(String.valueOf(putnik));
                                                buff.newLine();
                                                buff.close();
                                            } catch (Exception e) {
                                                MojLogger.log(Level.SEVERE, "Neuspjesan upis u evidenciju carinskog terminala.", e);
                                            }
                                            putnikIterator.remove();
                                        }
                                    }
                                    else{
                                        neMozePreciPutnik = false;
                                    }
                                }
                            }
                            if (!(neMozePreciKamion || neMozePreciPutnik)) {
                                if (pozicijaURedu[0] == 51 || pozicijaURedu[1] == 55) {
                                        System.out.println("Uslov za CT1");
                                        mapa[1][51] = null;
                                } else if (pozicijaURedu[0] == 53) {
                                        mapa[3][51] = null;
                                        System.out.println("Uslov za CT2");
                                }
                                try {
                                    FileWriter writer = new FileWriter(uredni, true);
                                    BufferedWriter buff = new BufferedWriter(writer);
                                    buff.write("u-Vozilo je uredno preslo granicu.");
                                    buff.newLine();
                                    buff.write(String.valueOf(vozilo));
                                    buff.newLine();
                                    buff.close();
                                } catch (Exception e) {
                                    MojLogger.log(Level.SEVERE, "Neuspjesan upis u evidenciju urednih.", e);
                                }
                                vozilo.kreceSe = false;
                                voziloIterator.remove();
                                System.out.println("Valjda obrisano: " + listaVozila.size());
                                System.out.println("Vozilo - pozicije: " + vozilo.listaPozicija);
                                Platform.runLater(() -> {
                                    opis.setText(vozilo + "\nprolazi carinski terminal.");
                                });
                            } else {
                                if (pozicijaURedu[0] == 51 || pozicijaURedu[1] == 55) {
                                        mapa[1][51] = null;
                                } else if (pozicijaURedu[0] == 53) {
                                        mapa[3][51] = null;
                                        System.out.println("Postavim ti tvoju matricu na null.");
                                }
                                try {
                                    FileWriter writer = new FileWriter(file, true);
                                    BufferedWriter buff = new BufferedWriter(writer);
                                    buff.write("kv-Vozac je imao nedozvoljene stvari u koferu.");
                                    buff.newLine();
                                    buff.write(String.valueOf(vozilo));
                                    buff.newLine();
                                    buff.close();
                                } catch (Exception e) {
                                    MojLogger.log(Level.SEVERE, "Neuspjesan upis u evidenciju carinskog terminala.", e);
                                }
                                vozilo.kreceSe = false;
                                voziloIterator.remove();
                                neMozePreciPutnik = false;
                                neMozePreciKamion = false;
                            }
                        }
                    }
                }
            }
        } while (!listaVozila.isEmpty());
    }
}
