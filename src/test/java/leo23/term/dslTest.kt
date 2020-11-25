package leo23.term

import kotlin.test.Test

class DslTest {
	@Test
	fun dsl() {
		v(0)
		v0
		v1
		v2

		nil
		v0.isNil

		boolean(true)
		v0.ifThenElse(v1, v2)

		number(10)
		number(10.0)
		v0.plus(v1)
		v0.minus(v1)
		v0.times(v1)
		v0.numberEquals(v1)
		v0.numberText

		text("Hello")
		v0.textAppend(v1)
		v0.textNumberOrNil

		v0 pairTo v1
		v0.lhs
		v0.rhs

		vector()
		vector(v0, v1, v2)
		v0[v1]

		fn(2, v0.minus(v1))
		fn0(v0)
		fn1(v0)
		fn2(v0)

		v0.apply(v1)
	}
}