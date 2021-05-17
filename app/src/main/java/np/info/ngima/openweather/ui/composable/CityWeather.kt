package np.info.ngima.openweather.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import np.info.ngima.openweather.R
import np.info.ngima.openweather.getWeatherBackgroundColor
import np.info.ngima.openweather.getWeatherDrawable
import np.info.ngima.openweather.models.weather_response.WeatherResponse
import np.info.ngima.openweather.ui.theme.White_50
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun CityWeather(
    weatherResponse: WeatherResponse
) {


    if (weatherResponse.statusCode == 200)
        Surface(
            color = colorResource(
                id = weatherResponse?.weather?.get(0)?.getWeatherBackgroundColor() ?: R.color.sunny
            ),
            modifier = Modifier
                .clip(shape = RoundedCornerShape(16.dp))
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
            ) {
                Image(
                    painter = painterResource(
                        id = weatherResponse?.weather?.get(0)?.getWeatherDrawable()
                            ?: R.drawable.sunny
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Row {
                    Image(
                        painter = painterResource(id = R.drawable.location_marker),
                        contentDescription = null,
                        modifier = Modifier
                            .height(48.dp)
                            .padding(end = 8.dp)
                    )
                    Column {

                        val loc = Locale("", weatherResponse.sys.country)

                        Text(
                            text = "${weatherResponse.name}, ${loc.displayCountry}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        val df = SimpleDateFormat("EEEE, dd MMMM", Locale.ENGLISH)
                        df.timeZone = TimeZone.getTimeZone("UTC")
                        val date = Date()
                        df.timeZone = TimeZone.getDefault()

                        Text(
                            text = df.format(date),
                            fontSize = 12.sp,
                        )
                    }
                }


                Column(modifier = Modifier.padding(top = 48.dp)) {
                    Text(
                        text = "${weatherResponse?.weather?.get(0)?.main ?: "Unknown"}",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${weatherResponse?.main?.temp?.toInt()}°",
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Light

                    )
                }


                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 32.dp)
                ) {

                    WeatherInfoText(
                        title = "Feels like",
                        value = "${weatherResponse?.main?.feels_like?.toInt()}°"
                    )

                    WeatherInfoText(
                        title = "Max",
                        value = "${weatherResponse?.main?.temp_max?.toInt()}°"
                    )

                    WeatherInfoText(
                        title = "Min",
                        value = "${weatherResponse?.main?.temp_min?.toInt()}°"
                    )

                    WeatherInfoText(
                        title = "Humidity",
                        value = "${weatherResponse?.main?.humidity?.toInt()}°"
                    )
                }
            }
        }
    else Surface(
        color = colorResource(
            id = R.color.error_bg
        ),
        modifier = Modifier
//            .clip(shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.sunny
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Column(modifier = Modifier.padding(top = 48.dp)) {
                Text(
                    text = "${weatherResponse.message}",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun WeatherInfoText(title: String, value: String) {


    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp)),
        color = White_50
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Light

            )
        }
    }
}