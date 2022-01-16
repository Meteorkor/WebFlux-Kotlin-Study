package com.meteor.app.config

import com.meteor.app.enum.CommandEnum
import com.meteor.app.service.command.ICommandService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class CommandConfig {

    @Bean
    fun commandServiceMap(commandServiceMap: Map<String, ICommandService>): EnumMap<CommandEnum, ICommandService> {
        return commandServiceMap.map {
            val serviceName = it.key
            Pair(
                CommandEnum.values().first { commandEnum -> commandEnum.commandServiceName == serviceName },
                it.value
            )
        }.fold(EnumMap(CommandEnum::class.java)) { acc, pair ->
            acc[pair.first] = pair.second
            acc
        }
    }
}