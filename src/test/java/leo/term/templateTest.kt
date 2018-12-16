package leo.term

import leo.base.assertEqualTo
import leo.oneWord
import leo.stringWord
import leo.twoWord
import kotlin.test.Test

class TemplateTest {
	@Test
	fun scriptTemplateInvoke() {
		template(term(oneWord apply term(twoWord apply null)))
			.invoke(personScript)
			.assertEqualTo(script(term(oneWord apply term(twoWord apply null))))
	}

	@Test
	fun argumentSelectorTemplateInvoke() {
		term(selector())
			.invoke(personScript)
			.assertEqualTo(personScript)
	}


	@Test
	fun rhsSelectorTemplateInvoke() {
		term(personLastNameSelector)
			.invoke(personScript)
			.assertEqualTo(term(stringWord))
	}
}