package leo13.compiler

import leo.base.orIfNull
import leo13.ObjectScripting
import leo13.pattern.NodePattern
import leo13.pattern.Pattern
import leo13.pattern.Recurse
import leo13.pattern.RecursePattern
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo13.traceName

data class TypeContext(val lhsOrNull: TypeContext?, val pattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() =
			traceName lineTo lhsOrNull?.scriptingLine?.rhs.orIfNull { script() }.plus(pattern.scriptingLine)

	fun plus(pattern: Pattern) = TypeContext(this, pattern)

	fun resolveOrNull(pattern: Pattern): TypeContext? =
		when (pattern) {
			is NodePattern -> plus(this.pattern)
			is RecursePattern -> resolveOrNull(pattern.recurse)
		}

	fun resolveOrNull(recurse: Recurse): TypeContext? =
		if (recurse.lhsOrNull == null) this
		else lhsOrNull?.resolveOrNull(recurse.lhsOrNull)
}

val Pattern.trace get() = TypeContext(null, this)
