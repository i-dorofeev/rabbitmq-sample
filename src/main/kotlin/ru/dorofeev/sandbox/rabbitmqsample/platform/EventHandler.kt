package ru.dorofeev.sandbox.rabbitmqsample.platform

interface Handler<T> {
	fun handle(ctx: ApplicationContext, obj: T): List<Message>
}

class LoggingEventHandler : Handler<Event> {
	override fun handle(ctx: ApplicationContext, event: Event): List<Message> {
		return listOf(LogAction(event.toString()))
	}

}

class AddBaseAssignmentsToNewPersonEventHandler : Handler<Event> {

	override fun handle(ctx: ApplicationContext, event: Event): List<Message> {
		if (event !is PersonCreatedEvent)
			return emptyList()

		val baseRoles = ctx.repository.getBaseRoles()
		return baseRoles.map {
			CreateAssignmentAction(event.name, it)
		}
	}
}

class AssignmentAddedEventHandler : Handler<Event> {

	override fun handle(ctx: ApplicationContext, event: Event): List<Message> {
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

		if (accountForRole == null)
			return listOf(Failure("noAccountForRoleApplication"))

		return emptyList()
	}

}

class NoAccountForRoleApplicationFailureHandler : Handler<Failure> {
	
	override fun handle(ctx: ApplicationContext, obj: Failure): List<Message> {
		throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}