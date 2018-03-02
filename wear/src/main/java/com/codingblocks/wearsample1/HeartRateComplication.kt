package com.codingblocks.wearsample1

import android.content.Context
import android.graphics.drawable.Icon
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.wearable.complications.ComplicationData
import android.support.wearable.complications.ComplicationManager
import android.support.wearable.complications.ComplicationProviderService
import android.support.wearable.complications.ComplicationText

class HeartRateComplication: ComplicationProviderService(), SensorEventListener {
    var bpm = -1
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.apply {
            if (sensor.type == Sensor.TYPE_HEART_RATE) {
                if (event.accuracy > 0) {
                    bpm = values[0].toInt()
                }
            }
        }
    }

    lateinit var sm: SensorManager
    lateinit var heartSensor: Sensor
    override fun onCreate() {
        super.onCreate()
        sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        heartSensor = sm.getDefaultSensor(Sensor.TYPE_HEART_RATE)
    }

    override fun onComplicationActivated(complicationId: Int, type: Int, manager: ComplicationManager?) {
        super.onComplicationActivated(complicationId, type, manager)
        sm.registerListener(
                this,
                heartSensor,
                1000 * 1000 * 10,
                1000 * 1000 * 5)
    }
    override fun onComplicationUpdate(complicationId: Int, type: Int, manager: ComplicationManager?) {
        val data = ComplicationData.Builder(ComplicationData.TYPE_SHORT_TEXT)
                .setIcon(Icon.createWithResource(this, R.drawable.ic_heartbeat))
                .setShortText(ComplicationText.plainText(
                        when(bpm) {
                            -1 -> "--"
                            else -> bpm.toString()
                        }
                ))
                .build()
        manager?.updateComplicationData(complicationId, data)
    }

    override fun onComplicationDeactivated(complicationId: Int) {
        super.onComplicationDeactivated(complicationId)
        sm.unregisterListener(this)
    }
}