package np.info.ngima.openweather

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import np.info.ngima.openweather.data.api.WeatherApi
import np.info.ngima.openweather.models.weather_response.WeatherResponse
import np.info.ngima.openweather.repo.WeatherRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest {
    var weatherResponseString =
        "{ \"coord\": { \"lon\": -10.5932, \"lat\": 6.6518 }, \"weather\": [ { \"id\": 803, \"main\": \"Clouds\", \"description\": \"broken clouds\", \"icon\": \"04n\" } ], \"base\": \"stations\", \"main\": { \"temp\": 23, \"feels_like\": 23.81, \"temp_min\": 23, \"temp_max\": 23, \"pressure\": 1011, \"humidity\": 94 }, \"visibility\": 10000, \"wind\": { \"speed\": 1.5, \"deg\": 278, \"gust\": 4.64 }, \"clouds\": { \"all\": 75 }, \"dt\": 1621199755, \"sys\": { \"type\": 1, \"id\": 2389, \"country\": \"LR\", \"sunrise\": 1621146361, \"sunset\": 1621191100 }, \"timezone\": 0, \"id\": 2274890, \"name\": \"New\", \"cod\": 200 }"
    var weatherResponseNotFoundString = "{ \"cod\": \"404\", \"message\": \"city not found\" }"
    var CITY_NEW = "New"
    var CITY_INVALID = "Invalid"


    @Mock
    lateinit var weatherApi: WeatherApi
    lateinit var weatherRepository: WeatherRepository
    lateinit var weatherResponse: WeatherResponse
    lateinit var weatherResponseNotFound: WeatherResponse
    lateinit var testObserver: TestObserver<WeatherResponse>

    @Before
    fun setUp() {

        weatherResponse = Gson().fromJson(weatherResponseString, WeatherResponse::class.java)
        weatherResponseNotFound = Gson().fromJson(
            weatherResponseNotFoundString,
            WeatherResponse::class.java
        )

        Mockito.`when`(weatherApi.getWeatherByCity(CITY_NEW))
            .thenReturn(Observable.just(weatherResponse))
        Mockito.`when`(weatherApi.getWeatherByCity(CITY_INVALID))
            .thenReturn(Observable.just(weatherResponseNotFound))

        weatherRepository = WeatherRepository(weatherApi)
        testObserver = TestObserver()
    }


    @Test
    fun testGetWeatherByInvalidCity() {
        weatherRepository.getWeatherByCity(CITY_INVALID)
            .subscribe(testObserver)

        testObserver
            .assertValue(weatherResponseNotFound)
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun testGetWeatherByCity() {
        weatherRepository.getWeatherByCity(CITY_NEW)
            .subscribe(testObserver)

        testObserver
            .assertValue(weatherResponse)
            .assertSubscribed()
            .assertComplete()
            .assertNoErrors()
    }
}