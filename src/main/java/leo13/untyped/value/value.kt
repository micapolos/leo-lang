package leo13.untyped.value

import leo.base.ifOrNull
import leo.base.updateIfNotNull
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo13.untyped.functionName
import leo13.untyped.valueName
import leo9.*

data class Value(val lhsFunctionOrNull: Function?, val rhsLineStack: Stack<ValueLine>) {
	override fun toString() = scriptLine.toString()
}

fun value(lhsFunctionOrNull: Function?, rhsLineStack: Stack<ValueLine>) =
	Value(lhsFunctionOrNull, rhsLineStack)

fun value(vararg lines: ValueLine) =
	Value(null, stack(*lines))

fun value(name: String) = value(name lineTo value())

fun value(function: Function, vararg lines: ValueLine) =
	Value(function, stack(*lines))

val Value.functionOrNull: Function?
	get() =
		ifOrNull(rhsLineStack.isEmpty) {
			lhsFunctionOrNull
		}

val Stack<ValueLine>.value get() =
	Value(null, this)

fun Value.plus(vararg lines: ValueLine) =
	rhsLineStack.pushAll(*lines).value

fun Value.firstLineRhsOrNull(name: String): Value? =
	rhsLineStack.mapFirst { rhsOrNull(name) }

fun Value.replaceLineOrNull(line: ValueLine): Value? =
	rhsLineStack
		.updateFirst { replaceOrNull(line) }
		?.let { value(lhsFunctionOrNull, it) }

fun Value.getOrNull(name: String): Value? =
	rhsLineStack
		.valueOrNull
		?.rhs
		?.firstLineRhsOrNull(name)
		?.let { value(name lineTo it) }

fun Value.setOrNull(line: ValueLine): Value? =
	rhsLineStack
		.linkOrNull
		?.let { link ->
			link.value.rhs
				.replaceLineOrNull(line)
				?.let { rhs ->
					value(lhsFunctionOrNull, link.stack.push(link.value.name lineTo rhs))
				}
		}

val Value.isEmpty get() = lhsFunctionOrNull == null && rhsLineStack.isEmpty

val Value.scriptLine get() =
	valueName lineTo bodyScript.emptyIfEmpty

val Value.bodyScript get() =
	script()
		.updateIfNotNull(lhsFunctionOrNull) { plus(functionName lineTo script()) }
		.fold(rhsLineStack.reverse) { plus(it.bodyScriptLine) }

val Value.previousOrNull: Value?
	get() =
		rhsLineStack.linkOrNull?.stack?.let {
			value(lhsFunctionOrNull, it)
		}
