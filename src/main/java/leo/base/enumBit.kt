package leo.base

enum class EnumBit {
	ZERO,
	ONE;

	override fun toString() = "bit $int"
}

val zeroBit = EnumBit.ZERO
val oneBit = EnumBit.ONE

val EnumBit.int
	get() =
		ordinal

val EnumBit.char
	get() =
		when (this) {
			EnumBit.ZERO -> '0'
			EnumBit.ONE -> '1'
		}

val EnumBit.inverse
	get() =
		when (this) {
			EnumBit.ZERO -> EnumBit.ONE
			EnumBit.ONE -> EnumBit.ZERO
		}

infix fun EnumBit.nand(bit: EnumBit) =
	and(bit).inverse

fun EnumBit.and(bit: EnumBit) =
	when (this) {
		EnumBit.ZERO ->
			when (bit) {
				EnumBit.ZERO -> EnumBit.ZERO
				EnumBit.ONE -> EnumBit.ZERO
			}
		EnumBit.ONE ->
			when (bit) {
				EnumBit.ZERO -> EnumBit.ZERO
				EnumBit.ONE -> EnumBit.ONE
			}
	}

fun EnumBit.or(bit: EnumBit) =
	when (this) {
		EnumBit.ZERO ->
			when (bit) {
				EnumBit.ZERO -> EnumBit.ZERO
				EnumBit.ONE -> EnumBit.ONE
			}
		EnumBit.ONE ->
			when (bit) {
				EnumBit.ZERO -> EnumBit.ONE
				EnumBit.ONE -> EnumBit.ONE
			}
	}

fun EnumBit.xor(bit: EnumBit) =
	when (this) {
		EnumBit.ZERO ->
			when (bit) {
				EnumBit.ZERO -> EnumBit.ZERO
				EnumBit.ONE -> EnumBit.ONE
			}
		EnumBit.ONE ->
			when (bit) {
				EnumBit.ZERO -> EnumBit.ONE
				EnumBit.ONE -> EnumBit.ZERO
			}
	}

val Int.enumBitOrNull
	get() =
		when (this) {
			0 -> EnumBit.ZERO
			1 -> EnumBit.ONE
			else -> null
		}

val Int.enumBit
	get() =
		if (this == 0) EnumBit.ZERO
		else EnumBit.ONE

val Int.lastEnumBit: EnumBit
	get() =
		if (and(1) == 0) EnumBit.ZERO
		else EnumBit.ONE

val Int.clampedEnumBit
	get() =
		if (this == 0) EnumBit.ZERO
		else EnumBit.ONE

fun <V> EnumBit.ifZero(value: V, fn: (V) -> V): V =
	when (this) {
		EnumBit.ZERO -> fn(value)
		EnumBit.ONE -> value
	}

fun Appendable.append(bit: EnumBit): Appendable =
	append(bit.char)

fun Appendable.appendBit(bitStream: Stream<EnumBit>): Appendable =
	read(bitStream) { bit, nextOrNull ->
		append(bit).ifNotNull(nextOrNull, Appendable::appendBit)
	}

val EnumBit.bitSeq get() = onlySeq

val EnumBit.boolean
	get() =
		when (this) {
			EnumBit.ZERO -> false
			EnumBit.ONE -> true
		}

val EnumBit.isZero
	get() =
		this == EnumBit.ZERO
