package lapor.uasga.a110121003067005;

import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import lapor.uasga.a110121003067005.databinding.ActivityMapsBinding;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 9;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    List<Lokasi> markerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        binding.buttonRefresh.setOnClickListener(view -> {
            refreshMap();
        });

        binding.buttonKirimLaporan.setOnClickListener(view -> {
            cekPerizinanLocation();
        });
    }

    private void cekPerizinanLocation() {

        LocationManager loc = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(loc.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            if(EasyPermissions.hasPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION})){
                startActivity(new Intent(this, FormLaporanActivity.class));
            }else {
                EasyPermissions.requestPermissions(this, "Aplikasi belum memiliki akses ke lokasi anda",
                        REQUEST_LOCATION_PERMISSION,Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }else {
            Toast.makeText(this, "Aktifkan GPS Location pada Menu Pengaturan untuk mengirim laporan!", Toast.LENGTH_LONG).show();
        }

    }

    private void refreshMap() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Retrofit retro = new Retrofit.Builder().baseUrl("http://10.0.2.2/kebakaran/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EndpointApi api = retro.create(EndpointApi.class);
        Call<ListLokasi> call = api.ambilLokasi();

        call.enqueue(new Callback<ListLokasi>() {
            @Override
            public void onResponse(Call<ListLokasi> call, Response<ListLokasi> response) {

                markerList = response.body().getLaporan();

                for (int i = 0; i < markerList.size(); i++){
                    LatLng lokasi = new LatLng(Double.parseDouble(markerList.get(i).getLat()),
                            Double.parseDouble(markerList.get(i).getLng()));

                    //menambah marker setiap Lokasi
                    mMap.addMarker(new MarkerOptions().position(lokasi).title(markerList.get(i).getNama_lokasi()));

                    LatLng loc = new LatLng(Double.parseDouble(markerList.get(0).getLat()),
                            Double.parseDouble(markerList.get(0).getLng()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.latitude, loc.longitude),11.0f));
                }
            }
            @Override
            public void onFailure(Call<ListLokasi> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "ada error " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}