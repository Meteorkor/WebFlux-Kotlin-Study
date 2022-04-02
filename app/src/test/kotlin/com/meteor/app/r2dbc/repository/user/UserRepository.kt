package com.meteor.app.r2dbc.repository.user

import com.meteor.app.r2dbc.domain.user.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface UserRepository : ReactiveCrudRepository<User, String> {
}