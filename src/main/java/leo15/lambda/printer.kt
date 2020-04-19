package leo15.lambda

data class Printer(val term: Term, val depth: Int)

fun Term.printer(depth: Int) = Printer(this, depth)
val Term.printer get() = printer(0)
val Term.print: String get() = printer.print

data class Fn(val name: String, val body: Term) {
	override fun toString() = "{ $name -> ${body.print} }"
}

fun fn(name: String, body: Term) = value(Fn(name, body))

val Printer.print: String
	get() =
		when (term) {
			is ValueTerm -> "${term.value}"
			is AbstractionTerm ->
				"${term.isRepeating.isRepeatingLambdaName} { v$depth -> ${term.body.printer(depth.inc()).print} }"
			is ApplicationTerm ->
				term.lhs.printer(depth).print + "(" + term.rhs.printer(depth).print + ")"
			is IndexTerm -> "v${depth - term.index - 1}"
			is RepeatTerm -> "${term.rhs.printer(depth).print}.repeat"
		}
