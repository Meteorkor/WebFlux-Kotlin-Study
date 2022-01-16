package com.meteor.app.enum

import com.meteor.app.const.CommandConst
import com.meteor.app.service.command.ICommandService.Companion.DELETE_COMMAND_SERVICE
import com.meteor.app.service.command.ICommandService.Companion.SAVE_COMMAND_SERVICE

enum class CommandEnum(
    val command: String,
    val commandServiceName: String
) {
    SAVE(CommandConst.SAVE, SAVE_COMMAND_SERVICE), DELETE(CommandConst.DELETE, DELETE_COMMAND_SERVICE)
    ;

    companion object {
        fun findByCommand(command: String): CommandEnum {
            return values().first { it.command == command }
        }
    }
}