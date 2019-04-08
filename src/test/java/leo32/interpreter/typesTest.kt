package leo32.interpreter

import leo.base.assertEqualTo
import leo.base.empty
import leo32.runtime.term
import leo32.runtime.to
import kotlin.test.Test

class TypesTest {
	@Test
	fun resolve() {
		val kinds = empty.types
			.put(term("bit" to term("zero")), type("any" to type("bit")))
			.put(term("bit" to term("one")), type("any" to type("bit")))

		kinds
			.at(term("bit"))
			.assertEqualTo(type("bit"))

		kinds
			.at(term("bit" to term("zero")))
			.assertEqualTo(type("any" to type("bit")))

		kinds
			.at(term("bit" to term("one")))
			.assertEqualTo(type("any" to type("bit")))

		kinds
			.at(term("the" to term("bit" to term("one"))))
			.assertEqualTo(type("the" to type("any" to type("bit"))))

		kinds
			.at(term("bit" to term("two")))
			.assertEqualTo(type("bit" to type("two")))

		kinds
			.at(
				term(
					"byte" to term(
						"the" to term("bit" to term("zero")),
						"the" to term("bit" to term("zero")),
						"the" to term("bit" to term("zero")),
						"the" to term("bit" to term("one")),
						"the" to term("bit" to term("zero")),
						"the" to term("bit" to term("zero")),
						"the" to term("bit" to term("zero")),
						"the" to term("bit" to term("zero")))))
			.assertEqualTo(
				type(
					"byte" to type(
						"the" to type("any" to type("bit")),
						"the" to type("any" to type("bit")),
						"the" to type("any" to type("bit")),
						"the" to type("any" to type("bit")),
						"the" to type("any" to type("bit")),
						"the" to type("any" to type("bit")),
						"the" to type("any" to type("bit")),
						"the" to type("any" to type("bit")))))
	}
}