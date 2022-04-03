package com.meteor.app

import com.meteor.app.r2dbc.domain.user.User
import com.meteor.app.r2dbc.domain.user.UserInfo
import com.meteor.app.r2dbc.repository.user.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserR2dbcTest {
    @Autowired
    lateinit var userRepository: UserRepository

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
    internal fun insertSuccess() {
        val userId = "userId"
//        var user = User(name = "kim", userId = userId)
        val userInfo = UserInfo(userId = userId, info = "asd")
        var user = User(name = "kim", userId = userId, userInfo = userInfo)

        userRepository.insert(user).block()
        val block = userRepository.findById(userId).block()
        println("block : ${block}")
    }
}