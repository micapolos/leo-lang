package leo13.interpreter

import leo.base.fold
import leo13.compiler.*
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineSeq
import leo13.value.*

data class Interpreter(
	val context: Context,
	val interpreted: Interpreted)

fun interpreter(context: Context, interpreted: Interpreted) = Interpreter(context, interpreted)
fun interpreter() = interpreter(context(), interpreted())

fun Interpreter.unsafePush(script: Script): Interpreter =
	fold(script.lineSeq) { unsafePush(it) }

fun Interpreter.unsafePush(scriptLine: ScriptLine): Interpreter =
	compiler(context, compiled(expr(op(interpreted.value)), interpreted.type))
		.unsafePush(scriptLine)
		.let { compiler ->
			evaluator(valueBindings(), interpreted.value)
				.evaluate(compiler.compiled.expr)
				.let { value ->
					interpreter(context, interpreted(value, compiler.compiled.type))
				}
		}
