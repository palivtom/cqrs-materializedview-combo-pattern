package cz.ctu.fee.palivtom.orderservice.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {
    override fun getUserId() = SecurityContextHolder.getContext().authentication.principal as String
}