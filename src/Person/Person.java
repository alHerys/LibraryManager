package Person;

import java.util.List;

public abstract class Person {
    private String nama;
    private String kelamin;
    private String alamat;
    private String role;
    private String id;
    private String password;

    public Person(String nama, String kelamin, String alamat, String id, String password, String Role) {
        this.nama = nama;
        this.kelamin = kelamin;
        this.alamat = alamat;
        this.id = id;
        this.password = password;
    }


    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getKelamin() {
        return kelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }
    
    @Override
    public abstract String toString();
}