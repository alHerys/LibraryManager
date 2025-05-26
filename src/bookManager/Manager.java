package bookManager;

import person.*;
import java.io.*;
import java.util.*;

public class Manager {
    private List<Buku<?>> daftarBuku;
    private Set<String> daftarGenre;
    private Queue<Buku<?>> pinjamanBuku;
    private Set<Person> daftarAnggota;
    private Map<String, Integer> jumlahGenrePerBuku;

    private final String USERS_FILE = "users.txt";
    private final String BOOKS_FILE = "books.txt";

    public Manager() {
        this.daftarBuku = new ArrayList<>();
        this.daftarAnggota = new HashSet<>();
        this.pinjamanBuku = new LinkedList<>();
        this.daftarGenre = new HashSet<>();
        this.jumlahGenrePerBuku = new HashMap<>();
        loadUsers();
        loadBooks();
        updateGenreData();
    }

    private void updateGenreData() {
        daftarGenre.clear();
        jumlahGenrePerBuku.clear();
        List<Buku<?>> allBooks = new ArrayList<>(daftarBuku);
        allBooks.addAll(pinjamanBuku);

        for (Buku<?> buku : allBooks) {
            if (buku.getGenre() != null) {
                String genreName = buku.getGenre().toString();
                daftarGenre.add(genreName);
                jumlahGenrePerBuku.merge(genreName, 1, Integer::sum);
            }
        }
    }

    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            System.out.println("File pengguna (" + USERS_FILE + ") tidak ditemukan. Akan dibuat saat ada pendaftaran baru.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Person person = Person.fromFileString(line);
                if (person != null) {
                    daftarAnggota.add(person);
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal memuat pengguna dari file: " + e.getMessage());
        }
    }

