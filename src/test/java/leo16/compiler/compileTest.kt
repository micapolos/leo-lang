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
			.compile(value(_zero()).typeOrNull!!)!!
			.assertEqualTo(compiled())

		value(_point(_x(_zero()), _y(_zero())))
			.compile(value(_point(_x(_zero()), _y(_zero()))).typeOrNull!!)!!
			.assertEqualTo(compiled())
	}

	@Test
	fun choice() {
		val type = value(_choice(_case(_zero()), _case(_one()), _case(_two()))).typeOrNull!!

		value(_zero())
			.compile(type)!!
			.assertEqualTo(compiled(0.primitive))

		value(_one())
			.compile(type)!!
			.assertEqualTo(compiled(1.primitive))

		value(_two())
			.compile(type)!!
			.assertEqualTo(compiled(2.primitive))
	}

	@Test
	fun independentChoices() {
		val type =
			_point(
				_x(_choice(_case(_zero()), _case(_one()))),
				_y(_choice(_case(_zero()), _case(_one()))))
				.value
				.typeOrNull!!

		value(_point(_x(_zero()), (_y(_one()))))
			.compile(type)!!
			.assertEqualTo(compiled(0.primitive, 1.primitive))
	}

	@Test
	fun dependentChoices() {
		val type =
			_option(
				_choice(
					_case(_absent()),
					_case(_present(_bit(_choice(_case(_zero()), _case(_one())))))))
				.value
				.typeOrNull!!

		value(_option(_absent()))
			.compile(type)!!
			.assertEqualTo(compiled(0.primitive).plusZeros(4))

		value(_option(_present(_bit(_zero()))))
			.compile(type)!!
			.assertEqualTo(compiled(1.primitive, 0.primitive))

		value(_option(_present(_bit(_one()))))
			.compile(type)!!
			.assertEqualTo(compiled(1.primitive, 1.primitive))
	}
}