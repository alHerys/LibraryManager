import Person.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Manager libraryManager = new Manager();
    private static Scanner scanner = new Scanner(System.in);
    private static Person user = null;

    public static void main(String[] args) {
        tambahAwalBuku();
        libraryManager.populateListPengguna();
        while (true) {
            mainMenu();
            int choice = -1;
            try {
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) {
                    System.out.println("Pilihan tidak boleh kosong.");
                    continue;
                }
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. Masukkan angka.");
                continue;
            }

            switch (choice) {
                case 1:
                    handleRegister();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 3:
                    System.out.println("Terima kasih telah menggunakan program ini. Sampai jumpa!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
            while(user != null) {
                userMenu();
            }
        }
    }

    private static void mainMenu() {
        System.out.println("\n=============== SELAMAT DATANG DI PERPUSTAKAAN ===============");
        System.out.println("1. Daftar Akun Baru");
        System.out.println("2. Login");
        System.out.println("3. Keluar");
        System.out.print("Masukkan pilihan Anda: ");
    }

    private static void handleRegister() {
        System.out.println("\n--- Pendaftaran Anggota Baru ---");
        System.out.print("Daftar sebagai (1. Dosen, 2. Mahasiswa): ");
        String inputStr = scanner.nextLine();
        int pilihanInt;
        try {
            pilihanInt = Integer.parseInt(inputStr);
        } catch (NumberFormatException e) {
            System.out.println("Pilihan peran tidak valid.");
            return;
        }

        System.out.print("Nama Lengkap: ");
        String nama = scanner.nextLine();
        System.out.print("Jenis Kelamin (Laki-laki/Perempuan): ");
        String kelamin = scanner.nextLine();
        System.out.print("Alamat: ");
        String alamat = scanner.nextLine();
        System.out.print("ID Pengguna (username): ");
        String id = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Person newUser = null;
        String role = "";

        if (pilihanInt == 1) {
            role = "Dosen";
            System.out.print("NIP: ");
            String nip = scanner.nextLine();
            System.out.print("Fakultas: ");
            String fakultas = scanner.nextLine();
            newUser = new Dosen(nip, fakultas, nama, kelamin, alamat, id, password);
        } else if (pilihanInt == 2) {
            role = "Mahasiswa";
            System.out.print("NIM: ");
            String nim = scanner.nextLine();
            System.out.print("Fakultas: ");
            String fakultas = scanner.nextLine();
            System.out.print("Program Studi: ");
            String prodi = scanner.nextLine();
            newUser = new Mahasiswa(nim, fakultas, prodi, nama, kelamin, alamat, id, password);
        } else {
            System.out.println("Pilihan peran tidak valid.");
            return;
        }

        if (PassManager.register(id, password, role)) {
            libraryManager.mendaftarAnggota(newUser);
        } else {
            System.out.println("Gagal mendaftarkan pengguna ke sistem. Username mungkin sudah ada atau ada kesalahan lain.");
        }
    }

    private static void handleLogin() {
        System.out.println("\n--- Login Pengguna ---");
        String[] loginDetails = PassManager.login(scanner);
        System.out.println(Arrays.toString(loginDetails)); // DEBUG

        if (loginDetails != null) {
            String loggedInId = loginDetails[0];
            System.out.println(loggedInId); // DEBUG
            user = libraryManager.mencariAnggota(loggedInId);
            if (user == null) {
                System.out.println("Autentikasi berhasil, tetapi detail pengguna tidak ditemukan dalam sistem list manager.");
            }
        } else {
            user = null;
        }
    }

    private static void userMenu() {
        System.out.println("\n--- Menu Pengguna (" + user.getRole() + ": " + user.getNama() + ") ---");
        System.out.println("1. Tampilkan Semua Buku");
        System.out.println("2. Cari Buku (berdasarkan judul)");
        System.out.println("3. Pinjam Buku");
        System.out.println("4. Kembalikan Buku");
        System.out.println("5. Tampilkan Buku yang Dipinjam");

        if (user.getRole().equals("Dosen")) {
            System.out.println("--- Menu Dosen ---");
            System.out.println("6. Tambah Buku Baru");
            System.out.println("7. Hapus Buku (berdasarkan ID)");
            System.out.println("8. Tampilkan Semua Anggota");
            System.out.println("9. Tampilkan Daftar Genre");
            System.out.println("10. Tampilkan Statistik Genre");
        }
        System.out.println("0. Logout");
        System.out.print("Masukkan pilihan Anda: ");

        int choice = -1;
        try {
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                System.out.println("Pilihan tidak boleh kosong.");
                return;
            }
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid. Masukkan angka.");
            return;
        }

        switch (choice) {
            case 1:
                libraryManager.menampilkanSemuaBuku();
                break;
            case 2:
                handleCariBuku();
                break;
            case 3:
                handlePinjamBuku();
                break;
            case 4:
                handleKembalikanBuku();
                break;
            case 5:
                libraryManager.menampilkanBukuDipinjam();
                break;
            case 0:
                user = null;
                System.out.println("Anda telah logout.");
                return;
            default:
                if ("Dosen".equals(user.getRole())) {
                    handleDosenMenu(choice);
                } else {
                    System.out.println("Pilihan tidak valid.");
                }
        }
    }

    private static void handleDosenMenu(int choice) {
        switch (choice) {
            case 6:
                handleTambahBuku();
                break;
            case 7:
                handleHapusBuku();
                break;
            case 8:
                libraryManager.menampilkanAnggota();
                break;
            case 9:
                libraryManager.menampilkanGenre();
                break;
            case 10:
                libraryManager.statistikGenre();
                break;
            default:
                System.out.println("Pilihan tidak valid");
        }
    }
    
    private static void handleCariBuku() {
        System.out.print("Masukkan judul buku yang dicari (atau bagian dari judul): ");
        String queryJudul = scanner.nextLine();
        List<Buku<?>> bukuKetemu = libraryManager.mencariBukuLewatJudul(queryJudul);
        if (bukuKetemu.isEmpty()) {
            System.out.println("Tidak ada buku yang cocok dengan pencarian Anda.");
        } else {
            System.out.println("\n--- Hasil Pencarian Buku ---");
            for (Buku<?> bukuLoop : bukuKetemu) {
                System.out.println("--------------------");
                System.out.println(bukuLoop.toString());
                 System.out.println("Status: " + (bukuLoop.isBorrowed() ? "Dipinjam" : "Tersedia"));
            }
            System.out.println("--- Akhir Hasil Pencarian ---");
        }
    }

    private static void handlePinjamBuku() {
        System.out.print("Masukkan ID Buku yang ingin dipinjam: ");
        try {
            int bukuID = Integer.parseInt(scanner.nextLine());
            libraryManager.peminjamanBuku(bukuID, user);
        } catch (NumberFormatException e) {
            System.out.println("ID Buku tidak valid. Harus berupa angka.");
        }
    }

    private static void handleKembalikanBuku() {
        System.out.print("Masukkan ID Buku yang ingin dikembalikan: ");
         try {
            int bukuID = Integer.parseInt(scanner.nextLine());
            libraryManager.mengembalikanBuku(bukuID);
        } catch (NumberFormatException e) {
            System.out.println("ID Buku tidak valid. Harus berupa angka.");
        }
    }

    private static void handleTambahBuku() {
        System.out.println("\n--- Tambah Buku Baru ---");
        Buku<String> bukuBaru = new Buku<>();
        try {
            System.out.print("ID Buku (angka unik): ");
            bukuBaru.setID(Integer.parseInt(scanner.nextLine()));
            System.out.print("Judul Buku: ");
            bukuBaru.setJudul(scanner.nextLine());
            System.out.print("Genre: ");
            bukuBaru.setGenre(scanner.nextLine());
            System.out.print("Tahun Terbit: ");
            bukuBaru.setTahunTerbit(scanner.nextLine());
            
            List<String> penulisList = new ArrayList<>();
            System.out.print("Jumlah Penulis: ");
            int jmlPenulis = Integer.parseInt(scanner.nextLine());
            for(int i=0; i < jmlPenulis; i++) {
                System.out.print("Nama Penulis " + (i+1) + ": ");
                penulisList.add(scanner.nextLine());
            }
            bukuBaru.setPenulis(penulisList);
            bukuBaru.setBorrowed(false);

            libraryManager.mengirimBuku(bukuBaru, user);

        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid untuk ID atau Jumlah Penulis. Harus berupa angka.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private static void handleHapusBuku() {
        System.out.print("Masukkan ID Buku yang akan dihapus: ");
        try {
            int bukuID = Integer.parseInt(scanner.nextLine());
            libraryManager.hapusBukuByID(bukuID, user);
        } catch (NumberFormatException e) {
            System.out.println("ID Buku tidak valid. Harus berupa angka.");
        }
    }

    private static void tambahAwalBuku() {
        Buku<String> buku1 = new Buku<>();
        buku1.setID(101);
        buku1.setJudul("Pemrograman Java Dasar");
        buku1.setPenulis(List.of("Budi Doremi"));
        buku1.setGenre("Edukasi");
        buku1.setTahunTerbit("2023");
        buku1.setBorrowed(false);
        libraryManager.tambahBuku(buku1);

        Buku<String> buku2 = new Buku<>();
        buku2.setID(102);
        buku2.setJudul("Algoritma dan Struktur Data");
        buku2.setPenulis(List.of("Candra Agus", "Dewi Lestari"));
        buku2.setGenre("Komputer");
        buku2.setTahunTerbit("2022");
        buku2.setBorrowed(false);
        libraryManager.tambahBuku(buku2);
        
        Buku<String> buku3 = new Buku<>();
        buku3.setID(103);
        buku3.setJudul("Fisika Kuantum Lanjutan");
        buku3.setPenulis(List.of("Prof. Einstein"));
        buku3.setGenre("Sains");
        buku3.setTahunTerbit("2020");
        buku3.setBorrowed(false);
        libraryManager.tambahBuku(buku3);
    }
}