package com.torresj.springbootadmin.notifications

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository
import de.codecentric.boot.admin.server.notify.Notifier
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NotifierConfig(val instance: InstanceRepository, val otherNotifiers: ObjectProvider<List<Notifier>>) {
    @Bean
    fun logNotifier():CustomLogger = CustomLogger(instance)

    @Bean
    @ConditionalOnProperty(
            value= ["spring.boot.admin.notify.custom.telegram.enabled"],
            havingValue = "true",
            matchIfMissing = false)
    fun notifier():CustomTelegramNotifier = CustomTelegramNotifier(instance)
}