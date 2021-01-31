package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))

    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    this.time += when(units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    return this
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(value: Int): String {

        return "$value ${pluralForm(value)}"
    }

    private fun pluralForm(value: Int): String {
        val plurals = mapOf(
                SECOND.name to listOf("секунд", "секунда", "секунды"),
                MINUTE.name to listOf("минут", "минута", "минуты"),
                HOUR.name to listOf("часов", "час", "часа"),
                DAY.name to listOf("дней", "день", "дня")
        )

        val pluralIndex: Int = when (abs(value) % 100) {
            1 -> 1
            in 2..4 -> 2
            0, in 5..19 -> 0
            else -> return pluralForm(abs(value) % 10)
        }

        return (plurals[this.name] ?: error(""))[pluralIndex]
    }
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = abs(this.time -  date.time)
    val isFuture = this.time > date.time

    return when {
        diff > DAY * 360 ->
            if (isFuture) "более чем через год" else "более года назад"
        diff > HOUR * 26 ->
            humanizeDiffMessage(TimeUnits.DAY.plural(millisecondsTo(diff, DAY)), isFuture)
        diff > HOUR * 22 ->
            humanizeDiffMessage("день", isFuture)
        diff > MINUTE * 75 ->
            humanizeDiffMessage(TimeUnits.HOUR.plural(millisecondsTo(diff, HOUR)), isFuture)
        diff > MINUTE * 45 -> humanizeDiffMessage("час", isFuture)
        diff > SECOND * 75 ->
            humanizeDiffMessage(TimeUnits.MINUTE.plural(millisecondsTo(diff, MINUTE)), isFuture)
        diff > SECOND * 45 ->
            humanizeDiffMessage("минуту", isFuture)
        diff > SECOND * 1 ->
            humanizeDiffMessage("несколько секунд", isFuture)
        else -> "только что"
    }
}

private fun humanizeDiffMessage(plural: String, isFuture: Boolean = false): String {
    return if (isFuture) "через $plural" else "$plural назад"
}

private fun millisecondsTo(milliseconds: Long, divisor: Long): Int {
    return (milliseconds / divisor).toInt()
}
