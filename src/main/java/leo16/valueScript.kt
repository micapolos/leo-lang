package leo16

import leo13.array
import leo13.map
import leo14.*
import leo15.*

val Value.script: Script
	get() =
		script(*fieldStack.map { scriptLine }.array)

val Field.scriptLine: ScriptLine
	get() =
		null
			?: textScriptLineOrNull
			?: numberScriptLineOrNull
			?: byteScriptLineOrNull
			?: intScriptLineOrNull
			?: longScriptLineOrNull
			?: floatScriptLineOrNull
			?: doubleScriptLineOrNull
			?: defaultScriptLine

val Field.defaultScriptLine: ScriptLine
	get() =
		when (this) {
			is SentenceField -> sentence.scriptLine
			is FunctionField -> function.printSentence.scriptLine
			is DictionaryField -> dictionary.printSentence.scriptLine
			is NativeField -> native.nativeScriptLine
		}

val Field.textScriptLineOrNull: ScriptLine?
	get() =
		matchText { it.literal.line }

val Field.numberScriptLineOrNull: ScriptLine?
	get() =
		matchNumber { literal(number(it)).line }

val Field.byteScriptLineOrNull: ScriptLine?
	get() =
		matchPrefix(byteName) { rhs ->
			rhs.matchNative { native ->
				nullIfThrowsException {
					byteName(literal(number((native as Byte).toInt().toBigDecimal())).line)
				}
			}
		}

val Field.intScriptLineOrNull: ScriptLine?
	get() =
		matchPrefix(intName) { rhs ->
			rhs.matchNative { native ->
				nullIfThrowsException {
					intName(literal(number(native as Int)).line)
				}
			}
		}

val Field.longScriptLineOrNull: ScriptLine?
	get() =
		matchPrefix(longName) { rhs ->
			rhs.matchNative { native ->
				nullIfThrowsException {
					longName(literal(number((native as Long).toBigDecimal())).line)
				}
			}
		}

val Field.floatScriptLineOrNull: ScriptLine?
	get() =
		matchPrefix(floatName) { rhs ->
			rhs.matchNative { native ->
				nullIfThrowsException {
					floatName(literal(number((native as Float).toBigDecimal())).line)
				}
			}
		}

val Field.doubleScriptLineOrNull: ScriptLine?
	get() =
		matchPrefix(doubleName) { rhs ->
			rhs.matchNative { native ->
				nullIfThrowsException {
					doubleName(literal(number((native as Double).toBigDecimal())).line)
				}
			}
		}

val Sentence.scriptLine: ScriptLine
	get() =
		word lineTo value.script

val Any?.nativeScriptLine: ScriptLine
	get() =
		nativeString.line