package leo14.untyped.typed

import leo.base.bitCount
import leo.base.orNullIf
import kotlin.math.max

const val maxBitCount = 64

val Int.asBitCountOrNull: Int?
	get() =
		orNullIf { this > 64 }

val Type.bitCountOrNull: Int?
	get() =
		when (this) {
			EmptyType -> 0
			AnythingType -> null
			NothingType -> null
			is LinkType -> link.bitCountOrNull
			is AlternativeType -> alternative.bitCountOrNull
			is FunctionType -> null
			is RepeatingType -> repeating.bitCountOrNull
			is RecursiveType -> null
			RecurseType -> null
		}

val TypeLink.bitCountOrNull: Int?
	get() =
		lhs.bitCountOrNull?.let { lhsBitCount ->
			line.bitCountOrNull?.let { lineBitCount ->
				lhsBitCount.plus(lineBitCount).asBitCountOrNull
			}
		}

val TypeLine.bitCountOrNull: Int?
	get() =
		when (this) {
			is LiteralTypeLine -> null
			is FieldTypeLine -> field.intBitCountOrNull
			NativeTypeLine -> null
		}

val TypeField.intBitCountOrNull: Int?
	get() =
		rhs.bitCountOrNull

val TypeAlternative.bitCountOrNull: Int?
	get() =
		maxAlternativeBitCountOrNull?.plus(alternativeCount.bitCount)?.asBitCountOrNull

val Type.alternativeCount: Int
	get() =
		if (this is AlternativeType) alternative.alternativeCount else 1

val TypeAlternative.alternativeCount: Int
	get() =
		lhs.alternativeCount + 1

val TypeAlternative.maxAlternativeBitCountOrNull: Int?
	get() =
		rhs.bitCountOrNull?.let { rhsBitCount ->
			if (lhs is AlternativeType) lhs.alternative.maxAlternativeBitCountOrNull?.let { lhsBitCount ->
				max(rhsBitCount, lhsBitCount)
			}
			else lhs.bitCountOrNull?.let { lhsBitCount ->
				max(rhsBitCount, lhsBitCount)
			}
		}

val TypeRepeating.bitCountOrNull: Int?
	get() =
		if (type.bitCountOrNull == 0) maxBitCount
		else null