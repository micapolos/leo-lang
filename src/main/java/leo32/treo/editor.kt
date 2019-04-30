package leo32.treo

import leo.binary.Bit

data class Editor(var rawTreo: Treo)

fun editor(treo: Treo) = Editor(treo)

fun Editor.enter(bit: Bit) =
	rawTreo.enter(bit)?.let { entered -> apply { rawTreo = entered } }

fun Editor.edit(bit: Bit) =
	rawTreo.edit(bit) { treo(leaf).withExitTrace(rawTreo) }

var Editor.treo
	get() = rawTreo
	set(treo) = set(treo)

fun Editor.set(treo: Treo) {
	val exitTrace = rawTreo.exitTrace
	rawTreo.exitTrace = null
	treo.exitTrace = exitTrace
}

val Editor.exit
	get() =
		apply { rawTreo = rawTreo.exit!! }

fun Editor.invoke(bit: Bit) =
	apply { rawTreo = rawTreo.invoke(bit) }
