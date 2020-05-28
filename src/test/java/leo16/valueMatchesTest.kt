package leo16

import leo.base.assert
import leo13.base.negate
import leo16.names.*
import kotlin.test.Test

class ValueMatchesTest {
	@Test
	fun struct() {
		value(_x(_zero()), _y(_one()))
			.matches2(value(_x(_zero()), _y(_one())))
			.assert

		value(_x(_one()), _y(_zero()))
			.matches2(value(_x(_zero()), _y(_one())))
			.negate
			.assert
	}

	@Test
	fun any() {
		value(_x(_zero()), _y(_one()))
			.matches2(value(_any()))
			.assert

		value(_x(_zero()), _y(_one()))
			.matches2(value(_x(_any()), _y(_any())))
			.assert

		value(_anything())
			.matches2(value(_anything()))
			.assert

		value(_anything())
			.matches2(value(_exact(_anything())))
			.assert

		value(_zero())
			.matches2(value(_exact(_anything())))
			.negate.assert
	}

	@Test
	fun or() {
		value(_zero())
			.matches2(value(_zero(), _or(_one())))
			.assert

		value(_one())
			.matches2(value(_zero(), _or(_one())))
			.assert

		value(_two())
			.matches2(value(_zero(), _or(_one())))
			.negate.assert

		value(_zero(), _or(_one()))
			.matches2(value(_zero(), _or(_one())))
			.negate.assert

		value(_zero(), _or(_one()))
			.matches2(value(_zero(), _exact(_or(_one()))))
			.assert
	}

	@Test
	fun taking() {
		value(value(_zero()).does(value(_one()).compiled))
			.matches2(value(_taking(_zero())))
			.assert

		value(_taking(_zero()))
			.matches2(value(_taking(_zero())))
			.assert

		value(_taking(_zero()))
			.matches2(value(_exact(_taking(_zero()))))
			.assert
	}
}