package leo32.term

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class TemplateResolverTest {
	@Test
	fun resolve() {
		empty.templateResolver
			.put(
				term(
					"any" fieldTo term("bit"),
					"and" fieldTo term("any" fieldTo term("bit"))).type,
				template(selector()))
			.assertEqualTo(null)
	}
}