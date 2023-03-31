package cz.ctu.fee.palivtom.orderupdaterservice.model

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.Event

class EventList(
    private var targetCount: Int = Int.MIN_VALUE,
    private val events: MutableList<Event> = mutableListOf()
) {
    fun addEvent(event: Event): Boolean {
        events.add(event)
        return isComplete()
    }

    fun getEvents(): List<Event> {
        return events
    }

    /**
     * Sets the target count of events.
     *
     * @param targetCount the number of events in source transaction
     * @return true if target is reached
     */
    fun setTargetCount(targetCount: Int): Boolean {
        this.targetCount = targetCount
        return isComplete()
    }

    /**
     * Target count is not required during instantiation—the [Int.MIN_VALUE] is set by default.
     *
     * @return the default value has been overridden
     */
    fun isCountSet(): Boolean {
        return targetCount != Int.MIN_VALUE
    }

    /**
     * Current event count matches the target count.
     *
     * @return true if matches
     */
    private fun isComplete(): Boolean {
        return events.size == targetCount
    }
}