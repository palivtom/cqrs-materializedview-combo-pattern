package cz.ctu.fee.palivtom.orderservice.blocker

import cz.ctu.fee.palivtom.orderservice.repository.TransactionRepository
import org.hibernate.EmptyInterceptor
import org.hibernate.Transaction
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

/**
 * Interceptor holds transaction ID per thread/request.
 *
 * Premiss for this approach is a single transaction per request. Otherwise, only the last one will be held.
 */
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