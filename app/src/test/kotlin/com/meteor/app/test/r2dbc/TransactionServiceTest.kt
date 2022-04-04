package com.meteor.app.test.r2dbc

import com.meteor.app.r2dbc.domain.user.Item
import com.meteor.app.r2dbc.domain.user.User
import com.meteor.app.r2dbc.repository.user.UserRepository
import com.meteor.app.r2dbc.service.ItemService
import com.meteor.app.r2dbc.service.TransactionService
import com.meteor.app.r2dbc.service.UserService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@SpringBootTest
class TransactionServiceTest {
    @Autowired
    private lateinit var itemService: ItemService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var transactionService: TransactionService


    @Test
    @DisplayName("Required Test")
    internal fun saveAndFindItemPerBlockDD() {
        val userId = "userId"
        IntRange(0, 10).map {
            Item(itemName = "itemName ${it}", userId = userId)
        }.toList()
            .let {
                it.forEach { item ->
                    //Request:  COMMIT
                    println("item : " + item)

                    itemService.saveItem(item).block()
                }
            }
    }

    @Test
    @DisplayName("Required Test")
    internal fun transactionWrapRequiredTest() {
        val userId = "userId"
        IntRange(0, 10).map {
            Item(itemName = "itemName ${it}", userId = userId)
        }.toList()
            .let { itemList ->
                transactionService.transaction {
                    Flux.fromIterable(itemList).flatMap {
                        println("it : ${it}")
                        itemService.saveItem(it)
                    }.then()
                }.block()
            }
    }

    @Test
    @DisplayName("addBatchTestNotAutoIncrementId")
    internal fun addBatchTestNotAutoIncrementId() {
        val userId = "userId"

        IntRange(0, 100).map {
            User(userId = "userId:${it}", "userName")
        }.toList()
            .let { userList ->
                transactionService.transaction {
                    Flux.fromIterable(userList).flatMap { user ->
                        println("user : ${user}")
                        userService.insertUser(user)
                    }.then()
                }.block()
            }

        userRepository.findAll().doOnNext {
            println("!!user : ${it}")
        }.blockLast()
    }

    @Test
    @DisplayName("requiredErrorTest")
    internal fun requiredErrorTest() {
        val userId = "userId"

        IntRange(0, 100).map {
            User(userId = "userId:${it}", "userName")
        }.toList()
            .let { userList ->
                transactionService.transaction {
                    Flux.fromIterable(userList).flatMap { user ->
                        println("user : ${user}")
                        userService.insertUser(user)
                    }.then()
                }.block()
            }

        IntRange(0, 100).map {
            User(userId = "userId:${it}", "${it}")
        }.toList()
            .let { userList ->
                try {
                    transactionService.transaction {
                        Flux.fromIterable(userList).flatMap { user ->
                            println("user222 : ${user}")
                            if (Integer.parseInt(user.name) > 10) {
                                Mono.error<String>(NullPointerException("!!!"))
                            } else {
                                userService.insertUser(user)
                            }
                        }.then()
                    }.block()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        userRepository.findAll().doOnNext {
            println("!!user : ${it}")
        }.blockLast()
    }
}