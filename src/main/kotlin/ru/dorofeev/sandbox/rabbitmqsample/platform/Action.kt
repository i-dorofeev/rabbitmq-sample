package ru.dorofeev.sandbox.rabbitmqsample.platform

import ru.dorofeev.sandbox.rabbitmqsample.model.Assignment
import ru.dorofeev.sandbox.rabbitmqsample.model.Person
import ru.dorofeev.sandbox.rabbitmqsample.model.Role

interface Action {
	fun doAction(ctx: ApplicationContext) : Event?
}

class CreateUserAction(val name: String) : Action {

	override fun doAction(ctx: ApplicationContext): Event {
		val newPerson = Person(name)
		ctx.repository.addPerson(newPerson)
		return PersonCreatedEvent(name)
	}

}

class CreateRoleAction(val name: String) : Action {

	override fun doAction(ctx: ApplicationContext): Event {
		val newRole = Role(name)
		ctx.repository.addRole(newRole)
		return RoleCreatedEvent(name)
	}

}

class LogAction(val message: String) : Action {

	override fun doAction(ctx: ApplicationContext): Event? {
		println(message)
		return null
	}

}

class CreateAssignmentAction(val personName: String, val roleName: String) : Action {

	override fun doAction(ctx: ApplicationContext): Event {
		val newAssignment = Assignment(personName, roleName)
		val id = ctx.repository.addAssignment(newAssignment)
		return AssignmentCreatedEvent(id)
	}

}

class AddBaseRoleAction(val roleName: String) : Action {

	override fun doAction(ctx: ApplicationContext): Event {
		ctx.repository.addBaseRole(roleName)
		return BaseRoleAddedEvent(roleName)
	}

}