package com.domain.event_reminder.features.detailsactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.domain.event_reminder.R
import com.domain.event_reminder.data.entities.AppEvent
import com.domain.event_reminder.features.homescreen.view.HomeActivity
import kotlinx.android.synthetic.main.layout_event_details.*

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_event_details)

        val bundle: Bundle? = intent?.getBundleExtra(HomeActivity.BUNDLE_KEY)
        val event: AppEvent? = bundle?.getParcelable(HomeActivity.EVENT_PARCELABLE_KEY)

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
