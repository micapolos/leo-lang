package leo14.untyped

import leo.base.ifOrNull
import leo14.Script

data class SwitchMatch(
	val param: Value,
	val body: Script)

fun switchMatch(param: Value, body: Script) =
	SwitchMatch(param, body)

val Value.resolveSwitchMatch
	get() =
		matchInfix(switchName) { lhs, rhs ->
			lhs.matchBody { param ->
				param.casesSwitchBody(rhs)?.let { body ->
					switchMatch(param, body)
				}
			}
		}

tailrec fun Value.casesSwitchBody(cases: Value): Script? =
	when (cases) {
		EmptyValue -> null
		is SequenceValue -> caseSwitchBody(cases.sequence.head) ?: casesSwitchBody(cases.sequence.tail.value)
	}

fun Value.caseSwitchBody(line: Line): Script? =
	line.fieldOrNull?.let { field ->
		ifOrNull(matches(field.name)) {
			field.rhs.scriptOrNull
		}
	}
