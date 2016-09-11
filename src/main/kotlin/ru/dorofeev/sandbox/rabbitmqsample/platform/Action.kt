package ru.dorofeev.sandbox.rabbitmqsample.platform

import ru.dorofeev.sandbox.rabbitmqsample.model.Assignment
import ru.dorofeev.sandbox.rabbitmqsample.model.Person
import ru.dorofeev.sandbox.rabbitmqsample.model.Role

interface Action : Message {
	fun doAction(ctx: ApplicationContext) : List<Message>
}

class CreateUserAction(val name: String) : Action {

	override fun doAction(ctx: ApplicationContext): List<Message> {
		val newPerson = Person(name)
		ctx.repository.addPerson(newPerson)
		return listOf(PersonCreatedEvent(name))
	}

}

class CreateRoleAction(
		val name: String,
        val resourceId: String) : Action {

	override fun doAction(ctx: ApplicationContext): List<Message> {
		val newRole = Role(name, resourceId)
		ctx.repository.addRole(newRole)
		return listOf(RoleCreatedEvent(name))
	}

}

class LogAction(val message: String) : Action {

	override fun doAction(ctx: ApplicationContext): List<Message> {
		println(message)
		return emptyList()
	}

}

class CreateAssignmentAction(val personName: String, val roleName: String) : Action {

	override fun doAction(ctx: ApplicationContext): List<Message> {
		val newAssignment = Assignment(personName, roleName)
		val id = ctx.repository.addAssignment(newAssignment)
		return listOf(AssignmentCreatedEvent(id))
	}

}

class AddBaseRoleAction(val roleName: String) : Action {

	override fun doAction(ctx: ApplicationContext): List<Message> {
		ctx.repository.addBaseRole(roleName)
		return listOf(BaseRoleAddedEvent(roleName))
	}

}