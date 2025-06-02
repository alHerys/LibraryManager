package person;

/**
 * Merepresentasikan seorang Dosen, yang merupakan turunan dari kelas {@link Person}.
 * Kelas ini menyimpan informasi spesifik untuk dosen seperti NIP dan fakultas.
 */
public class Dosen extends Person {
    private String nip;
    private String fakultas;

    /**
     * Konstruktor untuk membuat objek {@code Dosen} baru.
     *
     * @param nama     nama lengkap dosen.
     * @param kelamin  jenis kelamin dosen.
     * @param alamat   alamat dosen.
     * @param id       ID pengguna (username) dosen.
     * @param password password akun dosen.
     * @param nip      Nomor Induk Pegawai (NIP) dosen.
     * @param fakultas fakultas tempat dosen mengajar.
     */
    public Dosen(String nama, String kelamin, String alamat, String id, String password, String nip, String fakultas) {
        super(nama, kelamin, alamat, id, password, "Dosen"); // Memanggil konstruktor kelas induk dengan role "Dosen"
        this.nip = nip;
        this.fakultas = fakultas;
    }

    /**
     * Mendapatkan Nomor Induk Pegawai (NIP) dosen.
     *
     * @return NIP dosen.
     */
    public String getNip() {
        return nip;
    }

    /**
     * Mengembalikan representasi string dari data dosen untuk disimpan ke file.
     * Format ini melanjutkan format dari {@link Person#toBaseFileString()} dengan menambahkan
     * data spesifik dosen (NIP, fakultas) yang dipisahkan titik koma.
     *
     * @return string yang diformat untuk penyimpanan file.
     */
    @Override
    public String toFileString() {
        return super.toBaseFileString() + ";" +
                nip + ";" +
                fakultas;
    }

    /**
     * Mengembalikan representasi string dari objek dosen, diformat untuk tampilan.
     *
     * @return string yang diformat berisi detail dosen.
     */
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
                """, getNama(), nip, getId(), getJenisKelamin(), getAlamat(), fakultas, getRole());
    }
}