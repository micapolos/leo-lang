package leo25

import leo14.literal
import leo14.minus
import leo14.plus
import leo14.times

val textAppendTextBody
	get() =
		unsafeBody {
			resolveOrNull(textName, plusName) { rhs ->
				value(field(literal(textOrThrow.plus(rhs.textOrThrow))))
			}!!
		}

val numberAddNumberBody
	get() =
		unsafeBody {
			resolveOrNull(numberName, plusName) { rhs ->
				value(field(literal(numberOrThrow.plus(rhs.numberOrThrow))))
			}!!
		}

val numberSubtractNumberBody
	get() =
		unsafeBody {
			resolveOrNull(numberName, minusName) { rhs ->
				value(field(literal(numberOrThrow.minus(rhs.numberOrThrow))))
			}!!
		}

val numberMultiplyByNumberBody
	get() =
		unsafeBody {
			resolveOrNull(numberName, timesName) { rhs ->
				value(field(literal(numberOrThrow.times(rhs.numberOrThrow))))
			}!!
		}
