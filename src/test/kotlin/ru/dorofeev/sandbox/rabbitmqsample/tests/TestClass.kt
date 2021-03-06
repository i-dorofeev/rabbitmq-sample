package ru.dorofeev.sandbox.rabbitmqsample.tests

import org.junit.jupiter.api.Test
import ru.dorofeev.sandbox.rabbitmqsample.platform.*

class TestClass {

	@Test
	fun testMethod() {

		val app = ApplicationContext()
		val runner = app.initRunner(listOf(
				allEvents(LoggingEventHandler::class.java),
				mapping(PersonCreatedEvent::class.java, AddBaseAssignmentsToNewPersonEventHandler::class.java)
		))

		runner.add(CreateRoleAction("employee"))
		runner.add(AddBaseRoleAction("employee"))
		runner.add(CreateUserAction("dorofeev.iv"))
		runner.process()

	}
}