package com.codingblocks.wearsample1

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity(), SensorEventListener {
    lateinit var sm: SensorManager
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            text?.text = it.values[0].toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Enables Always-on
        setAmbientEnabled()

        btnHeartRate.setOnClickListener {
            requestPermissions(arrayOf(Manifest.permission.BODY_SENSORS), 111)
            listenHeartRate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("A", "DESTROY")
        sm.unregisterListener(this)
    }

    fun listenHeartRate () {
        val heartSensor = sm.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        sm.registerListener(this, heartSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
