package com.meteor.app.r2dbc.repository.user

import com.meteor.app.r2dbc.domain.user.User
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface UserInfoRepository : ReactiveCrudRepository<User, String> {
//    @Query(
//        value = """
//      insert into User(userId, name)
//      values (:#{#user.userId}, :#{#user.name})
//      """
//    )
//    fun insert(user: User): Mono<User>

    @Query(
        value = """
      insert into UserInfo(userId, info)
      values (:#{#user.userId}, :#{info})
      """
    )
    fun insert(user: User, info: String): Mono<User>
}