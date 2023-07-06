package training.androidkotlin.weather.ville

import android.R
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ListView


class CreateVilleDialogFragment : DialogFragment() {

    interface CreateCityDialogListener {
        fun onDialogPositiveClick(cityName: String)
        fun onDialogNegativeClick()
    }

    var listener: CreateCityDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)

        val input = EditText(context)
        val auto = AutoCompleteTextView(context)
        with(input) {
            inputType = InputType.TYPE_CLASS_TEXT
            hint = "chose your city"
        }
        val list = arrayOf(
            "Casablanca",
            "Rabat",
            "Fes",
            "Marrakech",
            "Tangier",
        )

        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1, list
        )
        auto.setAdapter(arrayAdapter)
        builder.setTitle(training.androidkotlin.weather.R.string.createcity_title)
            .setView(input)
            .setPositiveButton(
                training.androidkotlin.weather.R.string.createcity_positive,
                DialogInterface.OnClickListener { _, _ ->
                    listener?.onDialogPositiveClick(input.text.toString())
                })
            .setNegativeButton(training.androidkotlin.weather.R.string.createcity_negative,
                DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })

        builder.setAdapter(
            arrayAdapter,
            DialogInterface.OnClickListener { _, _ ->
                for (i in list.indices)
                //   adapterView.getItemAtPosition(i).toString() = CITY
                    listener?.onDialogPositiveClick(list.get(i))

            })
        return builder.create()
    }

}