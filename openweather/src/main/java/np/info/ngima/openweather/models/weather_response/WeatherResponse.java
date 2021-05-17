package np.info.ngima.openweather.models.weather_response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("cod")
    public int statusCode;
    public String name;
    public List<Weather> weather;
    public WeatherMain main;
    public String message;
    public String base;
    public Clouds clouds;
    @SerializedName("coord")
    public GeoCoordinate geoCoordinate;
    @SerializedName("dt")
    public long unixTimeStamp;
    @SerializedName("id")
    public int cityId;
    public WeatherSys sys;
    public int timezone;
    public int visibility;
    public Wind wind;

    public WeatherResponse(int statusCode) {
        this.statusCode = statusCode;
    }
}
