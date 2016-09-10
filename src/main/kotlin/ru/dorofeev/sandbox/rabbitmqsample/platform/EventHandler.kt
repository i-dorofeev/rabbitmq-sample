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