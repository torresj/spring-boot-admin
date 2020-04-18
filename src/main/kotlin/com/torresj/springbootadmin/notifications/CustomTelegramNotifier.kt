package com.torresj.springbootadmin.notifications

import de.codecentric.boot.admin.server.domain.entities.Instance
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository
import de.codecentric.boot.admin.server.domain.events.InstanceEvent
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import reactor.core.publisher.Mono
import java.net.URL

class CustomTelegramNotifier(repository: InstanceRepository) : AbstractEventNotifier(repository) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @Value("\${spring.boot.admin.notify.custom.telegram.auth-token}")
    private lateinit var token:String

    @Value("\${spring.boot.admin.notify.custom.telegram.chat-id}")
    private lateinit var chatId:String

    override fun doNotify(event: InstanceEvent, instance: Instance): Mono<Void> = Mono.fromRunnable {
        var message = "Instance ${instance.registration.name} (${event.instance}) is ${if (event is InstanceStatusChangedEvent) event.statusInfo.status else event.type}"
        val urlString = "https://api.telegram.org/bot${token}/sendMessage?chat_id=${chatId}&text=${message}"
        URL(urlString).readText()
    }
}