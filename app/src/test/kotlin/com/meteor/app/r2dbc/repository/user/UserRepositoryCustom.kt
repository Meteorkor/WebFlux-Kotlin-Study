package com.meteor.app.r2dbc.repository.user

import com.meteor.app.r2dbc.domain.user.User
import reactor.core.publisher.Mono

interface UserRepositoryCustom {
    fun findByIdWithUserInfo(userId: String): Mono<User>
}