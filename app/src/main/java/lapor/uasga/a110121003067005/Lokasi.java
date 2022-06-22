package lapor.uasga.a110121003067005;

public class Lokasi {

    private String nama_lokasi;
    private String lat;
    private String lng;

    public Lokasi(String nama_lokasi, String lat, String lng) {
        this.nama_lokasi = nama_lokasi;
        this.lat = lat;
        this.lng = lng;
    }

    public String getNama_lokasi() {
        return nama_lokasi;
    }

    public void setNama_lokasi(String nama_lokasi) {
        this.nama_lokasi = nama_lokasi;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
