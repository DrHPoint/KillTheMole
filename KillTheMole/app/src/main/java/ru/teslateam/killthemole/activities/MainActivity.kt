package ru.teslateam.killthemole.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.AdapterView
import ru.teslateam.killthemole.models.KillTheMoleClass
import ru.teslateam.killthemole.R
import ru.teslateam.killthemole.models.Mode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mode: Array<String> = resources.getStringArray(R.array.Modes_array)
        var modeInNumber = 4

        val adapter = ArrayAdapter(this, R.layout.spinner, mode)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View?, position: Int, id: Long){
                modeInNumber = Mode.getCardTypeByName(parent.getItemAtPosition(position).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>){
            }
        }

        buttonStart.setOnClickListener{
            val newIntent = Intent(this, KillTheMoleActivity::class.java)
            newIntent.putExtra(KillTheMoleClass.TOTAL_MODE, modeInNumber)
            startActivity(newIntent)
        }

        buttonExit.setOnClickListener {
            finish()
        }
    }
}
