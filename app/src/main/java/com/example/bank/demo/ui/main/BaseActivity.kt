package com.example.bank.demo.ui.main

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.bank.demo.R
import com.example.bank.demo.databinding.BaseActivityBinding

class BaseActivity : AppCompatActivity() {

    private var baseActivityBinding: BaseActivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.base_activity)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment))

        val navController = findNavController(R.id.nav_host_fragment)

        baseActivityBinding?.toolbarHeader?.setupWithNavController(
            navController,
            appBarConfiguration
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->

            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                destination.id.toString()
            }

            Log.i("BaseActivity", dest)
        }
        setSupportActionBar(baseActivityBinding?.toolbarHeader)
    }
}