package leo32.interpreter

import leo.base.assertEqualTo
import leo.base.empty
import leo32.runtime.to
import leo32.runtime.term
import kotlin.test.Test

class TypeResolverTest {
	@Test
	fun resolve() {
		val typeResolver = empty.typeResolver
			.put(term("bit" to term("zero")), term("any" to term("bit")).type)
			.put(term("bit" to term("one")), term("any" to term("bit")).type)

		typeResolver
			.resolve(term("bit" to term("zero")))
			.assertEqualTo(
				term("any" to term("bit")).type)

		typeResolver
			.resolve(term("bit" to term("one")))
			.assertEqualTo(term("any" to term("bit")).type)

		typeResolver
			.resolve(term("bit" to term("two")))
			.assertEqualTo(term("bit" to term("two")).type)

		typeResolver
			.resolve(
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
				term(
					"byte" to term(
						"the" to term("any" to term("bit")),
						"the" to term("any" to term("bit")),
						"the" to term("any" to term("bit")),
						"the" to term("any" to term("bit")),
						"the" to term("any" to term("bit")),
						"the" to term("any" to term("bit")),
						"the" to term("any" to term("bit")),
						"the" to term("any" to term("bit")))).type)

	}
}