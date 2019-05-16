package leo3

import leo.base.appendableString
import leo.base.empty
import leo.base.failIfOr

data class Value(
	val script: Script,
	val function: Function) {
	override fun toString() = appendableString { it.append(this) }
}

fun value(function: Function) =
	Value(empty.script, function)

fun value(script: Script) =
	Value(script, function())

fun Value.plus(token: Token): Value? =
	function.at(token)?.let { matchAtToken ->
		script.plus(token)?.let { scriptPlusToken ->
			matchAtToken.resolve(scriptPlusToken)
		}
	}

val Value.fullScript
	get() =
		failIfOr(!function.isEmpty) { script }

fun Appendable.append(value: Value): Appendable =
	this
		.append(value.script)
		.append(value.function)