package leo21.evaluator

import leo.base.runIfNotNull
import leo14.cosinus
import leo14.minus
import leo14.plus
import leo14.sinus
import leo14.times
import leo21.compiled.resolve
import leo21.type.isEmpty

val Evaluated.resolve: Evaluated
	get() =
		compiled.resolve.evaluated

val Evaluated.resolveOrNull: Evaluated?
	get() =
		linkOrNull?.let { link ->
			link.tail.run {
				link.head.fieldOrNull?.let { field ->
					if (field.rhs.type.isEmpty) resolveOrNull(field.name)
					else resolveOrNull(field.name, field.rhs)
				}
			}
		}

fun Evaluated.resolveOrNull(name: String): Evaluated? =
	null
		?: getOrNull(name)
		?: when (name) {
			"sinus" -> numberOrNull?.let { evaluated(it.sinus) }
			"cosinus" -> numberOrNull?.let { evaluated(it.cosinus) }
			else -> null
		}
		?: make(name)

fun Evaluated.resolveOrNull(name: String, rhs: Evaluated): Evaluated? =
	when (name) {
		"plus" -> null
			?: numberOrNull?.runIfNotNull(rhs.numberOrNull) { evaluated(this + it) }
			?: stringOrNull?.runIfNotNull(rhs.stringOrNull) { evaluated(this + it) }
		"minus" -> numberOrNull?.runIfNotNull(rhs.numberOrNull) { evaluated(this - it) }
		"times" -> numberOrNull?.runIfNotNull(rhs.numberOrNull) { evaluated(this * it) }
		else -> null
	}
