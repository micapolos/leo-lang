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
			.plusNo("zero" caseTo struct())
			.plusNo("one" caseTo struct())
			.plusYes("two" fieldTo typed())
			.plusNo("three" caseTo struct())
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
			.plusNo("zero" caseTo choice())
			.plusNo("one" caseTo struct())
			.plusYes("two" fieldTo typed())
			.plusNo("three" caseTo struct())
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