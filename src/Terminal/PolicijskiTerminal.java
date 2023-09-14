package Terminal;

import Aplikacija.MojLogger;
import Aplikacija.Pomjerac;
import Vozila.Putnik;
import Vozila.Vozilo;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

import static Aplikacija.Main.pauza;
import static Aplikacija.Main.pokrenut;
import static Aplikacija.Pomjerac.sinhro;

public class PolicijskiTerminal extends Terminal {
    public Boolean neMozePreci = false;
    public static Boolean jeSerijalizovan = false;
    public String vrijeme;
    public static ArrayList<Putnik> listaKaznjavanih = new ArrayList<>();

    public PolicijskiTerminal(ArrayList<Vozilo> listaVozila, Object[][] mapa, Integer pozicijaURedu, String file, String vrijeme, Label opis) {
        super(listaVozila, mapa, file, opis);
        this.pozicijaURedu[0] = pozicijaURedu;
        this.pozicijaURedu[1] = pozicijaURedu;
        this.vrijeme = vrijeme;
    }

    @Override
    public void run() {
        do {
            try {
                sleep(8001);
            } catch (InterruptedException e) {
                MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu PolicijskiTerminal.", e);
            }
            synchronized (pauza){
                if(!pokrenut){
                    try{
                        pauza.wait();
                    }catch(Exception e){
                        MojLogger.log(Level.SEVERE, "Pauza u policijskom terminalu.", e);
                    }
                }
            }
            synchronized (listaVozila) {
                synchronized (mapa) {
                    Iterator<Vozilo> voziloIterator = listaVozila.iterator();
                    while (voziloIterator.hasNext()) {
                        Vozilo vozilo = voziloIterator.next();
                        neMozePreci = false;
                        if ((vozilo.getPozicijaURedu() == pozicijaURedu[0]) && getuFunkciji()) {
                            Iterator<Putnik> putnikIterator = vozilo.listaPutnika.iterator();
                            while (putnikIterator.hasNext()) {
                                Putnik putnik = putnikIterator.next();
                                if (putnik.getNespravniDokumenti()) {
                                    dodajKaznjenog(putnik);
                                    if (putnik.getJeVozac()) {
                                        neMozePreci = true;
                                        Platform.runLater(() -> {
                                            opis.setText(putnik + "\nje vozac vozila \n" + vozilo + "\ni ima nesipravne dokumente.");
                                        });
                                        break;
                                    } else {
                                        Platform.runLater(() -> {
                                            opis.setText(putnik + "\nje putnik u vozilu \n" + vozilo + "\ni ima nesipravne dokumente.");
                                        });
                                        try {
                                            FileWriter writer = new FileWriter(file, true);
                                            BufferedWriter buff = new BufferedWriter(writer);
                                            buff.write("p-Problem putnika sa dokumentacijom.");
                                            buff.newLine();
                                            buff.write(putnik.upisiPutnikaUFajl());
                                            buff.newLine();
                                            buff.close();
                                        } catch (Exception e) {
                                            MojLogger.log(Level.SEVERE, "Neuspjesan upis u evidenciju policijskog terminala.", e);
                                        }
                                        putnikIterator.remove();
                                    }
                                }
                            }
                            if (!neMozePreci) {
                                if (pozicijaURedu[0] == 50) {
                                        if (mapa[1][51] == null) {
                                            mapa[1][51] = vozilo;
                                            vozilo.setPozicijaURedu(51);
                                            vozilo.listaPozicija.add(51);
                                            Platform.runLater(() -> {
                                                opis.setText(vozilo + "\ndolazi na carinski terminal CT1.");
                                            });
                                            mapa[0][50] = null;
                                        }
                                } else if (pozicijaURedu[0] == 52) {
                                        if (mapa[1][51] == null) {
                                            mapa[1][51] = vozilo;
                                            vozilo.setPozicijaURedu(55);
                                            vozilo.listaPozicija.add(55);
                                            Platform.runLater(() -> {
                                                opis.setText(vozilo + "\ndolazi na carinski terminal CT1.");
                                            });
                                            mapa[2][50] = null;
                                        }
                                } else if (pozicijaURedu[0] == 54) {
                                        if (mapa[3][51] == null) {
                                            mapa[3][51] = vozilo;
                                            vozilo.setPozicijaURedu(53);
                                            vozilo.listaPozicija.add(53);
                                            Platform.runLater(() -> {
                                                opis.setText(vozilo + "\ndolazi na carinski terminal CT2.");
                                            });
                                            System.out.println("Prebacio sam ga na CT2.");
                                            mapa[4][50] = null;
                                        }
                                }
                            } else {
                                if (pozicijaURedu[0] == 50) {
                                        mapa[0][50] = null;
                                } else if (pozicijaURedu[0] == 52) {
                                        mapa[2][50] = null;
                                } else if (pozicijaURedu[0] == 54) {
                                        mapa[4][50] = null;
                                }
                                try {
                                    FileWriter writer = new FileWriter(file, true);
                                    BufferedWriter buff = new BufferedWriter(writer);
                                    buff.write("pv-Problem vozaca sa dokumentacijom.");
                                    buff.newLine();
                                    buff.write(vozilo.upisiVoziloUFajl());
                                    buff.newLine();
                                    buff.close();
                                } catch (Exception e) {
                                    MojLogger.log(Level.SEVERE, "Neuspjesan upis u evidenciju policijskog terminala.", e);
                                }
                                vozilo.kreceSe = false;
                                voziloIterator.remove();
                            }
                        }
                    }
                }
            }
            System.out.println("Da li je u funkciji " + pozicijaURedu + " : " + getuFunkciji());
        } while (!listaVozila.isEmpty());
        setuFunkciji(false);
        String currentDir = System.getProperty("user.dir");
        String projectPath = File.separator + "src" + File.separator + "Serijalizacija" + File.separator;
        if (!jeSerijalizovan && pozicijaURedu[0] == 52) {
            try {
                System.out.println("Usao u pokusaj serijalizacije.");
                FileOutputStream fileOut = new FileOutputStream(currentDir + projectPath + vrijeme + ".ser", true);
                ObjectOutputStream obOut = new ObjectOutputStream(fileOut);

                obOut.writeObject(listaKaznjavanih);

                jeSerijalizovan = true;

                obOut.close();
                fileOut.close();
            } catch (IOException e) {
                MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu PolicijskiTerminal.", e);
            }
        }

    }

    public static void dodajKaznjenog(Putnik putnik) {
        listaKaznjavanih.add(putnik);
    }
}
