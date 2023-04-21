package cz.ctu.fee.palivtom.orderservice.repository.impl

import cz.ctu.fee.palivtom.orderservice.repository.TransactionRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class PSQLTransactionRepositoryImpl(
    @PersistenceContext
    private val entityManager: EntityManager
) : TransactionRepository {
    override fun getTransactionId(): String {
        return entityManager
            .createNativeQuery("SELECT CAST (txid_current() as text)")
            .singleResult as String
    }
}