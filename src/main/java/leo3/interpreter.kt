package leo3

import leo.base.fold
import leo.base.ifNotNullOr
import leo.binary.Bit

data class Interpreter(
	val scope: Scope,
	val evaluator: Evaluator)

val Scope.emptyInterpreter
	get() = Interpreter(this, emptyEvaluator)

fun Interpreter.plus(bit: Bit): Interpreter =
	evaluator.scope.matchAt(bit).ifNotNullOr(
		{ match -> match.resolve(scope, evaluator.buffer.plus(bit)) },
		{ copy(evaluator = evaluator.push(scope, bit)) })

fun Interpreter.plus(value: Value): Interpreter =
	fold(value.bitSeq, Interpreter::plus)