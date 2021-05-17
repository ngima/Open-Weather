package np.info.ngima.openweather.data.api;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ForecastApi {

    @GET("forecast")
    void getForecastByCity(@Query("q") String city);

    @GET("forecast")
    void getForecastByCityId(@Query("id") int city);

    @GET("forecast")
    void getForecastByZipCode(@Query("zip") int zipCode);

    @GET("forecast")
    void getForecastByGeoCoordinates(@Query("lat") double lat, @Query("lon") double lon);
}