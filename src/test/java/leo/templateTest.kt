package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class TemplateTest {
	@Test
	fun string() {
		template(term(oneWord))
			.string
			.assertEqualTo("template term identifier word one")
	}

	@Test
	fun parse_literal() {
		script(term(oneWord))
			.parseTemplate(pattern(term(personWord)))
			.assertEqualTo(template(term(oneWord)))
	}

	@Test
	fun parse_list() {
		script(
			term(
				nameWord fieldTo term(stringWord),
				ageWord fieldTo term(numberWord)))
			.parseTemplate(pattern(term(personWord)))
			.assertEqualTo(
				template(
					term(
						nameWord fieldTo term(stringWord),
						ageWord fieldTo term(numberWord))))
	}

	@Test
	fun parse_selector() {
		script(
			term(
				nameWord fieldTo term(
					oneWord fieldTo term(thisWord))))
			.parseTemplate(
				pattern(
					term(oneWord fieldTo term(numberWord))))
			.assertEqualTo(
				template(
					term(
						nameWord fieldTo term(selector(oneWord)))))
	}

	@Test
	fun bodyInvoke() {
		template(
			term(
				thisWord fieldTo term(selector(itWord)),
				timesWord fieldTo term(selector(plusWord))))
			.invoke(
				term(
					itWord fieldTo term(1),
					plusWord fieldTo term(2)))
			.assertEqualTo(
				term(
					thisWord fieldTo term(1),
					timesWord fieldTo term(2)))
	}
}