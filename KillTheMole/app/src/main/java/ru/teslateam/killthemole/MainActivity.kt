package ru.teslateam.killthemole

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Controller

        val newIntent = Intent(this, KillTheMoleActivity::class.java)

        buttonStart.setOnClickListener{
            startActivity(newIntent)
        }

        buttonExit.setOnClickListener {
            exitProcess(-1)
        }
    }
}
