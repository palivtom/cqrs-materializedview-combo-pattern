package cz.ctu.fee.palivtom.orderservice.config.db

import cz.ctu.fee.palivtom.orderservice.repository.command.TransactionRepository
import org.hibernate.EmptyInterceptor
import org.hibernate.Transaction
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
class HibernateTransactionInterceptor(
    @Lazy private val transactionRepository: TransactionRepository
) : EmptyInterceptor() {

    private val transactionId = ThreadLocal<String>()

    fun clear() {
        transactionId.remove()
    }

    fun getTransactionId(): String {
        return transactionId.get()
    }

    override fun beforeTransactionCompletion(tx: Transaction) {
        transactionRepository.getTransactionId().let {
            transactionId.set(it)
        }

        super.beforeTransactionCompletion(tx)
    }
}