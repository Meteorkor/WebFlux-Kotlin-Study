package com.meteor.app.r2dbc.repository.user

import com.meteor.app.r2dbc.domain.user.UserInfo
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface UserInfoRepository : ReactiveCrudRepository<UserInfo, Long> {
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
      values (:#{#userInfo.userId}, :#{#userInfo.info})
      """
    )
    fun insert(userInfo: UserInfo): Mono<Void>
}