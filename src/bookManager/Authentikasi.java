package bookManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Kelas utilitas untuk menangani proses autentikasi pengguna.
 * Kelas ini membaca data pengguna dari file dan memvalidasi kredensial login.
 */
public class Authentikasi {
    private static final String USERS_FILE = "users.txt"; // Nama file tempat data pengguna disimpan

    /**
     * Melakukan proses login pengguna. Meminta username dan password,
     * kemudian memvalidasinya terhadap data yang tersimpan di file {@code users.txt}.
     *
     * @param scan objek {@link Scanner} untuk membaca input dari pengguna.
     * @return array string yang berisi ID pengguna dan role jika login berhasil,
     * atau {@code null} jika login gagal atau file tidak ditemukan.
     * Format array: {username, role}.
     */
    public static String[] login(Scanner scan) {
        System.out.print("Masukkan Username Anda: ");
        String username = scan.nextLine().trim();
        System.out.print("Masukkan Password Anda: ");
        String password = scan.nextLine().trim();

        File file = new File(USERS_FILE);
        if (!file.exists()) {
            System.out.println("File pengguna (" + USERS_FILE + ") tidak ditemukan. Silakan daftar terlebih dahulu.");
            return null;
        }

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String myData;
            while ((myData = fileReader.readLine()) != null) {
                if (myData.trim().isEmpty()) { // Lewati baris kosong
                    continue;
                }
                String[] myDataArr = myData.split(";", -1); // Split dengan limit -1 untuk menyertakan field kosong

                // Data pengguna minimal memiliki role, username, dan password
                if (myDataArr.length >= 3) {
                    String storedRole = myDataArr[0]; // Role ada di indeks 0
                    String storedUser = myDataArr[1]; // Username ada di indeks 1
                    String storedPass = myDataArr[2]; // Password ada di indeks 2

                    if (username.equals(storedUser)) { // Jika username cocok
                        if (password.equals(storedPass)) { // Dan jika password cocok
                            System.out.println("Authentication Success. Welcome " + storedUser + " (" + storedRole + ")!");
                            return new String[]{storedUser, storedRole}; // Kembalikan username dan role
                        } else {
                            System.out.println("Password anda salah, tolong ulangi.");
                            return null; // Password salah
                        }
                    }
                } else {
                    // Menangani baris yang formatnya salah agar program tidak crash
                    System.err.println("Peringatan: Baris terformat salah diabaikan dalam file pengguna: " + myData);
                }
            }
        } catch (FileNotFoundException e) {
            // Seharusnya sudah ditangani oleh file.exists(), tapi sebagai fallback
            System.out.println("File pengguna tidak ditemukan. Silakan daftar terlebih dahulu.");
        } catch (IOException e) {
            System.err.println("Terjadi kesalahan saat membaca file pengguna: " + e.getMessage());
        }

        // Jika loop selesai tanpa menemukan username atau password yang cocok
        System.out.println("Login gagal, pengguna tidak ditemukan atau password salah.");
        return null;
    }
}