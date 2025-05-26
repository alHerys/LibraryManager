package person;

public class Mahasiswa extends Person {
    private String nim;
    private String fakultas;
    private String prodi;

    public Mahasiswa(String nama, String kelamin, String alamat, String id, String password, String nim, String fakultas, String prodi) {
        super(nama, kelamin, alamat, id, password, "Mahasiswa");
        this.nim = nim;
        this.fakultas = fakultas;
        this.prodi = prodi;
    }

    public String getNim() {
        return nim;
    }

    @Override
    public String toFileString() {
        return super.toBaseFileString() + ";" +
                nim + ";" +
                fakultas + ";" +
                prodi;
    }

    @Override
    public String toString() {
        return String.format("""
                Nama Mahasiswa  : %s
                NIM             : %s
                ID Pengguna     : %s
                Jenis Kelamin   : %s
                Alamat          : %s
                Fakultas        : %s
                Program Studi   : %s
                Role            : %s
                """, getNama(), nim, getId(), getJenisKelamin(), getAlamat(), fakultas, prodi, getRole());
    }
}