package com.meteor.app.r2dbc.domain.user

import org.springframework.data.relational.core.mapping.Table

@Table
data class User(val userId: String, val name: String) {
}