package Terminal;

import Vozila.Putnik;
import Vozila.Vozilo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class PolicijskiTerminal extends Thread{
    private Boolean uFunkciji = true;
    public ArrayList<Vozilo> listaVozila = new ArrayList<>();
    public Object[][] mapa;
    public Boolean neMozePreci = false;
    public Boolean prvi = false;
    public Integer pozicijaURedu;
    public String file;

    public PolicijskiTerminal(ArrayList<Vozilo> listaVozila, Object[][] mapa, Integer pozicijaURedu, String file){
        super();
        this.listaVozila = listaVozila;
        this.mapa = mapa;
        this.pozicijaURedu = pozicijaURedu;
        this.file = file;
    }

    public void setuFunkciji(Boolean uFunkciji) {
        this.uFunkciji = uFunkciji;
    }

    public Boolean getuFunkciji() {
        return uFunkciji;
    }

    @Override
    public void run(){
        do{
            synchronized (listaVozila) {
                Iterator<Vozilo> voziloIterator = listaVozila.iterator();
                while (voziloIterator.hasNext()) {
                    Vozilo vozilo = voziloIterator.next();
                    neMozePreci = false;
                    if ((vozilo.getPozicijaURedu() == pozicijaURedu) && uFunkciji) {
                        try {
                            FileWriter writer = new FileWriter(file, true);
                            BufferedWriter buff = new BufferedWriter(writer);
                            buff.write("Auto doslo na terminal.");
                            buff.newLine();
                            buff.write(String.valueOf(vozilo));
                            buff.newLine();
                            buff.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("Red: " + vozilo.getPozicijaURedu() + " - " + vozilo);
                        Iterator<Putnik> putnikIterator = vozilo.listaPutnika.iterator();
                        while (putnikIterator.hasNext()) {
                            Putnik putnik = putnikIterator.next();
                            if (putnik.getNespravniDokumenti()) {
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
                                        e.printStackTrace();
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
                                if (pozicijaURedu == 52) {
                                    synchronized (mapa) {
                                        if (mapa[1][51] == null) {
                                            mapa[1][51] = vozilo.getOznaka();
                                            vozilo.setPozicijaURedu(54);
                                            mapa[0][50] = null;
                                        }
                                    }
                                } else if (pozicijaURedu == 53) {
                                    synchronized (mapa) {
                                        if (mapa[1][51] == null) {
                                            mapa[1][51] = vozilo.getOznaka();
                                            vozilo.setPozicijaURedu(55);
                                            mapa[2][50] = null;
                                        }
                                    }
                                } else if (pozicijaURedu == 50) {
                                    synchronized (mapa) {
                                        if (mapa[3][51] == null) {
                                            mapa[3][51] = vozilo.getOznaka();
                                            vozilo.setPozicijaURedu(51);
                                            mapa[4][50] = null;
                                        }
                                    }
                                }
                           // }
                        } else {
                            //synchronized (mapa) {
                                if (pozicijaURedu == 52) {
                                    synchronized (mapa) {
                                        mapa[0][50] = null;
                                    }
                                } else if (pozicijaURedu == 53) {
                                    synchronized (mapa) {
                                        mapa[2][50] = null;
                                    }
                                } else if (pozicijaURedu == 50) {
                                    synchronized (mapa) {
                                        mapa[4][50] = null;
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
                                e.printStackTrace();
                            }
                            vozilo.kreceSe = false;
                            voziloIterator.remove();
                        }
                    }
                }
            }
            try{
                sleep(20);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }while(!listaVozila.isEmpty());
    }
}
