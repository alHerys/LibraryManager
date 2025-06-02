package bookManager;

import person.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Kelas {@code Manager} bertanggung jawab untuk mengelola semua operasi inti
 * dalam sistem perpustakaan. Ini termasuk manajemen buku (penambahan, peminjaman,
 * pengembalian, pencarian), manajemen anggota (pendaftaran, pencarian),
 * dan interaksi dengan file untuk persistensi data buku dan pengguna.
 * Kelas ini juga mengelola informasi genre dan statistik terkait.
 */
public class Manager {
    private List<Buku<?>> daftarBuku; // Daftar buku yang tersedia di perpustakaan
    private Set<String> daftarGenre; // Kumpulan unik semua genre buku yang ada
    private Queue<Buku<?>> pinjamanBuku; // Antrian buku yang sedang dipinjam
    private Set<Person> daftarAnggota; // Kumpulan anggota perpustakaan yang terdaftar
    private Map<String, Integer> jumlahGenrePerBuku; // Peta untuk menyimpan jumlah buku per genre

    private final String USERS_FILE = "users.txt"; // Nama file untuk data pengguna
    private final String BOOKS_FILE = "books.txt"; // Nama file untuk data buku

    /**
     * Konstruktor untuk kelas {@code Manager}.
     * Menginisialisasi semua koleksi data (daftar buku, anggota, genre, dll.)
     * dan memuat data pengguna serta buku dari file jika ada.
     */
    public Manager() {
        this.daftarBuku = new ArrayList<>();
        this.daftarAnggota = new HashSet<>();
        this.pinjamanBuku = new LinkedList<>(); // Menggunakan LinkedList karena cocok untuk Queue
        this.daftarGenre = new HashSet<>();
        this.jumlahGenrePerBuku = new HashMap<>();
        loadUsers(); // Memuat data pengguna dari file
        loadBooks(); // Memuat data buku dari file
        updateGenreData(); // Memperbarui data genre berdasarkan buku yang dimuat
    }

    /**
     * Memperbarui daftar genre unik dan jumlah buku per genre.
     * Metode ini membersihkan data genre yang ada dan menghitung ulang
     * berdasarkan semua buku yang ada di {@code daftarBuku} dan {@code pinjamanBuku}.
     */
    private void updateGenreData() {
        daftarGenre.clear();
        jumlahGenrePerBuku.clear();
        List<Buku<?>> allBooks = new ArrayList<>(daftarBuku);
        allBooks.addAll(pinjamanBuku); // Gabungkan buku yang tersedia dan yang dipinjam untuk data genre lengkap

        for (Buku<?> buku : allBooks) {
            if (buku.getGenre() != null) {
                String genreName = buku.getGenre().toString();
                daftarGenre.add(genreName);
                jumlahGenrePerBuku.merge(genreName, 1, Integer::sum); // Tambah 1 ke hitungan genre, atau inisialisasi jika belum ada
            }
        }
    }

    /**
     * Memuat data pengguna dari file {@code USERS_FILE}.
     * Setiap baris dalam file diharapkan merepresentasikan satu pengguna.
     * Menggunakan {@link Person#fromFileString(String)} untuk parsing.
     */
    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            System.out.println("File pengguna (" + USERS_FILE + ") tidak ditemukan. Akan dibuat saat ada pendaftaran baru.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Lewati baris kosong
                Person person = Person.fromFileString(line);
                if (person != null) {
                    daftarAnggota.add(person);
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal memuat pengguna dari file: " + e.getMessage());
        }
    }

