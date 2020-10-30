package leo20

import leo14.Script
import leo14.fieldOrNull
import leo14.isEmpty
import leo14.lineTo
import leo14.linkOrNull
import leo14.plus

data class Scope(
	val dictionary: Dictionary,
	val value: Value
)

val emptyScope = Scope(emptyDictionary, value())
val Scope.pushPrelude get() = Scope(dictionary.pushPrelude, value)

fun Scope.push(definition: Definition) = copy(dictionary = dictionary.push(definition))
fun Scope.push(value: Value) = copy(value = this.value.plus(value))

fun Scope.defineOrNull(script: Script): Scope? =
	script.linkOrNull?.let { link ->
		link.lhs.patternOrNull?.let { pattern ->
			link.line.fieldOrNull?.let { field ->
				when (field.string) {
					"does" -> defineDoes(pattern, field.rhs)
					else -> null
				}
			}
		}
	}

fun Scope.defineDoes(pattern: Pattern, script: Script): Scope =
	script.recursivelyBodyOrNull
		?.let { recursivelyBody ->
			push(Definition(pattern, function(body(recursivelyBody)), isRecursive = true))
		}
		?: push(Definition(pattern, function(body(script)), isRecursive = false))

fun Scope.test(script: Script) {
	val link = script.linkOrNull ?: error("syntax" lineTo script)
	val field = link.line.fieldOrNull ?: error("syntax" lineTo script)
	when (field.string) {
		"equals" -> {
			val lhsValue = value(link.lhs)
			val rhsValue = value(field.rhs)
			if (lhsValue != rhsValue)
				error(
					"test" lineTo script,
					"result" lineTo lhsValue.script.plus("equals" lineTo rhsValue.script))
		}
		"fails" -> {
			if (!field.rhs.isEmpty) error("syntax" lineTo script)
			try {
				value(link.lhs)
				error("test" lineTo script)
			} catch (exception: Exception) {
				// OK
			}
		}
		else -> error("syntax" lineTo script)
	}

}

fun Scope.getOrNull(name: String, vararg names: String) =
	value.lineOrNull(name, *names)?.let { value(it) }

fun Scope.getOrNull(script: Script): Value? =
	value("given" lineTo value).getOrNull(script)

fun Scope.unsafeGet(name: String, vararg names: String) =
	getOrNull(name, *names)!!
