package leo16

import leo.base.nullOf
import leo.base.runIfNotNull
import leo13.array
import leo13.map
import leo15.givesName
import leo15.isName
import leo15.matchName

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
	when (line.word) {
		isName -> plusIs(line.value)
		givesName -> plusGives(line.value)
		else -> append(line)
	}

fun Evaluator.plusIs(value: Value): Evaluator =
	parentOrNull.evaluator(
		closure.scope
			.plus(closure.value.script.exactPattern.bindingTo(value.body))
			.closure)

fun Evaluator.plusGives(value: Value): Evaluator =
	parentOrNull.evaluator(
		closure.scope
			.plus(closure.value.script.pattern.bindingTo(closure.scope.function(value.script).body))
			.closure)

fun Evaluator.append(line: Line): Evaluator? =
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
		parentOrNull.runIfNotNull(closure.apply) { evaluator(it) }

val Evaluator.applyMatch: Evaluator?
	get() =
		closure.value.matchInfix(matchName) { lhs, rhs ->
			rhs.structOrNull?.let { struct ->
				lhs.matchOrNull(*struct.lineStack.map {
					word.gives {
						closure.scope
							.plus(lhs.matchingBinding)
							.evaluate(value.script)
					}
				}.array)?.let { matchScript ->
					parentOrNull.evaluator(closure.updateValue { matchScript })
				}
			}
		}
