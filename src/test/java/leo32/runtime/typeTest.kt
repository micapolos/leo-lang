package leo32.runtime

import leo.base.assertEqualTo
import kotlin.test.Test

class TypeTest {
	@Test
	fun termType() {
		term()
			.type
			.assertEqualTo(type())

		term("zero")
			.type
			.assertEqualTo(type(either("zero")))

		term(
			"zero" to term(),
			"one" to term())
			.type
			.assertEqualTo(
				type(
					either("zero"),
					either("one")))

		term("either" to term("zero"))
			.type
			.assertEqualTo(type(either("zero")))

		term(
			"either" to term(
				"zero" to term(),
				"one" to term()))
			.type
			.assertEqualTo(
				type(
					either(
						"zero" to type(),
						"one" to type())))
	}

	@Test
	fun typeTerm() {
		type().term.assertEqualTo(term())

		type(either("zero"))
			.term
			.assertEqualTo(term("either" to term("zero")))

		type(either("zero" to type(), "one" to type()))
			.term
			.assertEqualTo(term("either" to term("zero" to term(), "one" to term())))

		type(either("zero"), either("one"))
			.term
			.assertEqualTo(term("either" to term("zero"), "either" to term("one")))
	}

	@Test
	fun typeInvoke() {
		type().invoke(term()).assertEqualTo(type())
		type(either("zero")).invoke(term("zero")).assertEqualTo(type())
		type(either("zero")).invoke(term("one")).assertEqualTo(null)
		type(either("zero" to type(), "one" to type())).invoke(term("zero")).assertEqualTo(type())
		type(either("zero" to type(), "one" to type())).invoke(term("one")).assertEqualTo(type())
		type(either("zero" to type(), "one" to type())).invoke(term("two")).assertEqualTo(null)
		type(either("zero"), either("one")).invoke(term("zero" to term())).assertEqualTo(type(either("one")))
		type(either("zero"), either("one")).invoke(term("one" to term())).assertEqualTo(null)
		type(either("zero"), either("one")).invoke(term("zero" to term(), "one" to term())).assertEqualTo(type())
		type(either("zero"), either("one")).invoke(term("zero" to term(), "zero" to term())).assertEqualTo(null)
	}

	@Test
	fun bitTypeInvoke() {
		val bitType = type(either("bit" to type(either("zero" to type(), "one" to type()))))

		bitType.match(term("bit" to term("zero"))).assertEqualTo(true)
		bitType.match(term("bit" to term("one"))).assertEqualTo(true)
		bitType.match(term("bit" to term("two"))).assertEqualTo(false)
	}

	@Test
	fun i2TypeInvoke() {
		val i2Type = type(
			either("i2" to type(
				either("bit" to type(
					either(
						"zero" to type(),
						"one" to type()))),
				either("bit" to type(
					either(
						"zero" to type(),
						"one" to type()))))))

		i2Type.match(term("i2" to term("bit" to term("zero"), "bit" to term("zero")))).assertEqualTo(true)
		i2Type.match(term("i2" to term("bit" to term("zero"), "bit" to term("one")))).assertEqualTo(true)
		i2Type.match(term("i2" to term("bit" to term("one"), "bit" to term("zero")))).assertEqualTo(true)
		i2Type.match(term("i2" to term("bit" to term("one"), "bit" to term("one")))).assertEqualTo(true)
		i2Type.match(term("i2" to term("bit" to term("zero")))).assertEqualTo(false)
	}
}