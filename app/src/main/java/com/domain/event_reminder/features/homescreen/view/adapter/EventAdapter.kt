package com.domain.event_reminder.features.homescreen.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.domain.event_reminder.R
import com.domain.event_reminder.data.entities.AppEvent
import kotlinx.android.synthetic.main.item_event.view.*
import kotlinx.android.synthetic.main.item_event.view.event_date_text_view
import kotlinx.android.synthetic.main.item_event.view.event_desc_text_view
import kotlinx.android.synthetic.main.item_event.view.event_temperature_text_view
import kotlinx.android.synthetic.main.item_event.view.event_time_text_view
import kotlinx.android.synthetic.main.item_event.view.event_title_text_view
import kotlinx.android.synthetic.main.item_event.view.event_weather_image_view
import kotlinx.android.synthetic.main.layout_event_details.view.*
import kotlin.random.Random


class EventAdapter(
    val twoPane: Boolean,
    val listener: ClickListener
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    var data: List<AppEvent> = emptyList()
        set(newList) {
            val calculateDiff = DiffUtil.calculateDiff(RepoDiffCallBack(field, newList))
            calculateDiff.dispatchUpdatesTo(this)

            field = newList
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], twoPane, listener)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(
            event: AppEvent,
            twoPane: Boolean,
            listener: ClickListener
        ) {
            with(itemView) {
                event_title_text_view.text = event.title
                event_date_text_view.text = event.date
                event_time_text_view.text = event.time
                event_desc_text_view.text = event.description

                val resId = setImageRes()
                event.resourceId = resId

                event_weather_image_view.setImageResource(resId)
                event_temperature_text_view.text = "${event.temperature} \u00B0C"

                setOnClickListener {
                    if (twoPane) {
                        listener.onTwoPanClicked(event)

                    } else {
                        listener.onSinglePanClicked(event)
                    }
                }
            }
        }

        private fun setImageRes(): Int {
            return when (Random.nextInt(0, 2)) {
                0 -> R.drawable.ic_sunny
                1 -> R.drawable.ic_cloudy
                else -> R.drawable.ic_raining
            }
        }
    }

    class RepoDiffCallBack(private val oldList: List<AppEvent>, private val newList: List<AppEvent>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size
    }

    interface ClickListener {
        fun onSinglePanClicked(event: AppEvent)
        fun onTwoPanClicked(event: AppEvent)
    }
}