package leo13.untyped

import leo.base.fold
import leo13.LeoStruct
import leo13.script.*

data class normalizer(val normalized: normalized = normalized(script())) : LeoStruct("normalizer", normalized) {
	override fun toString() = super.toString()
}

fun normalizer.plus(script: Script): normalizer =
	fold(script.lineSeq) { plus(it) }

fun normalizer.plus(line: ScriptLine): normalizer =
	if (line.rhs.isEmpty) normalizer(normalized(script(line.name lineTo normalized.script)))
	else normalizer(normalized(normalized.script.plus(line)))
