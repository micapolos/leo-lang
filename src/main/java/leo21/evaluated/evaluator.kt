package leo21.evaluated

import leo.base.fold
import leo.base.reverse
import leo14.Script
import leo14.ScriptLine
import leo14.lineSeq
import leo21.compiled.Compiled
import leo21.compiled.plus
import leo21.prim.evaluate.value

data class Evaluator(val context: Context, val evaluated: Evaluated)

val emptyEvaluator = Evaluator(emptyContext, emptyEvaluated)

fun Evaluator.plus(script: Script): Evaluator =
	fold(script.lineSeq.reverse) { plus(it) }

fun Evaluator.plus(scriptLine: ScriptLine): Evaluator =
	Compiled(context.compiledScope, evaluated.typed).plus(scriptLine).body
		.let { body ->
			body.term.value.let { value ->
				Evaluator(context, Evaluated(value, body.type))
			}
		}