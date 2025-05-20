import Person.Person;

import java.util.ArrayList;
import java.util.List;

public class Buku<T> {
    private int ID;
    private String judul;
    private List<String> penulis;
    private T genre;
    private String tahunTerbit;
    private boolean isBorrowed;
    private Person peminjamBuku;

    public Buku() {
        this.penulis = new ArrayList<>();
    }

    @Override
    public String toString() {
        if (penulis.size() > 1) {
            StringBuilder daftarPenulis = new StringBuilder();
            for (int i = 0; i < penulis.size(); i++) {
                daftarPenulis.append(penulis.get(i));
                if (i < penulis.size() - 1) {
                    daftarPenulis.append(", ");
                }
            }
            return String.format("""
                Judul           : %s
                ID              : %d
                Genre           : %s
                Tahun Terbit    : %s
                Penulis         : %s
                """, judul, ID, genre.toString(), tahunTerbit, daftarPenulis.toString());
        }

        return String.format("""
                Judul           : %s
                ID              : %d
                Genre           : %s
                Tahun Terbit    : %s
                Penulis         : %s
                """, judul, ID, genre.toString(), tahunTerbit, penulis.getFirst());
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }    

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public List<String> getPenulis() {
        return penulis;
    }

    public void setPenulis(List<String> penulis) {
        this.penulis = penulis;
    }

    public T getGenre() {
        return genre;
    }

    public void setGenre(T genre) {
        this.genre = genre;
    }

    public String getTahunTerbit() {
        return tahunTerbit;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public void setTahunTerbit(String tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }
}
