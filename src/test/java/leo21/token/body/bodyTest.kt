package leo21.token.body

import leo.base.assertEqualTo
import leo15.dsl.*
import leo21.compiled.apply
import leo21.compiled.compiled
import leo21.compiled.does
import leo21.type.numberType
import kotlin.test.Test

class BodyTest {
	@Test
	fun function() {
		body {
			function {
				number
				does { text("ok") }
			}
		}.assertEqualTo(
			Body(
				emptyModule,
				compiled(numberType does compiled("ok"))))
	}

	@Test
	fun functionApply() {
		body {
			function {
				number
				does { text("ok") }
			}
			apply { number(123) }
		}.assertEqualTo(
			Body(
				emptyModule,
				compiled(numberType does compiled("ok")).apply(compiled(123.0))))
	}


	@Test
	fun defineFunction() {
		body {
			define {
				function {
					number
					does { text("ok") }
				}
			}
		}.assertEqualTo(emptyBody.plus(functionDefinition(numberType, compiled("ok"))))
	}
}