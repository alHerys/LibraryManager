package bookManager;

import person.Person;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Merepresentasikan sebuah buku dalam sistem perpustakaan.
 * Kelas ini menyimpan informasi mengenai buku, termasuk ID, judul, genre,
 * tahun terbit, penulis, status peminjaman, dan peminjam buku.
 * Genre buku menggunakan tipe generik {@code T}.
 *
 * @param <T> tipe data untuk genre buku.
 */
public class Buku<T> {
    private int ID;
    private String judul;
    private List<String> penulis;
    private T genre;
    private String tahunTerbit;
    private boolean statusPeminjaman; // true jika dipinjam, false jika tersedia
    private Person peminjamBuku; // Orang yang meminjam buku, null jika tidak dipinjam

    /**
     * Konstruktor untuk membuat objek {@code Buku} baru.
     *
     * @param ID                ID unik buku.
     * @param judul             judul buku.
     * @param genre             genre buku.
     * @param tahunTerbit       tahun terbit buku.
     * @param statusPeminjaman  status peminjaman buku (true jika dipinjam, false jika tersedia).
     * @param penulis           daftar penulis buku.
     */
    public Buku(int ID, String judul, T genre, String tahunTerbit, boolean statusPeminjaman, List<String> penulis) {
        this.ID = ID;
        this.judul = judul;
        this.genre = genre;
        this.tahunTerbit = tahunTerbit;
        this.statusPeminjaman = statusPeminjaman;
        this.penulis = new ArrayList<>(penulis); // Membuat list baru untuk menghindari modifikasi eksternal
        this.peminjamBuku = null; // Awalnya, buku belum dipinjam siapa pun
    }

    /**
     * Mengembalikan representasi string dari objek buku, diformat untuk tampilan.
     *
     * @return string yang diformat berisi detail buku.
     */
    @Override
    public String toString() {
        String daftarPenulisStr = String.join(", ", penulis);
        return String.format("""
                Judul           : %s
                ID              : %d
                Genre           : %s
                Tahun Terbit    : %s
                Penulis         : %s
                """, judul, ID, genre != null ? genre.toString() : "N/A", tahunTerbit, daftarPenulisStr);
    }

    /**
     * Memformat data buku menjadi string yang dipisahkan titik koma untuk penyimpanan ke file.
     *
     * @return string yang merepresentasikan data buku untuk disimpan ke file.
     */
    public String formatUntukKeFile() {
        String penulisStr = String.join(",", penulis); // Penulis dipisahkan koma di dalam field mereka sendiri
        return ID + ";" +
                judul + ";" +
                (genre != null ? genre.toString() : "") + ";" + // Menangani genre yang null
                tahunTerbit + ";" +
                statusPeminjaman + ";" +
                penulisStr;
    }

    /**
     * Membuat objek {@code Buku} dengan mem-parsing string yang dipisahkan titik koma,
     * biasanya dibaca dari file. Diasumsikan genre adalah {@code String}.
     *
     * @param line string yang berisi data buku.
     * @return objek {@code Buku<String>} baru, atau {@code null} jika parsing gagal.
     */
    public static Buku<String> mengambilDariFile(String line) {
        String[] parts = line.split(";", -1); // -1 limit untuk menyertakan string kosong di akhir
        try {
            int id = Integer.parseInt(parts[0]);
            String judul = parts[1];
            String genreStr = parts[2];
            String tahunTerbit = parts[3];
            boolean isBorrowed = Boolean.parseBoolean(parts[4]);
            List<String> penulis = new ArrayList<>();
            if (parts.length > 5 && !parts[5].isEmpty()) { // Cek apakah bagian penulis ada dan tidak kosong
                penulis.addAll(Arrays.asList(parts[5].split(",")));
            }

            return new Buku<>(id, judul, genreStr, tahunTerbit, isBorrowed, penulis);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing nomor ID buku: " + parts[0] + " pada baris: " + line);
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Format data buku tidak sesuai (kurang dari 6 bagian): " + line);
            return null;
        }
    }

    /**
     * Mendapatkan ID buku.
     *
     * @return ID buku.
     */
    public int getID() {
        return ID;
    }

    /**
     * Mendapatkan judul buku.
     *
     * @return judul buku.
     */
    public String getJudul() {
        return judul;
    }

    /**
     * Mendapatkan genre buku.
     *
     * @return genre buku.
     */
    public T getGenre() {
        return genre;
    }

    /**
     * Mendapatkan status peminjaman buku.
     *
     * @return true jika buku sedang dipinjam, false jika tersedia.
     */
    public boolean getStatusPeminjaman() {
        return this.statusPeminjaman;
    }

    /**
     * Mengatur status peminjaman buku.
     *
     * @param statusPeminjaman status peminjaman baru.
     */
    public void setStatusPeminjaman(boolean statusPeminjaman) {
        this.statusPeminjaman = statusPeminjaman;
    }

    /**
     * Mendapatkan objek {@code Person} yang meminjam buku.
     *
     * @return peminjam buku, atau {@code null} jika buku tidak sedang dipinjam.
     */
    public Person getPeminjamBuku() {
        return peminjamBuku;
    }

    /**
     * Mengatur siapa yang meminjam buku.
     *
     * @param peminjamBuku objek {@code Person} yang meminjam buku.
     */
    public void setPeminjamBuku(Person peminjamBuku) {
        this.peminjamBuku = peminjamBuku;
    }
}