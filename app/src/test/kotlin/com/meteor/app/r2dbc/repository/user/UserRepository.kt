package com.meteor.app.r2dbc.repository.user

import com.meteor.app.r2dbc.domain.user.User
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import javax.persistence.Converter

interface UserRepository : ReactiveCrudRepository<User, String>, UserRepositoryCustom {
    @Query(
        value = """
      insert into User(userId, name)
      values (:#{#user.userId}, :#{#user.name})
      """
    )
    fun insert(user: User): Mono<User>

//    @Query(
//        value = """
//        select * from User, UserInfo
//        where User.userId=:userId and User.userId=UserInfo.userId
//    """
//    )
//    fun findByIdLeftInnerJoinUserInfo(userId: String): Mono<User>


}