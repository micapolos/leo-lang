package leo13.untyped.expression

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.caseName

data class Case(val name: String, val expression: Expression) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "case" lineTo script(name lineTo expression.bodyScript)
}

infix fun String.caseTo(rhs: Expression) = Case(this, rhs)

val Case.scriptLine
	get() =
		caseName lineTo script(name lineTo expression.bodyScript)