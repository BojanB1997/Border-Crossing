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
import java.util.logging.Level;

import static Aplikacija.Pomjerac.sinhro;

public class PolicijskiTerminal extends Thread {
    private Boolean uFunkciji = true;
    private List<String> putniciSNespravnomDokumentacijom = new ArrayList<>();
    public ArrayList<Vozilo> listaVozila = new ArrayList<>();
    public Object[][] mapa;
    public Boolean neMozePreci = false;
    public Boolean prvi = false;
    public static Boolean jeSerijalizovan = false;
    public Integer pozicijaURedu;
    public String file;
    public String vrijeme;
    public static ArrayList<Putnik> listaKaznjavanih = new ArrayList<>();
    public Label opis = new Label();

    public PolicijskiTerminal(ArrayList<Vozilo> listaVozila, Object[][] mapa, Integer pozicijaURedu, String file, String vrijeme, Label opis) {
        super();
        this.listaVozila = listaVozila;
        this.mapa = mapa;
        this.pozicijaURedu = pozicijaURedu;
        this.file = file;
        this.vrijeme = vrijeme;
        this.opis = opis;
    }

    public void setuFunkciji(Boolean uFunkciji) {
        this.uFunkciji = uFunkciji;
    }

    public Boolean getuFunkciji() {
        return uFunkciji;
    }

    @Override
    public void run() {
//        ArrayList<Putnik> listaKaznjavanih = new ArrayList<>();
        do {
            try {
                sleep(6000);
            } catch (InterruptedException e) {
                MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu PolicijskiTerminal.", e);
            }
            synchronized (listaVozila) {
                    Iterator<Vozilo> voziloIterator = listaVozila.iterator();
                    while (voziloIterator.hasNext()) {
                        Vozilo vozilo = voziloIterator.next();
                        neMozePreci = false;
                        if ((vozilo.getPozicijaURedu() == pozicijaURedu) && uFunkciji) {
//                        try{
//                            sleep(2000);
//                        }catch(InterruptedException e){
//                            MojLogger.log(Level.SEVERE, "Uspavljivanje treda.", e);
//                        }
//                            System.out.println("Red: " + vozilo.getPozicijaURedu() + " - " + vozilo);
//                            Platform.runLater(() -> {
//                                opis.setText(vozilo + " \ndolazi na policijski terminal.");
//                            });
                            Iterator<Putnik> putnikIterator = vozilo.listaPutnika.iterator();
                            try {
                                FileWriter writer = new FileWriter(file, true);
                                BufferedWriter buff = new BufferedWriter(writer);
                                buff.write("VOZILO NA TERMINALU");
                                buff.newLine();
                                buff.write(String.valueOf(vozilo));
                                buff.newLine();
                                buff.close();
                            } catch (Exception e) {
                                MojLogger.log(Level.SEVERE, "Neuspjesan upis u evidenciju policijskog terminala.", e);
                            }
                            while (putnikIterator.hasNext()) {
                                Putnik putnik = putnikIterator.next();
                                if (putnik.getNespravniDokumenti()) {
                                    dodajKaznjenog(putnik);
                                    if (putnik.getJeVozac()) {
                                        neMozePreci = true;
                                        break;
                                    } else {
                                        try {
                                            FileWriter writer = new FileWriter(file, true);
                                            BufferedWriter buff = new BufferedWriter(writer);
                                            buff.write("p-Problem putnika sa dokumentacijom.");
                                            buff.newLine();
                                            buff.write(String.valueOf(putnik));
                                            buff.newLine();
                                            buff.close();
                                        } catch (Exception e) {
                                            MojLogger.log(Level.SEVERE, "Neuspjesan upis u evidenciju policijskog terminala.", e);
                                        }
                                        putnikIterator.remove();
                                    }
                                }
                                try {
                                    sleep(vozilo.getVrijemeProcesuiranja());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!neMozePreci) {
                                // synchronized (mapa) {
                                if (pozicijaURedu == 50) {
                                    synchronized (mapa) {
                                        if (mapa[1][51] == null) {
                                            mapa[1][51] = vozilo;
                                            vozilo.setPozicijaURedu(51);
                                            vozilo.listaPozicija.add(51);
                                            Platform.runLater(() -> {
                                                opis.setText(vozilo + "\ndolazi na carinski terminal CT1.");
                                            });
                                            //pomjerac.run();
                                            mapa[0][50] = null;
                                        }
                                    }
                                } else if (pozicijaURedu == 52) {
                                    synchronized (mapa) {
                                        if (mapa[1][51] == null) {
                                            mapa[1][51] = vozilo;
                                            vozilo.setPozicijaURedu(55);
                                            vozilo.listaPozicija.add(55);
                                            Platform.runLater(() -> {
                                                opis.setText(vozilo + "\ndolazi na carinski terminal CT1.");
                                            });
                                            //pomjerac.run();
                                            mapa[2][50] = null;
                                        }
                                    }
                                } else if (pozicijaURedu == 54) {
                                    synchronized (mapa) {
                                        if (mapa[3][51] == null) {
                                            mapa[3][51] = vozilo;
                                            vozilo.setPozicijaURedu(53);
                                            vozilo.listaPozicija.add(53);
                                            Platform.runLater(() -> {
                                                opis.setText(vozilo + "\ndolazi na carinski terminal CT2.");
                                            });
                                            //pomjerac.run();
                                            mapa[4][50] = null;
                                        }
                                    }
                                }
                                // }
                            } else {
                                //synchronized (mapa) {
                                if (pozicijaURedu == 50) {
                                    synchronized (mapa) {
                                        mapa[0][50] = null;
                                        //pomjerac.run();
                                    }
                                } else if (pozicijaURedu == 52) {
                                    synchronized (mapa) {
                                        mapa[2][50] = null;
                                        //pomjerac.run();
                                    }
                                } else if (pozicijaURedu == 54) {
                                    synchronized (mapa) {
                                        mapa[4][50] = null;
                                        //pomjerac.run();
                                    }
                                }
                                //}
                                try {
                                    FileWriter writer = new FileWriter(file, true);
                                    BufferedWriter buff = new BufferedWriter(writer);
                                    buff.write("pv-Problem vozaca sa dokumentacijom.");
                                    buff.newLine();
                                    buff.write(String.valueOf(vozilo));
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
            try {
                sleep(900);
            } catch (InterruptedException e) {
                MojLogger.log(Level.SEVERE, "Uspavljivanje u tredu PolicijskiTerminal.", e);
            }
            System.out.println("Da li je u funkciji " + pozicijaURedu + " : " + uFunkciji);
        } while (!listaVozila.isEmpty());
        uFunkciji = false;
        String currentDir = System.getProperty("user.dir");
        String projectPath = File.separator + "src" + File.separator + "Serijalizacija" + File.separator;
        if(!jeSerijalizovan && pozicijaURedu == 52) {
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

    public static void dodajKaznjenog(Putnik putnik){
        listaKaznjavanih.add(putnik);
    }
}
