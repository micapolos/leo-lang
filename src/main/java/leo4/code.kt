package leo4

val Ap.code: String get() = term.code + line.code
val Line.code: String get() = string + "(" + script.code + ")"

val Term.code: String
	get() = when (this) {
		is LineTerm -> line.code
		is ApTerm -> ap.code
	}

val Script.code: String
	get() = when (this) {
		is EmptyScript -> ""
		is TermScript -> term.code
	}
