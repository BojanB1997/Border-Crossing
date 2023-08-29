package Vozila;

import java.util.Random;

public class Kamion extends Vozilo{
    private Boolean potrebnaDokumentacija;
    private float deklarisanaMasa;
    private float stvarnaMasa;
    private Boolean vecaStvarnaMasa;

    Random random = new Random();
    private float min = 5.1f;
    private float max = 11.5f;
    Integer brojPutnika;

    public Kamion(Object[][] mapa){
        super(mapa);
        brojPutnika = random.nextInt(3) + 1;
        this.setBrojPutnika(brojPutnika);
        this.setVrijemeProcesuiranja(500);
        this.setOznaka("K");
        this.setBoja("yellow");
        this.setPotrebnaDokumentacija(isVjerovatnoca(50));
        this.setDeklarisanaMasa(min + random.nextFloat() * (max - min));
        this.setVecaStvarnaMasa(isVjerovatnoca(20));
        if(getVecaStvarnaMasa()){
            this.setStvarnaMasa(getDeklarisanaMasa() * (random.nextFloat() * 0.3f));
        }
    }

    public void setPotrebnaDokumentacija(Boolean potrebnaDokumentacija) {
        this.potrebnaDokumentacija = potrebnaDokumentacija;
    }

    public Boolean getPotrebnaDokumentacija() {
        return potrebnaDokumentacija;
    }

    public void setDeklarisanaMasa(float deklarisanaMasa){
        this.deklarisanaMasa = deklarisanaMasa;
    }

    public float getDeklarisanaMasa() {
        return deklarisanaMasa;
    }

    public void setStvarnaMasa(float stvarnaMasa){
        this.stvarnaMasa = stvarnaMasa;
    }

    public float getStvarnaMasa() {
        return stvarnaMasa;
    }

    public void setVecaStvarnaMasa(Boolean vecaStvarnaMasa) {
        this.vecaStvarnaMasa = vecaStvarnaMasa;
    }

    public Boolean getVecaStvarnaMasa() {
        return vecaStvarnaMasa;
    }

    @Override
    public String toString(){
        return "Kamion - broj putnika: " + brojPutnika;
    }
}
