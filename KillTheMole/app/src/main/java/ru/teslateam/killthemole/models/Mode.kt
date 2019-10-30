package ru.teslateam.killthemole.models

enum class Mode(val inInt: Int) {

    NORMAL(4),
    HARD(3),
    HELL(2);

    companion object {
        fun getCardTypeByName(name: String): Int = valueOf(name.toUpperCase()).inInt
    }
}