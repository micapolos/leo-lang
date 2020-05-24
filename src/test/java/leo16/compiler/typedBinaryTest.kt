package leo16.compiler

import leo.base.assertEqualTo
import leo16.invoke
import leo16.names.*
import leo16.value
import kotlin.test.Test

class TypedBinaryTest {
	@Test
	fun staticValues() {
		value(_zero())
			.binary(value(_zero()).typeOrNull!!)
			.assertEqualTo(emptyBinary)

		value(_point(_x(_zero()), _y(_zero())))
			.binary(value(_point(_x(_zero()), _y(_zero()))).typeOrNull!!)
			.assertEqualTo(emptyBinary)
	}

	@Test
	fun choice2() {
		val type = value(_choice(_case(_zero()), _case(_one()))).typeOrNull!!

		value(_zero())
			.binary(type)
			.assertEqualTo(binary(0))

		value(_one())
			.binary(type)
			.assertEqualTo(binary(1))
	}

	@Test
	fun choice3() {
		val type = value(_choice(_case(_zero()), _case(_one()), _case(_two()))).typeOrNull!!

		value(_zero())
			.binary(type)
			.assertEqualTo(binary(0, 0))

		value(_one())
			.binary(type)
			.assertEqualTo(binary(1, 0))

		value(_two())
			.binary(type)
			.assertEqualTo(binary(0, 1))
	}

	@Test
	fun choice5() {
		val type = value(_choice(
			_case(_zero()),
			_case(_one()),
			_case(_two()),
			_case(_three()),
			_case(_four()))).typeOrNull!!

		value(_zero())
			.binary(type)
			.assertEqualTo(binary(0, 0, 0))

		value(_one())
			.binary(type)
			.assertEqualTo(binary(1, 0, 0))

		value(_two())
			.binary(type)
			.assertEqualTo(binary(0, 1, 0))

		value(_three())
			.binary(type)
			.assertEqualTo(binary(1, 1, 0))

		value(_four())
			.binary(type)
			.assertEqualTo(binary(0, 0, 1))
	}

	@Test
	fun twoChoices() {
		val type =
			_point(
				_x(_choice(_case(_zero()), _case(_one()))),
				_y(_choice(_case(_zero()), _case(_one()))))
				.value
				.typeOrNull!!

		value(_point(_x(_zero()), (_y(_one()))))
			.binary(type)
			.assertEqualTo(binary(0, 1))
	}

	@Test
	fun deepChoices() {
		val type =
			_option(
				_choice(
					_case(_absent()),
					_case(_present(_bit(_choice(_case(_zero()), _case(_one())))))))
				.value
				.typeOrNull!!

		value(_option(_absent()))
			.binary(type)
			.assertEqualTo(binary(0, 1))
	}
}