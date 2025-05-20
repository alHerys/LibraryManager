import java.util.*;

import Person.*;;

public class Manager {
    private List<Buku<?>> daftarBuku;
    private Set<String> daftarGenre;
    private Queue<Buku<?>> pinjamanBuku;
    private Set<Person> daftarAnggota;

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
        Person anggotaHapus = null;
        for (Person anggota : daftarAnggota) {
            if (anggota.getId().equals(id)) {
                anggotaHapus = anggota;
                break;
            }
        }

        if (anggotaHapus == null) {
            return false;
        }

        daftarAnggota.remove(anggotaHapus);
        return true;
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

    // Method Mencari Buku lewat parameter judul.
    // TO-DO: Implementasikan cara mencari lewat penulis, genre dan tahun terbit
    /* NOTE: Di dalam implementasi pengguna akan diminta untuk memasukan semua informasi mengenai buku,
        Judul, penulis, genre, dan tahun tersbit PENGGUNA DAPAT TIDAK MENGISI INFORMASI TERSEBUT, input dari
        pengguna tersebut akan digunakan sebagai
   */
    public List<Buku> mencariBukuLewatJudul(String judul) {
        List<Buku> daftarBukuCocok = new ArrayList<>();

        for (Buku<?> buku: daftarBuku){
            // Nilai inisial true
            boolean cocok = true;
            // Cek Berdasarkan Judul, jika judul kosong lewatkan
            if (judul != null){
                // Jika salah maka tidak cocok
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

   // Fungsi peminjaman buku menggunakan ID buku tersebut
   /* NOTE: Di dalam implementasi pengguna akan diminta untuk memasukan ID dari buku tersebut
      yang nantinya akan digunakan sebagai parameter untuk method ini.
   */
   public void peminjamanBuku(int ID) {
        for (Buku<?> buku: daftarBuku) {
            // Jika ID yang diberikan cocok dengan buku yang ada maka hilangkan buku dari daftarBuku dan tambahkan ke queue pinjamanBuku
            if (ID == buku.getID()) {
                daftarBuku.remove(buku);
                pinjamanBuku.add(buku);
                System.out.println("Peminjaman Berhasil!");
                break;
            }
            System.out.println("Buku dengan ID tersebut tidak ditemukan");
     }
   }

   // Fungsi pengembalian buku menggunakan ID buku tersebut
   /* NOTE: Di dalam implementasi pengguna akan diminta untuk memasukan ID dari buku tersebut
      yang nantinya akan digunakan sebagai parameter untuk method ini.
   */
   public void mengembalikanBuku(int ID) {
        for (Buku<?> buku: pinjamanBuku) {
            // Jika ID yang diberikan cocok dengan buku yang ada maka hilangkan buku dari pinjamanBuku dan tambahkan ke daftarBuku
                pinjamanBuku.remove(buku);
                daftarBuku.add(buku);
                System.out.println("Pengembalian Berhasil!");
                break;
            }
            System.out.println("Buku dengan ID tersebut tidak ditemukan");
     }

   // Fungsi menambahkan buku ke daftar buku yang hanya diberikan kepada Dosen
   /* NOTE: Di dalam implementasi, program kana mem pengguna akan diminta untuk memasukkan informasi mengenai buku
      lalu program akan menciptakan sebuah objek buku yang akan digunakan sebagai argumen
      untuk digunakan dalam method ini
   */
   public void mengirimBuku(Buku<?> book, Person pengguna) {
        if (pengguna.getClass().getName() != "Person.Dosen") {
            System.out.println("Mahasiswa tidak boleh mengirim Buku!");
            return;
        }

        daftarBuku.add(book);
        System.out.println("Buku sudah berhasil ditambahkan");
   }

   // Hanya untuk menguji fungsi
    public static void main(String[] args) {
        Dosen dosen1 = new Dosen("1","1");
        System.out.println(dosen1.getClass().getName());
        Mahasiswa mahasiswa1 = new Mahasiswa(null, null, null);
        System.out.println(mahasiswa1.getClass().getName());
        Manager manager1 = new Manager();

        Buku buku1 = new Buku<>();
        manager1.mengirimBuku(buku1,dosen1);
        manager1.mengirimBuku(buku1,mahasiswa1);
    }
}
