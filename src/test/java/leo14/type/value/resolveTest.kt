package leo14.type.value

import leo.base.assertEqualTo
import leo14.lambda.id
import leo14.polishDictionary
import leo14.type.fieldTo
import leo14.type.scope
import leo14.type.thunk.with
import leo14.type.type
import kotlin.test.Test

class ResolveTest {
	@Test
	fun resolveLast() {
		scope()
			.with(
				type(
					"punkt" fieldTo type(
						"x" fieldTo type("zero"),
						"y" fieldTo type("jeden"))))
			.value(id<Any>())
			.resolveLast(polishDictionary)
			.assertEqualTo(
				scope()
					.with(
						type(
							polishDictionary.last fieldTo type(
								"y" fieldTo type("jeden"))))
					.value(id())
			)
	}

	@Test
	fun resolvePrevious() {
		scope()
			.with(
				type(
					"punkt" fieldTo type(
						"x" fieldTo type("zero"),
						"y" fieldTo type("jeden"),
						"z" fieldTo type("dwa"))))
			.value(id<Any>())
			.resolvePrevious(polishDictionary)
			.assertEqualTo(
				scope()
					.with(
						type(
							polishDictionary.previous fieldTo type(
								"x" fieldTo type("zero"),
								"y" fieldTo type("jeden"))))
					.value(id())
			)
	}
}