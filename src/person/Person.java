package person;

/**
 * Kelas abstrak yang merepresentasikan seseorang dalam sistem.
 * Kelas ini menyimpan informasi dasar seperti nama, jenis kelamin, alamat, ID pengguna, password, dan role.
 * Kelas ini dimaksudkan untuk di-extend oleh kelas-kelas yang lebih spesifik seperti {@link Mahasiswa} dan {@link Dosen}.
 */
public abstract class Person {
    private String nama;
    private String jenisKelamin;
    private String alamat;
    private String role; // Peran pengguna, contoh: "Mahasiswa", "Dosen"
    private String id; // ID pengguna (username)
    private String password;

    /**
     * Konstruktor untuk kelas {@code Person}.
     *
     * @param nama         nama lengkap orang tersebut.
     * @param jenisKelamin jenis kelamin (misal, "Laki-laki", "Perempuan").
     * @param alamat       alamat tempat tinggal.
     * @param id           ID pengguna (username) unik.
     * @param password     password akun pengguna.
     * @param role         peran pengguna dalam sistem (misal, "Mahasiswa", "Dosen").
     */
    public Person(String nama, String jenisKelamin, String alamat, String id, String password, String role) {
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.id = id;
        this.password = password;
        this.role = role;
    }

    /**
     * Mengembalikan representasi string dari data dasar pengguna untuk disimpan ke file.
     * Data dipisahkan dengan titik koma.
     * Format: role;id;password;nama;jenisKelamin;alamat
     *
     * @return string yang diformat berisi data dasar pengguna.
     */
    public String toBaseFileString() {
        return role + ";" +
                id + ";" +
                password + ";" +
                nama + ";" +
                jenisKelamin + ";" +
                alamat;
    }

    /**
     * Membuat objek {@code Person} (atau subclass-nya seperti {@link Dosen} atau {@link Mahasiswa})
     * dari sebuah string yang dibaca dari file. String tersebut diharapkan memiliki format
     * yang sesuai dengan output dari {@link #toFileString()} dari subclass terkait.
     *
     * @param line baris string data pengguna dari file.
     * @return objek {@code Person} (Dosen atau Mahasiswa), atau {@code null} jika parsing gagal atau role tidak dikenali.
     */
    public static Person fromFileString(String line) {
        String[] parts = line.split(";", -1); // Menggunakan -1 agar string kosong di akhir tetap dihitung
        if (parts.length < 6) {
            System.err.println("Format data pengguna tidak sesuai (kurang dari 6 bagian): " + line);
            return null;
        }

        String role = parts[0];
        String id = parts[1];
        String password = parts[2];
        String nama = parts[3];
        String kelamin = parts[4];
        String alamat = parts[5];

        // Memeriksa role dan membuat objek yang sesuai
        if ("Dosen".equals(role)) {
            if (parts.length < 8) { // Data Dosen memerlukan minimal 8 bagian (base + nip + fakultas)
                System.err.println("Format data Dosen tidak sesuai (kurang dari 8 bagian): " + line);
                return null;
            }
            String nip = parts[6];
            String fakultasDosen = parts[7];
            return new Dosen(nama, kelamin, alamat, id, password, nip, fakultasDosen);
        } else if ("Mahasiswa".equals(role)) {
            if (parts.length < 9) { // Data Mahasiswa memerlukan minimal 9 bagian (base + nim + fakultas + prodi)
                System.err.println("Format data Mahasiswa tidak sesuai (kurang dari 9 bagian): " + line);
                return null;
            }
            String nim = parts[6];
            String fakultasMhs = parts[7];
            String prodi = parts[8];
            return new Mahasiswa(nama, kelamin, alamat, id, password, nim, fakultasMhs, prodi);
        } else {
            System.err.println("Role tidak dikenali: " + role + " pada baris: " + line);
            return null;
        }
    }

    /**
     * Mendapatkan ID pengguna (username).
     *
     * @return ID pengguna.
     */
    public String getId() {
        return id;
    }

    /**
     * Mendapatkan nama lengkap pengguna.
     *
     * @return nama pengguna.
     */
    public String getNama() {
        return nama;
    }

    /**
     * Mendapatkan jenis kelamin pengguna.
     *
     * @return jenis kelamin pengguna.
     */
    public String getJenisKelamin() {
        return jenisKelamin;
    }

    /**
     * Mendapatkan alamat pengguna.
     *
     * @return alamat pengguna.
     */
    public String getAlamat() {
        return alamat;
    }

    /**
     * Mendapatkan role pengguna.
     *
     * @return role pengguna (misal, "Mahasiswa", "Dosen").
     */
    public String getRole() {
        return role;
    }

    /**
     * Metode abstrak untuk mendapatkan representasi string dari objek,
     * yang harus diimplementasikan oleh subclass.
     *
     * @return string representasi objek.
     */
    @Override
    public abstract String toString();

    /**
     * Metode abstrak untuk mendapatkan representasi string dari objek untuk disimpan ke file,
     * yang harus diimplementasikan oleh subclass.
     *
     * @return string yang diformat untuk penyimpanan file.
     */
    public abstract String toFileString();
}