package main;

import bookManager.*;
import person.*;
import java.io.File;
import java.io.IOException; // Import IOException
import java.util.*;

/**
 * Kelas utama aplikasi Smart Library.
 * Kelas ini menangani alur program utama, interaksi dengan pengguna melalui konsol,
 * dan mengarahkan permintaan pengguna ke kelas {@link Manager}.
 */
public class Main {
    private static Manager libraryManager; // Objek manager untuk logika bisnis perpustakaan
    private static Scanner scanner = new Scanner(System.in); // Scanner untuk input pengguna
    private static Person user; // Pengguna yang sedang login, null jika tidak ada yang login

    /**
     * Metode utama (entry point) aplikasi.
     * Menginisialisasi {@link Manager}, membuat file data jika belum ada,
     * dan menjalankan loop menu utama.
     *
     * @param args argumen baris perintah (tidak digunakan).
     */
    public static void main(String[] args) {
        libraryManager = new Manager(); // Inisialisasi library manager

        // Persiapan file data pengguna dan buku
        File usersFile = new File("users.txt");
        File booksFile = new File("books.txt");

        // Buat file users.txt jika belum ada
        if (!usersFile.exists()){
            try {
                if (usersFile.createNewFile()) {
                    System.out.println("[INFO] File users.txt berhasil dibuat di lokasi default.");
                }
            } catch (IOException e) { // Tangani IOException secara spesifik
                System.err.println("[KESALAHAN] Gagal membuat file users.txt awal: " + e.getMessage());
            }
        }

        // Buat file books.txt jika belum ada
        if (!booksFile.exists()) {
            try {
                if (booksFile.createNewFile()) {
                    System.out.println("[INFO] File books.txt berhasil dibuat di lokasi default.");
                }
            } catch (IOException e) { // Tangani IOException secara spesifik
                System.err.println("[KESALAHAN] Gagal membuat file books.txt awal: " + e.getMessage());
            }
        }

        // Loop utama aplikasi
        while (true) {
            mainMenu(); // Tampilkan menu utama
            int choice;
            try {
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) {
                    System.out.println("\n(!) Pilihan tidak boleh kosong. Silakan masukkan angka pilihan menu.");
                    System.out.println("Tekan Enter untuk melanjutkan...");
                    scanner.nextLine(); // Menunggu pengguna menekan Enter
                    continue; // Kembali ke awal loop jika input kosong
                }
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n(!) Input tidak valid. Masukkan angka pilihan menu.");
                System.out.println("Tekan Enter untuk melanjutkan...");
                scanner.nextLine(); // Menunggu pengguna menekan Enter
                continue; // Kembali ke awal loop jika input bukan angka
            }

            switch (choice) {
                case 1:
                    handleRegister();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 3:
                    System.out.println("\n+-------------------------------------------------------------+");
                    System.out.println("| Terima kasih telah menggunakan Smart Library. Sampai jumpa! |");
                    System.out.println("+----------------------------------------------------------+\n");
                    scanner.close(); // Tutup scanner sebelum keluar
                    return; // Keluar dari aplikasi
                default:
                    System.out.println("\n(!) Pilihan tidak valid. Silakan coba lagi.");
            }

            // Tambahkan jeda sebelum kembali ke menu atau menampilkan menu user
            if (choice != 3 && user == null) { // Jika belum login atau gagal login/register dan bukan keluar
                System.out.println("\nTekan Enter untuk kembali ke Menu Utama...");
                scanner.nextLine();
            }

            // Loop untuk menu pengguna setelah login berhasil
            while(user != null) {
                userMenu(); // Tampilkan menu pengguna
            }
        }
    }

    /**
     * Menampilkan menu utama aplikasi kepada pengguna.
     * Output diformat agar lebih jelas dan menarik.
     */
    private static void mainMenu() {
        System.out.println("\n+----------------------------------------------------------+");
        System.out.println("|        SELAMAT DATANG DI SMART LIBRARY SYSTEM            |");
        System.out.println("+----------------------------------------------------------+");
        System.out.println("|  Silakan pilih salah satu opsi di bawah ini:             |");
        System.out.println("|                                                          |");
        System.out.println("|    1. Daftar Akun Baru                                   |");
        System.out.println("|    2. Login ke Akun                                      |");
        System.out.println("|    3. Keluar dari Aplikasi                               |");
        System.out.println("|                                                          |");
        System.out.println("+----------------------------------------------------------+");
        System.out.print(">> Masukkan pilihan Anda (1-3): ");
    }

    /**
     * Menangani proses pendaftaran anggota baru (Dosen atau Mahasiswa).
     * Meminta input data dari pengguna dan memanggil metode pendaftaran di {@link Manager}.
     * Output diformat untuk kemudahan pengisian data.
     */
    private static void handleRegister() {
        System.out.println("\n+========================================+");
        System.out.println("|       PENDAFTARAN ANGGOTA BARU       |");
        System.out.println("+========================================+");
        System.out.print(">> Daftar sebagai (1. Dosen, 2. Mahasiswa): ");
        String inputStr = scanner.nextLine();
        int pilihanInt;
        try {
            pilihanInt = Integer.parseInt(inputStr);
        } catch (NumberFormatException e) {
            System.out.println("\n(!) Pilihan peran tidak valid. Harus berupa angka (1 atau 2).");
            return; // Kembali jika input peran tidak valid
        }

        System.out.println("\n  Silakan masukkan data diri Anda:");
        System.out.println("  ---------------------------------");
        System.out.print("  Nama Lengkap                       : ");
        String nama = scanner.nextLine().trim();
        System.out.print("  Jenis Kelamin (Laki-laki/Perempuan): ");
        String kelamin = scanner.nextLine().trim();
        System.out.print("  Alamat                             : ");
        String alamat = scanner.nextLine().trim();
        System.out.print("  Username                           : ");
        String id = scanner.nextLine().trim();
        System.out.print("  Password                           : ");
        String password = scanner.nextLine().trim();

        // Validasi input dasar
        if (nama.isEmpty() || kelamin.isEmpty() || alamat.isEmpty() || id.isEmpty() || password.isEmpty()) {
            System.out.println("\n(!) Semua field wajib diisi. Pendaftaran dibatalkan.");
            return;
        }
        // Validasi karakter ilegal (titik koma)
        if (id.contains(";") || password.contains(";") || nama.contains(";") || kelamin.contains(";") || alamat.contains(";")) {
            System.out.println("\n(!) Data tidak boleh mengandung karakter ';'. Pendaftaran dibatalkan.");
            return;
        }

        Person newUser; // Objek Person yang akan dibuat

        if (pilihanInt == 1) { // Pendaftaran Dosen
            System.out.println("  ---------------------------------");
            System.out.println("  Data Spesifik Dosen:");
            System.out.print("  NIP                                : ");
            String nip = scanner.nextLine().trim();
            System.out.print("  Fakultas                           : ");
            String fakultas = scanner.nextLine().trim();

            if (nip.isEmpty() || fakultas.isEmpty() || nip.contains(";") || fakultas.contains(";")) {
                System.out.println("\n(!) NIP dan Fakultas wajib diisi dan tidak boleh mengandung ';'. Pendaftaran dibatalkan.");
                return;
            }
            newUser = new Dosen(nama, kelamin, alamat, id, password, nip, fakultas);

        } else if (pilihanInt == 2) { // Pendaftaran Mahasiswa
            System.out.println("  ---------------------------------");
            System.out.println("  Data Spesifik Mahasiswa:");
            System.out.print("  NIM                                : ");
            String nim = scanner.nextLine().trim();
            System.out.print("  Fakultas                           : ");
            String fakultas = scanner.nextLine().trim();
            System.out.print("  Program Studi                      : ");
            String prodi = scanner.nextLine().trim();

            if (nim.isEmpty() || fakultas.isEmpty() || prodi.isEmpty() ||
                    nim.contains(";") || fakultas.contains(";") || prodi.contains(";")) {
                System.out.println("\n(!) NIM, Fakultas, dan Program Studi wajib diisi dan tidak boleh mengandung ';'. Pendaftaran dibatalkan.");
                return;
            }
            newUser = new Mahasiswa(nama, kelamin, alamat, id, password, nim, fakultas, prodi);

        } else {
            System.out.println("\n(!) Pilihan peran tidak valid. Pilih 1 untuk Dosen atau 2 untuk Mahasiswa.");
            return; // Kembali jika pilihan peran tidak ada
        }

        System.out.println("\n  Sedang memproses pendaftaran...");

        if (libraryManager.mendaftarAnggota(newUser)) {
            System.out.println("Silahkan login!");
        }
    }

    /**
     * Menangani proses login pengguna.
     * Memanggil metode login dari kelas {@link Authentikasi} dan mencari detail pengguna di {@link Manager}.
     * Output diformat untuk proses login yang jelas.
     */
    private static void handleLogin() {
        System.out.println("\n+========================================+");
        System.out.println("|             LOGIN PENGGUNA             |");
        System.out.println("+========================================+");
        String[] loginDetails = Authentikasi.login(scanner); // Memanggil fungsi login

        if (loginDetails != null) { // Jika login berhasil (username dan password cocok)
            String loggedInId = loginDetails[0]; // Ambil username dari hasil login
            // Cari objek Person berdasarkan ID yang berhasil login
            user = libraryManager.mencariAnggota(loggedInId);

            if (user == null) { // melakukan handle terhadap error tambahan jika terjadi kesalahan
                System.out.println("\n[PERINGATAN] Autentikasi berhasil, tetapi detail pengguna tidak dapat dimuat dari sistem.");
                System.out.println("               Mohon hubungi administrator atau coba restart aplikasi.");
                user = null;
            } else {
                // Pesan sukses masuk
                System.out.println("\n   Selamat datang kembali, " + user.getNama() + "!");
                System.out.println("   Anda login sebagai " + user.getRole() + ".");
                System.out.println("\nTekan Enter untuk masuk ke Menu Pengguna...");
                scanner.nextLine();
            }
        } else {
            // Jika loginDetails null, berarti autentikasi gagal.
            // Pesan kegagalan sudah ditangani oleh Authentikasi.login()
            user = null;
        }
    }

    /**
     * Menampilkan menu khusus untuk pengguna yang telah login.
     * Menu yang ditampilkan disesuaikan dengan role pengguna (Dosen memiliki menu tambahan).
     * Output diformat agar navigasi menu lebih mudah.
     */
    private static void userMenu() {
        String userInfo = "Pengguna: " + user.getNama() + " (" + user.getRole() + ")";
        System.out.println("\n+==========================================================+");
        System.out.println("|                       MENU PENGGUNA                      |");
        System.out.printf("| %-56s |\n", userInfo); // Menampilkan informasi pengguna dengan padding
        System.out.println("+----------------------------------------------------------+");
        System.out.println("|  Pilihan Menu:                                           |");
        System.out.println("|    1. Tampilkan Semua Buku                               |");
        System.out.println("|    2. Cari Buku (berdasarkan judul)                      |");
        System.out.println("|    3. Pinjam Buku                                        |");
        System.out.println("|    4. Kembalikan Buku                                    |");
        System.out.println("|    5. Tampilkan Buku yang Saya Pinjam                    |");
        System.out.println("|    6. Informasi Genre Buku (Daftar & Statistik)          |");

        // Menu tambahan khusus untuk Dosen
        if (user.getRole().equals("Dosen")) {
            System.out.println("|----------------------- MENU DOSEN -----------------------|");
            System.out.println("|    7. Tambah Buku Baru ke Perpustakaan                   |");
            System.out.println("|----------------------------------------------------------|");
        }

        System.out.println("|    0. Logout                                             |");
        System.out.println("+----------------------------------------------------------+");
        System.out.print(">> Masukkan pilihan Anda: ");

        int choice;
        try {
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                System.out.println("\n(!) Pilihan tidak boleh kosong. Silakan masukkan angka pilihan menu.");
                System.out.println("Tekan Enter untuk melanjutkan...");
                scanner.nextLine();
                return; // Kembali ke pemanggilan userMenu() jika input kosong
            }
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("\n(!) Input tidak valid. Masukkan angka pilihan menu.");
            System.out.println("Tekan Enter untuk melanjutkan...");
            scanner.nextLine();
            return; // Kembali ke pemanggilan userMenu() jika input bukan angka
        }

        boolean shouldPause = true; // Untuk menentukan apakah perlu pause setelah aksi

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
                user = null; // Logout pengguna
                System.out.println("\n[INFO] Anda telah berhasil logout dari sistem.");
                return;
            default:
                // Tangani pilihan yang mungkin khusus untuk Dosen
                if ("Dosen".equals(user.getRole())) {
                    shouldPause = handleDosenMenu(choice); // handleDosenMenu akan return true jika ada aksi, false jika tidak valid
                } else {
                    System.out.println("\n(!) Pilihan tidak valid. Silakan coba lagi.");
                }
        }

        if (shouldPause) {
            System.out.println("\nTekan Enter untuk kembali ke Menu Pengguna...");
            scanner.nextLine();
        }
    }

    /**
     * Menangani pilihan menu khusus untuk Dosen.
     * Saat ini hanya ada satu pilihan tambahan (tambah buku).
     *
     * @param choice pilihan yang dimasukkan oleh Dosen.
     * @return boolean true jika aksi valid dilakukan, false jika pilihan tidak valid.
     */
    private static boolean handleDosenMenu(int choice) {
        if (choice == 7) { // Pilihan untuk menambah buku
            handleTambahBuku();
            return true; // Aksi valid
        } else {
            System.out.println("\n(!) Pilihan tidak valid untuk Dosen. Silakan coba lagi.");
            return false; // Pilihan tidak valid
        }
    }

    /**
     * Menangani proses pencarian buku berdasarkan judul.
     * Meminta input query judul dari pengguna dan menampilkan hasilnya dengan format yang lebih baik.
     */
    private static void handleCariBuku() {
        System.out.println("\n+========================================+");
        System.out.println("|          PENCARIAN BUKU              |");
        System.out.println("+========================================+");
        System.out.print(">> Masukkan judul buku yang dicari (atau bagian dari judul): ");
        String queryJudul = scanner.nextLine();
        List<Buku<?>> bukuKetemu = libraryManager.mencariBukuLewatJudul(queryJudul);

        if (bukuKetemu.isEmpty()) {
            System.out.println("\n(!) Tidak ada buku yang cocok dengan pencarian \"" + queryJudul + "\".");
        } else {
            System.out.println("\n--- Hasil Pencarian Buku untuk \"" + queryJudul + "\" ---");
            int count = 1;
            for (Buku<?> bukuLoop : bukuKetemu) {
                System.out.println("\n   Buku Ke-" + count++);
                System.out.println(bukuLoop.toString());
                System.out.println("   Status         : " + (bukuLoop.getStatusPeminjaman() ? "Dipinjam" : "Tersedia"));
                if (bukuLoop.getStatusPeminjaman() && bukuLoop.getPeminjamBuku() != null) {
                    Person peminjam = bukuLoop.getPeminjamBuku();
                    System.out.println("   Dipinjam Oleh  : " + peminjam.getNama() + " (" + peminjam.getRole() + ")");
                }
                System.out.println("   ------------------------------------");
            }
            System.out.println("\n--- Akhir Hasil Pencarian ---");
        }
    }

    /**
     * Menangani proses peminjaman buku.
     * Meminta ID buku dari pengguna dan memanggil metode peminjaman di {@link Manager}.
     */
    private static void handlePinjamBuku() {
        System.out.println("\n+========================================+");
        System.out.println("|            PEMINJAMAN BUKU             |");
        System.out.println("+========================================+");
        System.out.print(">> Masukkan ID Buku yang ingin dipinjam: ");
        try {
            int bukuID = Integer.parseInt(scanner.nextLine());
            System.out.println("\n   Memproses permintaan peminjaman...");
            libraryManager.peminjamanBuku(bukuID, user); // user adalah pengguna yang sedang login
        } catch (NumberFormatException e) {
            System.out.println("\n(!) ID Buku tidak valid. Harus berupa angka.");
        }
    }

    /**
     * Menangani proses pengembalian buku.
     * Meminta ID buku dari pengguna dan memanggil metode pengembalian di {@link Manager}.
     */
    private static void handleKembalikanBuku() {
        System.out.println("\n+========================================+");
        System.out.println("|           PENGEMBALIAN BUKU            |");
        System.out.println("+========================================+");
        System.out.print(">> Masukkan ID Buku yang ingin dikembalikan: ");
        try {
            int bukuID = Integer.parseInt(scanner.nextLine());
            System.out.println("\n   Memproses permintaan pengembalian...");
            libraryManager.mengembalikanBuku(bukuID);
        } catch (NumberFormatException e) {
            System.out.println("\n(!) ID Buku tidak valid. Harus berupa angka.");
        }
    }

    /**
     * Menangani proses penambahan buku baru oleh Dosen.
     * Meminta detail buku dari Dosen dan memanggil metode penambahan di {@link Manager}.
     * Output diformat untuk kemudahan input data buku.
     */
    private static void handleTambahBuku() {
        System.out.println("\n+========================================+");
        System.out.println("|      TAMBAH BUKU BARU (DOSEN)          |");
        System.out.println("+========================================+");
        try {
            System.out.println("  Silakan masukkan detail buku baru:");
            System.out.println("  ---------------------------------");
            System.out.print("  ID Buku (angka unik)           : ");
            int idBuku = Integer.parseInt(scanner.nextLine());

            System.out.print("  Judul Buku                     : ");
            String judulBuku = scanner.nextLine().trim();
            System.out.print("  Genre Buku                     : ");
            String genreBuku = scanner.nextLine().trim();
            System.out.print("  Tahun Terbit Buku              : ");
            String tahunTerbitBuku = scanner.nextLine().trim();

            if (judulBuku.isEmpty() || genreBuku.isEmpty() || tahunTerbitBuku.isEmpty() ||
                    judulBuku.contains(";") || genreBuku.contains(";") || tahunTerbitBuku.contains(";") ||
                    judulBuku.contains(",") || genreBuku.contains(",")) {
                System.out.println("\n(!) Judul, Genre, Tahun terbit wajib diisi dan tidak boleh mengandung ';' atau ','. Penambahan buku dibatalkan.");
                return;
            }

            System.out.print("  Jumlah Penulis (min. 1)        : ");
            int jmlPenulis = Integer.parseInt(scanner.nextLine());
            if (jmlPenulis <= 0) {
                System.out.println("\n(!) Jumlah penulis harus lebih dari 0. Penambahan buku dibatalkan.");
                return;
            }

            List<String> penulisList = new ArrayList<>();
            System.out.println("  ---------------------------------");
            System.out.println("  Masukkan Nama Penulis:");
            for(int i=0; i < jmlPenulis; i++) {
                System.out.print("    Penulis " + (i+1) + "                   : ");
                String namaPenulis = scanner.nextLine().trim();
                if (namaPenulis.isEmpty() || namaPenulis.contains(";") || namaPenulis.contains(",")) {
                    System.out.println("\n(!) Nama penulis tidak boleh kosong atau mengandung ';' atau ','. Penambahan buku dibatalkan.");
                    return;
                }
                penulisList.add(namaPenulis);
            }
            System.out.println("  ---------------------------------");

            // Membuat objek buku baru dengan genrenya
            Buku<String> bukuBaru = new Buku<>(idBuku, judulBuku, genreBuku, tahunTerbitBuku, false, penulisList);
            System.out.println("\n   Menambahkan buku ke sistem...");
            libraryManager.mengirimBuku(bukuBaru, user); // user adalah Dosen yang sedang login
        } catch (NumberFormatException e) {
            System.out.println("\n(!) Input tidak valid untuk ID Buku atau Jumlah Penulis. Harus berupa angka.");
        } catch (Exception e) {
            System.err.println("\n[KESALAHAN SISTEM] Terjadi kesalahan tak terduga saat menambah buku: " + e.getMessage());
        }
    }
}