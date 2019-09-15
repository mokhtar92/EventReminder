package com.domain.event_reminder.features.homescreen.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.domain.event_reminder.R
import com.domain.event_reminder.features.homescreen.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        bindObservers()
    }

    private fun bindObservers() {
        viewModel.events.observe(this, Observer {
            Toast.makeText(this, it.size.toString(), Toast.LENGTH_LONG).show()
        })

        viewModel.loading.observe(this, Observer { })

        viewModel.error.observe(this, Observer { })
    }
}
