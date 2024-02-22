package com.android.boilerplate.ui.sample.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.android.boilerplate.R
import com.android.boilerplate.databinding.ActivityMainBinding
import com.android.boilerplate.utils.setOnSingleClickListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupNavigationComponent()
        setClickListener()
    }

    private fun setupNavigationComponent() {

        setSupportActionBar(binding.toolbar)
        navView = binding.tabBottomNavigationView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.tabNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfig = AppBarConfiguration.Builder(INVALID_ID)
            .setFallbackOnNavigateUpListener {
                onBackPressed()
                false
            }.build()
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfig)
        navView.setupWithNavController(navController)
        val navigationIcon: Drawable? = binding.toolbar.getNavigationIcon()
        if (navigationIcon != null) {
            navigationIcon.setColorFilter(
                resources.getColor(android.R.color.white),
                PorterDuff.Mode.SRC_ATOP
            )
            binding.toolbar.setNavigationIcon(navigationIcon)
        }
    }

    fun setNoToolbar() = binding.run {
        toolbar.navigationIcon = null

    }

    private fun setClickListener() = binding.run {
        logoutImageView.setOnSingleClickListener {
            openLogoutConfirmation()
        }
    }

    private fun openLogoutConfirmation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.logout_title_lbl))
        builder.setMessage(getString(R.string.logout_desc_lbl))
        builder.setPositiveButton(getString(R.string.logout_btn)) { _, _ ->
            val intent = SplashScreenActivity.getIntent(this)
            startActivity(intent)
            finishAffinity()
        }
        builder.setNegativeButton(getString(R.string.logout_cancel_btn), null)
        builder.show()
    }
    override fun onBackPressed() {

    }

    companion object {
        private const val INVALID_ID = -1
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}