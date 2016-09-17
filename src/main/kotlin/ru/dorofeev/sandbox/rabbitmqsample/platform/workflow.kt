package ru.dorofeev.sandbox.rabbitmqsample.platform

object WorkflowService {

	val workflowInstances = mutableMapOf<WorkflowDescriptor, WorkflowState>()
	var counter = 0

	fun instantiate(workflowClass: Class<out Workflow>): Workflow {
		val workflowDescriptor = WorkflowDescriptor(counter++, workflowClass)
		val wfInstance = createWorkflowInstance(workflowDescriptor)
		workflowInstances.put(workflowDescriptor, wfInstance.getState())
		return wfInstance
	}

	private fun createWorkflowInstance(wfHandle: WorkflowDescriptor) : Workflow {
		val ctor = wfHandle.workflowClass.getDeclaredConstructor(WorkflowDescriptor::class.java)
		val wfInstance = ctor.newInstance(wfHandle)
		return wfInstance
	}

	fun getWorkflow(wfHandle: WorkflowDescriptor): Workflow {
		return createWorkflowInstance(wfHandle)
	}

}

data class WorkflowDescriptor(val id: Int, val workflowClass: Class<out Workflow>) {}

open class WorkflowState {

}

abstract class Workflow(val wfDescriptor: WorkflowDescriptor) {
	abstract fun getState() : WorkflowState

	abstract fun start() : Action

	abstract fun actionCompleted(actionId: Int) : Action?
}

class SimpleWorkflow(wfDescriptor: WorkflowDescriptor) : Workflow(wfDescriptor) {

	override fun getState(): WorkflowState {
		return SimpleWorkflowState()
	}

	override fun start(): WorkflowAction {
		return WorkflowAction(LogAction("workflow step 1"), wfDescriptor, 1)
	}

	override fun actionCompleted(actionId: Int): Action? {
		if (actionId < 5)
			return WorkflowAction(LogAction("workflow step ${actionId + 1}"), wfDescriptor, actionId + 1)
		else
			return null
	}
}

class SimpleWorkflowState : WorkflowState() {

}

class StartWorkflowAction() : Action {

	override fun doAction(ctx: ApplicationContext): List<Message> {

		val workflow = WorkflowService.instantiate(SimpleWorkflow::class.java)
		val startAction = workflow.start()

		return listOf(startAction)
	}

}

class WorkflowAction(val action: Action, val wfHandle: WorkflowDescriptor, val actionId: Int) : Action {

	override fun doAction(ctx: ApplicationContext): List<Message> {
		val actionResults = action.doAction(ctx)

		return listOf(ActionCompletedEvent(wfHandle, actionId)).plus(actionResults)
	}

}

class ActionCompletedEventHandler : Handler<Event> {

	override fun handle(ctx: ApplicationContext, obj: Event): List<Message> {

		if (obj !is ActionCompletedEvent)
			return emptyList()

		val workflow = WorkflowService.getWorkflow(obj.wfHandle)
		val nextAction = workflow.actionCompleted(obj.actionId)

		if (nextAction == null)
			return listOf(WorkflowCompletedEvent())
		else
			return listOf(nextAction)
	}

}

class WorkflowCompletedEvent : Event() {

}