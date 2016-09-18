package ru.dorofeev.sandbox.rabbitmqsample.tests

import org.junit.jupiter.api.Test
import ru.dorofeev.sandbox.attempt2.Engine

class TestClass1 {

	@Test
	fun testMethod() {

		val engine = Engine()

		engine.postMessage("my message")
		engine.doProcess()

	}
}