package com.meteor.app.r2dbc.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ByteArrayResource
import org.springframework.data.relational.core.mapping.NamingStrategy
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
class R2dbcConfig {

    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        var initializer = ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        initializer.setDatabasePopulator(
            ResourceDatabasePopulator(
                ByteArrayResource(
                    //val userId: String, val name: String
                    ("DROP TABLE IF EXISTS `user`;"
                            + """
                                create table IF NOT EXISTS `User`
                                (
                                	userId VARCHAR(30),
                                	name VARCHAR(30)
                                );
                              """
                            + """
                                create table IF NOT EXISTS `UserInfo`
                                (
                                    userInfoId number not null auto_increment,	
                                    userId VARCHAR(30),
                                	info VARCHAR(30)
                                );
                              """
                            + """
                                create table IF NOT EXISTS `Item`
                                (
                                    itemId number not null auto_increment,
                                    userId VARCHAR(30),
                                    itemName VARCHAR(30)
                                );
                              """

                            ).toByteArray()
                    //@Id var itemId: String?, var userId: String?, val itemName: String
                )
            )
        )
        return initializer;
    }

    @Bean
    fun namingStrategy(): NamingStrategy? {
        return object : NamingStrategy {
            override fun getTableName(type: Class<*>): String {
                return type.simpleName
            }

            override fun getColumnName(property: RelationalPersistentProperty): String {
                return property.name
            }
        }
    }
}