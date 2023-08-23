package Vozila;

import java.util.Random;

public class Putnik {

    private Boolean imaKofer = false;
    private Boolean nedozvoljeneStvari = false;
    private Boolean neispravniDokumenti = false;

    public Putnik()
    {
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
}
