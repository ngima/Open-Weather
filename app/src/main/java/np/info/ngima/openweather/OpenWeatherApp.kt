package np.info.ngima.openweather

import android.app.Application

class OpenWeatherApp: Application() {

    override fun onCreate() {
        super.onCreate()


        OpenWeather.Builder(BuildConfig.OPEN_WEATHER_API_KEY)
            .build()
    }
}