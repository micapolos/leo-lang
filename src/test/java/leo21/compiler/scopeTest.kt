package leo21.compiler

import leo.base.assertEqualTo
import leo21.term.plus
import leo21.type.lineTo
import leo21.type.plus
import leo21.type.stringType
import leo21.typed.of
import leo21.typed.reference
import leo21.typed.resolve
import leo21.typed.term1
import leo21.typed.term2
import kotlin.test.Test

class ScopeTest {
	@Test
	fun resolveStringPlus() {
		emptyBindings
			.resolve(
				term1.plus(term2)
					.of(stringType.plus("plus" lineTo stringType)))
			.assertEqualTo(
				term1.plus(term2)
					.of(stringType.plus("plus" lineTo stringType))
					.reference { resolve })
	}
}