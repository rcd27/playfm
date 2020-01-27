package com.rcd27.playfm.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.rcd27.playfm.App
import com.rcd27.playfm.R
import com.rcd27.playfm.auth.domain.Logined
import com.rcd27.playfm.auth.domain.NotLogined
import com.rcd27.playfm.extensions.hideKeyboard
import com.rcd27.playfm.extensions.plusAssign
import com.rcd27.playfm.main.dagger.ActivityComponent
import com.rcd27.playfm.main.dagger.ActivityModule
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private val cd = CompositeDisposable()

    var activityComponent: ActivityComponent by Delegates.notNull()

    private val showError: (errorMessage: String, onDismiss: () -> Unit) -> Unit =
        { errorMessage, action ->
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle(getString(R.string.err_common_err_title))
                .setMessage(errorMessage)
                .setIcon(R.drawable.ic_error_red_24dp)
                .setCancelable(false)
                .setNegativeButton(
                    getString(R.string.err_thats_shame)
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
            .plus(
                ActivityModule(
                    navController,
                    errorDisplay = showError
                )
            )

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

        cd += activityComponent.authStateMachine.state.subscribe({ authState ->
            when (authState) {
                is Logined -> {
                    // TODO: inflate proper menu here
                }
                is NotLogined -> {
                    navView.inflateMenu(R.menu.nav_drawer_menu_not_logined)
                }
            }
        }, { showError(getString(R.string.err_auth_state_check)) { Unit } })

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_log_in -> {
                    showError(getString(R.string.err_not_implemented)) { Unit }
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

    override fun onDestroy() {
        cd.clear()
        super.onDestroy()
    }
}