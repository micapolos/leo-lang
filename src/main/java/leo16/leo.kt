package leo16

import leo14.Literal

data class Leo(val context: Context, val reader: Reader)

fun Leo.begin(word: String): Leo =
	when (reader) {
		is EvaluatorReader -> TODO()
		is DictionarianReader -> TODO()
		is CompilerReader -> TODO()
	}

fun Leo.plus(literal: Literal): Leo =
	when (reader) {
		is EvaluatorReader -> TODO()
		is DictionarianReader -> TODO()
		is CompilerReader -> TODO()
	}

val Leo.end: Leo
	get() =
		when (reader) {
			is EvaluatorReader -> TODO()
			is DictionarianReader -> TODO()
			is CompilerReader -> TODO()
		}
