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
		?: argument

// === reflect

val Function.reflect: Field<Nothing>
	get() =
		functionWord
			.fieldTo(
				ruleStackOrNull
					?.reflect(Rule::reflect)
					?: identityWord.term)