package Person;

import java.util.List;

public abstract class Person {
    private String nama;
    private String kelamin;
    private String alamat;
    private String role;
    private String id;

    public Person() {
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

    @Override
    public abstract String toString();
}