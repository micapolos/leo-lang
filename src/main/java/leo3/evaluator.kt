package leo3

import leo.base.empty
import leo.base.ifNotNullOr
import leo.binary.Bit

data class Evaluator(
	val buffer: Buffer,
	val scope: Scope)

val Scope.emptyEvaluator
	get() = Evaluator(empty.buffer, this)

fun Evaluator.push(scope: Scope, bit: Bit): Evaluator =
	scope.matchAt(bit).ifNotNullOr(
		{ match -> match.resolve(scope, buffer.plus(bit)).evaluator },
		{ copy(buffer = buffer.plus(bit)) })

fun Evaluator.plus(bit: Bit, scope: Scope) =
	Evaluator(buffer.plus(bit), scope)