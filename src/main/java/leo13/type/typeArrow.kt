package leo13.type

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.arrowName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus
import leo13.toName

data class TypeArrow(val lhs: Type, val rhs: Type) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = arrowName lineTo
			lhs.scriptingLine.rhs.plus(toName lineTo rhs.scriptingLine.rhs)
}

infix fun Type.arrowTo(rhs: Type) = TypeArrow(this, rhs)

fun TypeArrow.contains(arrow: TypeArrow) =
	this == arrow // TODO: Consider weaker version, lhs.arrow.contains(lhs) && rhs.contains(arrow.rhs)

fun TypeArrow.rhsOrNull(type: Type): Type? =
	notNullIf(lhs == type) { rhs }