package leo25

import leo14.literal
import leo14.minus
import leo14.plus
import leo14.times

val textAppendTextBody
	get() =
		unsafeBody {
			resolveOrNull(textName, appendName) { rhs ->
				value(field(literal(textOrThrow.plus(rhs.textOrThrow))))
			}!!
		}

val numberAddNumberBody
	get() =
		unsafeBody {
			resolveOrNull(numberName, addName) { rhs ->
				value(field(literal(numberOrThrow.plus(rhs.numberOrThrow))))
			}!!
		}

val numberSubtractNumberBody
	get() =
		unsafeBody {
			resolveOrNull(numberName, subtractName) { rhs ->
				value(field(literal(numberOrThrow.minus(rhs.numberOrThrow))))
			}!!
		}

val numberMultiplyByNumberBody
	get() =
		unsafeBody {
			resolveOrNull(numberName, multiplyName) { rhs ->
				rhs.resolvePrefixOrNull(byName) { rhs ->
					value(field(literal(numberOrThrow.times(rhs.numberOrThrow))))
				}
			}!!
		}
