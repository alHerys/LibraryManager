import java.util.ArrayList;
import java.util.List;

public class Buku<T> {
    private int ID;
    private String judul;
    private List<String> penulis;
    private T genre;
    private String tahunTerbit;

    public Buku() {
        this.penulis = new ArrayList<>();
    }

    @Override
    public String toString() {
        return String.format("""
                
                """);
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

    public void setTahunTerbit(String tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }
}
