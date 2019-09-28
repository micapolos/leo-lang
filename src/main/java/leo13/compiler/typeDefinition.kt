package leo13.compiler

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.containsName
import leo13.definitionName
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo13.type.*

data class TypeDefinition(
	val line: TypeLine,
	val containsType: Type) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() = definitionName lineTo
			line.scriptingLine.rhs.plus(containsName lineTo script(containsType.scriptingLine))

	fun hasTypeOrNull(line: TypeLine): Type? =
		notNullIf(this.line == line) {
			containsType
		}

	fun resolveOrNull(line: TypeLine): TypeLine? =
		hasTypeOrNull(line)?.let {
			line.leafPlusOrNull(it)
		}
}

fun definition(line: TypeLine, hasType: Type) =
	TypeDefinition(line, hasType)

val booleanTypeDefinition =
	definition(booleanTypeLine.name lineTo type(), booleanTypeLine.rhs)