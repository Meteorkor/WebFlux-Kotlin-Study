package com.meteor.app.r2dbc.service

import com.meteor.app.r2dbc.domain.user.Item
import com.meteor.app.r2dbc.repository.user.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class ItemService(val itemRepository: ItemRepository) {
    @Transactional
    fun saveItems(list: List<Item>): Mono<Long> {
        return itemRepository.saveAll(list).count()
    }

    @Transactional
    fun saveItem(item: Item): Mono<Item> {
        return itemRepository.save(item);
    }

    @Transactional
    fun updateItem(item: Item):Mono<String> {
        item.userId = "asd!!@312"
        //update, commit 호출되지는 않음
        return Mono.empty<String>()
    }

    fun findById(userId: String): Mono<Item> {
        return itemRepository.findByUserId(userId);
    }
}