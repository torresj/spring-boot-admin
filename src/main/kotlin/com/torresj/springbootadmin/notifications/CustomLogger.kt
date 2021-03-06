package com.torresj.springbootadmin.notifications

import de.codecentric.boot.admin.server.domain.entities.Instance
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository
import de.codecentric.boot.admin.server.domain.events.InstanceEvent
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

class CustomLogger(repository: InstanceRepository) : AbstractEventNotifier(repository) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun doNotify(event: InstanceEvent, instance: Instance): Mono<Void> = Mono.fromRunnable {
        log.info("Instance ${instance.registration.name} (${event.instance}) is ${if (event is InstanceStatusChangedEvent) event.statusInfo.status else event.type}")
    }
}