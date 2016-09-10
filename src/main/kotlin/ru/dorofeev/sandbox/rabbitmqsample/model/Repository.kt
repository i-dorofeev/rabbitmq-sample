package ru.dorofeev.sandbox.rabbitmqsample.model

import java.util.*

class Repository {

	private val people = mutableListOf<Person>()
	private val assignments = mutableMapOf<Long, Assignment>()
	private val roles = mutableListOf<Role>()
	private val baseRoles = mutableListOf<String>()

	fun addPerson(newPerson: Person) {
		people.add(newPerson)
	}

	fun addAssignment(newAssignment: Assignment) : Long {
		val id = Date().time
		assignments.put(id, newAssignment)
		return id
	}

	fun addRole(newRole: Role) {
		roles.add(newRole)
	}

	fun addBaseRole(roleName: String) {
		baseRoles.add(roleName)
	}

	fun getBaseRoles() : List<String> {
		return baseRoles.toList()
	}

}