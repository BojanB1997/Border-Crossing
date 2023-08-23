package Terminal;

public class PolicijskiTerminal {
    private Boolean uFunkciji = true;

    public PolicijskiTerminal(Boolean uFunkciji){
        this.uFunkciji = uFunkciji;
    }

    public void setuFunkciji(Boolean uFunkciji) {
        this.uFunkciji = uFunkciji;
    }

    public Boolean getuFunkciji() {
        return uFunkciji;
    }
}
