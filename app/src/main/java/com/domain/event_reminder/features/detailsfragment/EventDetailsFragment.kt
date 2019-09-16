package com.domain.event_reminder.features.detailsfragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.domain.event_reminder.R
import com.domain.event_reminder.data.entities.AppEvent
import com.domain.event_reminder.features.homescreen.view.HomeActivity
import kotlinx.android.synthetic.main.layout_event_details.*


class EventDetailsFragment : Fragment() {

    private var event: AppEvent? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        event = arguments?.getParcelable(HomeActivity.EVENT_PARCELABLE_KEY)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_event_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        event?.let {
            publishUi(it)
        }
    }

    private fun publishUi(event: AppEvent) {
        event_title_text_view.text = event.title
        event_status_text_view.text = event.status
        event_date_text_view.text = event.date
        event_time_text_view.text = event.time
        event_desc_text_view.text = event.description
        event_weather_image_view.setImageResource(event.resourceId)
        event_temperature_text_view.text = "${event.temperature} \u00B0C"
        event_humidity_text_view.text = "${event.humidity} %"
    }
}
