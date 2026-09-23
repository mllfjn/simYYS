package com.mllfjn.simyys

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.max

@Composable
@Preview
fun App() {
    MaterialTheme {

    }
}

class Hero {
    var baseAttack = 100.0;
    var baseDefense = 100.0;

    val statuses = mutableListOf<Staus>()

    fun getAttack(): Double {
        var rtValue = baseAttack
        for (status in statuses) {
            rtValue += status.getAttribute(Attribute.ATTACK)
        }
        return max(rtValue, 0.0)
    }

    fun getDefense(): Double {
        var rtValue = baseDefense
        for (status in statuses) {
            rtValue += status.getAttribute(Attribute.DEFENSE)
        }
        return max(rtValue, 0.0)
    }
}

class Staus {
    var attributes = mutableMapOf<Attribute, Double>()

    fun getAttribute(attribute: Attribute): Double {
        return attributes[attribute] ?: 0.0;
    }

    fun attribute(attribute: Attribute, value: Double) {
        attributes[attribute] = value;
    }
}

fun interface Function<T, R> {
    fun apply(t: T): R
}

enum class Attribute(desc: String, getter: Function<Hero, Double>) {
    ATTACK("攻击力", Hero::getAttack),
    DEFENSE("防御力", Hero::getDefense),
}