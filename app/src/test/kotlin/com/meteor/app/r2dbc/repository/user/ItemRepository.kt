package com.meteor.app.r2dbc.repository.user

import com.meteor.app.r2dbc.domain.user.Item
import com.meteor.app.r2dbc.domain.user.User
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface ItemRepository : ReactiveCrudRepository<Item, Long> {
//    @Query(
//        value = """
//      insert into Item(userId, name)
//      values (:#{#user.userId}, :#{#user.name})
//      """
//    )
//    fun insert(user: User): Mono<User>
    fun findByUserId(userId: String): Mono<Item>
}