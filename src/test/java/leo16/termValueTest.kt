package leo16

import leo.base.assertEqualTo
import leo15.lambda.valueTerm
import leo16.names.*
import org.junit.Test

fun Value.assertConverts(value: Value) =
	this.value(this.termOrNull(value)!!).assertEqualTo(value)

class TermValueTest {
	@Test
	fun empty() {
		value()
			.assertConverts(value())
	}

	@Test
	fun native() {
		value(_any(_native()))
			.assertConverts("foo".nativeValue)
	}

	@Test
	fun anything() {
		value(_anything())
			.assertConverts(value(_zero()))
	}

	@Test
	fun link() {
		value(
			_any(_native()),
			_to(_any(_native())))
			.assertConverts("foo".nativeValue.plus(_to("bar".nativeValue)))
	}

	@Test
	fun alternative() {
		value(_x(_anything()), _or(_y(_anything())))
			.assertConverts(value(_x(_zero())))

		value(_x(_anything()), _or(_y(_anything())))
			.assertConverts(value(_y(_zero())))
	}

	@Test
	fun quote() {
		value(_quote(_anything()))
			.assertConverts(value(_anything()))
	}
}