package ru.dorofeev.sandbox.rabbitmqsample.platform

import java.util.*

abstract class Event : Message {

	val timestamp = Date()

	override fun toString(): String {
		return timestamp.toString() + ": "
	}
}

class PersonCreatedEvent(val name: String) : Event() {

	override fun toString(): String {
		return super.toString() + "Person created: $name"
	}
}

class RoleCreatedEvent(val name: String) : Event() {

	override fun toString(): String {
		return super.toString() + "Role created: $name"
	}
}

class AssignmentCreatedEvent(val id: Long) : Event() {

	override fun toString(): String {
		return super.toString() + "Assignment created: $id"
	}
}

class BaseRoleAddedEvent(val roleName: String) : Event() {

	override fun toString(): String {
		return super.toString() + "Base role added: $roleName"
	}
}