package leo.term

import leo.base.assertEqualTo
import leo.oneWord
import leo.twoWord
import kotlin.test.Test

class TemplateTest {
	@Test
	fun scriptTemplateInvoke() {
		template(oneWord apply template(twoWord apply null))
			.invoke(personScript)
			.assertEqualTo(script(oneWord apply script(twoWord apply null)))
	}

	@Test
	fun argumentSelectorTemplateInvoke() {
		selector().template
			.invoke(personScript)
			.assertEqualTo(personScript)
	}


	@Test
	fun rhsSelectorTemplateInvoke() {
		selector(rhsGetter).template
			.invoke(personScript)
			.assertEqualTo(personScript.term.application.argumentOrNull!!)
	}
}