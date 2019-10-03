package ru.teslateam.killthemole

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_kill_the_mole.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round
import kotlin.random.Random


class KillTheMoleActivity : AppCompatActivity() {

    var moleNumber = 0
    var moleScoreNumber = 0
    var score = 0

    fun onClickMole(view: View) {
        when((view as Button).id) {
            firstMole.id -> if (moleScoreNumber == 1) {
                score++
                moleScoreNumber = 0
            }
            secondMole.id -> if (moleScoreNumber == 2) {
                score++
                moleScoreNumber = 0
            }
            thirdMole.id -> if (moleScoreNumber == 3) {
                score++
                moleScoreNumber = 0
            }
            forthMole.id -> if (moleScoreNumber == 4) {
                score++
                moleScoreNumber = 0
            }
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
                    1 -> firstMole.setBackgroundResource(R.drawable.red)
                    2 -> secondMole.setBackgroundResource(R.drawable.red)
                    3 -> thirdMole.setBackgroundResource(R.drawable.red)
                    4 -> forthMole.setBackgroundResource(R.drawable.red)
                }
                2 -> when(moleNumber) {
                    1 -> firstMole.setBackgroundResource(R.drawable.yellow)
                    2 -> secondMole.setBackgroundResource(R.drawable.yellow)
                    3 -> thirdMole.setBackgroundResource(R.drawable.yellow)
                    4 -> forthMole.setBackgroundResource(R.drawable.yellow)
                }
                1 -> when(moleNumber) {
                    1 -> firstMole.setBackgroundResource(R.drawable.green)
                    2 -> secondMole.setBackgroundResource(R.drawable.green)
                    3 -> thirdMole.setBackgroundResource(R.drawable.green)
                    4 -> forthMole.setBackgroundResource(R.drawable.green)
                }
                0 -> {
                    when (moleNumber) {
                        1 -> firstMole.setBackgroundResource(R.drawable.black)
                        2 -> secondMole.setBackgroundResource(R.drawable.black)
                        3 -> thirdMole.setBackgroundResource(R.drawable.black)
                        4 -> forthMole.setBackgroundResource(R.drawable.black)
                    }
                    moleNumber = 0
                    moleScoreNumber = 0
                }
            }
        }

        val mole = object: CountDownTimer(1600, 500) {

            override fun onTick(millisUntilFinished: Long) {
                moleanimation(floor(millisUntilFinished.toDouble()/500).toInt())
            }
            override fun onFinish() {
                moleanimation(0)
            }
        }
        val game = object: CountDownTimer(60100, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                TimeToKill.text = floor(millisUntilFinished.toDouble()/1000).toInt().toString()
                if (floor(millisUntilFinished.toDouble()/1000).toInt()%5 == 0) {
                    RandMole()
                    mole.start()
                }

            }
            override fun onFinish() {
                TimeToKill.text = "0"
                textToReady.text = "Game Over"
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
