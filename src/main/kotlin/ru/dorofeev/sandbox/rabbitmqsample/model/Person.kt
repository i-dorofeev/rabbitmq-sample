package ru.dorofeev.sandbox.rabbitmqsample.model

class Person(val name: String) {

	private val accounts = mutableListOf<String>()

	fun getAccounts() : List<String> {
		return accounts.toList()
	}

}