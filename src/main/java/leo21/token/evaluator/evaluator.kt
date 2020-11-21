package leo21.token.evaluator

import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.abstraction
import leo14.lambda.value.function
import leo14.lambda.value.value
import leo14.lineTo
import leo14.script
import leo21.compiled.ArrowCompiled
import leo21.compiled.line
import leo21.evaluator.Evaluated
import leo21.evaluator.LineEvaluated
import leo21.evaluator.emptyEvaluated
import leo21.evaluator.evaluated
import leo21.evaluator.given
import leo21.evaluator.lineTo
import leo21.evaluator.of
import leo21.evaluator.plus
import leo21.token.body.Module
import leo21.value.value

data class Evaluator(val context: Context, val evaluated: Evaluated) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "evaluator" lineTo script(context.reflectScriptLine, evaluated.reflectScriptLine)
}

val emptyEvaluator = Evaluator(emptyContext, emptyEvaluated)

val Evaluator.resolve: Evaluator
	get() =
		copy(evaluated = context.resolve(evaluated))

fun Evaluator.plus(line: LineEvaluated): Evaluator =
	copy(evaluated = evaluated.plus(line)).resolve

fun Evaluator.plus(field: EvaluatorField): Evaluator =
	plus(field.name lineTo field.evaluator.evaluated)

val Evaluator.begin
	get() =
		Evaluator(context, evaluated())

val Evaluator.beginDo
	get() =
		Evaluator(context.plus(evaluated.given), evaluated())

fun Evaluator.do_(evaluator: Evaluator): Evaluator =
	copy(evaluated = evaluator.evaluated)

fun Evaluator.plus(module: Module): Evaluator =
	copy(context = context.plus(module))

fun Evaluator.plus(arrowCompiled: ArrowCompiled): Evaluator =
	TODO()
//plus(context.scope.value(arrowCompiled.term.abstraction { it })).of(leo21.type.line(arrowCompiled.arrow)))
