package Vozila;

import java.util.Random;

public class Putnik {

    private Integer id;
    private static Integer nextId = 1;
    private Boolean jeVozac = false;
    private Boolean imaKofer = false;
    private Boolean nedozvoljeneStvari = false;
    private Boolean neispravniDokumenti = false;
    private Boolean presaoGranicu;

    public Putnik()
    {
        this.id = nextId;
        nextId++;
        setImaKofer(isVjerovatnoca(70));
        if(getImaKofer()){
            setNedozvoljeneStvari(isVjerovatnoca(10));
        }
        setNeispravniDokumenti(isVjerovatnoca(3));
    }

    public void setImaKofer(Boolean imaKofer) {
        this.imaKofer = imaKofer;
    }

    public Boolean getImaKofer(){
        return imaKofer;
    }

    public void setNedozvoljeneStvari(Boolean nedozvoljeneStvari) {
        this.nedozvoljeneStvari = nedozvoljeneStvari;
    }

    public Boolean getNedozvoljeneStvari() {
        return nedozvoljeneStvari;
    }

    public void setNeispravniDokumenti(Boolean ispravniDokumenti) {
        this.neispravniDokumenti = ispravniDokumenti;
    }

    public Boolean getNespravniDokumenti() {
        return neispravniDokumenti;
    }

    public void setJeVozac(Boolean jeVozac) {
        this.jeVozac = jeVozac;
    }

    public Boolean getJeVozac() {
        return jeVozac;
    }

    public Integer getId() {
        return id;
    }

    public void setPresaoGranicu(Boolean presaoGranicu) {
        this.presaoGranicu = presaoGranicu;
    }

    public Boolean getPresaoGranicu() {
        return presaoGranicu;
    }

    public Boolean isVjerovatnoca(Integer vjerovatnoca){
        Random random = new Random();
        Integer broj = random.nextInt(100) + 1;
        if(broj <= vjerovatnoca){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public String toString(){
        return "Putnik[ID:" + getId() + "]-Vozac:" + jeVozac + "-Nesipravni dokumenti:" + neispravniDokumenti +
                "-Nedopustene stvari:" + nedozvoljeneStvari;
    }
}
