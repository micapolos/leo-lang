package leo.base

// Non-zero natural number.
data class Natural(
	val bitStackWithoutLeadingOneOrNull: Stack<EnumBit>?) {
	override fun toString() = string { append(it) }
}

fun Appendable.append(natural: Natural): Appendable =
	append("0b").fold(natural.bitStream, Appendable::append)

val Stack<EnumBit>?.withoutLeadingOneNatural: Natural
	get() =
		Natural(this)

val Natural.bitStream: Stream<EnumBit>
	get() =
		1.enumBit.onlyStream.then { bitStackWithoutLeadingOneOrNull?.reverse?.stream }

val Natural.dividedByTwo: Natural?
	get() =
		bitStackWithoutLeadingOneOrNull?.tail?.withoutLeadingOneNatural

val Natural.timesTwo: Natural
	get() =
		bitStackWithoutLeadingOneOrNull.push(0.enumBit).withoutLeadingOneNatural

fun Natural.timesTwoPlus(bit: EnumBit): Natural =
	bitStackWithoutLeadingOneOrNull.push(bit).withoutLeadingOneNatural

val Stream<EnumBit>.naturalOrNull: Natural?
	get() =
		nullOf<Natural>().fold(this) { bit ->
			when (bit) {
				EnumBit.ZERO -> this?.timesTwoPlus(bit)
				EnumBit.ONE -> this?.timesTwoPlus(bit) ?: naturalOne
			}
		}

val Byte.unsignedNaturalOrNull: Natural?
	get() =
		bitStream.naturalOrNull

val Short.unsignedNaturalOrNull: Natural?
	get() =
		bitStream.naturalOrNull

val Int.unsignedNaturalOrNull: Natural?
	get() =
		bitStream.naturalOrNull

val Long.unsignedNaturalOrNull: Natural?
	get() =
		bitStream.naturalOrNull

val Natural.plusOne: Natural
	get() =
		if (bitStackWithoutLeadingOneOrNull == null) timesTwo
		else when (bitStackWithoutLeadingOneOrNull.head) {
			EnumBit.ZERO -> bitStackWithoutLeadingOneOrNull.tail.push(EnumBit.ONE).withoutLeadingOneNatural
			EnumBit.ONE -> bitStackWithoutLeadingOneOrNull.tail.withoutLeadingOneNatural.plusOne.timesTwoPlus(EnumBit.ZERO)
		}

val Natural?.orNullPlusOne: Natural
	get() =
		this?.plusOne ?: naturalOne

val Natural.minusOne: Natural?
	get() =
		if (bitStackWithoutLeadingOneOrNull == null) null
		else when (bitStackWithoutLeadingOneOrNull.head) {
			EnumBit.ZERO -> bitStackWithoutLeadingOneOrNull.tail.withoutLeadingOneNatural.minusOne?.timesTwoPlus(EnumBit.ONE)
				?: naturalOne
			EnumBit.ONE -> bitStackWithoutLeadingOneOrNull.tail.push(EnumBit.ZERO).withoutLeadingOneNatural
		}

val Natural.bitCount: Natural
	get() =
		bitStream.run {
			naturalOne.fold(nextOrNull) { plusOne }
		}

// === Cached naturals

val naturalZero = null as Natural?
val naturalOne = Natural(null)
val naturalTwo = naturalOne.plusOne
val naturalThree = naturalTwo.plusOne
val naturalFour = naturalThree.plusOne
val naturalFive = naturalFour.plusOne
val naturalSix = naturalFive.plusOne
val naturalSeven = naturalSix.plusOne

