package com.rcd27.playfm.presentation

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.rcd27.playfm.App
import com.rcd27.playfm.R
import com.rcd27.playfm.dagger.main.ActivityComponent
import com.rcd27.playfm.dagger.main.ActivityModule
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    var activityComponent: ActivityComponent by Delegates.notNull()

    private val showError: (errorMessage: String, onDismiss: () -> Unit) -> Unit =
        { errorMessage, action ->
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Ошибка")
                .setMessage(errorMessage)
                .setIcon(R.drawable.ic_error_red_24dp)
                .setCancelable(false)
                .setNegativeButton(
                    "ОК, посмотрю другой"
                ) { dialog, _ ->
                    action.invoke()
                    dialog.cancel()
                }
            val alert = builder.create()
            alert.show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment?
            ?: throw RuntimeException("Unable to init navHostFragment")

        val navController = host.navController

        activityComponent = (application as App)
            .applicationComponent
            .plus(ActivityModule(navController, errorDisplay = showError))
    }
}