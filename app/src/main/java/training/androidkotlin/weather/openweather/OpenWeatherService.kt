package training.androidkotlin.weather.openweather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "93dd419a4eda725521e7bda2c6c30803"

interface OpenWeatherService {

    @GET("data/2.5/weather?units=metric&lang=fr")
    fun getWeather(@Query("q") cityName: String,
                   @Query("appid") apiKey: String = API_KEY) : Call<WeatherWrapper>
}