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

        val adapter = ArrayAdapter(this, R.layout.spinner, mode)

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                when (parent.getItemAtPosition(position).toString()) {
                    mode[0] -> modeInNumber = 4
                    mode[1] -> modeInNumber = 3
                    mode[2] -> modeInNumber = 2
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>){
            }
        }

        //Controller

        val newIntent = Intent(this, KillTheMoleActivity::class.java)

        buttonStart.setOnClickListener{
            newIntent.putExtra("mode", modeInNumber)
            startActivity(newIntent)
        }

        buttonExit.setOnClickListener {
            exitProcess(-1)
        }
    }
}
