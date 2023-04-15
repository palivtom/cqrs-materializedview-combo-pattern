package cz.ctu.fee.palivtom.orderupdaterservice.model

import cz.ctu.fee.palivtom.orderupdaterservice.model.event.Event

data class EventList(
    private var targetCount: Int = Int.MIN_VALUE,
    private val events: MutableList<Event> = mutableListOf()
) {
    fun addEvent(event: Event): Boolean = events.add(event)

    fun getEvents(): List<Event> = events

    /**
     * Sets the target count of events.
     *
     * @param targetCount the number of events in source transaction
     */
    fun setTargetCount(targetCount: Int) {
        this.targetCount = targetCount
    }

    /**
     * Target count is not required during instantiation—the [Int.MIN_VALUE] is set by default.
     *
     * @return true if the default value has been overridden
     */
    fun isCountSet(): Boolean = targetCount != Int.MIN_VALUE

    /**
     * Current event count matches the target count.
     *
     * @return true if matches
     */
    fun isComplete(): Boolean = events.size == targetCount
}