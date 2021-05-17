package np.info.ngima.openweather;

import np.info.ngima.openweather.exception.ApiKeyNotInitializedException;
import np.info.ngima.openweather.repo.WeatherRepository;

/**
 * OpenWeather is singleton class responsible for setting up Open Weather essential values such as
 * {@link OpenWeather#API_KEY} and {@link OpenWeather#UNIT}.
 * <p>
 * OpenWeather has {@link Builder} class to initialize the single instance of {@link OpenWeather}
 *
 * @author Ngima Sherpa
 * @see OpenWeather
 * @see Builder
 */

public class OpenWeather {
    public static String API_KEY;
    public static String UNIT = "metric";

    private static OpenWeather openWeather;

    private WeatherRepository weatherRepository;

    private OpenWeather() {
        weatherRepository = new WeatherRepository();
    }

    public static OpenWeather getInstance() {
        return openWeather;
    }

    public WeatherRepository WeatherRepository() {
        return weatherRepository;
    }


    /**
     * Builder is builder class responsible for building singleton instance of {@link OpenWeather}
     *
     * @author Ngima Sherpa
     * @see OpenWeather
     * @see Builder
     */
    public static class Builder {

        private String apiKey;
        private String unit = OpenWeather.UNIT;

        public Builder(String apiKey) {
            this.apiKey = apiKey;
        }


        /**
         * Set Open Weather API Key to @{@link OpenWeather#API_KEY}
         *
         * @param apiKey Open Weather API Key
         */
        public Builder setApiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }


        /**
         * Set Open Weather Unit of measurement to @{@link OpenWeather#UNIT}
         *
         * @param unit Open Weather Unit of measurement
         */
        public Builder setUnit(String unit) {
            this.unit = unit;
            return this;
        }


        /**
         * Builds @{@link OpenWeather} singleton instance
         *
         * @throws ApiKeyNotInitializedException if ${@link OpenWeather#API_KEY} is not initialized.
         *
         * @return {@link OpenWeather#openWeather} a singleton instance
         */
        public OpenWeather build() throws ApiKeyNotInitializedException {
            OpenWeather.API_KEY = apiKey;
            OpenWeather.UNIT = unit;

            if (apiKey == null) throw new ApiKeyNotInitializedException();
            OpenWeather.openWeather = new OpenWeather();

            return OpenWeather.openWeather;
        }
    }
}
