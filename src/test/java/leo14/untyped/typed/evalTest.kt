package leo14.untyped.typed

import leo14.invoke
import leo14.leo
import leo14.untyped.minusName
import leo14.untyped.plusName
import leo14.untyped.timesName
import kotlin.test.Test

class EvalTest {
	@Test
	fun normalization() {
		leo().assertEvalsTo(leo())
		leo("foo"()).assertEvalsTo(leo("foo"()))
		leo("foo"(), "bar"()).assertEvalsTo(leo("bar"("foo"())))
	}

	@Test
	fun primitives() {
		leo(2).assertEvalsTo(leo(2))
		leo("foo").assertEvalsTo(leo("foo"))

		leo(minusName(2)).assertEvalsTo(leo(-2))
		leo(2, minusName()).assertEvalsTo(leo(-2))
		leo(2, plusName(3)).assertEvalsTo(leo(5))
		leo(5, minusName(3)).assertEvalsTo(leo(2))
		leo(2, timesName(3)).assertEvalsTo(leo(6))
	}
}