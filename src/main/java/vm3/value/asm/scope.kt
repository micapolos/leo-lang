package vm3.value.asm

import leo.stak.Stak
import vm3.layout.Layout

data class Scope(val entryStak: Stak<Entry>) {
	data class Entry(val address: Int, val layout: Layout)
}