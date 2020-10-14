package leo19.compiler

import leo.base.assertEqualTo
import leo13.stack
import leo19.term.function
import leo19.term.nullTerm
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.caseTo
import leo19.type.choice
import leo19.type.fieldTo
import leo19.type.struct
import leo19.typed.of
import leo19.typed.typed
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
					resolver(
						functionBinding(
							Arrow(
								struct("zero" fieldTo struct()),
								choice("zero" caseTo struct())))),
					stack(term(0))))
	}

	@Test
	fun defineChoice_complex() {
		emptyContext
			.defineChoice(choice("zero" caseTo choice()))
			.assertEqualTo(
				Context(
					resolver(
						functionBinding(
							Arrow(
								struct("zero" fieldTo choice()),
								choice("zero" caseTo choice())))),
					stack(term(term(0), term(variable(0))))))
	}

	@Test
	fun defineChoice_deep() {
		val type = struct("bit" fieldTo choice("zero" caseTo struct()))
		emptyContext
			.defineChoice(type)
			.assertEqualTo(
				Context(
					resolver(
						functionBinding(
							Arrow(
								struct("bit" fieldTo struct("zero" fieldTo struct())),
								type))),
					stack(term(0))))
	}

	@Test
	fun defineChoice_twoCases() {
		val type = choice("zero" caseTo struct(), "one" caseTo struct())
		emptyContext
			.defineChoice(type)
			.assertEqualTo(
				Context(
					resolver(
						functionBinding(
							Arrow(
								struct("zero" fieldTo struct()),
								type)),
						functionBinding(
							Arrow(
								struct("one" fieldTo struct()),
								type))),
					stack(
						term(0),
						term(1))))
	}

	@Test
	fun defineIs() {
		emptyContext
			.defineIs(struct("zero"), term(variable(128)).of(struct("one")))
			.assertEqualTo(
				Context(
					emptyResolver.plus(constantBinding(Arrow(struct("zero"), struct("one")))),
					stack(term(variable(128)))))
	}

	@Test
	fun defineGives() {
		emptyContext
			.defineGives(struct("zero"), term(variable(128)).of(struct("one")))
			.assertEqualTo(
				Context(
					emptyResolver.plus(functionBinding(Arrow(struct("zero"), struct("one")))),
					stack(term(function(term(variable(128)))))))
	}
}
