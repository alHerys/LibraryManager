package bookManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Authentikasi {
    private static final String USERS_FILE = "users.txt";
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
                if (myData.trim().isEmpty()) {
                    continue;
                }
                String[] myDataArr = myData.split(";", -1);

                if (myDataArr.length >= 3) {
                    String storedRole = myDataArr[0];
                    String storedUser = myDataArr[1];
                    String storedPass = myDataArr[2];

                    if (username.equals(storedUser)) {
                        if (password.equals(storedPass)) {
                            System.out.println("Authentication Success. Welcome " + storedUser + " (" + storedRole + ")!");
                            return new String[]{storedUser, storedRole};
                        } else {
                            System.out.println("Password anda salah, tolong ulangi.");
                            return null;
                        }
                    }
                } else {
                    System.err.println("Peringatan: Baris terformat salah diabaikan dalam file pengguna: " + myData);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File pengguna tidak ditemukan. Silakan daftar terlebih dahulu.");
        } catch (IOException e) {
            System.err.println("Terjadi kesalahan saat membaca file pengguna: " + e.getMessage());
        }
        System.out.println("Login gagal, pengguna tidak ditemukan atau password salah.");
        return null;
    }
}