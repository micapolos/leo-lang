package leo16

import leo.base.notNullIf
import leo13.linkOrNull
import leo13.mapFirst
import leo13.onlyOrNull
import leo15.*

val Value.normalize: Value
	get() =
		structOrNull?.lineStack?.linkOrNull?.let { lineLink ->
			notNullIf(lineLink.value.value.isEmpty) {
				value(lineLink.value.word.invoke(lineLink.stack.struct.value))
			}
		} ?: this

val Value.thingOrNull: Value?
	get() =
		structOrNull?.lineStack?.onlyOrNull?.value

infix fun Value.getOrNull(word: String): Value? =
	thingOrNull?.accessOrNull(word)

infix fun Value.accessOrNull(word: String): Value? =
	when (this) {
		is StructValue -> accessOrNull(word)
		is FunctionValue -> accessOrNull(word)
		is ScopeValue -> accessOrNull(word)
	}

infix fun StructValue.accessOrNull(word: String): Value? =
	struct.lineStack.mapFirst {
		notNullIf(this.word == word) {
			value(this)
		}
	}

infix fun FunctionValue.accessOrNull(word: String): Value? =
	notNullIf(word == functionName)

infix fun ScopeValue.accessOrNull(word: String): Value? =
	notNullIf(word == scopeName)

infix fun Value.make(word: String): Value =
	value(word.invoke(this))

val Value.lastOrNull: Value?
	get() =
		matchPrefix(listName) { rhs ->
			rhs.structOrNull?.lineStack?.linkOrNull?.value?.let { line ->
				value(lastName(line))
			}
		}

val Value.previousOrNull: Value?
	get() =
		matchPrefix(listName) { rhs ->
			rhs.structOrNull?.lineStack?.linkOrNull?.stack?.struct?.value?.let { value ->
				value(previousName(listName(value)))
			}
		}
