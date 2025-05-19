package Person;

public class Dosen extends Person {
    private String nip;
    private String fakultas;

    public Dosen(String nip, String fakultas) {
        this.nip = nip;
        this.fakultas = fakultas;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    @Override
    public String toString() {
        return "";
    }
}