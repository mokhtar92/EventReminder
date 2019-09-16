package com.domain.event_reminder.features.homescreen.view

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.domain.event_reminder.R
import com.domain.event_reminder.data.repository
import com.domain.event_reminder.features.homescreen.viewmodel.HomeViewModel
import com.domain.event_reminder.features.homescreen.viewmodel.HomeViewModelFactory
import com.domain.event_reminder.utils.isNetworkConnected
import com.domain.event_reminder.utils.showCustomDialog
import com.fondesa.kpermissions.extension.listeners
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val factory = HomeViewModelFactory(repository)

        viewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)

        checkInternetConnection()

        bindObservers()
    }

    private fun bindObservers() {
        viewModel.events.observe(this, Observer {
            Log.d("bindObserversssss", it.toString())
            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
        })

        viewModel.loading.observe(this, Observer { })

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
                viewModel.getEvents()
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

}
