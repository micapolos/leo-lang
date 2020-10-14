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
import leo19.type.type
import leo19.type.structOrNull
import leo19.typed.of
import leo19.typed.typed
import kotlin.test.Test

class BindingTest {
	@Test
	fun constantBinding_match() {
		constantBinding(Arrow(type("zero"), type("one")))
			.resolveOrNull(term(variable(128)).of(type("zero")), 64)
			.assertEqualTo(term(variable(64)).of(type("one")))
	}

	@Test
	fun constantBinding_mismatch() {
		constantBinding(Arrow(type("zero"), type("one")))
			.resolveOrNull(term(variable(128)).of(type("one")), 64)
			.assertNull
	}

	@Test
	fun functionBinding_match() {
		functionBinding(Arrow(type("zero"), type("one")))
			.resolveOrNull(term(variable(128)).of(type("zero")), 64)
			.assertEqualTo(term(variable(64)).invoke(term(variable(128))).of(type("one")))
	}

	@Test
	fun functionBinding_mismatch() {
		functionBinding(Arrow(type("zero"), type("one")))
			.resolveOrNull(term(variable(128)).of(type("one")), 64)
			.assertNull
	}

	@Test
	fun structBinding_match0() {
		binding(
			type(
				"zero" fieldTo choice("foo".case),
				"one" fieldTo choice("bar".case)).structOrNull!!)
			.resolveOrNull(typed("zero"), 64)
			.assertEqualTo(
				term(variable(64))
					.get(term(0))
					.of(type("zero" fieldTo choice("foo".case))))
	}

	@Test
	fun structBinding_match1() {
		binding(
			type(
				"zero" fieldTo choice("foo".case),
				"one" fieldTo choice("bar".case)).structOrNull!!)
			.resolveOrNull(typed("one"), 64)
			.assertEqualTo(
				term(variable(64))
					.get(term(1))
					.of(type("one" fieldTo choice("bar".case))))
	}

	@Test
	fun structBinding_mismatch() {
		binding(
			type(
				"zero" fieldTo type(),
				"one" fieldTo type()).structOrNull!!)
			.resolveOrNull(typed("two"), 64)
			.assertNull
	}
}
