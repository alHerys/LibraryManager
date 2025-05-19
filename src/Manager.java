import java.util.*;

public class Manager {
    private List<Buku<?>> daftarBuku;

    private Set<String> genre;

    private Queue<Buku<?>> pinjamanBuku;

    private String tahunTerbit;

    // Method Menambah Buku
    public void tambahBuku(Buku book){
        daftarBuku.add(book);
    }

    // Method Menghapus Buku lewat ID 
    public void hapusBuku(int ID) {
        daftarBuku.removeIf(buku -> buku.getID() == ID);
    }

    // Method Mencari Buku lewat Judul dari buku
    public void mencariBukuLewatID(int ID) {
        for (Buku<?> buku : daftarBuku) {
            if (buku.getID() == ID) {
                System.out.println(buku);
            }
        }
    }
}
