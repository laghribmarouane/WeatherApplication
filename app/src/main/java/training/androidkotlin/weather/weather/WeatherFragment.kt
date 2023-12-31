package training.androidkotlin.weather.weather

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import training.androidkotlin.weather.App
import training.androidkotlin.weather.R
import training.androidkotlin.weather.openweather.WeatherWrapper
import training.androidkotlin.weather.openweather.mapOpenWeatherDataToWeather

class WeatherFragment : Fragment() {

    companion object {
        val EXTRA_CITY_NAME = "training.kotlin.weather.extras.EXTRA_CITY_NAME"

        fun newInstance() : WeatherFragment = WeatherFragment()
    }

    private val TAG = WeatherFragment::class.java.simpleName

    private val weatherService = App.weatherService
    private lateinit var cityName: String

    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var city: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var weatherDescription: TextView
    private lateinit var temperature: TextView
    private lateinit var humidity: TextView
    private lateinit var pressure: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        refreshLayout = view.findViewById(R.id.swipe_refresh)
        city = view.findViewById(R.id.city)
        weatherIcon = view.findViewById(R.id.weather_icon)
        weatherDescription = view.findViewById(R.id.weather_description)
        temperature = view.findViewById(R.id.temperature)
        humidity = view.findViewById(R.id.humidity)
        pressure = view.findViewById(R.id.pressure)

        refreshLayout.setOnRefreshListener { refreshWeather() }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity?.intent!!.hasExtra(EXTRA_CITY_NAME)) {
            updateWeatherForCity(activity!!.intent.getStringExtra(EXTRA_CITY_NAME))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_weather, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_refresh_weather -> {
            refreshWeather()
            true
        }
        else -> false
    }

    fun updateWeatherForCity(cityName: String) {
        this.cityName = cityName
        this.city.text = cityName

        if (!refreshLayout.isRefreshing) {
            refreshLayout.isRefreshing = true
        }

        val call = weatherService.getWeather("$cityName,ma")
        call.enqueue(object: Callback<WeatherWrapper> {
            override fun onFailure(call: Call<WeatherWrapper>?, t: Throwable?) {
                Log.e(TAG, "could not load city currentObservation", t)
                Toast.makeText(activity,
                        R.string.weather_message_error_could_not_load_weather,
                        Toast.LENGTH_SHORT).show()
                refreshLayout.isRefreshing = false
            }

            override fun onResponse(call: Call<WeatherWrapper>?, response: Response<WeatherWrapper>?) {
                refreshLayout.isRefreshing = false
                response?.body()?.let {
                    updateUi(mapOpenWeatherDataToWeather(it))
                }
            }
        })
    }

    fun updateUi(weather: Weather) {
        Picasso.get()
                .load(weather.iconUrl)
                .placeholder(R.drawable.ic_cloud_off_black_24dp)
                .into(weatherIcon)

        weatherDescription.text = weather.description
        temperature.text = getString(R.string.weather_temperature_value, weather.temperature.toInt())
        humidity.text = getString(R.string.weather_humidity_value, weather.humidity)
    }

    fun clearUi() {
        weatherIcon.setImageResource(R.drawable.ic_cloud_off_black_24dp)
        cityName = ""
        city.text = ""
        weatherDescription.text = ""
        temperature.text = ""
        humidity.text = ""
        pressure.text = ""

    }

    private fun refreshWeather() {
        updateWeatherForCity(cityName)
    }

}