package leo15.type

import leo15.minusName
import leo15.plusName
import leo15.timesName
import java.math.BigDecimal

val Typed.apply: Typed
	get() =
		null
			?: applyNumberOpNumber(plusName, BigDecimal::plus)
			?: applyNumberOpNumber(minusName, BigDecimal::minus)
			?: applyNumberOpNumber(timesName, BigDecimal::times)
			?: applyGet
			?: this

fun Typed.applyNumberOpNumber(name: String, fn: BigDecimal.(BigDecimal) -> BigDecimal): Typed? =
	matchInfix(name) { lhs, rhs ->
		lhs.matchNumber { lhs ->
			rhs.matchNumber { rhs ->
				lhs.applyValue(rhs) { (this as BigDecimal).fn(it as BigDecimal) }.javaNumberTyped
			}
		}
	}

val Typed.applyGet: Typed?
	get() =
		matchField { name, rhs ->
			rhs.get(name)
		}
