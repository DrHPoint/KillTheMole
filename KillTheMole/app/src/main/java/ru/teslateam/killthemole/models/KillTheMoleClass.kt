package ru.teslateam.killthemole.models

import ru.teslateam.killthemole.data.DataClassActivity
import kotlin.random.Random
import kotlin.math.floor

class KillTheMoleClass(data: DataClassActivity) {

    companion object {
        const val TOTAL_MODE = "total_mode"
        const val STANDARD_COUNT: Long = 1000
        const val MIN_COUNT: Long = 500
        const val GAME_COUNT: Long = 60100
        const val TIMER_COUNT: Long = 3100
    }

    var imageArray = Array(4){0}
    var moleNumber = 0
    var score = 0
    var life = 10
    var death = false
    private var modeNum = 0
    var moleCount: Long = 0
    var moleOnTickCount: Long = 0
    private var buttonIds: IntArray? = null

    init{
        for (i in 0..3) {
            imageArray[i] = data.context!!.resources
                .getIdentifier("zombie$i","drawable",
                    data.context.packageName)
        }
        buttonIds = data.ids
        modeNum = data.mode
        moleCount = ((modeNum*3+1)*100).toLong()
        moleOnTickCount = (modeNum*100).toLong()
    }

    fun randMole() {
        moleNumber = buttonIds!![Random.nextInt(0, 4)]
    }

    fun scorePlus() {
        death = true
        score++
        moleNumber = 0
    }

    fun reStart() {
        score = 0
        life = 10
    }

    fun onTick(millis: Long): Int = floor(millis.toDouble()/1000).toInt()

    fun loseLife() {
        moleNumber = 0
        if (!death) life--
    }

    fun loseGame(): Boolean = (life == 0)

    fun whichMole(millis: Long): Int = floor(millis.toDouble()/(modeNum*100)).toInt()

    fun newMole(millis: Long): Boolean = ((!loseGame()) && (onTick(millis) % (modeNum - 1) == 0))

    fun hunter(id: Int): Boolean = (moleNumber == id)

    fun move(): Boolean = (moleNumber != 0)


}