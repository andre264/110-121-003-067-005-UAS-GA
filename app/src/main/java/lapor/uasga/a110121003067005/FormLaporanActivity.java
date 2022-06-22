package lapor.uasga.a110121003067005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;

import lapor.uasga.a110121003067005.R;

public class FormLaporanActivity extends AppCompatActivity {

    Button kirim;
    EditText nama_lokasi, titik_lat, titik_lng;

    String lat, lng;

    FusedLocationProviderClient fusedLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_laporan);

        kirim = findViewById(R.id.button_kirim);
        nama_lokasi = findViewById(R.id.editText_nama_lokasi);
        titik_lat = findViewById(R.id.editText_lat);
        titik_lng = findViewById(R.id.editText_lng);

        fusedLoc = LocationServices.getFusedLocationProviderClient(this);
        lokasiSekarang();

        kirim.setOnClickListener(view -> {

            String namaLokasi = nama_lokasi.getText().toString();
            String titikLat = titik_lat.getText().toString();
            String titikLng = titik_lng.getText().toString();

            if (namaLokasi.isEmpty()) {
                Toast.makeText(this, "Nama lokasi belum diisi!", Toast.LENGTH_SHORT).show();
            }

            class KirimLaporan extends AsyncTask<Void, Void, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String, String> parameter = new HashMap<>();
                    parameter.put("nama_lokasi", namaLokasi);
                    parameter.put("lat", titikLat);
                    parameter.put("lng", titikLng);

                    RequestHandler rh = new RequestHandler();
                    String send = rh.sendPostRequest("http://10.0.2.2/kebakaran/kirimLaporan.php", parameter);
                    return send;
                }
                @Override
                protected void onPostExecute(String echo) {
                    super.onPostExecute(echo);
                    if (echo.equals("Terkirim")) {
                        Toast.makeText(FormLaporanActivity.this, "Laporan Berhasil dikirim!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormLaporanActivity.this, MapsActivity.class);
                        startActivity(i);
                    }
                }
            }
            KirimLaporan kirim = new KirimLaporan();
            kirim.execute();
        });
    }

    @SuppressLint("MissingPermission")
    private void lokasiSekarang() {
        fusedLoc.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        lat = String.valueOf(location.getLatitude());
                        lng = String.valueOf(location.getLongitude());
                        titik_lat.setText(lat);
                        titik_lng.setText(lng);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FormLaporanActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}