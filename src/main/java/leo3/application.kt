package leo3

data class Application(
	val lhs: Template,
	val word: Word,
	val rhs: Template)

fun application(lhs: Template, word: Word, rhs: Template) =
	Application(lhs, word, rhs)

fun Application.apply(parameter: Parameter) =
	lhs.apply(parameter)?.let { lhsResult ->
		rhs.apply(parameter)?.let { rhsResult ->
			result(term(lhsResult.termOrNull, word, rhsResult.termOrNull))
		}
	}
