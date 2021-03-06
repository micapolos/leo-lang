package leo16

import leo.base.assertEqualTo
import leo15.lambda.choiceTerm
import leo15.lambda.idTerm
import leo15.lambda.valueTerm
import leo15.plus
import leo16.names.*
import kotlin.test.Test

class ValueTermTest {
	@Test
	fun empty() {
		value()
			.termOrNull(value())
			.assertEqualTo(idTerm)
	}

	@Test
	fun native() {
		value(_any(_native()))
			.termOrNull("foo".nativeValue)
			.assertEqualTo("foo".valueTerm)
	}

	@Test
	fun anything() {
		value(_anything())
			.termOrNull(value(_zero()))
			.assertEqualTo(value(_zero()).valueTerm)
	}

	@Test
	fun link() {
		value(
			_any(_native()),
			_to(_any(_native())))
			.termOrNull("foo".nativeValue.plus(_to("bar".nativeValue)))
			.assertEqualTo("foo".valueTerm.plus("bar".valueTerm))
	}

	@Test
	fun alternative() {
		value(_x(_anything()), _or(_y(_anything())))
			.termOrNull(value(_x(_zero())))
			.assertEqualTo(choiceTerm(2, 1, value(_zero()).valueTerm))

		value(_x(_anything()), _or(_y(_anything())))
			.termOrNull(value(_y(_zero())))
			.assertEqualTo(choiceTerm(2, 0, value(_zero()).valueTerm))
	}

	@Test
	fun quote() {
		value(_quote(_anything()))
			.termOrNull(value(_anything()))
			.assertEqualTo(idTerm)
	}
}