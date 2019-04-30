package leo32.treo

import leo.base.seq

object Edit

val edit = Edit

@Suppress("unused")
val Edit.char
	get() = '#'
val Edit.charSeq get() = seq(char)