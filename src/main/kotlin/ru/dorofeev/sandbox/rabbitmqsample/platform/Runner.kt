package ru.dorofeev.sandbox.rabbitmqsample.platform

import java.util.*

class Runner(val applicationContext: ApplicationContext, val rules: List<Rule>) {

	val messageQueue = ArrayDeque<Any>()

	fun add(action: Action) {
		messageQueue.offer(action)
	}

	fun process() {

		do {
			val message = messageQueue.poll()
			if (message != null) {

				println("Message: ${message.javaClass}")

				if (message is Action) {
					val event = message.doAction(applicationContext)
					if (event != null)
						messageQueue.offer(event)
				} else if (message is Event) {

					rules.forEach {
						it.getEventHandler(message)?.handle(applicationContext, message)?.forEach {
							messageQueue.offer(it)
						}
					}
				}
			}
		} while (message != null)
	}
}

interface Rule {

	fun getEventHandler(event: Event) : EventHandler?
}

class MappingRule(val eventHandlerClass: Class<out EventHandler>, val eventClass: Class<out Event>?) : Rule {

	override fun getEventHandler(event: Event): EventHandler? {

		if (eventClass != null && event.javaClass != eventClass)
			return null

		return eventHandlerClass.newInstance()
	}

}

fun mapping(event: Class<out Event>, eventHandler: Class<out EventHandler>): Rule {
	return MappingRule(eventHandler, event)
}

fun allEvents(eventHandler: Class<out EventHandler>) : Rule {
	return MappingRule(eventHandler, null)
}

