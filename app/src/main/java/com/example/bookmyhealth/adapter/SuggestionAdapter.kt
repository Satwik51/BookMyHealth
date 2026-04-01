package com.example.bookmyhealth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.bookmyhealth.R

class SuggestionAdapter(
    context: Context,
    private var items: List<String>
) : ArrayAdapter<String>(context, 0, items) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    // ViewHolder pattern for performance
    private class ViewHolder(view: View) {
        val tvName: TextView = view.findViewById(R.id.tvSuggestion)
        val tvSub: TextView = view.findViewById(R.id.tvSubText)
        val tvAvatar: TextView = view.findViewById(R.id.tvAvatar)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_suggestion, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        // Crash Prevention: Check if position is still valid
        if (position < items.size) {
            val name = items[position]
            holder.tvName.text = name
            holder.tvSub.text = "Medicine"
            holder.tvAvatar.text = if (name.isNotEmpty()) name.first().uppercase() else "?"
        }

        return view
    }

    // CRITICAL: Override getCount to always return the current list size
    override fun getCount(): Int = items.size

    // CRITICAL: Override getItem to prevent internal 'Out of Bounds' crashes
    override fun getItem(position: Int): String? {
        return if (position >= 0 && position < items.size) items[position] else null
    }

    /**
     * FIX: AutoCompleteTextView needs a Filter to handle data updates correctly.
     * By overriding this, we tell the UI exactly what to show.
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                results.values = items
                results.count = items.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }

            // This ensures the correct string is put into the EditText when clicked
            override fun convertResultToString(resultValue: Any?): CharSequence {
                return resultValue as String
            }
        }
    }
}