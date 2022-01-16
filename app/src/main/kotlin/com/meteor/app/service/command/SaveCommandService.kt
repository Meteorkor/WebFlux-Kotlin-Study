package com.meteor.app.service.command

import com.meteor.app.service.command.ICommandService.Companion.SAVE_COMMAND_SERVICE
import com.meteor.app.service.command.vo.CommandVO
import org.springframework.stereotype.Service

@Service(SAVE_COMMAND_SERVICE)
class SaveCommandService : ICommandService {
    override fun command(commandVO: CommandVO) {
        TODO("Not yet implemented")
    }
}