package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.caseName

data class Case(val name: String, val expression: Expression)

val Case.scriptLine
	get() =
		caseName lineTo script(name lineTo expression.bodyScript)