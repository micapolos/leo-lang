package leo13.untyped

import leo.base.fold
import leo.base.orIfNull
import leo13.script.*

data class Pattern(val nodeOrNull: PatternNode?)

fun pattern(nodeOrNull: PatternNode?) = Pattern(nodeOrNull)

fun pattern(vararg lines: PatternLine) = pattern(null).fold(lines) { plus(it) }
fun pattern(choice: Choice, vararg lines: PatternLine) = pattern(node(choice)).fold(lines) { plus(it) }
fun pattern(name: String) = pattern(name lineTo pattern())

fun Pattern.plus(line: PatternLine) =
	if (nodeOrNull == null) pattern(node(line))
	else pattern(nodeOrNull.plus(line))

fun pattern(script: Script): Pattern =
	patternReader.unsafeBodyValue(script)

fun Pattern.matches(script: Script): Boolean =
	if (nodeOrNull == null) script.isEmpty
	else nodeOrNull.matches(script)

val patternName: String = "pattern"

val patternReader: Reader<Pattern> =
	reader(patternName) {
		if (isEmpty) pattern(null)
		else pattern(patternNodeReader.unsafeBodyValue(this))
	}

val patternWriter: Writer<Pattern> =
	writer(patternName) {
		nodeOrNull
			?.run { patternNodeWriter.bodyScript(this) }
			.orIfNull { script() }
	}
