package leo.lab

import leo.base.*
import leo.functionWord
import leo.identityWord

data class Function(
	val ruleStackOrNull: Stack<Rule>?) {
	override fun toString() = reflect.string
}

val Stack<Rule>?.function
	get() =
		Function(this)

val identityFunction
	get() =
		nullStack<Rule>().function

fun Function.push(rule: Rule) =
	copy(ruleStackOrNull = ruleStackOrNull.push(rule))

fun Function.invoke(argument: Term<Nothing>): Term<Nothing>? =
	ruleStackOrNull
		?.top { rule -> argument.matches(rule.choiceTerm) }
		?.body
		?.apply(argument)
		?: argument.invokeFallback

// === fallback

val Term<Nothing>.invokeFallback: Term<Nothing>?
	get() =
		when (this) {
			is Term.Meta -> this
			is Term.Structure ->
				when {
					rhsTermOrNull != null -> this
					lhsTermOrNull == null -> this
					else -> lhsTermOrNull.select(word)?.value ?: word.fieldTo(lhsTermOrNull).term
				}
		}

// === reflect

val Function.reflect: Field<Nothing>
	get() =
		functionWord
			.fieldTo(
				ruleStackOrNull
					?.reflect(Rule::reflect)
					?: identityWord.term)