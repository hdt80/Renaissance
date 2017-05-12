package net.hungerstruck.renaissance.countdown

abstract class Countdown {
    /**
     * Occurs when the Countdown is started
     *
     * @param timeLeft How many seconds are left in the Countdown
     */
    open fun onStart(timeLeft: Int) {}

    /**
     * Occurs when the Countdown has been ticked
     *
     * @param timeLeft How many seconds are left in the Countdown
     */
    abstract fun onTick(timeLeft: Int)

    /**
     * Occurs when the Countdown has finished
     */
    abstract fun onFinish()

    /**
     * Occurs when the Countdown has prematurely been cancelled
     */
    open fun onCancel() {}
}