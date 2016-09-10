package ru.dorofeev.sandbox.rabbitmqsample.platform

interface EventHandler {
	fun handle(ctx: ApplicationContext, event: Event): List<Action>
}

class LoggingEventHandler : EventHandler {
	override fun handle(ctx: ApplicationContext, event: Event): List<Action> {
		return listOf(LogAction(event.toString()))
	}

}

class AddBaseAssignmentsToNewPersonEventHandler : EventHandler {

	override fun handle(ctx: ApplicationContext, event: Event): List<Action> {
		if (event !is PersonCreatedEvent)
			return emptyList()

		val baseRoles = ctx.repository.getBaseRoles()
		return baseRoles.map {
			CreateAssignmentAction(event.name, it)
		}
	}
}

class AssignmentAddedEventHandler : EventHandler {

	override fun handle(ctx: ApplicationContext, event: Event): List<Action> {
		if (event !is AssignmentCreatedEvent)
			return emptyList()

		val assignment = ctx.repository.getAssignment(event.id) ?:
				throw RuntimeException("assignment[id=${event.id}] not found")

		val role = ctx.repository.getRole(assignment.roleName) ?:
				throw RuntimeException("role[${assignment.roleName}] not found")

		val person = ctx.repository.getPerson(assignment.personName) ?:
				throw RuntimeException("person[${assignment.personName}] not found")

		val accountForRole = person.getAccounts()
				.map { ctx.repository.getAccount(it) ?: throw RuntimeException("account[$it] not found") }
				.find { it.resourceId == role.resourceId }

		//if (accountForRole == null)
		return emptyList()
	}

}