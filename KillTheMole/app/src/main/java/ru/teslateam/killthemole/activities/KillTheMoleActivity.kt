package ru.teslateam.killthemole.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_kill_the_mole.*
import android.os.Handler
import ru.teslateam.killthemole.R
import ru.teslateam.killthemole.data.DataClassActivity
import ru.teslateam.killthemole.models.*


class KillTheMoleActivity : AppCompatActivity() {

    private lateinit var moleClass: KillTheMoleClass

    private fun kill(view: View) {
        scoreOfKill.text = moleClass.score.toString()
        view.setBackgroundResource(R.drawable.zombiedeath)
        Handler().postDelayed({
            view.setBackgroundResource(moleClass.imageArray[0])
        }, moleClass.moleOnTickCount)
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
        if (moleClass.move())
            findViewById<Button>(moleClass.moleNumber).setBackgroundResource(moleClass.imageArray[a])
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

    private fun onClickMole(view: View) {
        if (moleClass.hunter(view.id)) {
            moleClass.scorePlus()
            kill(view)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kill_the_mole)

        val thisData = DataClassActivity(
            "KillTheMole",
            MyApplication.applicationContext(), group1.referencedIds,
            intent.getIntExtra(KillTheMoleClass.TOTAL_MODE, 1)
        )
        moleClass = KillTheMoleClass(thisData)

        val mole = object : CountDownTimer(moleClass.moleCount, moleClass.moleOnTickCount) {
            override fun onTick(millisUntilFinished: Long) {
                if (moleClass.move())
                    moleAnimation(moleClass.whichMole(millisUntilFinished))
                else
                    moleAnimation(0)
            }

            override fun onFinish() {
                moleAnimation(0)
                moleClass.death = false
            }
        }
        val game =
            object : CountDownTimer(KillTheMoleClass.GAME_COUNT, KillTheMoleClass.STANDARD_COUNT) {
                override fun onTick(millisUntilFinished: Long) {
                    timeToKill.text = moleClass.onTick(millisUntilFinished).toString()
                    if (moleClass.newMole(millisUntilFinished)) {
                        moleClass.randMole()
                        mole.start()
                    }
                    if (moleClass.loseGame()) {
                        preNextGame()
                        cancel()
                    }
                }

                override fun onFinish() {
                    preNextGame()
                }
            }
        val timer =
            object : CountDownTimer(KillTheMoleClass.TIMER_COUNT, KillTheMoleClass.STANDARD_COUNT) {
                override fun onTick(millisUntilFinished: Long) {
                    textToReady.text = moleClass.onTick(millisUntilFinished).toString()
                }

                override fun onFinish() {
                    textToReady.text = resources.getString(R.string.Empty)
                    game.start()
                }
            }

        timer.start()

        buttonHome.setOnClickListener {
            finish()
        }

        buttonRestart.setOnClickListener {
            reStart()
            moleClass.reStart()
            timer.start()
        }

        group1.setAllOnClickListener(View.OnClickListener { v -> onClickMole(v) })


    }
}
