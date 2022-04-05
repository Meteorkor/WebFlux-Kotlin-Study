package com.meteor.app.test.r2dbc

import com.meteor.app.r2dbc.domain.user.User
import com.meteor.app.r2dbc.domain.user.UserInfo
import com.meteor.app.r2dbc.repository.user.UserInfoRepository
import com.meteor.app.r2dbc.repository.user.UserRepository
import com.meteor.app.r2dbc.service.TransactionService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@SpringBootTest
class UserR2dbcTest {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userInfoRepository: UserInfoRepository

    @Autowired
    lateinit var transactionService: TransactionService

    @Test
    internal fun test() {
        userRepository.findAll().map {
            println("it: ${it}")
        }.blockLast()
    }

    //    @Test
    internal fun insertFail() {
//        val userId = "userId"
//        val userInfo = UserInfo(userId = userId, info = "asd")
//        var user = User(name = "kim", userId = userId)
//
//        userRepository.save(user).block()
//        val block = userRepository.findById(userId).block()
//        println("block : ${block}")
    }

    @Test
    @DisplayName("@OneToOne Not Working")
    internal fun insertSuccess() {
        val userId = "userId"
//        var user = User(name = "kim", userId = userId)
        val userInfo = UserInfo(userId = userId, info = "asd")
        var user = User(name = "kim", userId = userId, userInfo = userInfo)

        userRepository.insert(user).block()
        val block = userRepository.findById(userId).block()
        println("block : ${block}")
        println("block?.userInfo : ${block?.userInfo}")

        Assertions.assertNull(block?.userInfo)
    }

    @Test
    @DisplayName("@OneToOne Manual")
    internal fun oneToOneManual() {
        val userId = "userId"
//        var user = User(name = "kim", userId = userId)
        val userInfo = UserInfo(userId = userId, info = "asd")
        var user = User(name = "kim", userId = userId, userInfo = userInfo)


        val userInfoInsert = userInfoRepository.insert(userInfo)
        val userInsert = userRepository.insert(user)

        transactionService.transaction {
            userInsert
                .log()
                .then(userInfoInsert)
        }.block()

        val block = userRepository.findByIdWithUserInfo(userId).block()
        Assertions.assertNotNull(block)
        Assertions.assertNotNull(block?.userInfo)
    }

    @Test
    @DisplayName("updateAndFindTest")
    internal fun updateAndFindTest() {
        val userId = "userId"
        val userInfo = UserInfo(userId = userId, info = "asd")
        var user = User(name = "kim", userId = userId, userInfo = userInfo)


        userRepository.insert(user).block()
        userInfoRepository.insert(userInfo).block()

        val changeName = "changeName"
        user.name = changeName
        //UPDATE user SET name = $1 WHERE user.userid = $2 {1: 'changeName', 2: 'userId'}
        userRepository.save(user).block()

//        val block = userRepository.findByIdWithUserInfo(userId).block()
//        println("block : ${block}")
//        Assertions.assertNotNull(block)
//        Assertions.assertNotNull(block?.userInfo)

        StepVerifier.create(userRepository.findByIdWithUserInfo(userId))
            .expectNextMatches {
                changeName == it.name && it.userInfo != null
            }
    }

    @Test
    @DisplayName("updateAndFindTestChain")
    internal fun updateAndFindTestChain() {
        val userId = "userId"
        val userInfo = UserInfo(userId = userId, info = "asd")
        var user = User(name = "kim", userId = userId, userInfo = userInfo)
        val changeName = "changeName"


        StepVerifier.create(
            userRepository.insert(user)
                .then(userInfoRepository.insert(userInfo))
                .then(Mono.defer {
                    user.name = changeName
                    userRepository.save(user)
                })
                .then(userRepository.findByIdWithUserInfo(userId))
        ).expectNextMatches {
            println("expectNextMatches!!")
            changeName == it.name && it.userInfo != null
        }.verifyComplete()
    }
}