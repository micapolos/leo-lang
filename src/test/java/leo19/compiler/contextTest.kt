package leo19.compiler

import leo.base.assertEqualTo
import leo13.stack
import leo19.term.function
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.caseTo
import leo19.type.choice
import leo19.type.fieldTo
import leo19.type.type
import leo19.typed.of
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
			.defineChoice(choice("zero" caseTo type()))
			.assertEqualTo(
				Context(
					resolver(
						functionBinding(
							Arrow(
								type("zero" fieldTo type()),
								choice("zero" caseTo type())))),
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
								type("zero" fieldTo choice()),
								choice("zero" caseTo choice())))),
					stack(term(term(0), term(variable(0))))))
	}

	@Test
	fun defineChoice_deep() {
		val type = type("bit" fieldTo choice("zero" caseTo type()))
		emptyContext
			.defineChoice(type)
			.assertEqualTo(
				Context(
					resolver(
						functionBinding(
							Arrow(
								type("bit" fieldTo type("zero" fieldTo type())),
								type))),
					stack(term(0))))
	}

	@Test
	fun defineChoice_twoCases() {
		val type = choice("zero" caseTo type(), "one" caseTo type())
		emptyContext
			.defineChoice(type)
			.assertEqualTo(
				Context(
					resolver(
						functionBinding(
							Arrow(
								type("zero" fieldTo type()),
								type)),
						functionBinding(
							Arrow(
								type("one" fieldTo type()),
								type))),
					stack(
						term(0),
						term(1))))
	}

	@Test
	fun defineIs() {
		emptyContext
			.defineIs(type("zero"), term(variable(128)).of(type("one")))
			.assertEqualTo(
				Context(
					emptyResolver.plus(constantBinding(Arrow(type("zero"), type("one")))),
					stack(term(variable(128)))))
	}

	@Test
	fun defineGives() {
		emptyContext
			.defineGives(type("zero"), term(variable(128)).of(type("one")))
			.assertEqualTo(
				Context(
					emptyResolver.plus(functionBinding(Arrow(type("zero"), type("one")))),
					stack(term(function(term(variable(128)))))))
	}
}
