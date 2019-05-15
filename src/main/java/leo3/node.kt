package leo3

data class Application(
	val lhs: Template,
	val word: Word,
	val rhs: Template)

fun application(lhs: Template, word: Word, rhs: Template) =
	Application(lhs, word, rhs)

fun Application.apply(term: Term) =
	term(lhs.apply(term), word, rhs.apply(term))