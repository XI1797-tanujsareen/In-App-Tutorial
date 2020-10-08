package com.example.bank.demo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.bank.demo.databinding.SplashScreenBinding
import com.example.bank.demo.ui.login.LoginActivity
import kotlinx.android.synthetic.main.splash_screen.*

class SplashScreen: AppCompatActivity() {

    private var splashScreenBinding: SplashScreenBinding ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreenBinding = DataBindingUtil.setContentView(this,R.layout.splash_screen)

        buttonLogin?.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
            },1000)
        }

    }


}