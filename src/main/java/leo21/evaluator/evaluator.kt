package leo21.evaluator

import leo.base.fold
import leo.base.reverse
import leo14.Literal
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.lineSeq
import leo21.compiled.lineTo
import leo21.compiler.Compiler
import leo21.compiler.plus
import leo21.compiler.plusData
import leo21.compiler.resolve
import leo21.prim.runtime.value
import leo21.value.value

data class Evaluator(val context: Context, val evaluated: Evaluated)

val emptyEvaluator = Evaluator(emptyContext, emptyEvaluated)

fun Evaluator.plus(script: Script): Evaluator =
	fold(script.lineSeq.reverse) { plus(it) }

fun Evaluator.plus(scriptLine: ScriptLine): Evaluator =
	Compiler(context.bindings, evaluated.compiled).plus(scriptLine).compiled
		.let { body ->
			body.term.value.let { value ->
				Evaluator(context, Evaluated(value, body.type))
			}
		}

fun Evaluator.plus(literal: Literal): Evaluator =
	copy(evaluated =
	Compiler(context.bindings, evaluated.compiled)
		.plus(literal)
		.compiled
		.evaluated)

fun Evaluator.plus(scriptField: ScriptField): Evaluator =
	when (scriptField.string) {
		"define" -> plusDefine(scriptField.rhs)
		else -> plusEvaluate(scriptField)
	}

fun Evaluator.plusDefine(rhs: Script): Evaluator =
	TODO()

fun Evaluator.plusEvaluate(scriptField: ScriptField): Evaluator =
	Compiler(context.bindings, evaluated.compiled).plus(scriptField).compiled
		.let { body ->
			body.term.value.let { value ->
				Evaluator(context, Evaluated(value, body.type))
			}
		}

fun Evaluator.plus(name: String, rhs: Evaluated): Evaluator =
	set(
		Compiler(context.bindings, evaluated.compiled)
			.plusData(name lineTo rhs.compiled)
			.resolve
			.compiled
			.let { compiled -> context.scope.value(compiled.term).of(compiled.type) })

fun Evaluator.set(evaluated: Evaluated): Evaluator =
	copy(evaluated = evaluated)

val Evaluator.doEvaluator: Evaluator
	get() =
		Evaluator(
			context.push(evaluated),
			emptyEvaluated)

fun Evaluator.beginEvaluator(name: String): Evaluator =
	Evaluator(
		context,
		emptyEvaluated)
