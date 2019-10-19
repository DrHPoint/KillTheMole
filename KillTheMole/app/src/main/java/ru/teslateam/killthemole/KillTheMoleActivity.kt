package ru.teslateam.killthemole

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import kotlinx.android.synthetic.main.activity_kill_the_mole.*
import kotlin.math.floor
import android.os.Handler
import android.widget.RadioGroup
import android.widget.Button
import kotlin.random.Random
import kotlin.system.exitProcess


class KillTheMoleActivity : AppCompatActivity() {

    private var moleClass = KillTheMoleClass()
    private var imageArray = arrayOf(0,0,0,0)

    //View

    fun onClickMole(view: View) {
        when(view.id) {
            firstMole.id -> moleClass.onClickId = 1
            secondMole.id -> moleClass.onClickId = 2
            thirdMole.id -> moleClass.onClickId = 3
            forthMole.id -> moleClass.onClickId = 4
        }
        if (moleClass.moleScoreNumber == moleClass.onClickId) kill(view)
    }

    private fun kill(view: View) {
        moleClass.scorePlus()
        scoreOfKill.text = moleClass.score.toString()
        view.setBackgroundResource(R.drawable.zombiedeath)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kill_the_mole)

        //Model

        moleClass.modeNum = intent.getIntExtra(KillTheMoleClass.TOTAL_MODE,1)
        moleClass.switchMode()

        for (i in 0..3) {
            imageArray[i] = resources.getIdentifier("zombie$i","drawable", getPackageName())
        }

        // View

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
                textToReady.text = resources.getString(R.string.Result) + moleClass.score.toString()
                Handler().postDelayed({
                    buttonRestart.showOrInvisible(true)
                    buttonHome.showOrInvisible(true)
                }, KillTheMoleClass.STANDARD_COUNT)
            }, KillTheMoleClass.MIN_COUNT)
        }

        fun moleAnimation(a: Int) {
            when (moleClass.moleNumber) {
                1 -> firstMole.setBackgroundResource(imageArray[a])
                2 -> secondMole.setBackgroundResource(imageArray[a])
                3 -> thirdMole.setBackgroundResource(imageArray[a])
                4 -> forthMole.setBackgroundResource(imageArray[a])
            }
            if (a == 0) {
                moleClass.loseLife()
                lifeBeforeKill.text = moleClass.life.toString()
            }
        }

        val mole = object: CountDownTimer(moleClass.moleCount, moleClass.moleOnTickCount) {
            override fun onTick(millisUntilFinished: Long) {
                if(moleClass.moleNumber == moleClass.moleScoreNumber)
                    moleAnimation(floor(millisUntilFinished.toDouble()/(moleClass.modeNum*100)).toInt())
                else
                    moleAnimation(0)
            }
            override fun onFinish() {
                moleAnimation(0)
                moleClass.death = false
            }
        }
        val game = object: CountDownTimer(KillTheMoleClass.GAME_COUNT, KillTheMoleClass.STANDARD_COUNT) {
            override fun onTick(millisUntilFinished: Long) {
                timeToKill.text = moleClass.onTick(millisUntilFinished).toString()
                if (moleClass.newMole(millisUntilFinished)) {
                        moleClass.randMole()
                        mole.start()
                } else {
                    nextGame()
                    cancel()
                }
            }
            override fun onFinish() {
                nextGame()
            }
        }
        val timer = object: CountDownTimer(KillTheMoleClass.TIMER_COUNT, KillTheMoleClass.STANDARD_COUNT) {
            override fun onTick(millisUntilFinished: Long) {
                textToReady.text = moleClass.onTick(millisUntilFinished).toString()
            }
            override fun onFinish() {
                textToReady.text = resources.getString(R.string.Empty)
                game.start()
            }
        }

        timer.start()

        //Controller

        buttonRestart.setOnClickListener{
            scoreOfKill.text = resources.getString(R.string.resultOfKill)
            lifeBeforeKill.text = resources.getString(R.string.lifeBeforeDeath)
            timeToKill.text = resources.getString(R.string.timeToKill)
            moleClass.reStart()
            timer.start()
            buttonRestart.showOrInvisible(false)
            buttonHome.showOrInvisible(false)
        }

        buttonHome.setOnClickListener{
            finish()
        }
    }
}
