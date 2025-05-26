package main;

import bookManager.*;
import person.*;
import java.io.File;
import java.util.*;

public class Main {
    private static Manager libraryManager;
    private static Scanner scanner = new Scanner(System.in);
    private static Person user;

    public static void main(String[] args) {
        libraryManager = new Manager();

        File booksFile = new File("books.txt");
        File usersFile = new File("users.txt");

        if (!usersFile.exists()){
            try {
                usersFile.createNewFile();
            } catch (Exception e) {
                System.err.println("Gagal membuat file users.txt awal: " + e.getMessage());
            }
        }

        if (!booksFile.exists()) {
            try {
                booksFile.createNewFile();
            } catch (Exception e) {
                System.err.println("Gagal membuat file books.txt awal: " + e.getMessage());
            }
        }

        while (true) {
            mainMenu();
            int choice;
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
        String nama = scanner.nextLine().trim();
        System.out.print("Jenis Kelamin (Laki-laki/Perempuan): ");
        String kelamin = scanner.nextLine().trim();
        System.out.print("Alamat: ");
        String alamat = scanner.nextLine().trim();
        System.out.print("Username: ");
        String id = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (nama.isEmpty() || kelamin.isEmpty() || alamat.isEmpty() || id.isEmpty() || password.isEmpty()) {
            System.out.println("Semua field wajib diisi.");
            return;
        }
        if (id.contains(";") || password.contains(";") || nama.contains(";") || kelamin.contains(";") || alamat.contains(";")) {
            System.out.println("Data tidak boleh mengandung karakter ';'.");
            return;
        }

        Person newUser;

        if (pilihanInt == 1) {
            System.out.print("NIP: ");
            String nip = scanner.nextLine().trim();
            System.out.print("Fakultas: ");
            String fakultas = scanner.nextLine().trim();
            if (nip.isEmpty() || fakultas.isEmpty() || nip.contains(";") || fakultas.contains(";")) {
                System.out.println("NIP dan Fakultas wajib diisi dan tidak boleh mengandung ';'.");
                return;
            }

//            Polimorfisme Person yakni menjadi Dosen
            newUser = new Dosen(nama, kelamin, alamat, id, password, nip, fakultas);

        } else if (pilihanInt == 2) {
            System.out.print("NIM: ");
            String nim = scanner.nextLine().trim();
            System.out.print("Fakultas: ");
            String fakultas = scanner.nextLine().trim();
            System.out.print("Program Studi: ");
            String prodi = scanner.nextLine().trim();
            if (nim.isEmpty() || fakultas.isEmpty() || prodi.isEmpty() ||
                    nim.contains(";") || fakultas.contains(";") || prodi.contains(";")) {
                System.out.println("NIM, Fakultas, dan Prodi wajib diisi dan tidak boleh mengandung ';'.");
                return;
            }

//            Polimorfisme Person yakni menjadi Mahasiswa
            newUser = new Mahasiswa(nama, kelamin, alamat, id, password, nim, fakultas, prodi);

        } else {
            System.out.println("Pilihan peran tidak valid.");
            return;
        }

        if (libraryManager.mendaftarAnggota(newUser)) {
            System.out.println("Silahkan login!");
        }
    }

    private static void handleLogin() {
        System.out.println("\n--- Login Pengguna ---");
        String[] loginDetails = Authentikasi.login(scanner);

        if (loginDetails != null) {
            String loggedInId = loginDetails[0];
            user = libraryManager.mencariAnggota(loggedInId);

            if (user == null) {
                System.out.println("Autentikasi berhasil, tetapi detail pengguna tidak ditemukan dalam sistem list manager. Silakan hubungi admin.");
                user = null;
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
        System.out.println("6. Informasi Genre (Daftar & Statistik)");

        if (user.getRole().equals("Dosen")) {
            System.out.println("--- Menu Dosen ---");
            System.out.println("7. Tambah Buku Baru");
        }
        System.out.println("0. Logout");
        System.out.print("Masukkan pilihan Anda: ");

        int choice;
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
            case 6:
                libraryManager.tampilkanInformasiGenre();
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
        if (choice == 7) {
            handleTambahBuku();
        } else {
            System.out.println("Pilihan tidak valid untuk Dosen.");
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
                System.out.println("Status: " + (bukuLoop.getStatusPeminjaman() ? "Dipinjam" : "Tersedia"));
                if (bukuLoop.getStatusPeminjaman() && bukuLoop.getPeminjamBuku() != null) {
                    Person peminjam = bukuLoop.getPeminjamBuku();
                    System.out.println("  Dipinjam Oleh: " + peminjam.getNama() + " (" + peminjam.getRole() + ")");
                }
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
        try {
            System.out.print("ID Buku (angka unik): ");
            int idBuku = Integer.parseInt(scanner.nextLine());
            System.out.print("Judul Buku: ");
            String judulBuku = scanner.nextLine().trim();
            System.out.print("Genre: ");
            String genreBuku = scanner.nextLine().trim();
            System.out.print("Tahun Terbit: ");
            String tahunTerbitBuku = scanner.nextLine().trim();

            if (judulBuku.isEmpty() || genreBuku.isEmpty() || tahunTerbitBuku.isEmpty() ||
                    judulBuku.contains(";") || genreBuku.contains(";") || tahunTerbitBuku.contains(";") ||
                    judulBuku.contains(",") || genreBuku.contains(",")) {
                System.out.println("Judul, Genre, Tahun terbit wajib diisi dan tidak boleh mengandung ';' atau ','.");
                return;
            }

            List<String> penulisList = new ArrayList<>();
            System.out.print("Jumlah Penulis: ");
            int jmlPenulis = Integer.parseInt(scanner.nextLine());
            for(int i=0; i < jmlPenulis; i++) {
                System.out.print("Nama Penulis " + (i+1) + ": ");
                String namaPenulis = scanner.nextLine().trim();
                if (namaPenulis.isEmpty() || namaPenulis.contains(";") || namaPenulis.contains(",")) {
                    System.out.println("Nama penulis tidak boleh kosong atau mengandung ';' atau ','. Penambahan buku dibatalkan.");
                    return;
                }
                penulisList.add(namaPenulis);
            }

            Buku<String> bukuBaru = new Buku<>(idBuku, judulBuku, genreBuku, tahunTerbitBuku, false, penulisList);
            libraryManager.mengirimBuku(bukuBaru, user);

        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid untuk ID atau Jumlah Penulis. Harus berupa angka.");
        } catch (Exception e) {
            System.err.println("Terjadi kesalahan saat menambah buku: " + e.getMessage());
        }
    }
}