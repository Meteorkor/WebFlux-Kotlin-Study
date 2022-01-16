package com.meteor.app.controller

import com.meteor.app.enum.CommandEnum
import com.meteor.app.service.command.ICommandService
import com.meteor.app.service.command.vo.CommandVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("command")
class CommandController(
    val commandServiceEnumMap: EnumMap<CommandEnum, ICommandService>
) {
    @GetMapping
    fun get(cmd: String): String {
        CommandEnum.findByCommand(cmd).let {
            commandServiceEnumMap[it]?.command(CommandVO())
        }

        return "hello"
    }
}