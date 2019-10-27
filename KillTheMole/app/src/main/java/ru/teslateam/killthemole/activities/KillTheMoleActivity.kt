package ru.teslateam.killthemole.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import kotlinx.android.synthetic.main.activity_kill_the_mole.*
import kotlin.math.floor
import android.os.Handler
import androidx.constraintlayout.widget.Group
import ru.teslateam.killthemole.models.KillTheMoleClass
import ru.teslateam.killthemole.R


class KillTheMoleActivity : AppCompatActivity() {

    private lateinit var moleClass : KillTheMoleClass

    private fun View.showOrInvisible(show: Boolean) {
        visibility = if(show) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun kill(view: View) {
        scoreOfKill.text = moleClass.score.toString()
        view.setBackgroundResource(R.drawable.zombiedeath)
    }

    fun preNextGame() {
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
            1 -> firstMole.setBackgroundResource(moleClass.imageArray[a])
            2 -> secondMole.setBackgroundResource(moleClass.imageArray[a])
            3 -> thirdMole.setBackgroundResource(moleClass.imageArray[a])
            4 -> forthMole.setBackgroundResource(moleClass.imageArray[a])
        }
        if (a == 0) {
            moleClass.loseLife()
            lifeBeforeKill.text = moleClass.life.toString()
        }
    }

    private fun reStart() {
        scoreOfKill.text = resources.getString(R.string.resultOfKill)
        lifeBeforeKill.text = resources.getString(R.string.lifeBeforeDeath)
        timeToKill.text = resources.getString(R.string.timeToKill)
        buttonRestart.showOrInvisible(false)
        buttonHome.showOrInvisible(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kill_the_mole)

        moleClass = KillTheMoleClass()
        moleClass.modeNum = intent.getIntExtra(KillTheMoleClass.TOTAL_MODE,1)
        moleClass.switchMode()

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
                    preNextGame()
                    cancel()
                }
            }
            override fun onFinish() {
                preNextGame()
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

        buttonHome.setOnClickListener{
            finish()
        }

        buttonRestart.setOnClickListener{
            reStart()
            moleClass.reStart()
            timer.start()
        }

        fun onClickMole(view: View) {
            when(view.id) {
                firstMole.id -> moleClass.onClickId = 1
                secondMole.id -> moleClass.onClickId = 2
                thirdMole.id -> moleClass.onClickId = 3
                forthMole.id -> moleClass.onClickId = 4
            }
            if (moleClass.moleScoreNumber == moleClass.onClickId) {
                moleClass.scorePlus()
                kill(view)
            }
        }

        fun Group.setAllOnClickListener(listener: View.OnClickListener?) {
            referencedIds.forEach { id ->
                rootView.findViewById<View>(id).setOnClickListener(listener)
            }
        }

        group1.setAllOnClickListener(View.OnClickListener { v -> onClickMole(v) })


    }
}
