package leo13.untyped

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.script.*

val caseReader: Reader<Case> =
	reader(caseName) {
		unsafeOnlyLine.run {
			name caseTo rhs
		}
	}

val caseWriter: Writer<Case> =
	writer(caseName) {
		script(name lineTo rhs)
	}

data class Case(val name: String, val rhs: Script) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "case" lineTo script(name lineTo rhs)

}

infix fun String.caseTo(rhs: Script) = Case(this, rhs)

fun Case.rhsOrNull(line: ScriptLine): Script? =
	notNullIf(name == line.name) { rhs }
