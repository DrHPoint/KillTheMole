package ru.teslateam.killthemole

import android.view.View
import kotlin.random.Random
import kotlin.math.floor

class KillTheMoleClass {

    companion object {
        const val TOTAL_MODE = "total_mode"
        const val STANDARD_COUNT: Long = 1000
        const val MIN_COUNT: Long = 500
        const val GAME_COUNT: Long = 60100
        const val TIMER_COUNT: Long = 3100
    }

    var moleNumber = 0
    var moleScoreNumber = 0
    var score = 0
    var life = 10
    var death = false
    var modeNum = 0
    var moleCount: Long = 0
    var moleOnTickCount: Long = 0
    var onClickId: Int = 0


    fun randMole() {
        moleNumber = Random.nextInt(1, 5)
        moleScoreNumber = moleNumber
    }

    fun scorePlus() {
        death = true
        score++
        moleScoreNumber = 0
    }

    fun reStart() {
        score = 0
        life = 10
    }

    fun switchMode() {
        moleCount = ((modeNum*3+1)*100).toLong()
        moleOnTickCount = (modeNum*100).toLong()
    }

    fun onTick(millis: Long):Int = floor(millis.toDouble()/1000).toInt()

    fun loseLife() {
        moleNumber = 0
        moleScoreNumber = 0
        if (!death) life--
    }

    fun newMole(millis: Long): Boolean = ((life != 0) && (onTick(millis) % (modeNum - 1) == 0))


}