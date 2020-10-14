package leo19.typed

import leo.base.assertEqualTo
import leo19.term.nullTerm
import leo19.term.term
import leo19.type.caseTo
import leo19.type.choice
import leo19.type.type
import kotlin.test.Test
import kotlin.test.assertFails

class TypedChoiceTest {
	@Test
	fun simple() {
		emptyTypedChoice
			.plusNo("zero" caseTo type())
			.plusNo("one" caseTo type())
			.plusYes("two" fieldTo typed())
			.plusNo("three" caseTo type())
			.typed
			.assertEqualTo(
				term(2)
					.of(choice(
						"zero" caseTo type(),
						"one" caseTo type(),
						"two" caseTo type(),
						"three" caseTo type())))
	}

	@Test
	fun complex() {
		emptyTypedChoice
			.plusNo("zero" caseTo choice())
			.plusNo("one" caseTo type())
			.plusYes("two" fieldTo typed())
			.plusNo("three" caseTo type())
			.typed
			.assertEqualTo(
				term(term(2), nullTerm)
					.of(choice(
						"zero" caseTo choice(),
						"one" caseTo type(),
						"two" caseTo type(),
						"three" caseTo type())))
	}

	@Test
	fun doubleSelection() {
		assertFails {
			emptyTypedChoice
				.plusYes("zero" fieldTo typed())
				.plusYes("one" fieldTo typed())
		}
	}

	@Test
	fun noSelection() {
		assertFails {
			emptyTypedChoice
				.plusNo("zero" caseTo choice())
				.typed
		}
	}
}