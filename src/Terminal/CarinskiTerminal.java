package Terminal;

public class CarinskiTerminal {
    private Boolean uFunkciji = true;

    public CarinskiTerminal(Boolean uFunkciji){
        this.uFunkciji = uFunkciji;
    }

    public Boolean getuFunkciji() {
        return uFunkciji;
    }

    public void setuFunkciji(Boolean uFunkciji) {
        this.uFunkciji = uFunkciji;
    }
}
