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

    // Method Mencari Buku lewat parameter judul, penulis, genre dan tahun terbit
    // Belum 
    public List<Buku> mencariBukuLewatJudul(String judul) {
        List<Buku> daftarBukuCocok = new ArrayList<>();

        for (Buku buku: daftarBuku){
            // Nilai inisial true
            boolean cocok = true;
            // Cek Berdasarkan Judul, jika judul kosong lewatkan
            if (judul != null){
                if (!buku.getJudul().toLowerCase().contains(judul.trim().toLowerCase())) {
                cocok = false;
                }
            }
            
            // Jika cocok maka tambahkan ke list
            if (cocok) {
                daftarBukuCocok.add(buku);
            }
        }
        
        return daftarBukuCocok;
   }
}
