package leo25

import leo14.literal
import leo14.minus
import leo14.plus
import leo14.times


val textAppendTextBody
	get() =
		unsafeBody {
			resolveOrNull(textName, appendName) { rhs ->
				value(field(literal(textOrNull!!.plus(rhs.textOrNull!!))))
			}!!
		}

val numberAddNumberBody
	get() =
		unsafeBody {
			resolveOrNull(numberName, addName) { rhs ->
				value(field(literal(numberOrNull!!.plus(rhs.numberOrNull!!))))
			}!!
		}

val numberSubtractNumberBody
	get() =
		unsafeBody {
			resolveOrNull(numberName, subtractName) { rhs ->
				value(field(literal(numberOrNull!!.minus(rhs.numberOrNull!!))))
			}!!
		}

val numberMultiplyByNumberBody
	get() =
		unsafeBody {
			resolveOrNull(numberName, multiplyName) { rhs ->
				rhs.resolveOrNull(byName) { rhs ->
					value(field(literal(numberOrNull!!.times(rhs.numberOrNull!!))))
				}
			}!!
		}

val getHashBody
	get() =
		unsafeBody {
			unlinkOrNull {
				hashValue
			}!!
		}

val isBody
	get() =
		unsafeBody {
			unlinkOrNull { rhs ->
				equals(rhs).isValue
			}!!
		}
