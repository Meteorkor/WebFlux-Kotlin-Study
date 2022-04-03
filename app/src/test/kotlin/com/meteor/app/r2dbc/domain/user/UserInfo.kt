package com.meteor.app.r2dbc.domain.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType

@Table
data class UserInfo(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var userInfoId: Long? = null,
    val userId: String,
    val info: String
) {
}