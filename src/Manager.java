import Person.*; 

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*; 

public class Manager {
    private List<Buku<?>> daftarBuku;
    private Set<String> daftarGenre;
    private Queue<Buku<?>> pinjamanBuku;
    private Set<Person> daftarAnggota;
    private Map<String, Integer> jumlahGenrePerBuku;

    public Manager() {
        this.pinjamanBuku = new LinkedList<>(); 
        this.daftarAnggota = new HashSet<>(); 
        this.daftarBuku = new ArrayList<>(); 
        this.daftarGenre = new HashSet<>(); 
        this.jumlahGenrePerBuku = new HashMap<>(); 
    }

    public void mendaftarAnggota(Person anggota) {
        daftarAnggota.add(anggota); 
        System.out.println("Anggota " + anggota.getNama() + " berhasil didaftarkan ke sistem manager.");
    }

    public boolean menghapusAnggota(String id) {
        boolean removed = daftarAnggota.removeIf(anggota -> anggota.getId().equals(id)); 
        if (removed) {
            System.out.println("Anggota dengan ID " + id + " berhasil dihapus.");
        } else {
            System.out.println("Anggota dengan ID " + id + " tidak ditemukan.");
        }
        return removed;
    }

    public Person mencariAnggota(String id) {
        for (Person anggota : daftarAnggota) { 
            if (anggota.getId().equals(id)) { 
                return anggota;
            }
        }
        return null;
    }

    public List<Buku<?>> mencariBukuLewatJudul(String judul) {
        List<Buku<?>> daftarBukuCocok = new ArrayList<>(); 

        for (Buku<?> buku: daftarBuku){ 
            boolean cocok = true;
            if (judul != null && !judul.trim().isEmpty()){ 
                if (!buku.getJudul().toLowerCase().contains(judul.trim().toLowerCase())) { 
                    cocok = false;
                }
            }
            if (cocok) {
                daftarBukuCocok.add(buku); 
            }
        }
        return daftarBukuCocok;
   }

   public void peminjamanBuku(int ID, Person peminjam) {
        Buku<?> bukuToBorrow = null;
        for (Buku<?> buku: daftarBuku) { 
            if (ID == buku.getID()) { 
                bukuToBorrow = buku;
                break;
            }
        }

        if (bukuToBorrow != null) {
            if (!bukuToBorrow.isBorrowed()) { 
                daftarBuku.remove(bukuToBorrow); 
                pinjamanBuku.add(bukuToBorrow); 
                bukuToBorrow.setBorrowed(true); 
                System.out.println("Buku '" + bukuToBorrow.getJudul() + "' berhasil dipinjam oleh " + peminjam.getNama() + ".");
            } else {
                System.out.println("Buku dengan ID " + ID + " sedang dipinjam.");
            }
        } else {
            System.out.println("Buku dengan ID " + ID + " tidak ditemukan.");
        }
   }

   public void mengembalikanBuku(int ID) {
        Buku<?> bukuToReturn = null;
        for (Buku<?> buku: pinjamanBuku) { 
            if (ID == buku.getID()){
                bukuToReturn = buku;
                break;
            }
        }
        if (bukuToReturn != null) {
            pinjamanBuku.remove(bukuToReturn); 
            daftarBuku.add(bukuToReturn); 
            bukuToReturn.setBorrowed(false); 
            System.out.println("Buku '" + bukuToReturn.getJudul() + "' berhasil dikembalikan.");
        } else {
            System.out.println("Buku dengan ID " + ID + " tidak ditemukan dalam daftar pinjaman.");
        }
   }

   public void mengirimBuku(Buku<?> book, Person pengguna) {
        if (!"Dosen".equals(pengguna.getRole())) { 
            System.out.println("Hanya Dosen yang boleh mengirim (menambah) Buku!");
            return;
        }

        for (Buku<?> existingBuku : daftarBuku) {
            if (existingBuku.getID() == book.getID()) {
                System.out.println("Buku dengan ID " + book.getID() + " sudah ada. Tidak dapat menambahkan.");
                return;
            }
        }
        
        tambahBuku(book); 
        System.out.println("Buku '" + book.getJudul() + "' sudah berhasil ditambahkan oleh " + pengguna.getNama() + ".");
   }

    public void tambahBuku(Buku<?> buku) {
        this.daftarBuku.add(buku); 
        this.daftarGenre.add(buku.getGenre().toString()); 
        jumlahGenrePerBuku.merge(buku.getGenre().toString(), 1, Integer::sum); 
    }

    public void hapusBukuByID(int id, Person pengguna) {
         if (!"Dosen".equals(pengguna.getRole())) {
            System.out.println("Hanya Dosen yang boleh menghapus Buku!");
            return;
        }

        Buku<?> bukuDihapus = null;
        String genreBukuDihapus = null;

        Iterator<Buku<?>> iterator = daftarBuku.iterator();
        while (iterator.hasNext()) {
            Buku<?> bukuLoop = iterator.next();
            if (bukuLoop.getID() == id) {
                bukuDihapus = bukuLoop;
                genreBukuDihapus = bukuLoop.getGenre().toString();
                iterator.remove();
                System.out.println("Buku '" + bukuDihapus.getJudul() + "' berhasil dihapus.");
                break;
            }
        }

        if (bukuDihapus == null) {
            System.out.println("Buku dengan ID: " + id + " tidak ditemukan.");
            return;
        }

        jumlahGenrePerBuku.merge(genreBukuDihapus, -1, Integer::sum); 
        if (jumlahGenrePerBuku.get(genreBukuDihapus) <= 0) { 
            jumlahGenrePerBuku.remove(genreBukuDihapus); 
            boolean genreMasihAda = false;
            for (Buku<?> bukuSisa : daftarBuku) {
                if (bukuSisa.getGenre().toString().equalsIgnoreCase(genreBukuDihapus)) {
                    genreMasihAda = true;
                    break;
                }
            }
            if (!genreMasihAda) {
                daftarGenre.remove(genreBukuDihapus); 
            }
        }
    }

