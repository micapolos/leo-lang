package leo15.lambda

data class SchemeGen(val term: Term, val depth: Int)

fun Term.schemeGen(depth: Int) = SchemeGen(this, depth)
val Term.schemeGen get() = schemeGen(0)
val Term.scheme: String get() = schemeGen.print

val SchemeGen.print: String
	get() =
		when (term) {
			is ValueTerm -> "${term.value}"
			is AbstractionTerm ->
				"(lambda (v$depth) ${term.body.schemeGen(depth.inc()).print})"
			is ApplicationTerm ->
				"(" + term.lhs.schemeGen(depth).print + " " + term.rhs.schemeGen(depth).print + ")"
			is IndexTerm -> "v${depth - term.index - 1}"
		}
