package leo21.evaluator

import leo.base.runIfNotNull
import leo21.compiled.resolve
import leo21.type.isEmpty
import kotlin.math.cos
import kotlin.math.sin

val Evaluated.resolve: Evaluated
	get() =
		compiled.resolve.evaluated

val Evaluated.resolveOrNull: Evaluated?
	get() =
		structOrNull?.linkOrNull?.let { link ->
			link.tail.evaluated.run {
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
			"sinus" -> doubleOrNull?.let { evaluated(sin(it)) }
			"cosinus" -> doubleOrNull?.let { evaluated(cos(it)) }
			else -> null
		}
		?: make(name)

fun Evaluated.resolveOrNull(name: String, rhs: Evaluated): Evaluated? =
	when (name) {
		"plus" -> null
			?: doubleOrNull?.runIfNotNull(rhs.doubleOrNull) { evaluated(this + it) }
			?: stringOrNull?.runIfNotNull(rhs.stringOrNull) { evaluated(this + it) }
		"minus" -> doubleOrNull?.runIfNotNull(rhs.doubleOrNull) { evaluated(this - it) }
		"times" -> doubleOrNull?.runIfNotNull(rhs.doubleOrNull) { evaluated(this * it) }
		else -> null
	}
