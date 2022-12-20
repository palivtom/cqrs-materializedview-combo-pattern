package cz.ctu.fee.palivtom.orderservice.repository.command

import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class TransactionRepository(
    private val entityManager: EntityManager
) {

    fun getTransactionId(): String {
        return entityManager
            .createNativeQuery("SELECT CAST (txid_current() as text)")
            .singleResult as String
    }

}