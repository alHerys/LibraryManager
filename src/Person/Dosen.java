package Person;

public class Dosen extends Person {
    private String nip;
    private String fakultas;

    public Dosen(String nip, String fakultas,String nama, String kelamin, String alamat, String id, String password) {
        super(nama, kelamin, alamat, id, password, "Dosen"); 
        this.nip = nip;
        this.fakultas = fakultas;
    }

    // TODO: GANTI INI, CEK class PERSON
    public Dosen(String id, String password, String role) {
        super(id, password, role);
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
        return String.format("""
                Nama Dosen      : %s
                NIP             : %s
                Jenis Kelamin   : %s
                Alamat          : %s
                Fakultas        : %s
                Role            : %s
                """, getNama(), nip, getKelamin(), getAlamat(), fakultas, getRole());
    }
}