import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class PassManager {
    public static boolean login() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Masukkan Username Anda: ");
        String username = scan.next();
        System.out.print("Masukkan Password Anda: ");
        String password = scan.next();
        // Objek file yang mennyimpan password kita
        File file = new File("password.txt");
        // Status autentikasi, jika true maka bisa login, jika tidak bisa maka gagal
        boolean auth = false;

        try{
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String myData = fileReader.readLine();
            while(myData != null){
                String[] myDataArr = myData.split(" ");
                myData = fileReader.readLine();
                if(!username.equals(myDataArr[0])){
                    continue;
                }
                if(!password.equals(myDataArr[1])){
                    break;
                }
                auth = true;
            }
            fileReader.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        if (auth){
            System.out.println("Auth Success");

        }
        else{
            System.out.println("Auth Failed");
        }

        scan.close();
        return auth;
    }

    public static boolean register() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Masukkan Username Anda: ");
        String username = scan.next();
        System.out.print("Masukkan Password Anda: ");
        String password = scan.next();

        // Objek file yang mennyimpan password kita
        File file = new File("password.txt");
        // Status availabilitas, jika true maka bisa register, jika tidak bisa maka gagal
        boolean available = false;

        try{
            BufferedReader fileReader = new BufferedReader(new FileReader(file));
            String myData = fileReader.readLine();
            while(myData != null){
                String[] myDataArr = myData.split(" ");
                myData = fileReader.readLine();
                if(username.equals(myDataArr[0])){
                    break;
                }
                available = true;
                String informasiLogin = username + " " + password;
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.append("\n");
                writer.append(informasiLogin);
                 writer.close();
            }
            fileReader.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        if (available){
            System.out.println("Registrasi Sukses!, silakan login");
        }
        else{
            System.out.println("Registrasi Gagal");
        }

        scan.close();
        return available;
    }
}