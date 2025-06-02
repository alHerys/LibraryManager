package person;

/**
 * Merepresentasikan seorang Mahasiswa, yang merupakan turunan dari kelas {@link Person}.
 * Kelas ini menyimpan informasi spesifik untuk mahasiswa seperti NIM, fakultas, dan program studi.
 */
public class Mahasiswa extends Person {
    private String nim;
    private String fakultas;
    private String prodi;

    /**
     * Konstruktor untuk membuat objek {@code Mahasiswa} baru.
     *
     * @param nama     nama lengkap mahasiswa.
     * @param kelamin  jenis kelamin mahasiswa.
     * @param alamat   alamat mahasiswa.
     * @param id       ID pengguna (username) mahasiswa.
     * @param password password akun mahasiswa.
     * @param nim      Nomor Induk Mahasiswa.
     * @param fakultas fakultas mahasiswa.
     * @param prodi    program studi mahasiswa.
     */
    public Mahasiswa(String nama, String kelamin, String alamat, String id, String password, String nim, String fakultas, String prodi) {
        super(nama, kelamin, alamat, id, password, "Mahasiswa"); // Memanggil konstruktor kelas induk dengan role "Mahasiswa"
        this.nim = nim;
        this.fakultas = fakultas;
        this.prodi = prodi;
    }

    /**
     * Mendapatkan Nomor Induk Mahasiswa (NIM).
     *
     * @return NIM mahasiswa.
     */
    public String getNim() {
        return nim;
    }

    /**
     * Mengembalikan representasi string dari data mahasiswa untuk disimpan ke file.
     * Format ini melanjutkan format dari {@link Person#toBaseFileString()} dengan menambahkan
     * data spesifik mahasiswa (NIM, fakultas, prodi) yang dipisahkan titik koma.
     *
     * @return string yang diformat untuk penyimpanan file.
     */
    @Override
    public String toFileString() {
        return super.toBaseFileString() + ";" +
                nim + ";" +
                fakultas + ";" +
                prodi;
    }

    /**
     * Mengembalikan representasi string dari objek mahasiswa, diformat untuk tampilan.
     *
     * @return string yang diformat berisi detail mahasiswa.
     */
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