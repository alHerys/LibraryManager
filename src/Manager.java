import Person.Person;

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
    }

    public void mendaftarAnggota(Person anggota) {
        daftarAnggota.add(anggota);
    }

    public boolean menghapusAnggota(String id) {
        return daftarAnggota.removeIf(anggota -> anggota.getId().equals(id));
    }

    public Person mencariAnggota(String id) {
        Person anggotaHapus = null;
        for (Person anggota : daftarAnggota) {
            if (anggota.getId().equals(id)) {
                anggotaHapus = anggota;
                break;
            }
        }
        return anggotaHapus;
    }

    public List<Buku> mencariBukuLewatJudul(String judul) {
        List<Buku> daftarBukuCocok = new ArrayList<>();

        for (Buku<?> buku: daftarBuku){
            boolean cocok = true;
            if (judul != null){
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

   public void peminjamanBuku(int ID) {
        for (Buku<?> buku: daftarBuku) {
            if (ID == buku.getID()) {
                hapusBuku(ID);
                pinjamanBuku.add(buku);
                System.out.println("Peminjaman Berhasil!");
                break;
            }
            System.out.println("Buku dengan ID tersebut tidak ditemukan");
        }
   }

   public void mengembalikanBuku(int ID) {
        for (Buku<?> buku: pinjamanBuku) {
            pinjamanBuku.remove(buku);
            daftarBuku.add(buku);
            System.out.println("Pengembalian Berhasil!");
            break;
        }
        System.out.println("Buku dengan ID tersebut tidak ditemukan");
   }

   public void mengirimBuku(Buku<?> book, Person pengguna) {
        if (pengguna.getClass().getName() != "Person.Dosen") {
            System.out.println("Mahasiswa tidak boleh mengirim Buku!");
            return;
        }

        tambahBuku(book);
        daftarGenre.add(book.getGenre().toString());
        System.out.println("Buku sudah berhasil ditambahkan");
   }

    public void tambahBuku(Buku<?> buku) {
        this.daftarBuku.add(buku);
        this.daftarGenre.add(buku.getGenre().toString());

        // Menambahkan kategori ke Map statistik dan jumlah produk dalam kategori tersebut (fitur tambahan)
        jumlahGenrePerBuku.merge(buku.getGenre().toString(), 1, Integer::sum);

//        System.out.print("\nProduk berhasil ditambahkan:\n" + buku);
    }

    public void hapusBuku(int id) {
        Buku<?> buku = null;
        String genre = null;

        // Mencari produk yang akan dihapus
        for (Buku<?> bukuLoop : daftarBuku) {
            if (bukuLoop.getID() == id) {
                buku = bukuLoop;
                genre = bukuLoop.getGenre().toString();
//                System.out.println("Buku berhasil dihapus:\n" + bukuLoop);
                break;
            }
        }

        // Jika produk tidak ditemukan
        if (buku == null) {
//            System.out.println("Produk dengan ID: " + idProduk + " tidak ditemukan.");
            return;
        }

        // Menghapus produk dari List dan Queue
        daftarBuku.remove(buku);

        // Mengurangi jumlah produk per kategori pada Map (fitur tambahan)
        jumlahGenrePerBuku.merge(buku.getGenre().toString(), -1, Integer::sum);
        // Menghapus kategori jika produknya sudah tidak ada lagi
        if (jumlahGenrePerBuku.get(buku.getGenre().toString()) == 0) {
            jumlahGenrePerBuku.remove(buku.getGenre().toString());
        }

        // Memeriksa apakah kategori masih digunakan oleh produk lain
        boolean adaGenre = false;
        for (Buku<?> bukuLoop : daftarBuku) {
            if (bukuLoop.getGenre().toString().equalsIgnoreCase(genre)) {
                adaGenre = true;
                break;
            }
        }

        // Jika kategori tidak digunakan lagi, hapus dari Set kategori
        if (!adaGenre) {
            daftarGenre.remove(genre);
        }
    }

   public void menampilkanAnggota() {
        if (daftarAnggota.isEmpty()) {
            System.out.println("Tidak ada anggota perpustakaan");
            return;
        }

        int count = 1;
        System.out.println("Daftar anggota perpustakaan:");
        for (Person anggota: daftarAnggota) {
            System.out.println("Anggota " + count);
            System.out.println(anggota);
            count++;
        }
   }

   public void menampilkanGenre() {
        if (daftarGenre.isEmpty()) {
            System.out.println("Genre kosong");
            return;
        }

        int count = 1;
        System.out.println("Daftar genre buku:");

        for (String genre: daftarGenre) {
            System.out.println(count + ". " + genre);
            count++;
        }
   }

   public void statistikGenre() {
        if (jumlahGenrePerBuku.isEmpty()) {
            System.out.println("Genre kosong");
            return;
        }

       System.out.println();

        int maks = 0;
        for (String n : jumlahGenrePerBuku.keySet()) {
            if (jumlahGenrePerBuku.get(n) >= maks) {
                maks = jumlahGenrePerBuku.get(n);
            }
        }

       System.out.println("STATISTIK JUMLAH GENRE PER PRODUK");

       // Membuat diagram batang vertikal
       for (int i = maks; i>= -1; i--) {
           if (i > 0) {
               // Menampilkan skala y-axis dan batang diagram
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
               // Menampilkan garis pemisah sumbu x dan y
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
               // Menampilkan kategori pada sumbu x
               System.out.printf("%7c", ' ');
               for (String m : jumlahGenrePerBuku.keySet()) {
                   System.out.printf("%s   ", m);
               }
           }
           System.out.println();
       }
   }
}