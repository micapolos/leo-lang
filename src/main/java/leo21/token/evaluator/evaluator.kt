package leo21.token.evaluator

import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.abstraction
import leo14.lambda.value.function
import leo14.lambda.value.value
import leo14.lineTo
import leo14.script
import leo21.compiled.ArrowCompiled
import leo21.evaluated.Evaluated
import leo21.evaluated.LineEvaluated
import leo21.evaluated.apply
import leo21.evaluated.emptyEvaluated
import leo21.evaluated.evaluated
import leo21.evaluated.given
import leo21.evaluated.lineTo
import leo21.evaluated.of
import leo21.evaluated.plus
import leo21.definition.Definition
import leo21.token.body.Module
import leo21.type.line

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

fun Evaluator.plus(definition: Definition): Evaluator =
	copy(context = context.plus(definition))

fun Evaluator.plus(arrowCompiled: ArrowCompiled): Evaluator =
	plus(
		value(context.scope.function(arrowCompiled.term.abstraction { it }))
			.of(line(arrowCompiled.arrow)))

fun Evaluator.apply(evaluator: Evaluator): Evaluator =
	copy(evaluated = evaluated.apply(evaluator.evaluated))