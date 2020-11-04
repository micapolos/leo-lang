package leo21.evaluator

import leo.base.fold
import leo.base.reverse
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.lineSeq
import leo21.compiler.Compiled
import leo21.compiler.plus
import leo21.prim.evaluate.value

data class Evaluator(val context: Context, val evaluated: Evaluated)

val emptyEvaluator = Evaluator(emptyContext, emptyEvaluated)

fun Evaluator.plus(script: Script): Evaluator =
	fold(script.lineSeq.reverse) { plus(it) }

fun Evaluator.plus(scriptLine: ScriptLine): Evaluator =
	Compiled(context.bindings, evaluated.typed).plus(scriptLine).body
		.let { body ->
			body.term.value.let { value ->
				Evaluator(context, Evaluated(value, body.type))
			}
		}

fun Evaluator.plus(scriptField: ScriptField): Evaluator =
	when (scriptField.string) {
		"define" -> plusDefine(scriptField.rhs)
		else -> plusEvaluate(scriptField)
	}

fun Evaluator.plusDefine(rhs: Script): Evaluator =
	TODO()

fun Evaluator.plusEvaluate(scriptField: ScriptField): Evaluator =
	Compiled(context.bindings, evaluated.typed).plus(scriptField).body
		.let { body ->
			body.term.value.let { value ->
				Evaluator(context, Evaluated(value, body.type))
			}
		}