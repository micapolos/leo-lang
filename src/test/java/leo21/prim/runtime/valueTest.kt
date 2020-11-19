package leo21.prim.runtime

import leo.base.assertEqualTo
import leo14.lambda.invoke
import leo14.lambda.pair
import leo14.lambda.term
import leo14.lambda.value.value
import leo21.prim.NumberCosinusPrim
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.NumberSinusPrim
import leo21.prim.NumberTimesNumberPrim
import leo21.prim.StringPlusStringPrim
import leo21.prim.prim
import kotlin.math.cos
import kotlin.math.sin
import kotlin.test.Test
import leo21.prim.runtime.value as primValue

class EvaluateTest {
	@Test
	fun values() {
		term(prim(10))
			.primValue
			.assertEqualTo(value(prim(10)))

		term(prim("Hello, world!"))
			.primValue
			.assertEqualTo(value(prim("Hello, world!")))

		term(NumberPlusNumberPrim)
			.invoke(pair(term(prim(2)), term(prim(3))))
			.primValue
			.assertEqualTo(value(prim(5)))

		term(NumberMinusNumberPrim)
			.invoke(pair(term(prim(5)), term(prim(3))))
			.primValue
			.assertEqualTo(value(prim(2)))

		term(NumberTimesNumberPrim)
			.invoke(pair(term(prim(2)), term(prim(3))))
			.primValue
			.assertEqualTo(value(prim(6)))

		term(NumberSinusPrim)
			.invoke(term(prim(1)))
			.primValue
			.assertEqualTo(value(prim(sin(1.0))))

		term(NumberCosinusPrim)
			.invoke(term(prim(1)))
			.primValue
			.assertEqualTo(value(prim(cos(1.0))))

		term(StringPlusStringPrim)
			.invoke(pair(term(prim("Hello, ")), term(prim("world!"))))
			.primValue
			.assertEqualTo(value(prim("Hello, world!")))
	}
}