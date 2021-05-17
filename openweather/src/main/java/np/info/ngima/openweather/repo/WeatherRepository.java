package np.info.ngima.openweather.repo;

import io.reactivex.Observable;
import np.info.ngima.openweather.data.api.WeatherApi;
import np.info.ngima.openweather.models.weather_response.WeatherResponse;

public class WeatherRepository {

    private WeatherApi weatherApi;

    public WeatherRepository(WeatherApi weatherApi) {
        this.weatherApi = weatherApi;
    }


    public Observable<WeatherResponse> getWeatherByCity(String city) {
        return weatherApi.getWeatherByCity(city);
    }

    public Observable<WeatherResponse> getWeatherByCityId(int cityId) {
        return weatherApi.getWeatherByCityId(cityId);
    }

    public Observable<WeatherResponse> getWeatherByZipCode(int zipCode) {
        return weatherApi.getWeatherByZipCode(zipCode);
    }

    public Observable<WeatherResponse> getWeatherByGeoCoordinates(double lat, double lon) {
        return weatherApi.getWeatherByGeoCoordinates(lat, lon);
    }
}
