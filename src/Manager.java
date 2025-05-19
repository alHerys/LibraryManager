import Person.Person;

import java.util.*;

public class Manager {
    private List<Buku<?>> daftarBuku;
    private Set<String> daftarGenre;
    private Queue<Buku<?>> pinjamanBuku;
    private Set<Person> daftarAnggota;

    public Manager() {
        this.pinjamanBuku = new LinkedList<>();
        this.daftarAnggota = new HashSet<>();
        this.daftarBuku = new ArrayList<>();
        this.daftarGenre = new HashSet<>();
    }

    public void mendaftarAnggota(Person anggota) {
        daftarAnggota.add(anggota);
    }

    public boolean menghapusAnggota(String id) {
        Person anggotaHapus = null;
        for (Person anggota : daftarAnggota) {
            if (anggota.getId().equals(id)) {
                anggotaHapus = anggota;
                break;
            }
        }

        if (anggotaHapus == null) {
            return false;
        }

        daftarAnggota.remove(anggotaHapus);
        return true;
    }

    public Person mencariAnggota(String id) {
        Person anggotaHapus = null;
        for (Person anggota : daftarAnggota) {
            if (anggota.getId().equals(id)) {
                anggotaHapus = anggota;
                break;
            }
        }

        return anggotaHapus;
    }

}
