package lapor.uasga.a110121003067005;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EndpointApi {

    @GET("markerDisplay.php")
    Call<ListLokasi> ambilLokasi();

}
