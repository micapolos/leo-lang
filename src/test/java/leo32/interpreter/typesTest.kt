package leo32.interpreter

import leo.base.assertEqualTo
import leo.base.empty
import leo32.runtime.to
import leo32.runtime.term
import kotlin.test.Test

class TypesTest {
	@Test
	fun resolve() {
		val types = empty.types
			.put(term("bit" to term("zero")), type(term("any" to term("bit"))))
			.put(term("bit" to term("one")), type(term("any" to term("bit"))))

		types
			.typeOf(term("bit"))
			.assertEqualTo(type(term("bit")))

		types
			.typeOf(term("bit" to term("zero")))
			.assertEqualTo(type(term("any" to term("bit"))))

		types
			.typeOf(term("bit" to term("one")))
			.assertEqualTo(type(term("any" to term("bit"))))

		types
			.typeOf(term("the" to term("bit" to term("one"))))
			.assertEqualTo(type(term("the" to term("any" to term("bit")))))

		types
			.typeOf(term("bit" to term("two")))
			.assertEqualTo(type(term("bit" to term("two"))))

		types
			.typeOf(
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
					term(
						"byte" to term(
							"the" to term("any" to term("bit")),
							"the" to term("any" to term("bit")),
							"the" to term("any" to term("bit")),
							"the" to term("any" to term("bit")),
							"the" to term("any" to term("bit")),
							"the" to term("any" to term("bit")),
							"the" to term("any" to term("bit")),
							"the" to term("any" to term("bit"))))))

	}
}