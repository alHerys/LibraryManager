import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PassManager {

    private static final String PASSWORD_FILE = "password.txt";

    /**
     * Fungsi ini merigister sebuah pengguna, menciptakan objeknya, lalu menyimpankannya ke file text
     * Sebelum memasukkan ke text file method ini akan mengecek jika  username itu sudah ada, jika tidak tambahkan pengguna dengan format: "username password role"
     */
    public static boolean register(String username, String password, String role) {
        File file = new File(PASSWORD_FILE);

        // Cek semua line jika username sudah ada atau tidak
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String myData;
            while ((myData = fileReader.readLine()) != null) {
                String[] myDataArr = myData.split(" ", 3);
                if (myDataArr.length > 0 && username.equals(myDataArr[0])) {
                    System.out.println("Username '" + username + "' Sudah diambil, tolong cari username lain");
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Password Tidak ditemukan");
            // Tidak perlu return false di sini, karena file akan dibuat saat penulisan
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }

        // Jika username tidak ada, dan tidak ada exception lainnya, tuliskan ke file txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String informasiLogin = username + " " + password + " " + role;
            if (file.length() > 0) { 
                writer.newLine();
            }
            writer.write(informasiLogin);
            System.out.println("Registrasi sukses untuk username: '" + username + "'. Silahkan login!");
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Method untuk login pengguna
     * @param scan supaya objek scanner yang dibuat di main bisa terpakai
     */
    public static String[] login(Scanner scan) {
        System.out.print("Masukkan Username Anda: ");
        String username = scan.nextLine().trim();
        System.out.print("Masukkan Password Anda: ");
        String password = scan.nextLine().trim();

        File file = new File(PASSWORD_FILE);

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String myData;
            while ((myData = fileReader.readLine()) != null) {
                if (myData.trim().isEmpty()) { // Lewati baris kosong kalau ada, seharusnya tidak ada
                    continue;
                }
                String[] myDataArr = myData.split(" ", 3);

                // definisikan informasi
                // TO-DO: Buat method ini menyetor lebih dari hanya tiga informasi itu, semuanya lebih baik
                if (myDataArr.length == 3) {
                    String storedUser = myDataArr[0];
                    String storedPass = myDataArr[1];
                    String storedRole = myDataArr[2];

                    if (username.equals(storedUser)) {
                        if (password.equals(storedPass)) {
                            System.out.println("Authentication Success. Welcome " + storedUser + " (" + storedRole + ")!");
                            return new String[]{storedUser, storedRole}; // Return username and role
                        } else {
                            System.out.println("Password anda salah, tolong ulangi.");
                            return null; // User ketemu tapi pass salah
                        }
                    }
                } else {
                    System.err.println("Peringatan: Baris terformat salah diabaikan dalam file password: " + myData);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Password Tidak ditemukkan");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Login gagal, pengguna tidak ditemukkan atau password salah.");
        return null;
    }
}