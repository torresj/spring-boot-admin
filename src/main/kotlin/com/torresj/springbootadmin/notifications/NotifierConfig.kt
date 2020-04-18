package com.torresj.springbootadmin.notifications

import de.codecentric.boot.admin.server.domain.entities.InstanceRepository
import de.codecentric.boot.admin.server.notify.Notifier
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NotifierConfig(val instance: InstanceRepository, val otherNotifiers: ObjectProvider<List<Notifier>>) {
    @Bean
    fun logNotifier():CustomTelegramNotifier = CustomTelegramNotifier(instance)
}