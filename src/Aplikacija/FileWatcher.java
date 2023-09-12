package Aplikacija;

import Terminal.CarinskiTerminal;
import Terminal.PolicijskiTerminal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class FileWatcher extends Thread{
    public Boolean traje = true;
    public Path konfiguracija;
    String currentDir = System.getProperty("user.dir");
    String projectPath = File.separator + "src" + File.separator + "Datoteke" + File.separator;
    PolicijskiTerminal pt1, pt2, pt3;
    CarinskiTerminal ct1, ct2;

    public FileWatcher(PolicijskiTerminal pt1, PolicijskiTerminal pt2, PolicijskiTerminal pt3, CarinskiTerminal ct1, CarinskiTerminal ct2){
        super();
        konfiguracija = Paths.get(currentDir + projectPath + "config.txt");
        this.pt1 = pt1;
        this.pt2 = pt2;
        this.pt3 = pt3;
        this.ct1 = ct1;
        this.ct2 = ct2;
    }

    @Override
    public void run(){
        try{
            WatchService watchService = FileSystems.getDefault().newWatchService();
            konfiguracija.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            System.out.println("Putanja: " + konfiguracija);
            while(traje){
                WatchKey key = watchService.poll(1, TimeUnit.SECONDS);
                if(key != null){
                    for(WatchEvent<?> event : key.pollEvents()){

                        if(event.kind() == StandardWatchEventKinds.ENTRY_MODIFY && "config.txt".equals(event.context().toString())){
                            System.out.println("config.txt je izmijenjen.");
                            try {
                                FileReader read = new FileReader(currentDir + projectPath + "config.txt");
                                BufferedReader reader = new BufferedReader(read);
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    String[] linija = line.split("-");
                                    if (linija.length == 2) {
                                        String kljuc = linija[0];
                                        String value = linija[1];
                                        Boolean radi = "True".equalsIgnoreCase(value);
                                        if ("PT1".equalsIgnoreCase(kljuc)) {
                                            pt1.setuFunkciji(radi);
                                        } else if ("PT2".equalsIgnoreCase(kljuc)) {
                                            pt2.setuFunkciji(radi);
                                        } else if ("PT3".equalsIgnoreCase(kljuc)) {
                                            pt3.setuFunkciji(radi);
                                        } else if ("CT1".equalsIgnoreCase(kljuc)) {
                                            ct1.setuFunkciji(radi);
                                        } else if ("CT2".equalsIgnoreCase(kljuc)) {
                                            ct2.setuFunkciji(radi);
                                        }
                                    }
                                }
                                reader.close();
                            } catch (Exception e) {
                                MojLogger.log(Level.SEVERE, "Neuspjesno citanje iz fajla config.txt.", e);
                            }
                        }
                    }
                    key.reset();
                }
                try{
                    sleep(311);
                }catch (InterruptedException e){
                    MojLogger.log(Level.SEVERE, "Uspavljivanje u FileWatcher tredu.", e);
                }
            }
        }catch(IOException | InterruptedException e){
            MojLogger.log(Level.SEVERE, "-------", e);
        }
    }
}
