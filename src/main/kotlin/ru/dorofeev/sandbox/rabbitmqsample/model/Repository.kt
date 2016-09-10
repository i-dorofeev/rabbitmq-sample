package ru.dorofeev.sandbox.rabbitmqsample.model

import java.util.*

class Repository {

	private val people = mutableListOf<Person>()
	private val assignments = mutableMapOf<Long, Assignment>()
	private val roles = mutableListOf<Role>()
	private val baseRoles = mutableListOf<String>()
	private val accounts = mutableListOf<Account>()

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

	fun getAssignment(id: Long): Assignment? {
		return assignments[id]
	}

	fun getRole(roleName: String): Role? {
		return roles.find { it.name == roleName }
	}

	fun getPerson(personName: String): Person? {
		return people.find { it.name == personName }
	}

	fun getAccount(accountName: String): Account? {
		return accounts.find { it.name == accountName }
	}

}