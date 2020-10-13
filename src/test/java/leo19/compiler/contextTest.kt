package leo19.compiler

import leo.base.assertEqualTo
import leo13.stack
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.caseTo
import leo19.type.choice
import leo19.type.fieldTo
import leo19.type.struct
import kotlin.test.Test

class ContextTest {
	@Test
	fun defineChoice_empty() {
		emptyContext
			.defineChoice(choice())
			.assertEqualTo(emptyContext)
	}

	@Test
	fun defineChoice_simple() {
		emptyContext
			.defineChoice(choice("zero" caseTo struct()))
			.assertEqualTo(
				Context(
					stack(term(0)),
					resolver(
						binding(
							Arrow(
								struct("zero" fieldTo struct()),
								choice("zero" caseTo struct()))))))
	}

	@Test
	fun defineChoice_complex() {
		emptyContext
			.defineChoice(choice("zero" caseTo choice()))
			.assertEqualTo(
				Context(
					stack(term(term(0), term(variable(0)))),
					resolver(
						binding(
							Arrow(
								struct("zero" fieldTo choice()),
								choice("zero" caseTo choice()))))))
	}

	@Test
	fun defineChoice_deep() {
		val type = struct("bit" fieldTo choice("zero" caseTo struct()))
		emptyContext
			.defineChoice(type)
			.assertEqualTo(
				Context(
					stack(term(0)),
					resolver(
						binding(
							Arrow(
								struct("bit" fieldTo struct("zero" fieldTo struct())),
								type)))))
	}

	@Test
	fun defineChoice_twoCases() {
		val type = choice("zero" caseTo struct(), "one" caseTo struct())
		emptyContext
			.defineChoice(type)
			.assertEqualTo(
				Context(
					stack(
						term(0),
						term(1)),
					resolver(
						binding(
							Arrow(
								struct("zero" fieldTo struct()),
								type)),
						binding(
							Arrow(
								struct("one" fieldTo struct()),
								type)))))
	}
}