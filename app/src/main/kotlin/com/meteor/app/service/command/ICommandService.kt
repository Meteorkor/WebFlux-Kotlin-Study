package com.meteor.app.service.command

import com.meteor.app.service.command.vo.CommandVO

interface ICommandService {
    companion object {
        const val SAVE_COMMAND_SERVICE = "saveCommandService"
        const val DELETE_COMMAND_SERVICE = "deleteCommandService"
    }

    fun command(commandVO: CommandVO)
}