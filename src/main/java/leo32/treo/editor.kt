package leo32.treo

import leo.binary.Bit

data class Editor(
	val parentOrNull: Editor? = null,
	var rawTreo: Treo)

fun editor(treo: Treo) = Editor(null, treo)

fun Editor.enter(bit: Bit) =
	rawTreo.enter(bit)?.let { entered -> apply { rawTreo = entered } }

fun Editor.edit(bit: Bit) =
	rawTreo.edit(bit) { treo(leaf) }

var Editor.treo
	get() = rawTreo
	set(treo) = set(treo)

fun Editor.set(treo: Treo) =
	run { rawTreo = rawTreo.replace(treo) }

val Editor.exit
	get() =
		apply { rawTreo = rawTreo.exit!! }

fun Editor.invoke(bit: Bit) =
	apply { rawTreo = rawTreo.invoke(bit) }

val Editor.begin
	get() =
		Editor(this, treo.exitRoot)