    private void addUserToFile(Person person) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(person.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Gagal menambahkan pengguna ke file: " + e.getMessage());
        }
    }

    private boolean checkIdUsers(String id) {
        File file = new File(USERS_FILE);
        if (!file.exists()) return false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";", -1);
                if (parts.length > 1 && parts[1].equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error saat mengecek ID pengguna di file: " + e.getMessage());
        }
        return false;
    }

    private void loadBooks() {
        File file = new File(BOOKS_FILE);
        if (!file.exists()) {
            System.out.println("File buku (" + BOOKS_FILE + ") tidak ditemukan. Daftar buku akan kosong.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Buku<String> buku = Buku.mengambilDariFile(line);
                if (buku != null) {
                    if (buku.getStatusPeminjaman()) {
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

    public void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE, false))) {
            for (Buku<?> buku : daftarBuku) {
                writer.write(buku.formatUntukKeFile());
                writer.newLine();
            }
            for (Buku<?> buku : pinjamanBuku) {
                writer.write(buku.formatUntukKeFile());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Gagal menyimpan buku ke file: " + e.getMessage());
        }
    }

    public boolean mendaftarAnggota(Person anggota) {
        if (checkIdUsers(anggota.getId())) {
            System.out.println("Username '" + anggota.getId() + "' sudah diambil. Gagal mendaftarkan.");
            return false;
        }
        for (Person p : daftarAnggota) {
            if (p.getId().equals(anggota.getId())) {
                System.out.println("Username '" + anggota.getId() + "' sudah ada di memori. Gagal mendaftarkan.");
                return false;
            }
        }

        daftarAnggota.add(anggota);
        addUserToFile(anggota);
        System.out.println("Anggota " + anggota.getNama() + " (" + anggota.getRole() + ") dengan ID " + anggota.getId() + " berhasil didaftarkan.");
        return true;
    }

    public Person mencariAnggota(String id) {
        for (Person anggota : daftarAnggota) {
            if (anggota.getId().equals(id)) {
                return anggota;
            }
        }
        return null;
    }

    public void mengirimBuku(Buku<?> book, Person pengguna) {
        if (!"Dosen".equals(pengguna.getRole())) {
            System.out.println("Hanya Dosen yang boleh mengirim (menambah) buku!");
            return;
        }
        boolean idExists = daftarBuku.stream().anyMatch(b -> b.getID() == book.getID()) ||
                pinjamanBuku.stream().anyMatch(b -> b.getID() == book.getID());

        if (idExists) {
            System.out.println("Buku dengan ID " + book.getID() + " sudah ada. Tidak dapat menambahkan.");
            return;
        }

        daftarBuku.add(book);
        updateGenreData();
        saveBooksToFile();
        System.out.println("Buku '" + book.getJudul() + "' sudah berhasil ditambahkan oleh " + pengguna.getNama() + ".");
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
            if (!bukuToBorrow.getStatusPeminjaman()) {
                daftarBuku.remove(bukuToBorrow);
                bukuToBorrow.setStatusPeminjaman(true);
                bukuToBorrow.setPeminjamBuku(peminjam);
                pinjamanBuku.add(bukuToBorrow);
                saveBooksToFile();
                System.out.println("Buku '" + bukuToBorrow.getJudul() + "' berhasil dipinjam oleh " + peminjam.getNama() + ".");
            } else {
                System.out.println("Buku dengan ID " + ID + " statusnya sudah dipinjam (data inkonsisten).");
            }
        } else {
            boolean alreadyBorrowedByOther = pinjamanBuku.stream().anyMatch(b -> b.getID() == ID);
            if(alreadyBorrowedByOther) {
                System.out.println("Buku dengan ID " + ID + " sedang dipinjam.");
            } else {
                System.out.println("Buku dengan ID " + ID + " tidak ditemukan.");
            }
        }
    }

    public void mengembalikanBuku(int ID) {
        Buku<?> bukuToReturn = null;
        Iterator<Buku<?>> iterator = pinjamanBuku.iterator();
        while(iterator.hasNext()){
            Buku<?> buku = iterator.next();
            if (ID == buku.getID()){
                bukuToReturn = buku;
                iterator.remove();
                break;
            }
        }

        if (bukuToReturn != null) {
            bukuToReturn.setStatusPeminjaman(false);
            bukuToReturn.setPeminjamBuku(null);
            daftarBuku.add(bukuToReturn);
            saveBooksToFile();
            System.out.println("Buku '" + bukuToReturn.getJudul() + "' berhasil dikembalikan.");
        } else {
            System.out.println("Buku dengan ID " + ID + " tidak ditemukan dalam daftar pinjaman.");
        }
    }

    public void menampilkanSemuaBuku() {
        List<Buku<?>> semuaBukuPerpustakaan = new ArrayList<>(daftarBuku);
        semuaBukuPerpustakaan.addAll(pinjamanBuku);

        if (semuaBukuPerpustakaan.isEmpty()) {
            System.out.println("Belum ada buku di perpustakaan.");
            return;
        }
        System.out.println("\n--- Daftar Semua Buku di Perpustakaan ---");
        for (Buku<?> buku : semuaBukuPerpustakaan.stream().sorted(Comparator.comparingInt(Buku::getID)).toList()) {
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

    public void menampilkanBukuDipinjam() {
        System.out.println("\n--- Daftar Buku yang Sedang Dipinjam ---");
        if (pinjamanBuku.isEmpty()) {
            System.out.println("Tidak ada buku yang sedang dipinjam.");
            return;
        }
        for (Buku<?> buku : pinjamanBuku.stream().sorted(Comparator.comparingInt(Buku::getID)).toList()) {
            System.out.println("--------------------");
            System.out.println(buku.toString());
            Person peminjam = buku.getPeminjamBuku();
            if (peminjam != null) {
                System.out.println("Dipinjam oleh:");
                System.out.println("  Nama   : " + peminjam.getNama());
                System.out.println("  Role   : " + peminjam.getRole());
                if (peminjam instanceof Dosen dosen) {
                    System.out.println("  NIP    : " + dosen.getNip());
                } else if (peminjam instanceof Mahasiswa mahasiswa) {
                    System.out.println("  NIM    : " + mahasiswa.getNim());
                }
            } else {
                System.out.println("Dipinjam oleh: Informasi peminjam tidak tersedia.");
            }
        }
        System.out.println("--- Akhir Daftar Buku Dipinjam ---");
    }

    public void menampilkanGenre() {
        if (daftarGenre.isEmpty()) {
            System.out.println("Genre kosong, belum ada buku yang ditambahkan atau dimuat.");
            return;
        }
        int count = 1;
        System.out.println("\n--- Daftar Genre Buku ---");
        for (String genre: daftarGenre.stream().sorted().toList()) {
            System.out.println(count + ". " + genre);
            count++;
        }
        System.out.println("--- Akhir Daftar Genre ---");
    }

    public List<Buku<?>> mencariBukuLewatJudul(String judul) {
        List<Buku<?>> daftarBukuCocok = new ArrayList<>();
        List<Buku<?>> semuaBukuPerpustakaan = new ArrayList<>(daftarBuku);
        semuaBukuPerpustakaan.addAll(pinjamanBuku);

        for (Buku<?> buku: semuaBukuPerpustakaan){
            if (judul != null && !judul.trim().isEmpty()){
                if (buku.getJudul().toLowerCase().contains(judul.trim().toLowerCase())) {
                    daftarBukuCocok.add(buku);
                }
            } else {
                daftarBukuCocok.add(buku);
            }
        }
        return daftarBukuCocok;
    }

    public void statistikGenre() {
        if (jumlahGenrePerBuku.isEmpty()) {
            System.out.println("Statistik genre tidak tersedia (tidak ada buku atau genre).");
            return;
        }
        System.out.println();
        int maks = 0;
        for (Integer count : jumlahGenrePerBuku.values()) {
            if (count != null && count > maks) {
                maks = count;
            }
        }

        System.out.println("STATISTIK JUMLAH BUKU PER GENRE");

        for (int i = maks; i>= -1; i--) {
            if (i > 0) {
                System.out.printf("%-3d %c%2c", i, '|', ' ');
                for (String m : jumlahGenrePerBuku.keySet().stream().sorted().toList()) {
                    Integer currentCount = jumlahGenrePerBuku.get(m);
                    if (currentCount != null && i <= currentCount) {
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
                for (String k : jumlahGenrePerBuku.keySet().stream().sorted().toList()) {
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
                for (String m : jumlahGenrePerBuku.keySet().stream().sorted().toList()) {
                    System.out.printf("%s   ", m);
                }
            }
            System.out.println();
        }
    }

    public void tampilkanInformasiGenre() {
        System.out.println("\n========== INFORMASI GENRE ==========");
        menampilkanGenre();
        statistikGenre();
        System.out.println("=====================================");
    }
}