package leo13.type

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.lineName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class TypeLine(val name: String, val unexpandedRhs: Type) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			lineName lineTo script(name lineTo unexpandedRhs.scriptingLine.rhs)

	val rhs get() = expand().unexpandedRhs

	fun rhsOrNull(name: String) =
		notNullIf(this.name == name) { unexpandedRhs }

	fun setRhsOrNull(line: TypeLine): TypeLine? =
		notNullIf(name == line.name) { line }

	fun leafPlusOrNull(type: Type): TypeLine? =
		rhs.leafPlusOrNull(type)?.let { name lineTo it }

	val onlyNameOrNull: String? get() = notNullIf(unexpandedRhs.isEmpty) { name }

	fun expand(rootOrNull: RecurseRoot? = null): TypeLine =
		name lineTo unexpandedRhs.expand(rootOrNull.orNullRecurseIncrease(this))

	fun contains(line: TypeLine, traceOrNull: TypeTrace?): Boolean =
		name == line.name && unexpandedRhs.contains(line.unexpandedRhs, traceOrNull)

	val isStatic: Boolean
		get() =
			unexpandedRhs.isStatic
}

infix fun String.lineTo(rhs: Type) = TypeLine(this, rhs)

val ScriptLine.typeLine: TypeLine get() = name lineTo rhs.type

val String.typeLine: TypeLine get() = lineTo(type())