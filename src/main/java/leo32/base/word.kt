package leo32.base

sealed class Word<Arch>
data class Word32(val i32: I32): Word<Arch32>()
data class Word64(val i64: I64): Word<Arch64>()

val I32.word: Word32 get() = Word32(this)
val I64.word: Word64 get() = Word64(this)

fun <Arch> I32.word(): Word<Arch> = Word32(this).cast()
fun <Arch> I64.word(): Word<Arch> = Word64(this).cast()

@Suppress("UNCHECKED_CAST")
fun <Arch> Word<*>.cast() =
	this as Word<Arch>

val <Arch> Word<Arch>.inc: Word<Arch>
	get() =
	when (this) {
		is Word32 -> i32.inc.word()
		is Word64 -> i64.inc.word()
	}

val <Arch> Word<Arch>.dec: Word<Arch>
	get() =
	when (this) {
		is Word32 -> i32.dec.word()
		is Word64 -> i64.dec.word()
	}

