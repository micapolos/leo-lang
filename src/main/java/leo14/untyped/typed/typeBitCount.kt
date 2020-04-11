package leo14.untyped.typed

import leo.base.bitCount
import kotlin.math.max

val Type.bitCountOrNull: Int?
	get() =
		when (this) {
			EmptyType -> 0
			AnythingType -> null
			NothingType -> null
			is LinkType -> link.bitCountOrNull
			is AlternativeType -> alternative.bitCountOrNull
			is FunctionType -> null
			is RepeatingType -> null
			is RecursiveType -> null
			RecurseType -> null
		}

val TypeLink.bitCountOrNull: Int?
	get() =
		lhs.bitCountOrNull?.let { lhsBitCount ->
			line.bitCountOrNull?.let { lineBitCount ->
				lhsBitCount.plus(lineBitCount)
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
		maxAlternativeBitCountOrNull?.plus(alternativeCount.bitCount)

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
