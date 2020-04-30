package leo16

import leo.base.nullOf
import leo.base.runIfNotNull

data class Evaluator(val parentOrNull: EvaluatorParent?, val closure: Closure)
data class EvaluatorParent(val evaluator: Evaluator, val word: String)

fun EvaluatorParent?.evaluator(closure: Closure) = Evaluator(this, closure)
val Closure.evaluator get() = nullOf<EvaluatorParent>().evaluator(this)
val Scope.evaluator get() = closure(value()).evaluator
fun Evaluator.parent(word: String) = EvaluatorParent(this, word)
val emptyEvaluator = emptyScope.closure(value()).evaluator

operator fun Evaluator.plus(token: Token): Evaluator? =
	when (token) {
		is BeginToken -> begin(token.word)
		EndToken -> end
	}

fun Evaluator.begin(word: String): Evaluator? =
	when (closure.value) {
		is StructValue -> parent(word).evaluator(closure.begin)
		is FunctionValue -> null
	}

val Evaluator.end: Evaluator?
	get() =
		parentOrNull?.endEvaluator(closure)

fun EvaluatorParent.endEvaluator(closure: Closure): Evaluator? =
	evaluator.plus(word.invoke(closure.value))

operator fun Evaluator.plus(line: Line): Evaluator? =
	closure.plus(line)?.let { closure ->
		parentOrNull.evaluator(closure).normalizeAndApply
	}

val Evaluator.normalizeAndApply: Evaluator
	get() =
		parentOrNull.evaluator(closure.updateValue { normalize }).apply

val Evaluator.apply: Evaluator
	get() =
		null
			?: applyClosure
			?: applyMatch
			?: this

val Evaluator.applyClosure: Evaluator?
	get() =
		parentOrNull.runIfNotNull(closure.value.apply) { evaluator(closure.scope.closure(it)) }

val Evaluator.applyMatch: Evaluator?
	get() =
		TODO()
//	fragment.script.matchInfix(matchName) { lhs, rhs ->
//		lhs.matchOrNull(*rhs.sentenceStack.map {
//			word.gives {
//				closure.scope.plus(script(matchingName())
//					.bindingTo(closure.scope.closure(this)))
//					.evaluate(script)
//					.script
//			}
//		}.array)?.let { matchScript ->
//			parentOrNull.evaluator(scope, fragment.updateScript { matchScript })
//		}
//	}
