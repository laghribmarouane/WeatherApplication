package training.androidkotlin.weather.ville

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import training.androidkotlin.weather.R

class VilleAdapter(private val cities: List<Ville>,
                   private val cityListener: VilleAdapter.CityItemListener)
    : RecyclerView.Adapter<VilleAdapter.ViewHolder>(), View.OnClickListener {

    interface CityItemListener {
        fun onCitySelected(city: Ville)
        fun onCityDeleted(city: Ville)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.findViewById<CardView>(R.id.card_view)!!
        val cityNameView = itemView.findViewById<TextView>(R.id.name)!!
        val deleteView = itemView.findViewById<View>(R.id.delete)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_city, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cities[position]
        with(holder) {
            cardView.setOnClickListener(this@VilleAdapter)
            cardView.tag = city
            cityNameView.text = city.name
            deleteView.tag = city
            deleteView.setOnClickListener(this@VilleAdapter)
        }
    }

    override fun getItemCount(): Int = cities.size

    override fun onClick(v: View) {
        when (v.id) {
            R.id.card_view -> cityListener.onCitySelected(v.tag as Ville)
            R.id.delete -> cityListener.onCityDeleted(v.tag as Ville)
        }
    }
}