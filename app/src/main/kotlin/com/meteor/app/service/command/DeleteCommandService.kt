package com.meteor.app.service.command

import com.meteor.app.service.command.ICommandService.Companion.DELETE_COMMAND_SERVICE
import com.meteor.app.service.command.vo.CommandVO
import org.springframework.stereotype.Service

@Service(DELETE_COMMAND_SERVICE)
class DeleteCommandService : ICommandService {
    override fun command(commandVO: CommandVO) {
        TODO("Not yet implemented")
    }
}