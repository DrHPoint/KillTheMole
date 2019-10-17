package ru.teslateam.killthemole

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import kotlinx.android.synthetic.main.activity_kill_the_mole.*
import kotlin.math.floor
import android.os.Handler
import kotlin.random.Random
import kotlin.system.exitProcess


class KillTheMoleActivity : AppCompatActivity() {

    var moleNumber = 0
    var moleScoreNumber = 0
    var score = 0
    var life = 10
    private var death = false
    var imageArray = arrayOf(0,0,0,0)

    companion object {
        const val TOTAL_MODE = "total_mode"
        const val STANDARD_COUNT: Long = 1000
        const val GAME_COUNT: Long = 60100
        const val TIMER_COUNT: Long = 3100
    }

    //View

    fun onClickMole(view: View) {
        when(view.id) {
            firstMole.id -> if (moleScoreNumber == 1) scorePlus(view)
            secondMole.id -> if (moleScoreNumber == 2) scorePlus(view)
            thirdMole.id -> if (moleScoreNumber == 3) scorePlus(view)
            forthMole.id -> if (moleScoreNumber == 4) scorePlus(view)
        }
        scoreOfKill.text = score.toString()
    }

    //Controller

    private fun scorePlus(view: View) {
        death = true
        view.setBackgroundResource(R.drawable.zombiedeath)
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

        val modeNum: Int = intent.getIntExtra(TOTAL_MODE,1)
        val moleCount: Long = ((modeNum*3+1)*100).toLong()
        val moleOnTickCount: Long = (modeNum*100).toLong()
        
        fun View.showOrInvisible(show: Boolean) {
            visibility = if(show) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }

        fun nextGame() {
            timeToKill.text = resources.getString(R.string.resultOfKill)
            textToReady.text = resources.getString(R.string.GameOver)
            Handler().postDelayed({
                textToReady.text = resources.getString(R.string.Result) + score.toString()
                Handler().postDelayed({
                    buttonRestart.showOrInvisible(true)
                    buttonHome.showOrInvisible(true)
                }, 1000)
            }, 500)
        }

        fun moleAnimation(a: Int) {
            when (moleNumber) {
                1 -> firstMole.setBackgroundResource(imageArray[a])
                2 -> secondMole.setBackgroundResource(imageArray[a])
                3 -> thirdMole.setBackgroundResource(imageArray[a])
                4 -> forthMole.setBackgroundResource(imageArray[a])
            }
            if (a == 0) {
                moleNumber = 0
                moleScoreNumber = 0
                if (!death) {
                    life--
                    lifeBeforeKill.text = life.toString()
                }
            }
        }

        val mole = object: CountDownTimer(moleCount, moleOnTickCount) {
            override fun onTick(millisUntilFinished: Long) {
                if(moleNumber == moleScoreNumber)
                    moleAnimation(floor(millisUntilFinished.toDouble()/(modeNum*100)).toInt())
                else
                    moleAnimation(0)
            }
            override fun onFinish() {
                moleAnimation(0)
                death = false
            }
        }
        val game = object: CountDownTimer(GAME_COUNT, STANDARD_COUNT) {
            override fun onTick(millisUntilFinished: Long) {
                if (life != 0) {
                    timeToKill.text =
                        floor(millisUntilFinished.toDouble() / 1000).toInt().toString()
                    if (floor(millisUntilFinished.toDouble() / 1000).toInt() % (modeNum - 1) == 0) {
                        randMole()
                        mole.start()
                    }
                } else {
                    nextGame()
                    cancel()
                }
            }
            override fun onFinish() {
                nextGame()
            }
        }

        val timer = object: CountDownTimer(TIMER_COUNT, STANDARD_COUNT) {
            override fun onTick(millisUntilFinished: Long) {
                textToReady.text = floor(millisUntilFinished.toDouble()/1000).toInt().toString()
            }
            override fun onFinish() {
                textToReady.text = ""
                game.start()
            }
        }

        for (i in 0..3) {
            imageArray[i] = resources.getIdentifier("zombie$i","drawable", getPackageName())
        }

        timer.start()

        //Controller

        buttonRestart.setOnClickListener{
            scoreOfKill.text = resources.getString(R.string.resultOfKill)
            lifeBeforeKill.text = resources.getString(R.string.lifeBeforeDeath)
            timeToKill.text = resources.getString(R.string.timeToKill)
            score = 0
            life = 10
            timer.start()
            buttonRestart.showOrInvisible(false)
            buttonHome.showOrInvisible(false)
        }

        buttonHome.setOnClickListener{
            //val homeIntent = Intent(this, MainActivity::class.java)
            //startActivity(homeIntent)
            exitProcess(-1)
        }
    }
}
