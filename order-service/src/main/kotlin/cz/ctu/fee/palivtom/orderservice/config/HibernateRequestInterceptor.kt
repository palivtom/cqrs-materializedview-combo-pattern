package cz.ctu.fee.palivtom.orderservice.config

import cz.ctu.fee.palivtom.orderservice.config.db.HibernateTransactionInterceptor
import org.springframework.stereotype.Component
import org.springframework.ui.ModelMap
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.WebRequestInterceptor
import java.lang.Exception

@Component
class HibernateRequestInterceptor(
    private val transactionInterceptor: HibernateTransactionInterceptor
) : WebRequestInterceptor {
    override fun preHandle(request: WebRequest) {
        transactionInterceptor.initialize()
    }

    override fun postHandle(request: WebRequest, model: ModelMap?) {
        // nothing to do
    }

    override fun afterCompletion(request: WebRequest, ex: Exception?) {
        transactionInterceptor.clear()
    }
}