    /**
     * Menambahkan data pengguna baru ke file {@code USERS_FILE}.
     * Menggunakan {@link Person#toFileString()} untuk format data.
     *
     * @param person objek {@link Person} yang akan ditambahkan ke file.
     */
    private void addUserToFile(Person person) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) { // true untuk append
            writer.write(person.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Gagal menambahkan pengguna ke file: " + e.getMessage());
        }
    }

    /**
     * Memeriksa apakah ID pengguna sudah ada di dalam file {@code USERS_FILE}.
     * Ini berguna untuk mencegah duplikasi username saat pendaftaran.
     *
     * @param id ID pengguna (username) yang akan diperiksa.
     * @return {@code true} jika ID sudah ada di file, {@code false} jika tidak atau file tidak ada.
     */
    private boolean checkIdUsers(String id) {
        File file = new File(USERS_FILE);
        if (!file.exists()) return false; // Jika file tidak ada, ID dianggap belum ada
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";", -1);
                // Username ada di indeks ke-1 (setelah role)
                if (parts.length > 1 && parts[1].equals(id)) {
                    return true; // ID ditemukan
                }
            }
        } catch (IOException e) {
            System.err.println("Error saat mengecek ID pengguna di file: " + e.getMessage());
        }
        return false; // ID tidak ditemukan setelah memeriksa seluruh file
    }

    /**
     * Memuat data buku dari file {@code BOOKS_FILE}.
     * Setiap baris dalam file diharapkan merepresentasikan satu buku.
     * Menggunakan {@link Buku#mengambilDariFile(String)} untuk parsing.
     * Buku yang statusnya dipinjam akan dimasukkan ke {@code pinjamanBuku},
     * sisanya ke {@code daftarBuku}.
     */
    private void loadBooks() {
        File file = new File(BOOKS_FILE);
        if (!file.exists()) {
            System.out.println("File buku (" + BOOKS_FILE + ") tidak ditemukan. Daftar buku akan kosong.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Lewati baris kosong
                Buku<String> buku = Buku.mengambilDariFile(line); // Diasumsikan genre String saat dibaca dari file
                if (buku != null) {
                    if (buku.getStatusPeminjaman()) {
                        // TODO: Saat memuat buku yang dipinjam, idealnya data peminjam juga dimuat.
                        // Saat ini, peminjamBuku akan null setelah load dari file.
                        // Perlu mekanisme untuk menghubungkan buku pinjaman dengan objek Person peminjamnya.
                        pinjamanBuku.add(buku);
                    } else {
                        daftarBuku.add(buku);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal memuat buku dari file: " + e.getMessage());
        }
    }

    /**
     * Menyimpan semua data buku (dari {@code daftarBuku} dan {@code pinjamanBuku})
     * ke file {@code BOOKS_FILE}. File yang ada akan ditimpa.
     * Menggunakan {@link Buku#formatUntukKeFile()} untuk format data.
     */
    public void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE, false))) { // false untuk menimpa file
            // Simpan buku yang tersedia
            for (Buku<?> buku : daftarBuku) {
                writer.write(buku.formatUntukKeFile());
                writer.newLine();
            }
            // Simpan buku yang dipinjam
            for (Buku<?> buku : pinjamanBuku) {
                writer.write(buku.formatUntukKeFile());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Gagal menyimpan buku ke file: " + e.getMessage());
        }
    }

    /**
     * Mendaftarkan anggota baru ke sistem.
     * Memeriksa apakah ID pengguna sudah ada di memori atau di file.
     * Jika belum ada, anggota ditambahkan ke {@code daftarAnggota} dan {@code USERS_FILE}.
     *
     * @param anggota objek {@link Person} yang akan didaftarkan.
     * @return {@code true} jika pendaftaran berhasil, {@code false} jika gagal (misal, ID sudah ada).
     */
    public boolean mendaftarAnggota(Person anggota) {

        // Cek di file (users.txt)
        if (checkIdUsers(anggota.getId())) {
            System.out.println("Username '" + anggota.getId() + "' sudah diambil (ada di file). Gagal mendaftarkan.");
            return false;
        }

        // Cek di memori (daftarAnggota) dulu
        for (Person p : daftarAnggota) {
            if (p.getId().equals(anggota.getId())) {
                System.out.println("Username '" + anggota.getId() + "' sudah ada di memori. Gagal mendaftarkan.");
                return false;
            }
        }

        daftarAnggota.add(anggota);
        addUserToFile(anggota); // Simpan ke file
        System.out.println("Anggota " + anggota.getNama() + " (" + anggota.getRole() + ") dengan ID " + anggota.getId() + " berhasil didaftarkan.");
        return true;
    }

    /**
     * Mencari anggota berdasarkan ID pengguna (username) dari {@code daftarAnggota} yang ada di memori.
     *
     * @param id ID pengguna yang dicari.
     * @return objek {@link Person} jika ditemukan, atau {@code null} jika tidak.
     */
    public Person mencariAnggota(String id) {
        for (Person anggota : daftarAnggota) {
            if (anggota.getId().equals(id)) {
                return anggota;
            }
        }
        return null; // Anggota tidak ditemukan
    }

    /**
     * Menambahkan buku baru ke perpustakaan. Hanya pengguna dengan role "Dosen" yang bisa.
     * Memeriksa apakah ID buku sudah ada. Jika belum, buku ditambahkan ke
     * {@code daftarBuku}, data genre diperbarui, dan perubahan disimpan ke file.
     *
     * @param book     objek {@link Buku} yang akan ditambahkan.
     * @param pengguna objek {@link Person} yang melakukan penambahan (harus Dosen).
     */
    public void mengirimBuku(Buku<?> book, Person pengguna) {
        if (!"Dosen".equals(pengguna.getRole())) {
            System.out.println("Hanya Dosen yang boleh mengirim (menambah) buku!");
            return;
        }
        // Cek apakah ID buku sudah ada di daftar buku tersedia atau daftar buku dipinjam
        boolean idExists = daftarBuku.stream().anyMatch(b -> b.getID() == book.getID()) ||
                pinjamanBuku.stream().anyMatch(b -> b.getID() == book.getID());

        if (idExists) {
            System.out.println("Buku dengan ID " + book.getID() + " sudah ada. Tidak dapat menambahkan.");
            return;
        }

        daftarBuku.add(book);
        updateGenreData(); // Perbarui info genre setelah buku baru ditambahkan
        saveBooksToFile(); // Simpan perubahan ke file
        System.out.println("Buku '" + book.getJudul() + "' sudah berhasil ditambahkan oleh " + pengguna.getNama() + ".");
    }

    /**
     * Memproses peminjaman buku oleh seorang pengguna.
     * Buku dicari di {@code daftarBuku}. Jika ditemukan dan tersedia,
     * statusnya diubah menjadi dipinjam, ditambahkan ke {@code pinjamanBuku},
     * dihapus dari {@code daftarBuku}, dan perubahan disimpan ke file.
     *
     * @param ID       ID buku yang akan dipinjam.
     * @param peminjam objek {@link Person} yang meminjam buku.
     */
    public void peminjamanBuku(int ID, Person peminjam) {
        Buku<?> bukuToBorrow = null;
        // Cari buku di daftar buku yang tersedia
        for (Buku<?> buku: daftarBuku) {
            if (ID == buku.getID()) {
                bukuToBorrow = buku;
                break;
            }
        }

        if (bukuToBorrow != null) {
            // Seharusnya statusPeminjaman false jika ada di daftarBuku, tapi cek lagi untuk konsistensi
            if (!bukuToBorrow.getStatusPeminjaman()) {
                daftarBuku.remove(bukuToBorrow); // Hapus dari daftar tersedia
                bukuToBorrow.setStatusPeminjaman(true);
                bukuToBorrow.setPeminjamBuku(peminjam); // Set siapa yang meminjam
                pinjamanBuku.add(bukuToBorrow); // Tambahkan ke daftar pinjaman
                saveBooksToFile(); // Simpan perubahan
                System.out.println("Buku '" + bukuToBorrow.getJudul() + "' berhasil dipinjam oleh " + peminjam.getNama() + ".");
            } else {
                // Kasus ini seharusnya tidak terjadi jika logika konsisten
                System.out.println("Buku dengan ID " + ID + " statusnya sudah dipinjam (data inkonsisten).");
            }
        } else {
            // Cek apakah buku ada di daftar pinjaman (berarti sudah dipinjam orang lain)
            boolean alreadyBorrowedByOther = pinjamanBuku.stream().anyMatch(b -> b.getID() == ID);
            if(alreadyBorrowedByOther) {
                System.out.println("Buku dengan ID " + ID + " sedang dipinjam.");
            } else {
                System.out.println("Buku dengan ID " + ID + " tidak ditemukan.");
            }
        }
    }

    /**
     * Memproses pengembalian buku.
     * Buku dicari di {@code pinjamanBuku}. Jika ditemukan, statusnya diubah
     * menjadi tersedia, peminjam di-reset, ditambahkan ke {@code daftarBuku},
     * dihapus dari {@code pinjamanBuku}, dan perubahan disimpan ke file.
     *
     * @param ID ID buku yang akan dikembalikan.
     */
    public void mengembalikanBuku(int ID) {
        Buku<?> bukuToReturn = null;
        Iterator<Buku<?>> iterator = pinjamanBuku.iterator();
        // Cari buku di daftar buku yang dipinjam
        while(iterator.hasNext()){
            Buku<?> buku = iterator.next();
            if (ID == buku.getID()){
                bukuToReturn = buku;
                iterator.remove(); // Hapus dari daftar pinjaman
                break;
            }
        }

        if (bukuToReturn != null) {
            bukuToReturn.setStatusPeminjaman(false);
            bukuToReturn.setPeminjamBuku(null); // Reset peminjam
            daftarBuku.add(bukuToReturn); // Tambahkan kembali ke daftar tersedia
            saveBooksToFile(); // Simpan perubahan
            System.out.println("Buku '" + bukuToReturn.getJudul() + "' berhasil dikembalikan.");
        } else {
            System.out.println("Buku dengan ID " + ID + " tidak ditemukan dalam daftar pinjaman.");
        }
    }

    /**
     * Menampilkan semua buku yang ada di perpustakaan (baik yang tersedia maupun yang dipinjam).
     * Buku diurutkan berdasarkan ID.
     */
    public void menampilkanSemuaBuku() {
        List<Buku<?>> semuaBukuPerpustakaan = new ArrayList<>(daftarBuku);
        semuaBukuPerpustakaan.addAll(pinjamanBuku); // Gabungkan kedua daftar

        if (semuaBukuPerpustakaan.isEmpty()) {
            System.out.println("Belum ada buku di perpustakaan.");
            return;
        }
        System.out.println("\n--- Daftar Semua Buku di Perpustakaan ---");
        // Urutkan buku berdasarkan ID sebelum ditampilkan
        for (Buku<?> buku : semuaBukuPerpustakaan.stream().sorted(Comparator.comparingInt(Buku::getID)).collect(Collectors.toList())) {
            System.out.println("--------------------");
            System.out.println(buku.toString());
            System.out.println("Status: " + (buku.getStatusPeminjaman() ? "Dipinjam" : "Tersedia"));
            if (buku.getStatusPeminjaman() && buku.getPeminjamBuku() != null) {
                Person peminjam = buku.getPeminjamBuku();
                System.out.println("  Dipinjam Oleh: " + peminjam.getNama() + " (" + peminjam.getRole() + ")");
            }
        }
        System.out.println("--- Akhir Daftar Buku ---");
    }

    /**
     * Menampilkan semua buku yang sedang dipinjam beserta informasi peminjamnya.
     * Buku diurutkan berdasarkan ID.
     */
    public void menampilkanBukuDipinjam() {
        System.out.println("\n--- Daftar Buku yang Sedang Dipinjam ---");
        if (pinjamanBuku.isEmpty()) {
            System.out.println("Tidak ada buku yang sedang dipinjam.");
            return;
        }
        // Urutkan buku berdasarkan ID sebelum ditampilkan
        for (Buku<?> buku : pinjamanBuku.stream().sorted(Comparator.comparingInt(Buku::getID)).collect(Collectors.toList())) {
            System.out.println("--------------------");
            System.out.println(buku.toString());
            Person peminjam = buku.getPeminjamBuku();
            if (peminjam != null) {
                System.out.println("Dipinjam oleh:");
                System.out.println("  Nama   : " + peminjam.getNama());
                System.out.println("  Role   : " + peminjam.getRole());
                // Tampilkan info spesifik jika peminjam adalah Dosen atau Mahasiswa
                if (peminjam instanceof Dosen dosen) {
                    System.out.println("  NIP    : " + dosen.getNip());
                } else if (peminjam instanceof Mahasiswa mahasiswa) {
                    System.out.println("  NIM    : " + mahasiswa.getNim());
                }
            } else {
                // Seharusnya tidak terjadi jika buku ada di pinjamanBuku dan datanya konsisten
                System.out.println("Dipinjam oleh: Informasi peminjam tidak tersedia.");
            }
        }
        System.out.println("--- Akhir Daftar Buku Dipinjam ---");
    }

    /**
     * Menampilkan daftar semua genre buku yang unik dan terurut.
     * Data diambil dari {@code daftarGenre}.
     */
    private void menampilkanGenre() {
        if (daftarGenre.isEmpty()) {
            System.out.println("Genre kosong, belum ada buku yang ditambahkan atau dimuat.");
            return;
        }
        int count = 1;
        System.out.println("\n--- Daftar Genre Buku ---");
        // Urutkan genre sebelum ditampilkan
        for (String genre: daftarGenre.stream().sorted().collect(Collectors.toList())) {
            System.out.println(count + ". " + genre);
            count++;
        }
        System.out.println("--- Akhir Daftar Genre ---");
    }

    /**
     * Mencari buku berdasarkan judul (atau bagian dari judul).
     * Pencarian bersifat case-insensitive.
     *
     * @param judul query judul yang dicari. Jika kosong atau null, semua buku akan dikembalikan.
     * @return daftar {@link Buku} yang cocok dengan query judul.
     */
    public List<Buku<?>> mencariBukuLewatJudul(String judul) {
        List<Buku<?>> daftarBukuCocok = new ArrayList<>();
        List<Buku<?>> semuaBukuPerpustakaan = new ArrayList<>(daftarBuku);
        semuaBukuPerpustakaan.addAll(pinjamanBuku); // Cari di semua buku

        for (Buku<?> buku: semuaBukuPerpustakaan){
            if (judul != null && !judul.trim().isEmpty()){ // Pastikan judul pencarian tidak null atau kosong
                if (buku.getJudul().toLowerCase().contains(judul.trim().toLowerCase())) { // Pencarian case-insensitive
                    daftarBukuCocok.add(buku);
                }
            } else {
                // Jika query judul kosong, kembalikan semua buku (sesuai implementasi awal)
                // Sebaiknya ini dipertimbangkan lagi, mungkin lebih baik tidak mengembalikan apa-apa jika query kosong.
                daftarBukuCocok.add(buku);
            }
        }
        return daftarBukuCocok;
    }

    /**
     * Menampilkan statistik jumlah buku per genre dalam bentuk diagram batang sederhana di konsol.
     * Data diambil dari {@code jumlahGenrePerBuku}.
     */
    private void statistikGenre() {
        if (jumlahGenrePerBuku.isEmpty()) {
            System.out.println("Statistik genre tidak tersedia (tidak ada buku atau genre).");
            return;
        }
        System.out.println(); // Baris kosong untuk spasi
        int maks = 0; // Cari nilai maksimum untuk skala diagram
        for (Integer count : jumlahGenrePerBuku.values()) {
            if (count != null && count > maks) {
                maks = count;
            }
        }

        System.out.println("STATISTIK JUMLAH BUKU PER GENRE");

        // Loop untuk menggambar diagram dari atas ke bawah
        for (int i = maks; i>= -1; i--) { // i = -1 untuk menggambar nama genre di bawah
            if (i > 0) { // Baris untuk skala dan batang diagram
                System.out.printf("%-3d %c%2c", i, '|', ' '); // Skala Y
                for (String m : jumlahGenrePerBuku.keySet().stream().sorted().collect(Collectors.toList())) { // Iterasi genre terurut
                    Integer currentCount = jumlahGenrePerBuku.get(m);
                    if (currentCount != null && i <= currentCount) { // Jika hitungan genre >= skala saat ini, gambar blok
                        for (int k = 0; k < m.length(); k++) {
                            System.out.printf("%s", "█"); // Karakter blok
                        }
                        System.out.print("   "); // Spasi antar genre
                    } else { // Jika tidak, gambar spasi kosong
                        for (int k = 0; k < m.length(); k++) {
                            System.out.printf("%s", " ");
                        }
                        System.out.print("   ");
                    }
                }
            } else if (i == 0) { // Baris untuk sumbu X (garis horizontal)
                ArrayList<Integer> categoryLength = new ArrayList<>();
                // Kumpulkan panjang nama setiap genre untuk penyesuaian garis
                for (String k : jumlahGenrePerBuku.keySet().stream().sorted().collect(Collectors.toList())) {
                    categoryLength.add(k.length());
                }
                System.out.printf("%7c", ' '); // Spasi awal untuk menyamakan dengan skala Y
                for (int j = 0; j < jumlahGenrePerBuku.size(); j++) {
                    for (int k = 0; k < categoryLength.get(j) + 3; k++) { // +3 untuk spasi antar genre
                        System.out.printf("%s", "─"); // Karakter garis
                    }
                }
            } else { // Baris untuk label nama genre (i == -1)
                System.out.printf("%7c", ' '); // Spasi awal
                for (String m : jumlahGenrePerBuku.keySet().stream().sorted().collect(Collectors.toList())) {
                    System.out.printf("%s   ", m); // Cetak nama genre dengan spasi
                }
            }
            System.out.println(); // Pindah baris
        }
    }

    /**
     * Menampilkan informasi lengkap mengenai genre, termasuk daftar genre dan statistik jumlah buku per genre.
     */
    public void tampilkanInformasiGenre() {
        System.out.println("\n========== INFORMASI GENRE ==========");
        menampilkanGenre(); // Tampilkan daftar genre
        statistikGenre();   // Tampilkan statistik genre
        System.out.println("=====================================");
    }
}