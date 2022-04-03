package com.meteor.app

import com.meteor.app.r2dbc.domain.user.Item
import com.meteor.app.r2dbc.service.ItemService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.test.StepVerifier

@SpringBootTest
class ItemServiceTest {
    @Autowired
    private lateinit var itemService: ItemService

    @Test
    @DisplayName("itemId 생성 확인 테스트")
    internal fun saveItem() {
        val item1 = Item(itemName = "itemName1", userId = null)
        Assertions.assertNotNull(itemService.saveItem(item1).block()!!.itemId)
    }

    @Test
    @DisplayName("save and find block으로 분리하여 확인")
    internal fun saveAndFindItemPerBlock() {
        val userId = "userId"
        val itemName = "itemName1"
        val item1 = Item(itemName = itemName, userId = userId)
        itemService.saveItem(item1).block()

        val findItem = itemService.findById(userId).block()
        Assertions.assertEquals(findItem!!.itemName, itemName)
    }

    @Test
    @DisplayName("save and find 확인")
    internal fun saveAndFindItem() {
        val userId = "userId"
        val itemName = "itemName1"
        val item1 = Item(itemName = itemName, userId = userId)
        StepVerifier.create(itemService.saveItem(item1).flatMap {
            itemService.findById(userId)
        }).expectNextMatches {
            itemName.equals(it.itemName)
        }.verifyComplete()
    }

    @Test
    @DisplayName("save and setupdate and find 확인, 중간 커밋 확인")
    internal fun saveAndSaveAndFindItem() {
        val userId = "userId"
        val itemName = "itemName1"
        val item1 = Item(itemName = itemName, userId = userId)
        StepVerifier.create(
            itemService.saveItem(item1).doOnNext {
                itemService.updateItem(item1)
            }.flatMap {
                itemService.findById(userId)
            }).expectNextMatches {
            itemName.equals(it.itemName)
        }.verifyComplete()
    }

    @Test
    @DisplayName("saveList addBatch 테스트(autoId)")
    internal fun saveListAddBatchCheck() {
        val userId = "userId"
        IntRange(0, 10).map {
            Item(itemName = "itemName ${it}", userId = userId)
        }.toList().let {
            itemService.saveItems(it).block()
        }
    }

    @Test
    @DisplayName("save item List each 테스트(autoId)")
    internal fun saveListEachCheck() {
        val userId = "userId"
        IntRange(0, 10).map {
            Item(itemName = "itemName ${it}", userId = userId)
        }.toList().let {
            it.forEach {//Request:  COMMIT
                itemService.saveItem(it).block()
            }
        }
    }



//    @DisplayName("Transaction rollback Test")

}