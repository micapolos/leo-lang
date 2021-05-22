package leo25

import leo14.literal
import leo14.minus
import leo14.plus
import leo14.times


val textAppendTextBody
	get() =
		unsafeBody {
			unlinkOrNull { rhs ->
				value(line(literal(textOrNull!!.plus(rhs.textOrNull!!))))
			}!!
		}

val numberAddNumberBody
	get() =
		unsafeBody {
			unlinkOrNull { rhs ->
				value(line(literal(numberOrNull!!.plus(rhs.numberOrNull!!))))
			}!!
		}

val numberSubtractNumberBody
	get() =
		unsafeBody {
			unlinkOrNull { rhs ->
				value(line(literal(numberOrNull!!.minus(rhs.numberOrNull!!))))
			}!!
		}

val numberMultiplyByNumberBody
	get() =
		unsafeBody {
			unlinkOrNull { rhs ->
				value(line(literal(numberOrNull!!.times(rhs.bodyOrNull!!.numberOrNull!!))))
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
