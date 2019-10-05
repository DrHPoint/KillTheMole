package ru.teslateam.killthemole

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import kotlinx.android.synthetic.main.activity_kill_the_mole.*
import kotlin.math.floor
import android.os.Handler
import kotlin.random.Random


class KillTheMoleActivity : AppCompatActivity() {

    var moleNumber = 0
    var moleScoreNumber = 0
    var score = 0
    
    //View

    fun onClickMole(view: View) {
        view.setBackgroundResource(R.drawable.empty)
        when(view.id) {
            firstMole.id -> if (moleScoreNumber == 1) scorePlus()
            secondMole.id -> if (moleScoreNumber == 2) scorePlus()
            thirdMole.id -> if (moleScoreNumber == 3) scorePlus()
            forthMole.id -> if (moleScoreNumber == 4) scorePlus()
        }
        ScoreOfKill.text = score.toString()
    }

    //Controller

    private fun scorePlus() {
        score++
        moleScoreNumber = 0
    }

    fun randMole() {
        moleNumber = Random.nextInt(1, 5)
        moleScoreNumber = moleNumber
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kill_the_mole)

        //Model

        val modeNum: Int = intent.getIntExtra("mode",1)
        
        fun View.showOrInvisible(show: Boolean) {
            visibility = if(show) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }

        fun moleAnimation(a: Int) {
            when(a) {
                3 -> when(moleNumber) {
                    1 -> firstMole.setBackgroundResource(R.drawable.mole1)
                    2 -> secondMole.setBackgroundResource(R.drawable.mole1)
                    3 -> thirdMole.setBackgroundResource(R.drawable.mole1)
                    4 -> forthMole.setBackgroundResource(R.drawable.mole1)
                }
                2 -> when(moleNumber) {
                    1 -> firstMole.setBackgroundResource(R.drawable.mole2)
                    2 -> secondMole.setBackgroundResource(R.drawable.mole2)
                    3 -> thirdMole.setBackgroundResource(R.drawable.mole2)
                    4 -> forthMole.setBackgroundResource(R.drawable.mole2)
                }
                1 -> when(moleNumber) {
                    1 -> firstMole.setBackgroundResource(R.drawable.mole3)
                    2 -> secondMole.setBackgroundResource(R.drawable.mole3)
                    3 -> thirdMole.setBackgroundResource(R.drawable.mole3)
                    4 -> forthMole.setBackgroundResource(R.drawable.mole3)
                }
                0 -> {
                    when (moleNumber) {
                        1 -> firstMole.setBackgroundResource(R.drawable.empty)
                        2 -> secondMole.setBackgroundResource(R.drawable.empty)
                        3 -> thirdMole.setBackgroundResource(R.drawable.empty)
                        4 -> forthMole.setBackgroundResource(R.drawable.empty)
                    }
                    moleNumber = 0
                    moleScoreNumber = 0
                }
            }
        }

        val mole = object: CountDownTimer(((modeNum*3+1)*100).toLong(), (modeNum*100).toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                if(moleNumber ==moleScoreNumber)
                    moleAnimation(floor(millisUntilFinished.toDouble()/(modeNum*100)).toInt())
                else
                    moleAnimation(0)
            }
            override fun onFinish() {
                moleAnimation(0)
            }
        }
        val game = object: CountDownTimer(60100, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                TimeToKill.text = floor(millisUntilFinished.toDouble()/1000).toInt().toString()
                if (floor(millisUntilFinished.toDouble()/1000).toInt()%(modeNum-1) == 0) {
                    randMole()
                    mole.start()
                }
            }
            override fun onFinish() {
                TimeToKill.text = resources.getString(R.string.timeToKill)
                textToReady.text = resources.getString(R.string.GameOver)
                Handler().postDelayed({
                    textToReady.text = resources.getString(R.string.Result) + score.toString()
                    Handler().postDelayed({
                        buttonRestart.showOrInvisible(true)
                        buttonHome.showOrInvisible(true)
                    }, 1000)
                }, 500)
            }
        }

        val timer = object: CountDownTimer(3100, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                textToReady.text = floor(millisUntilFinished.toDouble()/1000).toInt().toString()
            }
            override fun onFinish() {
                textToReady.text = ""
                game.start()
            }
        }

        timer.start()

        //Controller

        buttonRestart.setOnClickListener{
            ScoreOfKill.text = resources.getString(R.string.resultOfKill)
            textToReady.text = resources.getString(R.string.timeToKill)
            timer.start()
            buttonRestart.showOrInvisible(false)
            buttonHome.showOrInvisible(false)
        }

        buttonHome.setOnClickListener{
            val homeIntent = Intent(this, MainActivity::class.java)
            startActivity(homeIntent)
        }
    }
}
