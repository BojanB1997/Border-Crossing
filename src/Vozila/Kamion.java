package Vozila;

import java.util.Random;

public class Kamion extends Vozilo implements MasaIDokumentacija{
    private Boolean potrebnaDokumentacija;
    private Boolean imaDokumentaciju = false;
    private float deklarisanaMasa;
    private float stvarnaMasa;
    private Boolean vecaStvarnaMasa;
    private Integer kId;
    private static Integer nextId = 1;

    Random random = new Random();
    private float min = 5.1f;
    private float max = 11.5f;
    Integer brojPutnika;

    public Kamion(Object[][] mapa){
        super(mapa);
        this.kId = nextId;
        nextId++;
        brojPutnika = random.nextInt(3) + 1;
        this.setBrojPutnika(brojPutnika);
        this.setVrijemeProcesuiranja(500);
        this.setOznaka("K");
        this.setBoja("yellow");
        for(int i = 0; i < brojPutnika; i++){
            this.listaPutnika.add(new Putnik(putnikId));
            putnikId++;
        }
        this.listaPutnika.get(0).setJeVozac(true);
        this.setPotrebnaDokumentacija(isVjerovatnoca(50));
        this.setDeklarisanaMasa(min + random.nextFloat() * (max - min));
        this.setVecaStvarnaMasa(isVjerovatnoca(20));
    }

    public void setPotrebnaDokumentacija(Boolean potrebnaDokumentacija) {
        this.potrebnaDokumentacija = potrebnaDokumentacija;
    }

    @Override
    public Boolean isPotrebnaDokumentacija() {
        return potrebnaDokumentacija;
    }

    public void setDeklarisanaMasa(float deklarisanaMasa){
        this.deklarisanaMasa = deklarisanaMasa;
    }

    public Integer getkId() {
        return kId;
    }

    @Override
    public float getDeklarisanaMasa() {
        return deklarisanaMasa;
    }

    public void setStvarnaMasa(float stvarnaMasa){
        this.stvarnaMasa = stvarnaMasa;
    }

    @Override
    public float getStvarnaMasa() {
        if(getVecaStvarnaMasa()){
            float procenat = random.nextFloat() * 0.3f;
            stvarnaMasa = getDeklarisanaMasa() * (1.0f + procenat);
        }else{
            stvarnaMasa = 4.0f + random.nextFloat() * (getDeklarisanaMasa() - 4.0f);
        }
        return stvarnaMasa;
    }

    public void setVecaStvarnaMasa(Boolean vecaStvarnaMasa) {
        this.vecaStvarnaMasa = vecaStvarnaMasa;
    }

    public Boolean getVecaStvarnaMasa() {
        return vecaStvarnaMasa;
    }

    @Override
    public void setImaDokumentaciju(Boolean imaDokumentaciju) {
        this.imaDokumentaciju = imaDokumentaciju;
    }

    @Override
    public Boolean isImaDokumentaciju() {
        return imaDokumentaciju;
    }

    @Override
    public Integer vrijemeCekanjaNaCarini(){
        return 500 + this.brojPutnika*this.getVrijemeProcesuiranja();
    }

    @Override
    public String toString(){
        return "Kamion[ID:" + getkId() + "]-Broj putnika:" + brojPutnika + "-Deklarisana masa:" + getDeklarisanaMasa()
                + "-Stvarna masa:" + getStvarnaMasa() + "-Putnici:" + listaPutnika + "-Potrebna carinska dokumentacija:"
                + isPotrebnaDokumentacija() + "-Ima dokumentaciju:" + isImaDokumentaciju();
    }
}
