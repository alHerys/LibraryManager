package bookManager;

import person.Person;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Buku<T> {
    private int ID;
    private String judul;
    private List<String> penulis;
    private T genre;
    private String tahunTerbit;
    private boolean statusPeminjaman;
    private Person peminjamBuku;

    public Buku(int ID, String judul, T genre, String tahunTerbit, boolean statusPeminjaman, List<String> penulis) {
        this.ID = ID;
        this.judul = judul;
        this.genre = genre;
        this.tahunTerbit = tahunTerbit;
        this.statusPeminjaman = statusPeminjaman;
        this.penulis = new ArrayList<>(penulis);
        this.peminjamBuku = null;
    }

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

    public String formatUntukKeFile() {
        String penulisStr = String.join(",", penulis);
        return ID + ";" +
                judul + ";" +
                genre + ";" +
                tahunTerbit + ";" +
                statusPeminjaman + ";" +
                penulisStr;
    }

    public static Buku<String> mengambilDariFile(String line) {
        String[] parts = line.split(";", -1);
        try {
            int id = Integer.parseInt(parts[0]);
            String judul = parts[1];
            String genreStr = parts[2];
            String tahunTerbit = parts[3];
            boolean isBorrowed = Boolean.parseBoolean(parts[4]);
            List<String> penulis = new ArrayList<>();
            penulis.addAll(Arrays.asList(parts[5].split(",")));

            return new Buku<String>(id, judul, genreStr, tahunTerbit, isBorrowed, penulis);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing nomor ID buku: " + parts[0] + " pada baris: " + line);
            return null;
        }
    }

    public int getID() {
        return ID;
    }

    public String getJudul() {
        return judul;
    }

    public T getGenre() {
        return genre;
    }


    public boolean getStatusPeminjaman() {
        return this.statusPeminjaman;
    }

    public void setStatusPeminjaman(boolean statusPeminjaman) {
        this.statusPeminjaman = statusPeminjaman;
    }

    public Person getPeminjamBuku() {
        return peminjamBuku;
    }

    public void setPeminjamBuku(Person peminjamBuku) {
        this.peminjamBuku = peminjamBuku;
    }
}