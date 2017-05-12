package net.hungerstruck.renaissance.countdown

/**
 * Manages all the countdowns
 */
class CountdownManager {
    private val runningCountdowns: MutableList<CountdownWrapper> = arrayListOf()

    fun start(countdown: Countdown, time: Int) {
        val wrapper = CountdownWrapper(countdown)
        wrapper.start(time)
        runningCountdowns.add(wrapper)
    }

    fun cancel(clazz: Class<out Countdown>) {
        runningCountdowns.removeAll {
            if (it.javaClass == clazz) {
                it.cancel()
                true
            } else {
                false
            }
        }
    }

    fun cancelAll() {
        runningCountdowns.removeAll {
            it.cancel()
            true
        }
    }

    fun removeCountdown(cd: Countdown) {
        for (cdw : CountdownWrapper in runningCountdowns) {
            if (cdw.countdown == cd) {
                runningCountdowns.remove(cdw)
                return
            }
        }
    }

    fun hasCountdown(clazz: Class<out Countdown>) = runningCountdowns.filter { it.countdown.javaClass == clazz }.any()
}