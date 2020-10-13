package leo19.compiler

import leo.base.notNullOrError
import leo13.Stack
import leo13.isEmpty
import leo13.linkOrNull
import leo13.reverse
import leo13.stack
import leo19.type.Case
import leo19.type.Choice
import leo19.typed.TypedField
import leo19.typed.TypedSwitch
import leo19.typed.emptyTypedSwitch
import leo19.typed.plus

data class SwitchBuilder(
	val remainingCaseStack: Stack<Case>,
	val switch: TypedSwitch
)

val Choice.switchBuilder
	get() =
		SwitchBuilder(caseStack.reverse, emptyTypedSwitch)

fun switchBuilder(vararg cases: Case) =
	SwitchBuilder(stack(*cases).reverse, emptyTypedSwitch)

fun SwitchBuilder.plus(case: TypedField) =
	remainingCaseStack
		.linkOrNull
		.notNullOrError("exhausted")
		.let { link ->
			if (link.value.name != case.name) error("case name mismatch: ${link.value.name} - ${case.name}")
			else copy(remainingCaseStack = link.stack, switch = switch.plus(case))
		}

val SwitchBuilder.build: TypedSwitch
	get() =
		if (remainingCaseStack.isEmpty) switch
		else error("non-exhaustive switch")