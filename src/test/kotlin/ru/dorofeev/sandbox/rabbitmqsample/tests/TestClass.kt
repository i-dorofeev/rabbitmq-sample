package ru.dorofeev.sandbox.rabbitmqsample.tests

import org.junit.jupiter.api.Test
import ru.dorofeev.sandbox.rabbitmqsample.platform.*

class TestClass {

	@Test
	fun testMethod() {

		val app = ApplicationContext()
		val runner = app.initRunner(
				listOf(
					allEvents(LoggingEventHandler::class.java),
					mapping(PersonCreatedEvent::class.java, AddBaseAssignmentsToNewPersonEventHandler::class.java)),
				listOf(
					{ failure -> if (failure.type == "noAccountForRoleApplication") NoAccountForRoleApplicationFailureHandler() else null }
				)
		)

		runner.add(CreateRoleAction("employee", resourceId = "ad"))
		runner.add(AddBaseRoleAction("employee"))
		runner.add(CreateUserAction("dorofeev.iv"))
		runner.process()

	}
}