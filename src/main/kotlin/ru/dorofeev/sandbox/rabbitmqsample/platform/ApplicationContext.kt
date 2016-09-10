package ru.dorofeev.sandbox.rabbitmqsample.platform

import ru.dorofeev.sandbox.rabbitmqsample.model.Repository

class ApplicationContext {

	val repository = Repository()

	fun initRunner(rules: List<Rule>): Runner {

		return Runner(this, rules)
	}

}