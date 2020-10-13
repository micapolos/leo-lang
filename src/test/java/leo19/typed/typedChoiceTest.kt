package leo19.typed

import leo.base.assertEqualTo
import leo19.term.nullTerm
import leo19.term.term
import leo19.type.caseTo
import leo19.type.choice
import leo19.type.struct
import kotlin.test.Test
import kotlin.test.assertFails

class TypedChoiceTest {
	@Test
	fun simple() {
		emptyTypedChoice
			.plusIgnored("zero" caseTo struct())
			.plusIgnored("one" caseTo struct())
			.plusSelected("two" fieldTo typed())
			.plusIgnored("three" caseTo struct())
			.typed
			.assertEqualTo(
				term(2)
					.of(choice(
						"zero" caseTo struct(),
						"one" caseTo struct(),
						"two" caseTo struct(),
						"three" caseTo struct())))
	}

	@Test
	fun complex() {
		emptyTypedChoice
			.plusIgnored("zero" caseTo choice())
			.plusIgnored("one" caseTo struct())
			.plusSelected("two" fieldTo typed())
			.plusIgnored("three" caseTo struct())
			.typed
			.assertEqualTo(
				term(term(2), nullTerm)
					.of(choice(
						"zero" caseTo choice(),
						"one" caseTo struct(),
						"two" caseTo struct(),
						"three" caseTo struct())))
	}

	@Test
	fun doubleSelection() {
		assertFails {
			emptyTypedChoice
				.plusSelected("zero" fieldTo typed())
				.plusSelected("one" fieldTo typed())
		}
	}

	@Test
	fun noSelection() {
		assertFails {
			emptyTypedChoice
				.plusIgnored("zero" caseTo choice())
				.typed
		}
	}
}