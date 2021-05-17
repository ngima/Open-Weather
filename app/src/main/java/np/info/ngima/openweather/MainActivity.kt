package np.info.ngima.openweather

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import np.info.ngima.openweather.models.weather_response.Weather
import np.info.ngima.openweather.models.weather_response.WeatherResponse
import np.info.ngima.openweather.ui.composable.CityWeather
import np.info.ngima.openweather.ui.composable.DelayTextSearchBar
import np.info.ngima.openweather.ui.composable.TextSearchBar
import np.info.ngima.openweather.ui.theme.OpenWeatherTheme

class MainActivity : ComponentActivity() {

    var cities = arrayListOf<String>("Lalitpur", "Kathmandu", "New Delhi", "jpt")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Home(cities = cities)
        }

    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun Home(
    cities: List<String>,
    openWeather: OpenWeather = OpenWeather.getInstance()
) {

    var weatherResponseList by rememberSaveable {
        mutableStateOf<ArrayList<WeatherResponse>>(
            arrayListOf()
        )
    }

    /*cities.forEach { city ->

        openWeather.WeatherRepository()
            .getWeatherByCity(city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<WeatherResponse> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: WeatherResponse) {
                    var list = arrayListOf(t)
                    list.addAll(weatherResponseList)

                    weatherResponseList = list
                }

                override fun onError(e: Throwable) {

                    Log.e("Weather Error", "${e.message}")
                }

                override fun onComplete() {

                }
            })
    }*/

    val scope = rememberCoroutineScope()

//    OpenWeatherTheme {
    Scaffold(
        topBar = {
            HomeTopBar(
                openWeather = openWeather,
                weatherResponseList = weatherResponseList,
            ) { weatherResponseList = it }
        },
        floatingActionButton = {
            /*FloatingActionButton(
                onClick = {
                    Log.e("FAB", "Clicked")
                    scope.launch {
                        sheetState.show()
                    }
                },
                backgroundColor = Blue200,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            )*/
        },
        content = { HomeContent(weatherResponseList = weatherResponseList) }
    )
//    }
}


@Composable
fun HomeContent(weatherResponseList: List<WeatherResponse>) {

    if (weatherResponseList.isEmpty()) {
        HomeEmptyView()
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(
            24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        /*itemsIndexed(weathers) { index, item ->
            CityWeather(city = item)
        }*/
        itemsIndexed(weatherResponseList) { index, item ->
            CityWeather(weatherResponse = item)
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeTopBar(
    openWeather: OpenWeather,
    weatherResponseList: ArrayList<WeatherResponse>,
    onChangeWeatherResponseList: (ArrayList<WeatherResponse>) -> Unit = {}
) {
    var keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.background(colorResource(id = R.color.sunny))) {
        TopAppBar(
            title = { Text("OpenWeather") },
            backgroundColor = colorResource(id = R.color.blue_500),
            contentColor = Color.White
        )

        var searchQuery by remember { mutableStateOf("") }
        var searching by remember { mutableStateOf(false) }

        Box(modifier = Modifier.background(colorResource(id = R.color.sunny))) {
            DelayTextSearchBar(
                modifier = Modifier.padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 8.dp,
                    bottom = 16.dp
                ),
                value = searchQuery,
                label = "Search by City",
                onValueChanged = {
                    searchQuery = it
                },
                onClearClick = { searchQuery = "" },
                onFocusChanged = { },
                onValueReadyAfterDelay = {

                    Log.e("Delay", it)
                    openWeather.WeatherRepository()
                        .getWeatherByCity(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<WeatherResponse> {
                            override fun onSubscribe(d: Disposable) {
                                searching = true
                            }

                            override fun onNext(t: WeatherResponse) {

                                if (weatherResponseList.filter { it.cityId == t.cityId }
                                        .isEmpty()) {
                                    var list = arrayListOf(t)
                                    list.addAll(weatherResponseList)
                                    onChangeWeatherResponseList(list)

                                    //hide keyboard
                                    keyboardController?.hide()
                                }
                            }

                            override fun onError(e: Throwable) {
                                searching = false
                                Log.e("Weather Error", "${e.message}")
                            }

                            override fun onComplete() {
                                searching = false
                            }
                        })
                }
            )
        }

        if (searching) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = Modifier
                    .align(
                        Alignment.CenterHorizontally
                    )
                    .height(12.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeEmptyView() {

    var icons = arrayListOf<Int>(

        R.drawable.sunny,
        R.drawable.cloudy
    )

    Surface(
        color = colorResource(
            id = R.color.sunny
        ),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        LazyVerticalGrid(
            cells = GridCells.Adaptive(minSize = 128.dp),
            modifier = Modifier
                .padding(24.dp),
        ) {

            itemsIndexed(icons) { index, item ->
                Image(
                    painter = painterResource(
                        id = item
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp),
                    alpha = 0.10f
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Search by City to view weather of the City",
                modifier = Modifier
                    .padding(start = 48.dp, end = 48.dp)
                    .alpha(0.5f),
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OpenWeatherTheme {
        Home(cities = arrayListOf())
//        CityWeather(weatherResponse = "Lalitpur")
    }
}

@Composable
fun SearchByCity(
    onFocusChanged: (FocusState) -> Unit = {},
) {

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxHeight()
    ) {

        var searchQuery by remember { mutableStateOf("") }

        TextSearchBar(
            modifier = Modifier.padding(0.dp),
            value = searchQuery,
            label = "Search City or Zip Code",
            onValueChanged = { searchQuery = it },
            onClearClick = { searchQuery = "" },
            onFocusChanged = onFocusChanged
        )
    }
}

@DrawableRes
fun Weather.getWeatherDrawable(): Int {
    return when (description) {
        "clear sky" -> R.drawable.sunny
        "few clouds", "broken clouds" -> R.drawable.cloudy
        "scattered clouds" -> R.drawable.mostly_sunny
        "mist" -> R.drawable.cloudy
        "shower rain", "rain" -> R.drawable.rainy
        "thunderstorm" -> R.drawable.thunder
        "snow" -> R.drawable.snow
        else -> R.drawable.sunny
    }
}

@ColorRes
fun Weather.getWeatherBackgroundColor(): Int {
    return when (description) {
        "clear sky" -> R.color.sunny
        "few clouds", "broken clouds" -> R.color.cloudy
        "scattered clouds" -> R.color.mostly_sunny
        "mist" -> R.color.cloudy
        "shower rain", "rain" -> R.color.rainy
        "thunderstorm" -> R.color.thunder
        "snow" -> R.color.snow
        else -> R.color.sunny
    }
}
