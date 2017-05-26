package net.hungerstruck.renaissance.util

import net.hungerstruck.renaissance.RLogger
import java.util.*

class RandomCollection<E> {
    val map: NavigableMap<Double, E> = TreeMap()
    val random: Random = Random()
    var total: Double = 0.0

    fun clear() {
        map.clear()
        total = 0.0
    }

    operator fun set(weight: Double, entry: E) = add(weight, entry)

    fun add(weight: Double, entry: E) {
        if (weight <= 0) return
        total += weight
        map.put(total, entry)
    }

    fun next(): E {
        val value = random.nextDouble() * total
        return map.ceilingEntry(value).value
    }
}