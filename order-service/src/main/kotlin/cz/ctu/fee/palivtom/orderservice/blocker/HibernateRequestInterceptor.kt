package cz.ctu.fee.palivtom.orderservice.blocker

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
        // nothing to do
    }

    override fun postHandle(request: WebRequest, model: ModelMap?) {
        // nothing to do
    }

    /**
     * Clears thread-locked variable before request response.
     */
    override fun afterCompletion(request: WebRequest, ex: Exception?) {
        transactionInterceptor.clear()
    }
}