package ru.dorofeev.sandbox.rabbitmqsample.platform

import ru.dorofeev.sandbox.rabbitmqsample.model.Repository

class ApplicationContext {

	val repository = Repository()

	fun initRunner(
			eventHandlers: List<Rule<Event>>,
			failureHandlers: List<Rule<Failure>>): Runner {

		return Runner(this, eventHandlers, failureHandlers)
	}

}