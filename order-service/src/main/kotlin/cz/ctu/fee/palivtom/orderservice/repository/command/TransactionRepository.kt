package cz.ctu.fee.palivtom.orderservice.repository.command

interface TransactionRepository {
    fun getTransactionId(): String
}