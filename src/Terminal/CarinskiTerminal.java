package Terminal;

import Vozila.Putnik;
import Vozila.Vozilo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class CarinskiTerminal extends Thread {
    private Boolean uFunkciji = true;
    public ArrayList<Vozilo> listaVozila = new ArrayList<>();
    public Object[][] mapa;
    public Boolean neMozePreciKamion = false;
    public Boolean neMozePreciPutnik = false;
    public Integer[] pozicijaURedu = new Integer[2];
    public String file;
    public String uredni;

    public CarinskiTerminal(ArrayList<Vozilo> listaVozila, Object[][] mapa, Integer pozicijaURedu, String file, String uredni) {
        super();
        this.listaVozila = listaVozila;
        this.mapa = mapa;
        this.pozicijaURedu[0] = pozicijaURedu;
        if(this.pozicijaURedu[0] == 54){
            this.pozicijaURedu[1] = 55;
        }else{
            this.pozicijaURedu[1] = -10;
        }
        this.file = file;
        this.uredni = uredni;
    }

    public Boolean getuFunkciji() {
        return uFunkciji;
    }

    public void setuFunkciji(Boolean uFunkciji) {
        this.uFunkciji = uFunkciji;
    }

    @Override
    public void run() {
        do {
            synchronized (listaVozila) {
                Iterator<Vozilo> voziloIterator = listaVozila.iterator();

                while (voziloIterator.hasNext()) {
                    Vozilo vozilo = voziloIterator.next();
                    neMozePreciKamion = false;
                    neMozePreciPutnik = false;
                    if (((vozilo.getPozicijaURedu() == pozicijaURedu[0]) || (vozilo.getPozicijaURedu() == pozicijaURedu[1])) && uFunkciji) {
                        System.out.println("Red: " + vozilo.getPozicijaURedu() + " - " + vozilo);
                        // Cekanje na CT
                        try{
                            sleep(11);
                        }catch(InterruptedException  e){
                            e.printStackTrace();
                        }
                        if (vozilo.getStvarnaMasa() > vozilo.getDeklarisanaMasa()) {
                            neMozePreciKamion = true;
                            try {
                                FileWriter writer = new FileWriter(file, true);
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
                        if (!neMozePreciKamion) {
                            Iterator<Putnik> putnikIterator = vozilo.listaPutnika.iterator();
                            while (putnikIterator.hasNext()) {
                                Putnik putnik = putnikIterator.next();
                                if (putnik.getNedozvoljeneStvari()) {
                                    if (putnik.getJeVozac()) {
                                        neMozePreciPutnik = true;
                                        System.out.println(putnik);
                                        break;
                                    } else {
                                        try {
                                            FileWriter writer = new FileWriter(file, true);
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
                        if (!(neMozePreciKamion || neMozePreciPutnik)) {
                            //synchronized (mapa) {
                                if (pozicijaURedu[0] == 51) {
                                    synchronized (mapa) {
                                        mapa[3][51] = null;
                                    }
                                } else if (pozicijaURedu[0] == 54 || pozicijaURedu[1] == 55) {
                                    synchronized (mapa) {
                                        mapa[1][51] = null;
                                    }
                                }
                            //}
                            try {
                                FileWriter writer = new FileWriter(uredni, true);
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
                            //synchronized (mapa) {
                                if (pozicijaURedu[0] == 51) {
                                    synchronized (mapa) {
                                        mapa[3][51] = null;
                                    }
                                } else if (pozicijaURedu[0] == 54 || pozicijaURedu[1] == 55) {
                                    synchronized (mapa) {
                                        mapa[1][51] = null;
                                    }
                                }
                            //}
                            try {
                                FileWriter writer = new FileWriter(file, true);
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
                    }
                }
            }
            try{
                sleep(17);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        } while (!listaVozila.isEmpty());
    }
}
