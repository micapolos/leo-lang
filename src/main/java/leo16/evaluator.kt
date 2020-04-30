package leo16

import leo.base.nullOf
import leo.base.runIfNotNull
import leo13.array
import leo13.map
import leo15.givesName
import leo15.isName
import leo15.matchName

data class Evaluator(val parentOrNull: EvaluatorParent?, val evaluated: Evaluated)
data class EvaluatorParent(val evaluator: Evaluator, val word: String)

fun EvaluatorParent?.evaluator(evaluated: Evaluated) = Evaluator(this, evaluated)
val Evaluated.evaluator get() = nullOf<EvaluatorParent>().evaluator(this)
val Scope.evaluator get() = evaluated(value()).evaluator
fun Evaluator.parent(word: String) = EvaluatorParent(this, word)
val emptyEvaluator = emptyScope.evaluated(value()).evaluator

operator fun Evaluator.plus(token: Token): Evaluator? =
	when (token) {
		is BeginToken -> begin(token.word)
		EndToken -> end
	}

fun Evaluator.begin(word: String): Evaluator? =
	when (evaluated.value) {
		is StructValue -> parent(word).evaluator(evaluated.begin)
		is FunctionValue -> null
	}

val Evaluator.end: Evaluator?
	get() =
		parentOrNull?.endEvaluator(evaluated)

fun EvaluatorParent.endEvaluator(evaluated: Evaluated): Evaluator? =
	evaluator.plus(word.invoke(evaluated.value))

operator fun Evaluator.plus(line: Line): Evaluator? =
	when (line.word) {
		isName -> plusIs(line.value)
		givesName -> plusGives(line.value)
		else -> append(line)
	}

fun Evaluator.plusIs(value: Value): Evaluator =
	parentOrNull.evaluator(
		evaluated.scope
			.plus(evaluated.value.script.exactPattern.bindingTo(value.body))
			.evaluated(value()))

fun Evaluator.plusGives(value: Value): Evaluator =
	parentOrNull.evaluator(
		evaluated.scope
			.plus(evaluated.value.script.pattern.bindingTo(evaluated.scope.function(value.script).body))
			.evaluated(value()))

fun Evaluator.append(line: Line): Evaluator? =
	evaluated.plus(line)?.let { evaluated ->
		parentOrNull.evaluator(evaluated).normalizeAndApply
	}

val Evaluator.normalizeAndApply: Evaluator
	get() =
		parentOrNull.evaluator(evaluated.updateValue { normalize }).apply

val Evaluator.apply: Evaluator
	get() =
		null
			?: applyClosure
			?: applyMatch
			?: this

val Evaluator.applyClosure: Evaluator?
	get() =
		parentOrNull.runIfNotNull(evaluated.apply) { evaluator(it) }

val Evaluator.applyMatch: Evaluator?
	get() =
		evaluated.value.matchInfix(matchName) { lhs, rhs ->
			rhs.structOrNull?.let { struct ->
				lhs.matchOrNull(*struct.lineStack.map {
					word.gives {
						evaluated.scope
							.plus(matchingBinding)
							.evaluate(value.script)
					}
				}.array)?.let { matchScript ->
					parentOrNull.evaluator(evaluated.updateValue { matchScript })
				}
			}
		}
