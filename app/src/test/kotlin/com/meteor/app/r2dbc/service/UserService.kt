package com.meteor.app.r2dbc.service

import com.meteor.app.r2dbc.domain.user.User
import com.meteor.app.r2dbc.repository.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class UserService(val userRepository: UserRepository) {
    @Transactional
    fun insertUser(user: User): Mono<Void> {
        return userRepository.insert(user)
    }
}