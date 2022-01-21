package com.meteor.app.service.document

import com.meteor.app.service.command.SaveCommandService
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractDocumentSaveService {
    @Autowired
    protected lateinit var saveCommandService: SaveCommandService
}