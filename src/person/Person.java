package person;

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

    public String toBaseFileString() {
        return role + ";" +
                id + ";" +
                password + ";" +
                nama + ";" +
                kelamin + ";" +
                alamat;
    }

    public abstract String toFileString();

    public static Person fromFileString(String line) {
        String[] parts = line.split(";", -1);
        if (parts.length < 6) {
            System.err.println("Format data pengguna tidak sesuai (kurang dari 6 bagian): " + line);
            return null;
        }

        String role = parts[0];
        String id = parts[1];
        String password = parts[2];
        String nama = parts[3];
        String kelamin = parts[4];
        String alamat = parts[5];

        if ("Dosen".equals(role)) {
            if (parts.length < 8) {
                System.err.println("Format data Dosen tidak sesuai (kurang dari 8 bagian): " + line);
                return null;
            }
            String nip = parts[6];
            String fakultasDosen = parts[7];
            return new Dosen(nama, kelamin, alamat, id, password, nip, fakultasDosen);
        } else if ("Mahasiswa".equals(role)) {
            if (parts.length < 9) {
                System.err.println("Format data Mahasiswa tidak sesuai (kurang dari 9 bagian): " + line);
                return null;
            }
            String nim = parts[6];
            String fakultasMhs = parts[7];
            String prodi = parts[8];
            return new Mahasiswa(nama, kelamin, alamat, id, password, nim, fakultasMhs, prodi);
        } else {
            System.err.println("Role tidak dikenali: " + role + " pada baris: " + line);
            return null;
        }
    }

    @Override
    public abstract String toString();
}