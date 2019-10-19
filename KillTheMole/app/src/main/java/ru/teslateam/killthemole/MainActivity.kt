package ru.teslateam.killthemole

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess
import android.widget.AdapterView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mode: Array<String> = resources.getStringArray(R.array.Modes_array)
        var modeInNumber = 4

        fun spinnerSelect(x: String): Int {
            when (x) {
                mode[0] -> return 4
                mode[1] -> return 3
                mode[2] -> return 2
                else -> return 4
            }
        }

        val adapter = ArrayAdapter(this, R.layout.spinner, mode)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View?, position: Int, id: Long){
                modeInNumber = spinnerSelect(parent.getItemAtPosition(position).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>){
            }
        }

        //Controller

        buttonStart.setOnClickListener{
            val newIntent = Intent(this, KillTheMoleActivity::class.java)
            newIntent.putExtra(KillTheMoleClass.TOTAL_MODE, modeInNumber)
            startActivity(newIntent)
        }

        buttonExit.setOnClickListener {
            exitProcess(-1)
        }
    }
}
