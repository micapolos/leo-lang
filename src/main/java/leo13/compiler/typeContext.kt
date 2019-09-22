package leo13.compiler

import leo.base.orIfNull
import leo13.ObjectScripting
import leo13.pattern.*
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

	fun resolveOrNull(type: Type): TypeContext? =
		when (type) {
			is PatternType -> plus(pattern)
			is RecurseType -> resolveOrNull(type.traced)
		}

	fun resolveOrNull(traced: Recurse): TypeContext? =
		if (traced.lhsOrNull == null) this
		else lhsOrNull?.resolveOrNull(traced.lhsOrNull)

	fun contains(otherPattern: Pattern): Boolean =
		when (pattern) {
			is EmptyPattern -> otherPattern.isEmpty
			is OptionsPattern -> pattern.options.contains(otherPattern)
			is ArrowPattern -> otherPattern is ArrowPattern && pattern.arrow.contains(otherPattern.arrow)
			is LinkPattern -> otherPattern is LinkPattern && pattern.link.contains(otherPattern.link)
		}
}

val Pattern.trace get() = TypeContext(null, this)
