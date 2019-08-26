package leo13

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.script.evaluator.Fn
import leo9.*

data class Value(
	val fnOrNull: Fn?,
	val lineStack: Stack<ValueLine>) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine get() = "value" lineTo asMetaFirstScript("fn", fnOrNull, lineStack.reverse.seq)
}

fun value(fnOrNull: Fn?, lineStack: Stack<ValueLine>) = Value(fnOrNull, lineStack)
fun value(fn: Fn, vararg lines: ValueLine) = Value(fn, stack(*lines))
fun value(vararg lines: ValueLine) = Value(null, stack(*lines))
fun value(name: String) = value(valueLine(name))
fun Value.plus(line: ValueLine) = value(fnOrNull, lineStack.push(line))
val Script.value: Value get() = value(null, lineStack.map { valueLine })
val Value.scriptOrNull: Script? get() = ifOrNull(fnOrNull == null) { lineStack.mapOrNull { scriptLineOrNull }?.script }
val Value.onlyLineOrNull get() = lineStack.onlyOrNull
val Value.onlyFnOrNull get() = ifOrNull(lineStack.isEmpty) { fnOrNull }

val Value.linkOrNull: ValueLink?
	get() = lineStack.linkOrNull?.let { stackLink ->
		link(Value(fnOrNull, stackLink.stack), stackLink.value)
	}

fun Value.accessOrNull(name: String): Value? =
	onlyLineOrNull?.rhs?.lineStack?.mapOnly {
		notNullIf(name == this.name) {
			value(this)
		}
	}
