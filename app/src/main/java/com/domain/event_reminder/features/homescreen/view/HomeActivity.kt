package com.domain.event_reminder.features.homescreen.view

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.domain.event_reminder.R
import com.domain.event_reminder.data.entities.AppEvent
import com.domain.event_reminder.data.repository
import com.domain.event_reminder.features.detailsactivity.EventDetailsActivity
import com.domain.event_reminder.features.detailsfragment.EventDetailsFragment
import com.domain.event_reminder.features.homescreen.view.adapter.EventAdapter
import com.domain.event_reminder.features.homescreen.viewmodel.HomeViewModel
import com.domain.event_reminder.features.homescreen.viewmodel.HomeViewModelFactory
import com.domain.event_reminder.utils.*
import com.fondesa.kpermissions.extension.listeners
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home_content.*

class HomeActivity : AppCompatActivity(), EventAdapter.ClickListener {

    private lateinit var adapter: EventAdapter
    private lateinit var viewModel: HomeViewModel
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (fragment_container != null) {
            twoPane = true
        }

        setupRecyclerView()

        setupViewModel()

        checkInternetConnection()

        bindObservers()
    }

    override fun onSinglePanClicked(event: AppEvent) {
        val bundle = Bundle().apply { putParcelable(EVENT_PARCELABLE_KEY, event) }
        openActivity(EventDetailsActivity::class.java, BUNDLE_KEY, bundle)
    }

    override fun onTwoPanClicked(event: AppEvent) {
        val fragment = EventDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(EVENT_PARCELABLE_KEY, event)
            }
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(twoPane, this)
        event_recycler_view.adapter = adapter
    }

    private fun setupViewModel() {
        val factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    }

    private fun bindObservers() {
        viewModel.events.observe(this, Observer {
            adapter.data = it
        })

        viewModel.loading.observe(this, Observer { if (it) progressBar.visible() else progressBar.gone() })

        viewModel.error.observe(this, Observer { Snackbar.make(parentView, it, Snackbar.LENGTH_LONG) })
    }

    private fun checkInternetConnection() {
        if (isNetworkConnected()) {
            checkPermissions().send()

        } else {
            showCustomDialog(
                title = getString(R.string.general_error_no_connection_title),
                message = getString(R.string.general_error_no_connection_message),
                positiveButtonTitle = getString(R.string.app_general_retry),
                positiveButtonClicked = { checkInternetConnection() },
                onCancel = { finish() }
            )
        }
    }

    private fun checkPermissions(): PermissionRequest {
        return permissionsBuilder(Manifest.permission.WRITE_EXTERNAL_STORAGE).build()
            .also { addPermissionRequestListener(it) }
    }

    private fun addPermissionRequestListener(request: PermissionRequest) {
        request.listeners {
            onAccepted {
                viewModel.getCredentials()
            }

            onDenied {
                // Notified when the permissions are denied.
            }

            onPermanentlyDenied {
                // Notified when the permissions are permanently denied.
                Snackbar.make(
                    parentView,
                    getString(R.string.general_error_permanently_denied_message),
                    Snackbar.LENGTH_LONG
                )
            }

            onShouldShowRationale { _, nonce ->
                // Notified when the permissions should show a rationale.
                // The nonce can be used to request the permissions again.
                nonce.use()
            }
        }
    }

    companion object {
        const val BUNDLE_KEY = "BUNDLE_KEY"
        const val EVENT_PARCELABLE_KEY = "EVENT_PARCELABLE_KEY"
    }
}
