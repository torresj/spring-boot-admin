package com.torresj.springbootadmin.notifications

import com.torresj.springbootadmin.exceptions.StatusException
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

    private val STATUS_UP = "UP"
    private val STATUS_DOWN = "OFFLINE"
    private val STATUS_REGISTERED = "REGISTERED"
    private val STATUS_DEREGISTERED = "DEREGISTERED"
    private val STATUS_INFO_UPDATED = "INFO_CHANGED"

    @Value("\${spring.boot.admin.notify.custom.telegram.auth-token}")
    private lateinit var token:String

    @Value("\${spring.boot.admin.notify.custom.telegram.chat-id}")
    private lateinit var chatId:String

    override fun doNotify(event: InstanceEvent, instance: Instance): Mono<Void> = Mono.fromRunnable {
        try {
            var status = when (val stringStatus = if (event is InstanceStatusChangedEvent) event.statusInfo.status else event.type) {
                STATUS_UP -> "UP"
                STATUS_DOWN -> "DOWN"
                STATUS_REGISTERED -> "REGISTERED"
                STATUS_DEREGISTERED -> "DEREGISTERED"
                STATUS_INFO_UPDATED -> "INFO UPDATED"
                else -> throw StatusException(stringStatus)
            }
            var message = "Service <b>${instance.registration.name}</b> (<i>${event.instance}</i>) is <b>${status}</b>"
            val urlString = "https://api.telegram.org/bot${token}/sendMessage?chat_id=${chatId}&text=${message}&parse_mode=html"
            URL(urlString).readText()
        } catch (exception: StatusException){
            log.warn(exception.getMessageError())
        }
    }
}