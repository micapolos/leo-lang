package leo14.untyped

import leo.base.notNullIf

data class SwitchMatch(
	val param: Program,
	val body: Program)

fun switchMatch(param: Program, body: Program) =
	SwitchMatch(param, body)

val Program.resolveSwitchMatch
	get() =
		matchInfix("switch") { lhs, rhs ->
			lhs.casesSwitchBody(rhs)?.let { body ->
				switchMatch(lhs, body)
			}
		}

tailrec fun Program.casesSwitchBody(cases: Program): Program? =
	when (cases) {
		EmptyProgram -> null
		is SequenceProgram -> caseSwitchBody(cases.sequence.head) ?: casesSwitchBody(cases.sequence.tail)
	}

fun Program.caseSwitchBody(value: Value) =
	value.fieldOrNull?.let { field ->
		notNullIf(matches(field.name)) {
			field.rhs
		}
	}
