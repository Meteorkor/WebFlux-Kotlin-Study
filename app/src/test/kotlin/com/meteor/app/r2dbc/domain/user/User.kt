package com.meteor.app.r2dbc.domain.user

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Table

/*
1.
@Id를 달면 Repo에서 save시 Id 지정하면 에러 발생
@Id를 달지 않으면 repo.save시 정상적으로 동작


2.
Required identifier property
@Id를 설정하지 않으면 findById시 위 에러가 발생

3.
@OneToOne 도 동작하지 않음

4.
Repository.save 시 innerEntity가 있으면 에러발생
@Transient를 추가해서 save 대상에서 제외

 */


@Table
data class User(
    @Id val userId: String, var name: String,
    @Transient val userInfo: UserInfo? = null
) {
}