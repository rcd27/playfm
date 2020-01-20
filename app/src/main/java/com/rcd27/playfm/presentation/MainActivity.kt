package com.rcd27.playfm.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.rcd27.playfm.App
import com.rcd27.playfm.R
import com.rcd27.playfm.dagger.main.ActivityComponent
import com.rcd27.playfm.dagger.main.ActivityModule
import com.rcd27.playfm.domain.auth.LogIned
import com.rcd27.playfm.domain.auth.NotLogIned
import com.rcd27.playfm.extensions.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    var activityComponent: ActivityComponent by Delegates.notNull()

    private val showError: (errorMessage: String, onDismiss: () -> Unit) -> Unit =
        { errorMessage, action ->
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Unexpected error")
                .setMessage(errorMessage)
                .setIcon(R.drawable.ic_error_red_24dp)
                .setCancelable(false)
                .setNegativeButton(
                    "That's a shame"
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

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val host = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment?
            ?: throw RuntimeException("Unable to init navHostFragment")

        val navController = host.navController

        activityComponent = (application as App)
            .applicationComponent
            .plus(ActivityModule(navController, errorDisplay = showError))

        object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_menu,
            R.string.close_menu
        ) {

            override fun onDrawerOpened(drawerView: View) {
                hideKeyboard()
                super.onDrawerOpened(drawerView)
            }
        }
            .apply {
                drawerLayout.addDrawerListener(this)
                syncState()
            }

        activityComponent.authStateMachine.state.subscribe({ authState ->
            when (authState) {
                is LogIned -> {
                    TODO("handle")
                }
                is NotLogIned -> {
                    navView.inflateMenu(R.menu.nav_drawer_menu_not_logined)
                }
            }
        }, {

        })

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_log_in -> {
                    TODO("handle login logic here")
                }
            }
            drawerLayout.closeDrawers()
            true
        }
        /*
        navView.addHeaderView(
            layoutInflater.inflate(
                R.layout.view_navigation_drawer_logined_header,
                navView
            )
        )
         */
    }
}