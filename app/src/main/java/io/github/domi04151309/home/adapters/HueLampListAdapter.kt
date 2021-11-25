package io.github.domi04151309.home.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import io.github.domi04151309.home.R
import android.view.LayoutInflater
import android.widget.Switch
import io.github.domi04151309.home.data.ListViewItem
import android.graphics.drawable.LayerDrawable
import androidx.core.content.ContextCompat
import io.github.domi04151309.home.interfaces.RecyclerViewHelperInterface

class HueLampListAdapter(
    private var items: ArrayList<ListViewItem>,
    private var colors: ArrayList<Int>,
    private val helperInterface: RecyclerViewHelperInterface
    ) : RecyclerView.Adapter<HueLampListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val finalDrawable = LayerDrawable(arrayOf(
            ContextCompat.getDrawable(context, R.drawable.ic_hue_lamp_base),
            ContextCompat.getDrawable(context, R.drawable.ic_hue_lamp_color)
        ))
        finalDrawable.getDrawable(1).setTint(colors[position])
        holder.drawable.setImageDrawable(finalDrawable)
        holder.title.text = items[position].title
        holder.summary.text = items[position].summary
        holder.hidden.text = items[position].hidden
        holder.stateSwitch.isChecked = items[position].state ?: false
        holder.stateSwitch.setOnCheckedChangeListener(items[position].stateListener)
        holder.itemView.setOnClickListener { helperInterface.onItemClicked(holder.itemView, position) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: ArrayList<ListViewItem>, newColors: ArrayList<Int>) {
        if (newItems.size != items.size) {
            items = newItems
            colors = newColors
            notifyDataSetChanged()
        } else {
            val changed = arrayListOf<Int>()
            for (i in 0 until items.size) {
                if (items[i] != newItems[i] || colors[i] != newColors[i]) changed.add(i)
            }
            items = newItems
            colors = newColors
            changed.forEach {
                notifyItemChanged(it)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val drawable: ImageView = view.findViewById(R.id.drawable)
        val title: TextView = view.findViewById(R.id.title)
        val summary: TextView = view.findViewById(R.id.summary)
        val hidden: TextView = view.findViewById(R.id.hidden)
        val stateSwitch: Switch = view.findViewById<Switch>(R.id.state)
    }
}