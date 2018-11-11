package leo

import leo.base.*

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
	ruleStackOrNull.push(rule).function

fun Function.invoke(argument: Script): Script =
	ruleStackOrNull.invoke(argument)

fun Stack<Rule>?.invoke(argument: Script): Script =
	fold(argument) { foldedArgument, rule ->
		var currentArgument = foldedArgument
		while (true) {
			val result = rule.apply(currentArgument)
			if (result == null) break
			else currentArgument = result
		}
		currentArgument
	}

// === reflect

val Function.reflect: Field<Nothing>
	get() =
		functionWord
			.fieldTo(
				ruleStackOrNull
					?.reflect(Rule::reflect)
					?: term(identityWord))