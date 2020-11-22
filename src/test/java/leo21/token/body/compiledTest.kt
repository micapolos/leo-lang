package leo21.token.body

import leo.base.assertEqualTo
import leo15.dsl.*
import leo21.compiled.compiled
import leo21.compiled.does
import leo21.definition.functionDefinition
import leo21.type.numberType
import kotlin.test.Test

class CompiledTest {
	@Test
	fun function() {
		compiled {
			function {
				number
				does { text("ok") }
			}
		}.assertEqualTo(compiled(numberType does compiled("ok")))
	}

	@Test
	fun defineFunction() {
		compiled {
			define {
				function {
					number
					does { text("ok") }
				}
			}
		}.assertEqualTo(emptyBody.plus(functionDefinition(numberType, compiled("ok"))).wrapCompiled)
	}
}