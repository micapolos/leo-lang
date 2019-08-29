package leo13.interpreter

import leo.base.fold
import leo13.compiler.Context
import leo13.compiler.compiler
import leo13.compiler.context
import leo13.compiler.unsafePush
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineSeq
import leo13.type.Type
import leo13.type.type
import leo13.type.typed
import leo13.value.*

data class Interpreter(
	val context: Context,
	val value: Value,
	val type: Type)

fun interpreter(context: Context, value: Value, type: Type) = Interpreter(context, value, type)
fun interpreter() = interpreter(context(), value(), type())

fun Interpreter.unsafePush(script: Script): Interpreter =
	fold(script.lineSeq) { unsafePush(it) }

fun Interpreter.unsafePush(scriptLine: ScriptLine): Interpreter =
	compiler(context, typed(expr(), type))
		.unsafePush(scriptLine)
		.let { compiler ->
			evaluator(valueBindings(), value)
				.evaluate(compiler.typed.expr)
				.let { value ->
					interpreter(context, value, compiler.typed.type)
				}
		}
