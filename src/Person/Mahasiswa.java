package Person;

public class Mahasiswa extends Person{

    private String nim;
    private String fakultas;
    private String prodi;

    public Mahasiswa(String nim, String fakultas, String prodi, String nama, String kelamin, String alamat, String id, String password) {
        super(nama, kelamin, alamat, id, password, "Mahasiswa");
        this.nim = nim;
        this.fakultas = fakultas;
        this.prodi = prodi;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    @Override
    public String toString() {
        return String.format("""
                Nama Mahasiswa  : %s
                NIM             : %s
                Jenis Kelamin   : %s
                Alamat          : %s
                Fakultas        : %s
                Program Studi   : %s
                Role            : %s
                """, getNama(), nim, getKelamin(), getAlamat(), fakultas, prodi, getRole());
    }
}