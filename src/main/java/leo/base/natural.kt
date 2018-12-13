package leo.base

// Non-zero natural number.
data class Natural(
	val bitStackWithoutLeadingOneOrNull: Stack<Bit>?) {
	override fun toString() = string { append(it) }
}

fun Appendable.append(natural: Natural): Appendable =
	append("0b").fold(natural.bitStream, Appendable::append)

val Stack<Bit>?.withoutLeadingOneNatural: Natural
	get() =
		Natural(this)

val Natural.bitStream: Stream<Bit>
	get() =
		1.bit.onlyStream.then { bitStackWithoutLeadingOneOrNull?.reverse?.stream }

val Natural.dividedByTwo: Natural?
	get() =
		bitStackWithoutLeadingOneOrNull?.tail?.withoutLeadingOneNatural

val Natural.timesTwo: Natural
	get() =
		bitStackWithoutLeadingOneOrNull.push(0.bit).withoutLeadingOneNatural

fun Natural.timesTwoPlus(bit: Bit): Natural =
	bitStackWithoutLeadingOneOrNull.push(bit).withoutLeadingOneNatural

val Stream<Bit>.naturalOrNull: Natural?
	get() =
		nullOf<Natural>().fold(this) { bit ->
			when (bit) {
				Bit.ZERO -> this?.timesTwoPlus(bit)
				Bit.ONE -> this?.timesTwoPlus(bit) ?: naturalOne
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
			Bit.ZERO -> bitStackWithoutLeadingOneOrNull.tail.push(Bit.ONE).withoutLeadingOneNatural
			Bit.ONE -> bitStackWithoutLeadingOneOrNull.tail.withoutLeadingOneNatural.plusOne.timesTwoPlus(Bit.ZERO)
		}

val Natural?.orNullPlusOne: Natural
	get() =
		this?.plusOne ?: naturalOne

val Natural.minusOne: Natural?
	get() =
		if (bitStackWithoutLeadingOneOrNull == null) null
		else when (bitStackWithoutLeadingOneOrNull.head) {
			Bit.ZERO -> bitStackWithoutLeadingOneOrNull.tail.withoutLeadingOneNatural.minusOne?.timesTwoPlus(Bit.ONE)
				?: naturalOne
			Bit.ONE -> bitStackWithoutLeadingOneOrNull.tail.push(Bit.ZERO).withoutLeadingOneNatural
		}

// === Cached naturals

val naturalOne = Natural(null)
val naturalTwo = naturalOne.plusOne
val naturalThree = naturalTwo.plusOne
val naturalFour = naturalThree.plusOne
val naturalFive = naturalFour.plusOne
val naturalSix = naturalFive.plusOne
val naturalSeven = naturalSix.plusOne

