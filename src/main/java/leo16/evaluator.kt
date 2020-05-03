package leo16

import leo.base.notNullIf
import leo.base.nullOf
import leo.base.orIfNull
import leo13.fold
import leo13.reverse
import leo14.*
import leo15.compilerName
import leo15.nothingName
import leo15.parentName
import leo15.wordName

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
val emptyEvaluator = emptyScope.emptyEvaluated.evaluator

val Evaluator.asField: Field
	get() =
		compilerName(
			parentOrNull?.asField.orIfNull { parentName(nothingName()) },
			evaluated.asField,
			mode.asField)

val EvaluatorParent.asField: Field
	get() =
		parentName(evaluator.asField, wordName(word()))

operator fun Evaluator.plus(token: Token): Evaluator? =
	when (token) {
		is LiteralToken -> plus(token.literal)
		is BeginToken -> begin(token.begin.string)
		is EndToken -> end
	}

operator fun Evaluator.plus(value: Value): Evaluator =
	fold(value.fieldStack.reverse) { plus(it) }

operator fun Evaluator.plus(field: Field): Evaluator =
	when (field) {
		is SentenceField -> plus(field.sentence)
		is FunctionField -> append(field)
		is DictionaryField -> append(field)
		is NativeField -> append(field)
	}

operator fun Evaluator.plus(sentence: Sentence): Evaluator =
	begin(sentence.word).plus(sentence.value).end!!

operator fun Evaluator.plus(literal: Literal): Evaluator =
	when (literal) {
		is StringLiteral -> plus(literal.string.field)
		is NumberLiteral -> plus(literal.number.bigDecimal.field)
	}

fun Evaluator.begin(word: String): Evaluator =
	parent(word).evaluator(evaluated.begin, mode.begin(word.mode))

val Evaluator.end: Evaluator?
	get() =
		parentOrNull?.endEvaluator(evaluated)

fun EvaluatorParent.endEvaluator(evaluated: Evaluated): Evaluator =
	evaluator.append(word.invoke(evaluated.value))

fun Evaluator.append(field: Field): Evaluator =
	updateCompiled {
		applyCompiler(field) ?: apply(field, mode)
	}

fun Evaluator.append(sentence: Sentence): Evaluator =
	append(sentence.field)

fun Evaluator.applyCompiler(field: Field): Evaluated? =
	notNullIf(field == compilerName(value())) {
		evaluated.scope.evaluated(value(asField))
	}

fun Evaluator.updateCompiled(fn: Evaluated.() -> Evaluated): Evaluator =
	copy(evaluated = evaluated.fn())
