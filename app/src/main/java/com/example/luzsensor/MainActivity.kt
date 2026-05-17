package com.example.luzsensor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import android.graphics.Color

//Comentário sobre o projeto par testar o Commit.
class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var sensorLuz: Sensor? = null
    private lateinit var textoLuz: TextView
    private lateinit var layoutPrincipal: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textoLuz = findViewById(R.id.text_luz)
        layoutPrincipal = findViewById(R.id.layout_principal)

        //Iniciar Gerencamento de Sensores
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //Apontando para um sensor de Luz
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }
    //Esse metodo é chamado toda vez que o sensor detecta mudança
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null){
            val valorLuz = event.values[0] // valor de luz em lux
            textoLuz.text = "Luminosidade: $valorLuz lx"

            //mudar cor se estiver escuro
            if (valorLuz < 50){
                layoutPrincipal.setBackgroundColor(Color.DKGRAY)
                textoLuz.setTextColor(Color.WHITE)
            }else if (valorLuz < 500){
                layoutPrincipal.setBackgroundColor(Color.LTGRAY)
                textoLuz.setTextColor(Color.BLACK)
            }else{
                layoutPrincipal.setBackgroundColor(Color.WHITE)
                textoLuz.setTextColor(Color.BLUE)
            }
        }
    }
    // Registrar o sensor quando o app abre
    override fun onResume() {
        super.onResume()
        sensorLuz?.let{
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    // pausa o sensor quando sai
    override fun onPause(){
        super.onPause()
        sensorManager.unregisterListener(this)
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int){}
}