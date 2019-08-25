package leo13

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.script.Expr
import leo9.*

data class Value(
	val exprOrNull: Expr?,
	val lineStack: Stack<ValueLine>) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine get() = "value" lineTo asMetaFirstScript("expr", exprOrNull, lineStack.reverse.seq)
}

fun value(exprOrNull: Expr?, lineStack: Stack<ValueLine>) = Value(exprOrNull, lineStack)
fun value(expr: Expr, vararg lines: ValueLine) = Value(expr, stack(*lines))
fun value(vararg lines: ValueLine) = Value(null, stack(*lines))
fun value(name: String) = value(valueLine(name))
fun Value.plus(line: ValueLine) = value(exprOrNull, lineStack.push(line))
val Script.value: Value get() = value(null, lineStack.map { valueLine })
val Value.scriptOrNull: Script? get() = ifOrNull(exprOrNull == null) { lineStack.mapOrNull { scriptLineOrNull }?.script }
val Value.onlyLineOrNull get() = lineStack.onlyOrNull

val Value.linkOrNull: ValueLink?
	get() = lineStack.linkOrNull?.let { stackLink ->
		link(Value(exprOrNull, stackLink.stack), stackLink.value)
	}

fun Value.accessOrNull(name: String): Value? =
	onlyLineOrNull?.rhs?.lineStack?.mapOnly {
		notNullIf(name == this.name) {
			value(this)
		}
	}
