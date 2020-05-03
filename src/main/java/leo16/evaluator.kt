package leo16

import leo.base.notNullIf
import leo.base.nullOf
import leo.base.orIfNull
import leo13.fold
import leo13.reverse
import leo14.*
import leo15.*

data class Evaluator(val parentOrNull: EvaluatorParent?, val compiled: Evaluated, val isMeta: Boolean) {
	override fun toString() = asField.toString()
}

data class EvaluatorParent(val evaluator: Evaluator, val word: String) {
	override fun toString() = asField.toString()
}

fun EvaluatorParent?.evaluator(evaluated: Evaluated, isMeta: Boolean) = Evaluator(this, evaluated, isMeta)
infix fun EvaluatorParent?.evaluator(evaluated: Evaluated) = Evaluator(this, evaluated, isMeta = false)
val Evaluated.evaluator get() = nullOf<EvaluatorParent>().evaluator(this)
val Scope.evaluator get() = emptyEvaluated.evaluator
fun Evaluator.parent(word: String) = EvaluatorParent(this, word)
val emptyEvaluator = emptyScope.emptyEvaluated.evaluator

val Evaluator.asField: Field
	get() =
		compilerName(
			parentOrNull?.asField.orIfNull { parentName(nothingName()) },
			compiled.asSentence,
			metaName(if (isMeta) trueName() else falseName()))

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
	parent(word).evaluator(compiled.begin, isMeta || word.wordIsMeta)

val Evaluator.end: Evaluator?
	get() =
		parentOrNull?.endEvaluator(compiled)

fun EvaluatorParent.endEvaluator(evaluated: Evaluated): Evaluator =
	evaluator.append(word.invoke(evaluated.value))

fun Evaluator.append(field: Field): Evaluator =
	updateCompiled {
		applyCompiler(field) ?: if (isMeta) plus(field)
		else apply(field)
	}

fun Evaluator.append(sentence: Sentence): Evaluator =
	append(sentence.field)

fun Evaluator.applyCompiler(field: Field): Evaluated? =
	notNullIf(field == compilerName(value())) {
		compiled.scope.evaluated(value(asField))
	}

fun Evaluator.updateCompiled(fn: Evaluated.() -> Evaluated): Evaluator =
	copy(compiled = compiled.fn())
