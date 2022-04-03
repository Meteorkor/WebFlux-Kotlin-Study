package com.meteor.app

import com.meteor.app.r2dbc.domain.user.Item
import com.meteor.app.r2dbc.repository.user.ItemRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ItemR2dbcTest {
    @Autowired
    lateinit var itemRepository: ItemRepository

    @Test
    internal fun test() {
        itemRepository.findAll().map {
            println("it: ${it}")
        }.blockLast()
    }

    @Test
    internal fun insertFail() {
        val userId = "userId"
//        var user = User(name = "kim", userId = userId)
        val item1 = Item(itemName = "itemName1", userId = null)
        val item2 = Item(itemName = "itemName2", userId = null)

        val block1 = itemRepository.save(item1).block()
        val block2 = itemRepository.save(item2).block()
        println("block1 : ${block1}" )
        println("block2 : ${block2}" )
        val block = itemRepository.findById(1L).block()
        println("block : ${block}")
    }

//    @Test
//    internal fun insertSuccess() {
//        val userId = "userId"
//        var user = User(name = "kim", userId = userId)
//
//        userRepository.insert(user).block()
//        val block = userRepository.findById(userId).block()
//        println("block : ${block}")
//    }
}