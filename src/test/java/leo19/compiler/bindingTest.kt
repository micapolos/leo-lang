package leo19.compiler

import leo.base.assertEqualTo
import leo.base.assertNull
import leo19.term.invoke
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.fieldTo
import leo19.type.struct
import leo19.type.structOrNull
import leo19.typed.of
import leo19.typed.typed
import kotlin.test.Test

class BindingTest {
	@Test
	fun arrowBinding_match() {
		binding(Arrow(struct("zero"), struct("one")))
			.resolveOrNull(term(variable(128)).of(struct("zero")), 64)
			.assertEqualTo(term(variable(64)).invoke(term(variable(128))).of(struct("one")))
	}

	@Test
	fun arrowBinding_mismatch() {
		binding(Arrow(struct("zero"), struct("one")))
			.resolveOrNull(term(variable(128)).of(struct("one")), 64)
			.assertNull
	}

	@Test
	fun structBinding_match() {
		binding(
			struct(
				"zero" fieldTo struct(),
				"one" fieldTo struct()).structOrNull!!)
			.resolveOrNull(typed("given"), 64)
			.assertEqualTo(
				term(variable(64))
					.of(struct("given" fieldTo struct(
						"zero" fieldTo struct(),
						"one" fieldTo struct()))))
	}

	@Test
	fun structBinding_mismatch() {
		binding(
			struct(
				"zero" fieldTo struct(),
				"one" fieldTo struct()).structOrNull!!)
			.resolveOrNull(typed("foo"), 64)
			.assertNull
	}
}
