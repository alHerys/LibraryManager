package Person;

public abstract class Person {
    private String nama;
    private String kelamin;
    private String alamat;
    private String role;
    private String id;
    private String password;

    public Person(String nama, String kelamin, String alamat, String id, String password, String role) {
        this.nama = nama;
        this.kelamin = kelamin;
        this.alamat = alamat;
        this.id = id;
        this.password = password;
        this.role = role;
    }

    // Constructor untuk default user
    // TO-DO: GANTI ini, buat supaya semua informasi bisa disetor di file txt, bukan hanya password, id dan role
    public Person(String id, String password, String role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

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

    public String getPassword() {
        return password;
    }
    
    @Override
    public abstract String toString();
}