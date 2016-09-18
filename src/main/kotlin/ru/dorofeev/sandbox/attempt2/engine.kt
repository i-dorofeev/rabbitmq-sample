package ru.dorofeev.sandbox.attempt2

import java.util.*

class Engine {

	private val messageQueue = ArrayDeque<Any>()
	private val messageHandlers = mutableMapOf<Class<out Any>, List<Class<out MessageHandler>>>()

	fun postMessage(message: Any) {
		messageQueue.offer(message)
	}

	fun doProcess() {

		do {
			val message = messageQueue.poll()
			if (message != null) {

				if (message is MessageHandlerDescriptor) {
					processMessageHandler(message)
				} else {
					processAny(message)
				}

			}
		} while (message != null)

	}

	private fun processAny(message: Any) {
		val messageHandlerClasses = messageHandlers[message.javaClass]
		if (messageHandlerClasses != null) {
			messageHandlerClasses.forEach {
				messageQueue.offer(MessageHandlerDescriptor(message, it))
			}
		} else {
			throw RuntimeException("No registered handlers for message type ${message.javaClass} found")
		}
	}

	private fun processMessageHandler(messageHandlerDescriptor: MessageHandlerDescriptor) {
		val messageHandlerInstance = messageHandlerDescriptor.messageHandler.newInstance()
		val results = messageHandlerInstance.handleMessage(messageHandlerDescriptor.message)
		results.forEach {
			messageQueue.offer(it)
		}
	}

}

class ActionRequest(val actionUri: String, params: Map<String, Any>) { }

interface MessageHandler {

	fun handleMessage(message: Any) : List<Any>

}

class ActionRequestHandler : MessageHandlerBase<ActionRequest>(ActionRequest::class.java) {

	override fun handleTypedMessage(message: ActionRequest): List<Any> {
		throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}

abstract class MessageHandlerBase<T>(val messageClass: Class<T>) : MessageHandler {

	override fun handleMessage(message: Any): List<Any> {
		if (messageClass.isAssignableFrom(message.javaClass))
			return handleTypedMessage(message as T)
		else
			throw RuntimeException("Unexpected message type [${message.javaClass}] for handler [${this.javaClass}]")
	}

	abstract fun handleTypedMessage(message: T): List<Any>
}

class MessageHandlerDescriptor(val message: Any, val messageHandler: Class<out MessageHandler>) { }
