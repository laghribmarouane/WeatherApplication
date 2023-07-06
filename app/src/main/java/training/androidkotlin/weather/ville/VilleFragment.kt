package training.androidkotlin.weather.ville

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Toast
import training.androidkotlin.weather.App
import training.androidkotlin.weather.Database
import training.androidkotlin.weather.R
import training.androidkotlin.weather.ville.CreateVilleDialogFragment.CreateCityDialogListener

class VilleFragment : Fragment(), VilleAdapter.CityItemListener {

    private val TAG = VilleFragment::class.java.simpleName

    interface CityFragmentListener {
        fun onCitySelected(city: Ville)
        fun onSelectionCleared()
        fun onEmptyCities()
    }

    var listener: CityFragmentListener? = null

    private lateinit var cities: MutableList<Ville>
    private lateinit var database: Database
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VilleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = App.database
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_city, container, false)
        recyclerView = view.findViewById(R.id.cities_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cities = database.getAllCities()
        Log.i(TAG, "cities $cities")

        adapter = VilleAdapter(cities, this)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_city, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_create_city -> {
                showCreateCityDialog()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCitySelected(city: Ville) {
        listener?.onCitySelected(city)
    }

    override fun onCityDeleted(city: Ville) {
        showDeleteCityDialog(city)
    }

    fun selectFirstCity() {
        when (cities.isEmpty()) {
            true -> listener?.onEmptyCities()
            false -> onCitySelected(cities.first())
        }
    }

    private fun showCreateCityDialog() {
        val createCityFragment = CreateVilleDialogFragment()
        createCityFragment.listener = object: CreateCityDialogListener {
            override fun onDialogPositiveClick(cityName: String) {
                saveCity(Ville(cityName))
            }

            override fun onDialogNegativeClick() { }
        }

        createCityFragment.show(fragmentManager, "CreateCityDialogFragment")
    }

    private fun showDeleteCityDialog(city: Ville) {
        val deleteCityFragment = DeleteVilleDialogFragment.newInstance(city.name)
        deleteCityFragment.listener = object: DeleteVilleDialogFragment.DeleteCityDialogListener {
            override fun onDialogPositiveClick() {
                deleteCity(city)
            }

            override fun onDialogNegativeClick() { }
        }

        deleteCityFragment.show(fragmentManager, "DeleteCityDialogFragment")
    }

    private fun saveCity(city: Ville) {
        if (database.saveCity(city)) {
            cities.add(city)
            adapter.notifyDataSetChanged()
        } else {
            Toast.makeText(context,
                    R.string.city_message_error_could_not_create_city,
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteCity(city: Ville) {
        if (database.deleteCity(city)) {
            cities.remove(city)
            adapter.notifyDataSetChanged()
            selectFirstCity()
            Toast.makeText(context,
                    getString(R.string.city_message_info_city_deleted, city.name),
                    Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context,
                    getString(R.string.city_message_error_could_not_delete_city, city.name),
                    Toast.LENGTH_SHORT).show()
        }
    }

}