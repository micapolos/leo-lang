package leo21.evaluator

import leo.base.fold
import leo14.Script
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.value.Value
import leo14.lambda.value.term
import leo14.lambda.value.value
import leo14.lineTo
import leo14.script
import leo21.compiled.Compiled
import leo21.prim.Prim
import leo21.prim.nilPrim
import leo21.type.Type
import leo21.type.resolve
import leo21.type.switch
import leo21.type.type

data class Evaluated(val value: Value<Prim>, val type: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "evaluated" lineTo script(value.reflectScriptLine, type.reflectScriptLine)
}

infix fun Value<Prim>.of(type: Type) = Evaluated(this, type)

val emptyEvaluated = Evaluated(value(nilPrim), type())

val StructEvaluated.evaluated: Evaluated get() = value of type(struct)
val ChoiceEvaluated.evaluated: Evaluated get() = valueOrNull!! of type(choice)

fun evaluated(vararg lines: LineEvaluated): Evaluated =
	emptyEvaluated.fold(lines) { plus(it) }

fun <R> Evaluated.switch(
	structFn: (StructEvaluated) -> R,
	choiceFn: (ChoiceEvaluated) -> R
): R =
	type.switch(
		{ struct -> structFn(value of struct) },
		{ choice -> choiceFn(value of choice) },
		{ recursive -> value.of(recursive.resolve).switch(structFn, choiceFn) },
		{ recurse -> null!! })

val Evaluated.structOrNull: StructEvaluated? get() = switch({ it }, { null })
val Evaluated.choiceOrNull: ChoiceEvaluated? get() = switch({ null }, { it })

val Evaluated.compiled: Compiled
	get() =
		Compiled(value.term, type)

val Evaluated.script: Script
	get() =
		script(value, type)

fun Evaluated.plus(evaluated: LineEvaluated): Evaluated =
	structOrNull!!.plus(evaluated).evaluated

fun evaluated(lineEvaluated: LineEvaluated): Evaluated =
	lineEvaluated.value of type(lineEvaluated.line)

fun evaluated(string: String) = evaluated(string.lineEvaluated)
fun evaluated(double: Double) = evaluated(double.lineEvaluated)

val Evaluated.rhsOrNull: Evaluated?
	get() =
		structOrNull?.onlyLineOrNull?.fieldOrNull?.rhs