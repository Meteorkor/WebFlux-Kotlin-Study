package com.meteor.app.r2dbc.repository.user

import com.meteor.app.r2dbc.domain.user.User
import com.meteor.app.r2dbc.domain.user.UserInfo
import org.springframework.r2dbc.core.DatabaseClient
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.*

/**
 * UserRepositoryCustomImple와 같은 이름은 안됨
 * UserRepositoryCustomImpl, "Impl"이름이 들어가야 정상 동작
 */
class UserRepositoryCustomImpl(val databaseClient: DatabaseClient) : UserRepositoryCustom {

    override fun findByIdWithUserInfo(userId: String): Mono<User> {
        return databaseClient.sql(
            """
                    select * from User, UserInfo
                    where User.userId=:userId and User.userId=UserInfo.userId
                """
        ).bind("userId", userId).fetch().first().map {
            //{USERID=userId, NAME=kim, USERINFOID=1, INFO=asd}
            val userId = it["userId"] as String
            val name = it["name"] as String

            val userInfo = Optional.ofNullable(it["userInfoId"]).map { userInfoId ->
                val info = it.getOrDefault("info", "") as String
                if (userInfoId is BigDecimal) {
                    UserInfo(userInfoId = userInfoId.toLong(), userId = userId, info)
                } else {
                    UserInfo(userInfoId = userInfoId as Long, userId = userId, info)
                }
            }.orElse(null)

            User(userId, name, userInfo)
        }
    }
}