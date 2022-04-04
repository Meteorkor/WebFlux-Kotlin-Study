package com.meteor.app.r2dbc.domain.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.OneToOne

/*
1.
@Id를 달면 Repo에서 save시 Id 지정하면 에러 발생
@Id를 달지 않으면 repo.save시 정상적으로 동작


2.
Required identifier property
@Id를 설정하지 않으면 findById시 위 에러가 발생

3.
@OneToOne 도 동작하지 않음

 */


@Table
data class User(
    @Id val userId: String, val name: String,
    @OneToOne(mappedBy = "userId")
    val userInfo: UserInfo? = null
) {
}