   public void menampilkanAnggota() {
        if (daftarAnggota.isEmpty()) { 
            System.out.println("Tidak ada anggota perpustakaan yang terdaftar.");
            return;
        }

        int count = 1;
        System.out.println("\n--- Daftar Anggota Perpustakaan ---");
        for (Person anggota: daftarAnggota) { 
            System.out.println("\nAnggota " + count + ":");
            System.out.println(anggota.toString()); 
            count++;
        }
        System.out.println("--- Akhir Daftar Anggota ---");
   }

   public void menampilkanSemuaBuku() {
        if (daftarBuku.isEmpty()) {
            System.out.println("Belum ada buku di perpustakaan.");
            return;
        }
        System.out.println("\n--- Daftar Semua Buku di Perpustakaan ---");
        for (Buku<?> buku : daftarBuku) {
            System.out.println("--------------------");
            System.out.println(buku.toString());
            System.out.println("Status: " + (buku.isBorrowed() ? "Dipinjam" : "Tersedia")); 
        }
        System.out.println("--- Akhir Daftar Buku ---");
   }
    
   public void menampilkanBukuDipinjam() {
        if (pinjamanBuku.isEmpty()) {
            System.out.println("Tidak ada buku yang sedang dipinjam.");
            return;
        }
        System.out.println("\n--- Daftar Buku yang Sedang Dipinjam ---");
        for (Buku<?> buku : pinjamanBuku) {
            System.out.println("--------------------");
            System.out.println(buku.toString());
        }
        System.out.println("--- Akhir Daftar Buku Dipinjam ---");
   }


   public void menampilkanGenre() {
        if (daftarGenre.isEmpty()) { 
            System.out.println("Genre kosong, belum ada buku yang ditambahkan.");
            return;
        }

        int count = 1;
        System.out.println("\n--- Daftar Genre Buku ---");
        for (String genre: daftarGenre) { 
            System.out.println(count + ". " + genre);
            count++;
        }
        System.out.println("--- Akhir Daftar Genre ---");
   }

   public void statistikGenre() {
        if (jumlahGenrePerBuku.isEmpty()) { 
            System.out.println("Statistik genre tidak tersedia (tidak ada buku atau genre).");
            return;
        }
       System.out.println();
        int maks = 0;
        for (String n : jumlahGenrePerBuku.keySet()) { 
            if (jumlahGenrePerBuku.get(n) >= maks) { 
                maks = jumlahGenrePerBuku.get(n); 
            }
        }

       System.out.println("STATISTIK JUMLAH BUKU PER GENRE");

       for (int i = maks; i>= -1; i--) { 
           if (i > 0) {
               System.out.printf("%-3d %c%2c", i, '|', ' '); 
               for (String m : jumlahGenrePerBuku.keySet()) { 
                   if (i <= jumlahGenrePerBuku.get(m)) { 
                       for (int k = 0; k < m.length(); k++) { 
                           System.out.printf("%s", "█"); 
                       }
                       System.out.print("   "); 
                   } else {
                       for (int k = 0; k < m.length(); k++) { 
                           System.out.printf("%s", " "); 
                       }
                       System.out.print("   "); 
                   }
               }
           } else if (i == 0) { 
               ArrayList<Integer> categoryLength = new ArrayList<>(); 
               for (String k : jumlahGenrePerBuku.keySet()) { 
                   categoryLength.add(k.length()); 
               }
               System.out.printf("%7c", ' '); 
               for (int j = 0; j < jumlahGenrePerBuku.size(); j++) { 
                   for (int k = 0; k < categoryLength.get(j) + 3; k++) { 
                       System.out.printf("%s", "─"); 
                   }
               }
           } else {
               System.out.printf("%7c", ' '); 
               for (String m : jumlahGenrePerBuku.keySet()) { 
                   System.out.printf("%s   ", m); 
               }
           }
           System.out.println(); 
       }
   }

   public void populateListPengguna() {
        File file = new File("password.txt");
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String myData;
            while ((myData = fileReader.readLine()) != null) {
                String[] myDataArr = myData.split(" ", 3);
                if (myDataArr[2].equals("Dosen")){
                    Dosen objekDosen = new Dosen(myDataArr[0],myDataArr[1],myDataArr[2]);
                    daftarAnggota.add(objekDosen);
                }

                else {
                    Mahasiswa objekMahasiswa = new Mahasiswa(myDataArr[0],myDataArr[1],myDataArr[2]);
                    daftarAnggota.add(objekMahasiswa);
                }
            } 
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
   }
}