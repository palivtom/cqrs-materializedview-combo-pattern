package cz.ctu.fee.palivtom.orderservice.repository

interface TransactionRepository {
    /**
     * Calls database and asks for the current transaction ID.
     *
     * @return transaction ID
     */
    fun getTransactionId(): String
}