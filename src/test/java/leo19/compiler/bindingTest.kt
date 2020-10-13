package leo19.compiler

import leo.base.assertEqualTo
import leo.base.assertNull
import leo19.term.get
import leo19.term.invoke
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.case
import leo19.type.choice
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
	fun structBinding_match0() {
		binding(
			struct(
				"zero" fieldTo choice("foo".case),
				"one" fieldTo choice("bar".case)).structOrNull!!)
			.resolveOrNull(typed("zero"), 64)
			.assertEqualTo(
				term(variable(64))
					.get(term(0))
					.of(struct("zero" fieldTo choice("foo".case))))
	}

	@Test
	fun structBinding_match1() {
		binding(
			struct(
				"zero" fieldTo choice("foo".case),
				"one" fieldTo choice("bar".case)).structOrNull!!)
			.resolveOrNull(typed("one"), 64)
			.assertEqualTo(
				term(variable(64))
					.get(term(1))
					.of(struct("one" fieldTo choice("bar".case))))
	}

	@Test
	fun structBinding_mismatch() {
		binding(
			struct(
				"zero" fieldTo struct(),
				"one" fieldTo struct()).structOrNull!!)
			.resolveOrNull(typed("two"), 64)
			.assertNull
	}
}
