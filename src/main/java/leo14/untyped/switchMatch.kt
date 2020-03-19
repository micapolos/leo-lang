package leo14.untyped

import leo.base.ifOrNull
import leo14.Script

data class SwitchMatch(
	val param: Program,
	val body: Script)

fun switchMatch(param: Program, body: Script) =
	SwitchMatch(param, body)

val Program.resolveSwitchMatch
	get() =
		matchInfix("switch") { lhs, rhs ->
			lhs.matchBody { param ->
				param.casesSwitchBody(rhs)?.let { body ->
					switchMatch(param, body)
				}
			}
		}

tailrec fun Program.casesSwitchBody(cases: Program): Script? =
	when (cases) {
		EmptyProgram -> null
		is SequenceProgram -> caseSwitchBody(cases.sequence.head) ?: casesSwitchBody(cases.sequence.tail)
	}

fun Program.caseSwitchBody(value: Value): Script? =
	value.fieldOrNull?.let { field ->
		ifOrNull(matches(field.name)) {
			field.rhs.scriptOrNull
		}
	}
