package leo19.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo19.term.nullTerm
import leo19.type.fieldTo
import leo19.type.struct
import leo19.typed.fieldTo
import leo19.typed.getOrNull
import leo19.typed.make
import leo19.typed.nullTyped
import leo19.typed.of
import leo19.typed.plus
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
}