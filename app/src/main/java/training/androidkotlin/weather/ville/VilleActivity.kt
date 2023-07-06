package training.androidkotlin.weather.ville

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import training.androidkotlin.weather.R
import training.androidkotlin.weather.weather.WeatherActivity
import training.androidkotlin.weather.weather.WeatherFragment

class VilleActivity : AppCompatActivity(), VilleFragment.CityFragmentListener {

    private lateinit var cityFragment: VilleFragment
    private var weatherFragment: WeatherFragment? = null
    private var currentCity: Ville? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
        cityFragment = supportFragmentManager.findFragmentById(R.id.city_fragment) as VilleFragment
        cityFragment.listener = this

        weatherFragment = supportFragmentManager.findFragmentById(R.id.weather_fragment) as WeatherFragment?
    }

    override fun onResume() {
        super.onResume()
        if (!isHandsetLayout() && currentCity != null) {
            weatherFragment?.updateWeatherForCity(currentCity!!.name)
        }
    }

    override fun onCitySelected(city: Ville) {
        currentCity = city
        if (isHandsetLayout()) {
            startWeatherActivity(city)
        } else {
            weatherFragment?.updateWeatherForCity(city.name)
        }
    }

    override fun onSelectionCleared() {
        cityFragment.selectFirstCity()
    }

    override fun onEmptyCities() {
        weatherFragment?.clearUi()
    }

    private fun startWeatherActivity(city: Ville) {
        val intent = Intent(this, WeatherActivity::class.java)
        intent.putExtra(WeatherFragment.EXTRA_CITY_NAME, city.name)
        startActivity(intent)
    }

    private fun isHandsetLayout(): Boolean = weatherFragment == null

}
