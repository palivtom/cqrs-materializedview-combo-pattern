package cz.ctu.fee.palivtom.transactiongrouperservice.model

class EventList(
    transaction: Transaction? = null,
    private val events: MutableMap<String, TableEvent> = mutableMapOf()
) {

    private var transaction: Transaction? = null

    init {
        transaction?.also { this.setTransaction(it) }
    }

    /**
     * Adds an event to internal [MutableMap]. If the event is already added, it returns false.
     *
     * The event key is composed by [composeKey].
     */
    fun addEvent(event: TableEvent): Boolean {
        val key = composeKey(event)

        return if (!events.contains(key)) {
            events[key] = event
            true
        } else false
    }

    fun getEvents(): MutableCollection<TableEvent> = events.values

    /**
     * Sets [Transaction] if the value is not already initialized. Otherwise, throws [UnsupportedOperationException].
     */
    fun setTransaction(transaction: Transaction) {
        if (isTransactionSet()) {
            throw UnsupportedOperationException("Value has been already initialized.")
        }
        this.transaction = transaction
    }

    fun getTransaction(): Transaction? = transaction

    /**
     * Returns true if [Transaction] is set.
     */
    private fun isTransactionSet(): Boolean = transaction != null

    /**
     * The current event count (size of internal [MutableMap]) matches the target count
     * (total event count [Transaction.eventCount]).
     *
     * The transaction is required to be set (otherwise, false is returned).
     */
    fun isComplete(): Boolean = events.size == transaction?.eventCount

    /**
     * Composes key as txId#txTotalOrder.
     */
    private fun composeKey(event: TableEvent): String = "${event.transactionMetadata.txId}#${event.transactionMetadata.txTotalOrder}"
}