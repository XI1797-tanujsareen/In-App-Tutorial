package com.example.bank.demo.ui.login

import android.app.Activity
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.bank.demo.R
import com.example.bank.demo.ui.main.BaseActivity
import com.example.bank.demo.ui.tutorial.BottomSheetDialog
import com.example.bank.demo.ui.tutorial.CustomBottomSheetGuide
import com.example.bank.demo.ui.tutorial.callback.ICallBackSheet
import kotlin.math.sqrt

class LoginActivity : AppCompatActivity(), SensorEventListener, ICallBackSheet {

    private lateinit var loginViewModel: LoginViewModel

    var start: Boolean = false
    var accel: Float = 0f
    var accelCurrent: Float = 0f
    var accelLast: Float = 0f
    var shakeReset: Long = 2500
    var timeStamp: Long = 0

    var mySensor: Sensor? = null
    var mySensorManager: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, BaseActivity::class.java))
            }, 1000)
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }

        sensorInit()
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }


    private fun sensorInit() {

        Log.i("SelfAssistance", "sensorInit")
        // CREATE SENSOR MANAGER
        mySensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        // CREATE ACCELERATION SENSOR
        mySensor = mySensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)




    }


    override fun onResume() {
        super.onResume()

        registerSensor()
    }

    private fun registerSensor() {
        // REGISTERING OUR SENSOR
        mySensorManager?.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_UI)
        start = true

        // SETTING ACCELERATION VALUES
        accel = 0.00f
        accelCurrent = SensorManager.GRAVITY_EARTH
        accelLast = SensorManager.GRAVITY_EARTH
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        if (start) {
            // STORING THE VALUES OF THE AXIS
            val x: Float = sensorEvent?.values?.get(0) ?: 0.0f
            val y: Float = sensorEvent?.values?.get(1) ?: 0.0f
            val z: Float = sensorEvent?.values?.get(2) ?: 0.0f
            // ACCELEROMETER LAST READ EQUAL TO THE CURRENT ONE
            this.accelLast = accelCurrent
            // QUICK MAFS TO CALCULATE THE ACCELERATION
            accelCurrent = sqrt(x * x + y * y + (z * z).toDouble()).toFloat()
            // DELTA BETWEEN THE CURRENT AND THE LAST READ OF THE ACCELEROMETER
            val delta = accelCurrent - accelLast
            // QUICK MAFS TO CALCULATE THE ACCEL THAT WILL DECLARE IF IT SHAKED OR NOT
            accel = accel * 0.9f + delta
            // DID IT SHAKE??
            if (accel > 5) {
                val timenow = System.currentTimeMillis()
                if (timeStamp + shakeReset > timenow) {
                    return
                }
                timeStamp = timenow

                Log.i("SelfAssistance", "onSensorChanged")

                display()

            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    private fun display() {
        val bottomSheet = BottomSheetDialog(this)

        supportFragmentManager.let { fragmentManager ->
            bottomSheet.show(
                fragmentManager,
                "BottomSheetDialog"
            )
        }
    }

    override fun startGuide() {

        val customBottomSheetGuide = CustomBottomSheetGuide()
        val bundle = Bundle()
        bundle.putString("header", "Login Screen")
        bundle.putString(
            "description",
            "This screen will validates your credentials which is sent to your Email-ID"
        )
        customBottomSheetGuide.arguments = bundle

        supportFragmentManager.let { fragmentManager ->
            customBottomSheetGuide.show(
                fragmentManager,
                "BottomSheetDialog"
            )
        }
    }


    override fun onPause() {
        super.onPause()
        mySensorManager?.unregisterListener(this)
    }


}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })

}