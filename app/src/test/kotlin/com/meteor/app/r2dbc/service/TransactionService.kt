package com.meteor.app.r2dbc.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
class TransactionService() {
    @Transactional
    fun <T> transaction(apply: () -> Mono<T>): Mono<T> {
        return apply.invoke()
    }
}