package ru.dorofeev.sandbox.rabbitmqsample.platform

import java.util.*

class Runner(
		val applicationContext: ApplicationContext,
		val eventHandlers: List<Rule<Event>>,
		val failureHandlers: List<Rule<Failure>>) {

	val messageQueue = ArrayDeque<Message>()

	fun add(action: Action) {
		messageQueue.offer(action)
	}

	fun process() {

		do {
			val message = messageQueue.poll()
			if (message != null) {

				println("Message: ${message.javaClass}")

				if (message is Action) {
					message.doAction(applicationContext).forEach {
						messageQueue.offer(it)
					}

				} else if (message is Event) {

					eventHandlers.forEach { rule ->
						rule(message)?.let { handler ->
							messageQueue.offer(EventHandlerInstanceMessage(handler, message))
						}
					}
				} else if (message is EventHandlerInstanceMessage) {
					message.eventHandler.handle(applicationContext, message.event).forEach { action ->
						messageQueue.offer(action)
					}
				} else if (message is Failure) {
					failureHandlers.forEach { rule ->
						rule(message)?.let { handler ->
							messageQueue.offer(FailureHandlerInstanceMessage(handler, message))
						}
					}
				}
			}
		} while (message != null)
	}
}

class EventHandlerInstanceMessage(val eventHandler: Handler<Event>, val event: Event) : Message {

}

class FailureHandlerInstanceMessage(val failureHandler: Handler<Failure>, val failure: Failure) : Message {

}

typealias Rule<T> = (T) -> Handler<T>?

fun mapping(eventClass: Class<out Event>?, eventHandlerClass: Class<out Handler<Event>>): Rule<Event> {
	return { event ->
		if (eventClass != null && event.javaClass != eventClass) null
		else eventHandlerClass.newInstance()
	}
}

fun allEvents(eventHandler: Class<out Handler<Event>>) : Rule<Event> {
	return mapping(null, eventHandler)
}

