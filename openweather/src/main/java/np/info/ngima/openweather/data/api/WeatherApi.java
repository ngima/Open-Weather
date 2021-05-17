package np.info.ngima.openweather.data.api;

import io.reactivex.Observable;
import np.info.ngima.openweather.models.weather_response.WeatherResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("weather")
    Observable<WeatherResponse> getWeatherByCity(@Query("q") String city);

    @GET("weather")
    Observable<WeatherResponse> getWeatherByCityId(@Query("id") int cityId);

    @GET("weather")
    Observable<WeatherResponse> getWeatherByZipCode(@Query("zip") int zipCode);

    @GET("weather")
    Observable<WeatherResponse> getWeatherByGeoCoordinates(@Query("lat") double lat, @Query("lon") double lon);
}
