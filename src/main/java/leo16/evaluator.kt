package leo16

import leo.base.ifOrNull
import leo.base.notNullIf
import leo.base.nullOf
import leo.base.orIfNull
import leo13.fold
import leo13.reverse
import leo14.BeginToken
import leo14.EndToken
import leo14.Literal
import leo14.LiteralToken
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.Token
import leo16.names.*

data class Evaluator(val parentOrNull: EvaluatorParent?, val evaluated: Evaluated, val mode: Mode) {
	override fun toString() = asField.toString()
}

data class EvaluatorParent(val evaluator: Evaluator, val word: String) {
	override fun toString() = asField.toString()
}

fun EvaluatorParent?.evaluator(evaluated: Evaluated, mode: Mode) = Evaluator(this, evaluated, mode)
infix fun EvaluatorParent?.evaluator(evaluated: Evaluated) = Evaluator(this, evaluated, Mode.EVALUATE)
val Evaluated.evaluator get() = nullOf<EvaluatorParent>().evaluator(this)
val Scope.emptyEvaluator get() = emptyEvaluated.evaluator
fun Evaluator.parent(word: String) = EvaluatorParent(this, word)
val emptyEvaluator get() = emptyScope.emptyEvaluated.evaluator
val baseEvaluator get() = emptyScope.emptyEvaluated.evaluator.plus(_use(_base()))

val Evaluator.asField: Field
	get() =
		_evaluator(
			parentOrNull?.asField.orIfNull { _parent(_nothing()) },
			evaluated.asField,
			mode.asField)

val EvaluatorParent.asField: Field
	get() =
		_parent(evaluator.asField, _word(word()))

operator fun Evaluator.plus(token: Token): Evaluator? =
	when (token) {
		is LiteralToken -> plus(token.literal)
		is BeginToken -> begin(token.begin.string)
		is EndToken -> end
	}

inline operator fun Evaluator.plus(value: Value): Evaluator =
	fold(value.fieldStack.reverse) { plus(it) }

inline operator fun Evaluator.plus(field: Field): Evaluator =
	when (field) {
		is SentenceField -> plus(field.sentence)
		is FunctionField -> append(field)
		is NativeField -> append(field)
		is LazyField -> append(field)
		is EvaluatedField -> append(field)
	}

operator fun Evaluator.plus(sentence: Sentence): Evaluator =
	begin(sentence.word).plus(sentence.value).end!!

operator fun Evaluator.plus(literal: Literal): Evaluator =
	when (literal) {
		is StringLiteral -> plus(literal.string.field)
		is NumberLiteral -> plus(literal.number.bigDecimal.field)
	}

fun Evaluator.begin(word: String): Evaluator =
	parent(word).evaluator(evaluated.begin, mode(word))

fun Evaluator.mode(word: String): Mode =
	modeOrNull(word) ?: mode.begin(word)

fun Evaluator.modeOrNull(word: String): Mode? =
	ifOrNull(mode == Mode.EVALUATE) {
		evaluated.scope.dictionary.modeOrNull(word)
	}

inline val Evaluator.end: Evaluator?
	get() =
		parentOrNull?.endEvaluator(evaluated)

inline fun EvaluatorParent.endEvaluator(evaluated: Evaluated): Evaluator =
	evaluator.endWith(word, evaluated)

inline fun Evaluator.endWith(word: String, evaluated: Evaluated): Evaluator =
	updateEvaluated {
		applyDebug(word(evaluated.value)) ?: apply(word, evaluated, mode)
	}

fun Evaluator.append(field: Field): Evaluator =
	updateEvaluated {
		applyDebug(field) ?: apply(field, mode)
	}

fun Evaluator.append(sentence: Sentence): Evaluator =
	append(sentence.field)

fun Evaluator.applyDebug(field: Field): Evaluated? =
	notNullIf(field == _debug(value())) {
		evaluated.scope.evaluated(evaluated.scope.dictionary.printSentence.field.value)
	}

inline fun Evaluator.updateEvaluated(fn: Evaluated.() -> Evaluated): Evaluator =
	copy(evaluated = evaluated.fn())
