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

    fun setTargetCount(targetCount: Int): Boolean {
        this.targetCount = targetCount
        return isComplete()
    }

    fun isCountSet(): Boolean {
        return targetCount != Int.MIN_VALUE
    }

    private fun isComplete(): Boolean {
        return events.size == targetCount
    }
}