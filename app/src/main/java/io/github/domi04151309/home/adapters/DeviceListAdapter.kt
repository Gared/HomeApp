package io.github.domi04151309.home.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import io.github.domi04151309.home.R
import android.view.LayoutInflater
import io.github.domi04151309.home.data.SimpleListItem
import io.github.domi04151309.home.interfaces.RecyclerViewHelperInterface

class DeviceListAdapter(
    private val items: ArrayList<SimpleListItem>,
    private val helperInterface: RecyclerViewHelperInterface
    ) : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item_devices, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.drawable.setImageResource(items[position].icon)
        holder.title.text = items[position].title
        holder.summary.text = items[position].summary
        holder.hidden.text = items[position].hidden
        holder.itemView.setOnClickListener { helperInterface.onItemClicked(holder.itemView, position) }
        if (position == itemCount - 1) holder.handle.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var drawable: ImageView = view.findViewById(R.id.drawable)
        var title: TextView = view.findViewById(R.id.title)
        var summary: TextView = view.findViewById(R.id.summary)
        var hidden: TextView = view.findViewById(R.id.hidden)
        var handle: ImageView = view.findViewById(R.id.handle)
    }
}