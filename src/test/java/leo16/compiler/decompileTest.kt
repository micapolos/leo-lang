package leo16.compiler

import leo.base.assertEqualTo
import leo16.Value
import leo16.invoke
import leo16.names.*
import leo16.value
import kotlin.test.Test

fun Value.testCompilation(type: Type) =
	compile(type)!!.decompile(type).assertEqualTo(this)

class DecompileTest {
	@Test
	fun static() {
		value(_zero()).testCompilation(value(_zero()).typeOrNull!!)
		value(_point(_x(_zero()), _y(_zero())))
			.testCompilation(value(_point(_x(_zero()), _y(_zero()))).typeOrNull!!)
	}

	@Test
	fun choice() {
		val type = value(_choice(_case(_zero()), _case(_one()), _case(_two()))).typeOrNull!!
		value(_zero()).testCompilation(type)
		value(_one()).testCompilation(type)
	}

	@Test
	fun independentChoices() {
		val type =
			_point(
				_x(_choice(_case(_zero()), _case(_one()))),
				_y(_choice(_case(_zero()), _case(_one()))))
				.value
				.typeOrNull!!

		value(_point(_x(_zero()), (_y(_zero())))).testCompilation(type)
		value(_point(_x(_zero()), (_y(_one())))).testCompilation(type)
		value(_point(_x(_one()), (_y(_zero())))).testCompilation(type)
		value(_point(_x(_one()), (_y(_one())))).testCompilation(type)
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

		value(_option(_absent())).testCompilation(type)
		value(_option(_present(_bit(_zero())))).testCompilation(type)
		value(_option(_present(_bit(_one())))).testCompilation(type)
	}
}