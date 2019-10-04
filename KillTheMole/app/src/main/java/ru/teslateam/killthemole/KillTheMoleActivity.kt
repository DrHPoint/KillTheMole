package ru.teslateam.killthemole

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_kill_the_mole.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round
import android.os.Handler
import kotlin.random.Random
import kotlin.system.exitProcess


class KillTheMoleActivity : AppCompatActivity() {

    var moleNumber = 0
    var moleScoreNumber = 0
    var score = 0

    fun ScorePlus() {
        score++
        moleScoreNumber = 0
    }

    fun onClickMole(view: View) {
        view.setBackgroundResource(R.drawable.empty)
        when(view.id) {
            firstMole.id -> if (moleScoreNumber == 1) ScorePlus()
            secondMole.id -> if (moleScoreNumber == 2) ScorePlus()
            thirdMole.id -> if (moleScoreNumber == 3) ScorePlus()
            forthMole.id -> if (moleScoreNumber == 4) ScorePlus()
        }
        ScoreOfKill.text = score.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kill_the_mole)

        fun RandMole() {
            moleNumber = Random.nextInt(1, 5)
            moleScoreNumber = moleNumber
        }

        fun moleanimation(a: Int) {
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

        val mole = object: CountDownTimer(1600, 500) {
            override fun onTick(millisUntilFinished: Long) {
                if(moleNumber ==moleScoreNumber)
                    moleanimation(floor(millisUntilFinished.toDouble()/500).toInt())
                else
                    moleanimation(0)
            }
            override fun onFinish() {
                moleanimation(0)
            }
        }
        val game = object: CountDownTimer(60100, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                TimeToKill.text = floor(millisUntilFinished.toDouble()/1000).toInt().toString()
                if (floor(millisUntilFinished.toDouble()/1000).toInt()%3 == 0) {
                    RandMole()
                    mole.start()
                }
            }
            override fun onFinish() {
                TimeToKill.text = "0"
                textToReady.text = "Game Over"
                Handler().postDelayed({
                    textToReady.text = "Result: $score"
                    Handler().postDelayed({
                        exitProcess(-1)
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
    }
}
