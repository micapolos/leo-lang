package leo16

import leo.base.assert
import leo13.base.negate
import leo16.names.*
import kotlin.test.Test

class ValueMatchesTest {
	@Test
	fun struct() {
		value(_x(_zero()), _y(_one()))
			.matches(value(_x(_zero()), _y(_one())))
			.assert

		value(_x(_one()), _y(_zero()))
			.matches(value(_x(_zero()), _y(_one())))
			.negate
			.assert
	}

	@Test
	fun anything() {
		value(_anything())
			.matches(value(_x(_zero()), _y(_one())))
			.assert

		value(_x(_anything()), _y(_anything()))
			.matches(value(_x(_zero()), _y(_one())))
			.assert

		value(_anything())
			.matches(value(_anything()))
			.assert

		value(_meta(_anything()))
			.matches(value(_anything()))
			.assert

		value(_meta(_anything()))
			.matches(value(_zero()))
			.negate.assert
	}

	@Test
	fun or() {
		value(_zero(), _or(_one()))
			.matches(value(_zero()))
			.assert

		value(_zero(), _or(_one()))
			.matches(value(_one()))
			.assert

		value(_zero(), _or(_one()))
			.matches(value(_two()))
			.negate.assert

		value(_zero(), _or(_one()))
			.matches(value(_zero(), _or(_one())))
			.negate.assert

		value(_zero(), _meta(_or(_one())))
			.matches(value(_zero(), _or(_one())))
			.assert
	}

	@Test
	fun taking() {
		value(_function(_zero()))
			.matches(value(value(_zero()).does(value(_one()).compiled)))
			.assert

		value(_function(_zero()))
			.matches(value(_function(_zero())))
			.assert

		value(_meta(_function(_zero())))
			.matches(value(_function(_zero())))
			.assert
	}

	@Test
	fun quote() {
		value(_quote(_anything(), _or(_anything())))
			.matches(value(_anything(), _or(_anything())))
			.assert
	}

	@Test
	fun repeating() {
		value(_natural(_zero(), _or(_next(_repeating(_natural())))))
			.matches(value(_natural(_zero())))
			.assert

		value(_natural(_zero(), _or(_next(_repeating(_natural())))))
			.matches(value(_natural(_next(_natural(_zero())))))
			.assert
	}
}