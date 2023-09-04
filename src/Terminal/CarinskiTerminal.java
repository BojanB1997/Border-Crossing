package Terminal;

import Vozila.Putnik;
import Vozila.Vozilo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class CarinskiTerminal extends Thread{
    private Boolean uFunkciji = true;
    public ArrayList<Vozilo> listaVozila = new ArrayList<>();
    public Object[][] mapa;
    public Boolean neMozePreciKamion = false;
    public Boolean neMozePreciPutnik = false;
    public Integer pozicijaURedu;
    public String file;
    public String uredni;

    public CarinskiTerminal(ArrayList<Vozilo> listaVozila, Object[][] mapa, Integer pozicijaURedu, String file, String uredni){
        super();
        this.listaVozila = listaVozila;
        this.mapa = mapa;
        this.pozicijaURedu = pozicijaURedu;
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
    public void run(){
        do{
            Iterator<Vozilo> voziloIterator = listaVozila.iterator();
            while(voziloIterator.hasNext()){
                Vozilo vozilo = voziloIterator.next();
                neMozePreciKamion = false;
                neMozePreciPutnik = false;
                if((vozilo.getPozicijaURedu() == pozicijaURedu) && uFunkciji){
                    // Cekanje na CT
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
                        synchronized (mapa) {
                            if(pozicijaURedu == 51) {
                                mapa[3][51] = null;
                            }else if(pozicijaURedu == 54 || pozicijaURedu == 55){
                                mapa[1][51] = null;
                            }
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
                            e.printStackTrace();
                        }
                        vozilo.kreceSe = false;
                        voziloIterator.remove();
                        System.out.println("Valjda obrisano: " + listaVozila.size());
                    } else {
                        synchronized (mapa) {
                            if(pozicijaURedu == 51) {
                                mapa[3][51] = null;
                            }else if(pozicijaURedu == 54 || pozicijaURedu == 55){
                                mapa[1][51] = null;
                            }
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
                            e.printStackTrace();
                        }
                        vozilo.kreceSe = false;
                        voziloIterator.remove();
                    }
                }
            }
        }while(!listaVozila.isEmpty());
    }
}
