package leo.lab.v2

import leo.base.assertEqualTo
import leo.oneWord
import leo.twoWord
import kotlin.test.Test

class TemplateTest {
	@Test
	fun scriptTemplateInvoke() {
		template(oneWord to template(twoWord to null))
			.invoke(personScript)
			.assertEqualTo(script(oneWord to script(twoWord to null)))
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
			.assertEqualTo(personScript.rhsOrNull!!)
	}
}