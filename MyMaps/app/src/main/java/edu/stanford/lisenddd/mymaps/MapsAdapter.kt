package edu.stanford.lisenddd.mymaps

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.stanford.lisenddd.mymaps.models.UserMap
import java.util.*

private const val TAG = "MapsAdapter"
class MapsAdapter(val context: Context, val userMaps: MutableList<UserMap>, val onClickListener: OnClickListener) : RecyclerView.Adapter<MapsAdapter.ViewHolder>(), Filterable{
    private val userMapFull = userMaps.toList()
    interface OnClickListener {
        fun onItemClick(position: Int)

        fun onItemLongClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user_map, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = userMaps.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userMap = userMaps[position]
        holder.itemView.setOnClickListener {
            Log.i(TAG, "Tapped on position $position")
            onClickListener.onItemClick(position)
        }
        holder.itemView.setOnLongClickListener {
            onClickListener.onItemLongClick(position)
            return@setOnLongClickListener true
        }
        val textViewTitle = holder.itemView.findViewById<TextView>(R.id.tvMapTItle)
        textViewTitle.text = userMap.title

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val fitleredMap : MutableList<UserMap> = mutableListOf()
                if (constraint.isNullOrBlank()) {
                    fitleredMap.addAll(userMapFull)
                } else {
                    val filterPattern =
                        constraint.toString().toLowerCase(Locale.ROOT).trim()
                    for (item in userMapFull) {
                        if (item.title.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            fitleredMap.add(item)
                        }
                    }
                }

                return FilterResults().also { it.values = fitleredMap }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                userMaps.clear()
                userMaps.addAll(results!!.values as MutableList<UserMap>)
                notifyDataSetChanged()
            }

        }
    }
}
