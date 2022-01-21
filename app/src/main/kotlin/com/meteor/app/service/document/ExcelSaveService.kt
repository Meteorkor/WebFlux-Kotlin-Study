package com.meteor.app.service.document

import com.meteor.app.service.command.vo.CommandVO
import org.springframework.stereotype.Service

@Service
class ExcelSaveService() : AbstractDocumentSaveService() {
    fun save() {
        saveCommandService.command(CommandVO())
    }
}