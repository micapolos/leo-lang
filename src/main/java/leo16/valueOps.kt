package leo16

import leo.base.notNullIf
import leo13.linkOrNull
import leo13.mapFirst
import leo13.onlyOrNull
import leo13.stack
import leo15.functionName

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
	}

infix fun StructValue.accessOrNull(word: String): Value? =
	struct.lineStack.mapFirst {
		notNullIf(this.word == word) {
			value(this)
		}
	}

infix fun FunctionValue.accessOrNull(word: String): Value? =
	notNullIf(word == functionName)

infix fun Value.make(word: String): Value =
	value(word.invoke(this))

fun Value.matchOrNull(vararg cases: Case): Value? =
	thingOrNull
		?.structOrNull
		?.lineStack
		?.onlyOrNull
		?.let { line ->
			stack(*cases).mapFirst {
				notNullIf(line.word == selectedWord) {
					fn.invoke(line.value)
				}
			}
		}
