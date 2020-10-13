package leo19.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo19.term.function
import leo19.term.invoke
import leo19.term.nullTerm
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.bitType
import leo19.type.booleanType
import leo19.type.caseTo
import leo19.type.choice
import leo19.type.fieldTo
import leo19.type.plus
import leo19.type.struct
import leo19.typed.castTo
import leo19.typed.fieldTo
import leo19.typed.getOrNull
import leo19.typed.of
import leo19.typed.typed
import kotlin.test.Test

class CompilerTest {
	@Test
	fun empty() {
		script()
			.typed
			.assertEqualTo(typed())
	}

	@Test
	fun name() {
		script("foo" lineTo script())
			.typed
			.assertEqualTo(typed("foo" fieldTo typed()))
	}

	@Test
	fun field() {
		script("foo" lineTo script("bar"))
			.typed
			.assertEqualTo(typed("foo" fieldTo typed("bar" fieldTo typed())))
	}

	@Test
	fun fields() {
		script(
			"x" lineTo script("zero"),
			"y" lineTo script("one"))
			.typed
			.assertEqualTo(
				typed(
					"x" fieldTo typed("zero" fieldTo typed()),
					"y" fieldTo typed("one" fieldTo typed())))
	}

	@Test
	fun substruct() {
		script(
			"point" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")))
			.typed
			.assertEqualTo(
				typed(
					"point" fieldTo typed(
						"x" fieldTo typed("zero" fieldTo typed()),
						"y" fieldTo typed("one" fieldTo typed()))))
	}

	@Test
	fun get0() {
		script(
			"point" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")),
			"x" lineTo script())
			.typed
			.assertEqualTo(
				typed(
					"point" fieldTo typed(
						"x" fieldTo typed("zero" fieldTo typed()),
						"y" fieldTo typed("one" fieldTo typed()))).getOrNull("x"))
	}

	@Test
	fun get1() {
		script(
			"point" lineTo script(
				"x" lineTo script("zero"),
				"y" lineTo script("one")),
			"y" lineTo script())
			.typed
			.assertEqualTo(
				typed(
					"point" fieldTo typed(
						"x" fieldTo typed("zero" fieldTo typed()),
						"y" fieldTo typed("one" fieldTo typed()))).getOrNull("y"))

	}

	@Test
	fun make() {
		script(
			"x" lineTo script("zero"),
			"y" lineTo script("one"),
			"point" lineTo script())
			.typed
			.assertEqualTo(
				typed(
					"point" fieldTo typed(
						"x" fieldTo typed("zero" fieldTo typed()),
						"y" fieldTo typed("one" fieldTo typed()))))
	}

	@Test
	fun resolve() {
		emptyResolver
			.plus(
				functionBinding(
					Arrow(
						struct("bit" fieldTo struct("zero" fieldTo struct())),
						bitType)))
			.typed(script("bit" lineTo script("zero")))
			.assertEqualTo(
				term(variable(0))
					.invoke(nullTerm)
					.of(bitType))
	}

	@Test
	fun resolveDynamic() {
		emptyResolver
			.plus(
				functionBinding(
					Arrow(
						struct("bit" fieldTo struct("zero" fieldTo struct())),
						bitType)))
			.plus(
				functionBinding(
					Arrow(
						bitType.plus("boolean" fieldTo struct()),
						booleanType)))
			.typed(
				script(
					"bit" lineTo script("zero"),
					"boolean" lineTo script()))
			.assertEqualTo(
				term(variable(0))
					.invoke(term(variable(1)).invoke(nullTerm))
					.of(booleanType))
	}

	@Test
	fun give() {
		script(
			"zero" lineTo script(),
			"give" lineTo script("given"))
			.typed
			.assertEqualTo(
				term(function(term(variable(0))))
					.invoke(nullTerm)
					.of(struct("given" fieldTo struct("zero" fieldTo struct()))))
	}

	@Test
	fun giveComplex() {
		script(
			"x" lineTo script("zero"),
			"y" lineTo script("one"),
			"give" lineTo script(
				"given" lineTo script(),
				"x" lineTo script(),
				"and" lineTo script(
					"given" lineTo script(),
					"y" lineTo script())))
			.typed
			.assertEqualTo(
				term(function(nullTerm))
					.invoke(nullTerm)
					.of(
						struct(
							"x" fieldTo struct("zero" fieldTo struct()),
							"and" fieldTo struct("y" fieldTo struct("one" fieldTo struct())))))
	}

	@Test
	fun cast() {
		script(
			"bit" lineTo script("zero"),
			"as" lineTo script(
				"bit" lineTo script(
					"choice" lineTo script(
						"zero" lineTo script(),
						"one" lineTo script()))))
			.typed
			.assertEqualTo(
				typed("bit" fieldTo typed("zero"))
					.castTo(
						struct(
							"bit" fieldTo choice(
								"zero" caseTo struct(),
								"one" caseTo struct()))))
	}

	@Test
	fun choice() {
		script(
			"choice" lineTo script(
				"no" lineTo script("circle" lineTo script("radius")),
				"yes" lineTo script("square" lineTo script("side"))))
			.typed
			.assertEqualTo(
				emptyResolver
					.choice(
						script(
							"no" lineTo script("circle" lineTo script("radius")),
							"yes" lineTo script("square" lineTo script("side"))))
					.typed)
	}
}