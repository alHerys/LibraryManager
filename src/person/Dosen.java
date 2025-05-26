package person;

public class Dosen extends Person {
    private String nip;
    private String fakultas;

    public Dosen(String nama, String kelamin, String alamat, String id, String password, String nip, String fakultas) {
        super(nama, kelamin, alamat, id, password, "Dosen");
        this.nip = nip;
        this.fakultas = fakultas;
    }

    public String getNip() {
        return nip;
    }

    @Override
    public String toFileString() {
        return super.toBaseFileString() + ";" +
                nip + ";" +
                fakultas;
    }

    @Override
    public String toString() {
        return String.format("""
                Nama Dosen      : %s
                NIP             : %s
                ID Pengguna     : %s
                Jenis Kelamin   : %s
                Alamat          : %s
                Fakultas        : %s
                Role            : %s
                """, getNama(), nip, getId(), getKelamin(), getAlamat(), fakultas, getRole());
    }
}