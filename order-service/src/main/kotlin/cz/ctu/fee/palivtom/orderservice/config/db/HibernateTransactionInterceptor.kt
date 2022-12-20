package cz.ctu.fee.palivtom.orderservice.config.db

import cz.ctu.fee.palivtom.orderservice.repository.command.TransactionRepository
import org.hibernate.EmptyInterceptor
import org.hibernate.Transaction
import org.hibernate.type.Type
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class HibernateTransactionInterceptor(
    @Lazy private val transactionRepository: TransactionRepository
) : EmptyInterceptor() {

    private val queryCount = ThreadLocal<Long>()
    private val transactionId = ThreadLocal<String>()

    fun initialize() {
        queryCount.set(0L)
    }

    fun clear() {
        queryCount.remove()
        transactionId.remove()
    }

    fun getQueryCount(): Long {
        return queryCount.get()
    }

    private fun increaseQueryCount() {
        queryCount.set(queryCount.get() + 1)
    }

    fun getTransactionId(): String {
        return transactionId.get()
    }

    override fun onSave(entity: Any?, id: Serializable?, state: Array<out Any>?, propertyNames: Array<out String>?, types: Array<out Type>?): Boolean {
        increaseQueryCount()

        return super.onSave(entity, id, state, propertyNames, types)
    }

    override fun beforeTransactionCompletion(tx: Transaction) {
        transactionRepository.getTransactionId().let {
            transactionId.set(it)
        }

        super.beforeTransactionCompletion(tx)
    }
}