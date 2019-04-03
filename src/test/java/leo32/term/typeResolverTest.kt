package leo32.term

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class TypeResolverTest {
	@Test
	fun resolve() {
		val typeResolver = empty.typeResolver
			.put(term("bit" fieldTo term("zero")), term("any" fieldTo term("bit")).type)
			.put(term("bit" fieldTo term("one")), term("any" fieldTo term("bit")).type)

		typeResolver
			.resolve(term("bit" fieldTo term("zero")))
			.assertEqualTo(term("any" fieldTo term("bit")).type)

		typeResolver
			.resolve(term("bit" fieldTo term("one")))
			.assertEqualTo(term("any" fieldTo term("bit")).type)

		typeResolver
			.resolve(term("bit" fieldTo term("two")))
			.assertEqualTo(term("bit" fieldTo term("two")).type)

		typeResolver
			.resolve(
				term(
					"byte" fieldTo term(
						"the" fieldTo term("bit" fieldTo term("zero")),
						"the" fieldTo term("bit" fieldTo term("zero")),
						"the" fieldTo term("bit" fieldTo term("zero")),
						"the" fieldTo term("bit" fieldTo term("one")),
						"the" fieldTo term("bit" fieldTo term("zero")),
						"the" fieldTo term("bit" fieldTo term("zero")),
						"the" fieldTo term("bit" fieldTo term("zero")),
						"the" fieldTo term("bit" fieldTo term("zero")))))
			.assertEqualTo(
				term(
					"byte" fieldTo term(
						"the" fieldTo term("any" fieldTo term("bit")),
						"the" fieldTo term("any" fieldTo term("bit")),
						"the" fieldTo term("any" fieldTo term("bit")),
						"the" fieldTo term("any" fieldTo term("bit")),
						"the" fieldTo term("any" fieldTo term("bit")),
						"the" fieldTo term("any" fieldTo term("bit")),
						"the" fieldTo term("any" fieldTo term("bit")),
						"the" fieldTo term("any" fieldTo term("bit")))).type)

	}
}