package leo14.type.value

import leo.base.assertEqualTo
import leo14.lambda.id
import leo14.type.fieldTo
import leo14.type.scope
import leo14.type.thunk.with
import leo14.type.type
import kotlin.test.Test

class ResolveTest {
	@Test
	fun resolveGet() {
		scope()
			.with(
				type(
					"punkt" fieldTo type(
						"x" fieldTo type("zero"),
						"y" fieldTo type("jeden"))))
			.value(id<Any>())
			.apply { resolveGet("x").assertEqualTo(scope().with(type("x" fieldTo type("zero"))).value(id())) }
			.apply { resolveGet("y").assertEqualTo(scope().with(type("y" fieldTo type("jeden"))).value(id())) }
			.apply { resolveGet("z").assertEqualTo(null) }
	}
}