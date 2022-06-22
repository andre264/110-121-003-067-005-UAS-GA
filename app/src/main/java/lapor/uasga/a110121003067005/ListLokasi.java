package lapor.uasga.a110121003067005;


import java.util.List;

public class ListLokasi {
    private List<Lokasi> laporan;

    //konstruktor
    public ListLokasi(List<Lokasi> laporan) {this.laporan = laporan; }

    public List<Lokasi> getLaporan() { return laporan;}

    public void setLaporan(List<Lokasi> laporan) {this.laporan = laporan;}
}
