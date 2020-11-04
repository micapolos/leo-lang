package leo21.type

import leo.base.ifOrNull
import leo14.ScriptLine
import leo14.match

data class TypeCompiler(val type: Type)

val emptyTypeCompiler = TypeCompiler(type())

fun TypeCompiler.plus(scriptLine: ScriptLine): TypeCompiler =
	null
		?: plusChoiceOrNull(scriptLine)
		?: plusField(scriptLine)

fun TypeCompiler.plusChoiceOrNull(scriptLine: ScriptLine): TypeCompiler? =
	ifOrNull(type == type()) {
		scriptLine.match("choice") { rhs ->
			copy(type = type(rhs.choice))
		}
	}

fun TypeCompiler.plusField(scriptLine: ScriptLine): TypeCompiler =
	copy(type = type.plus(scriptLine.typeLine))
