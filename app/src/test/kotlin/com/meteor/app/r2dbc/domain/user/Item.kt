package com.meteor.app.r2dbc.domain.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType

/*
@GeneratedValue 만은 동작하지 않음
INSERT INTO item (itemname) VALUES ($1) {1: 'itemName1'}
현재는 지원하지 않는것 같음(추후 확인 필요)
Table 생성시 AutoIncrement로 설정시 테이블 동작으로 인해 자동으로 할당 되긴 함

 */


@Table
data class Item(@GeneratedValue(strategy = GenerationType.IDENTITY) @Id var itemId: Long? = null, var userId: String?, val itemName: String) {
}