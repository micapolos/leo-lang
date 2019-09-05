package leo13.untyped

import leo.base.fold
import leo13.LeoStruct
import leo13.script.*

data class Normalizer(val normalized: Normalized) : LeoStruct("normalizer", normalized) {
	override fun toString() = super.toString()
}

fun normalizer(normalized: Normalized = normalized(script())) = Normalizer(normalized)

fun leo13.untyped.Normalizer.plus(script: Script): leo13.untyped.Normalizer =
	fold(script.lineSeq) { plus(it) }

fun leo13.untyped.Normalizer.plus(line: ScriptLine): leo13.untyped.Normalizer =
	if (line.rhs.isEmpty) Normalizer(Normalized(script(line.name lineTo normalized.script)))
	else Normalizer(Normalized(normalized.script.plus(line)))
