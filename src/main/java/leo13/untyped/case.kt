package leo13.untyped

import leo.base.notNullIf
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

data class Case(val name: String, val rhs: Script) {
	override fun toString() = super.toString()
}

infix fun String.caseTo(rhs: Script) = Case(this, rhs)

fun Case.rhsOrNull(line: ScriptLine): Script? =
	notNullIf(name == line.name) { rhs }
