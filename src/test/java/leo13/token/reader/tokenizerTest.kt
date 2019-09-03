package leo13.token.reader

import leo.base.assertEqualTo
import leo13.base.writer
import leo13.colon
import leo13.space
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo9.stack
import kotlin.test.Test
import kotlin.test.assertFails

fun test(string: String, tokenizer: Tokenizer) =
	tokenizer().push(string).assertEqualTo(tokenizer)

class TokenizerTest {
	@Test
	fun letter() {
		tokenizer()
			.push("f")
			.assertEqualTo(
				tokenizer(
					writer(stack()),
					parent(),
					head(input(colon(false), "f"))))
	}

	@Test
	fun pushTab() {
		assertFails { tokenizer().push("\t") }
	}

	@Test
	fun pushSpace() {
		assertFails { tokenizer().push(" ") }
	}

	@Test
	fun colon() {
		assertFails { tokenizer().push(":") }
	}

	@Test
	fun newline() {
		assertFails { tokenizer().push("\n") }
	}

	@Test
	fun other() {
		assertFails { tokenizer().push("1") }
	}

	@Test
	fun letterLetter() {
		tokenizer()
			.push("fo")
			.assertEqualTo(
				tokenizer(
					writer(),
					parent(),
					head(input(colon(false), "fo"))))
	}

	@Test
	fun letterSpace() {
		tokenizer()
			.push("f ")
			.assertEqualTo(
				tokenizer(
					writer(
						token(opening("f")),
						token(closing)),
					parent(),
					head(input(colon(false), ""))))
	}

	@Test
	fun letterTab() {
		assertFails { tokenizer().push("f\t") }
	}

	@Test
	fun letterColon() {
		tokenizer()
			.push("f:")
			.assertEqualTo(
				tokenizer(
					writer(token(opening("f"))),
					parent(indent(tab(space))),
					head(colon)))
	}

	@Test
	fun letterNewline() {
		tokenizer()
			.push("f\n")
			.assertEqualTo(
				tokenizer(
					writer(token(opening("f"))),
					parent(),
					head(indent(tab(space)))))
	}

	@Test
	fun letterNewlineTab() {
		tokenizer()
			.push("f\n\t")
			.assertEqualTo(
				tokenizer(
					writer(token(opening("f"))),
					parent(indent(tab(space))),
					head(input(colon(false), ""))))
	}

	@Test
	fun letterColonSpace() {
		tokenizer()
			.push("f: ")
			.assertEqualTo(
				tokenizer(
					writer(token(opening("f"))),
					parent(indent(tab(space))),
					head(input(colon(true), ""))))
	}

	@Test
	fun letterColonSpaceLetter() {
		tokenizer()
			.push("f: g")
			.assertEqualTo(
				tokenizer(
					writer(token(opening("f"))),
					parent(indent(tab(space))),
					head(input(colon(true), "g"))))
	}

	@Test
	fun letterColonSpaceLetterNewline() {
		tokenizer()
			.push("f: g\n")
			.assertEqualTo(
				tokenizer(
					writer(
						token(opening("f")),
						token(opening("g"))),
					parent(),
					head(indent(tab(space, space)))))
	}

	@Test
	fun letterColonSpaceLetterNewlineTab() {
		tokenizer()
			.push("f: g\n\t")
			.assertEqualTo(
				tokenizer(
					writer(
						token(opening("f")),
						token(opening("g"))),
					parent(indent(tab(space, space))),
					head(input(colon(false), ""))))
	}

	@Test
	fun letterColonSpaceLetterSpace() {
		tokenizer()
			.push("f: g ")
			.assertEqualTo(
				tokenizer(
					writer(
						token(opening("f")),
						token(opening("g")),
						token(closing)),
					parent(indent(tab(space))),
					head(input(colon(true), ""))))
	}

	@Test
	fun switch_one() {
		tokenizer()
			.push("switch\n\tone")
			.assertEqualTo(
				tokenizer(
					writer(
						token(opening("switch"))),
					parent(indent(tab(space))),
					head(input(colon(false), "one"))))
	}

	@Test
	fun switch_one__() {
		tokenizer()
			.push("switch\n\tone ")
			.assertEqualTo(
				tokenizer(
					writer(
						token(opening("switch")),
						token(opening("one")),
						token(closing)),
					parent(indent(tab(space))),
					head(input(colon(false), ""))))
	}

	@Test
	fun switch_one__gives() {
		tokenizer()
			.push("switch\n\tone gives")
			.assertEqualTo(
				tokenizer(
					writer(
						token(opening("switch")),
						token(opening("one")),
						token(closing)),
					parent(indent(tab(space))),
					head(input(colon(false), "gives"))))
	}

	@Test
	fun switch_one__gives_() {
		tokenizer()
			.push("switch\n\tone gives:")
			.assertEqualTo(
				tokenizer(
					writer(
						token(opening("switch")),
						token(opening("one")),
						token(closing),
						token(opening("gives"))),
					parent(indent(tab(space), tab(space))),
					head(colon)))
	}

	@Test
	fun switch_one__gives_space() {
		tokenizer()
			.push("switch\n\tone gives: ")
			.assertEqualTo(
				tokenizer(
					writer(
						token(opening("switch")),
						token(opening("one")),
						token(closing),
						token(opening("gives"))),
					parent(indent(tab(space), tab(space))),
					head(input(colon(true), ""))))
	}

	@Test
	fun switch_one__gives_jeden() {
		tokenizer()
			.push("switch\n\tone gives: jeden")
			.assertEqualTo(
				tokenizer(
					writer(
						token(opening("switch")),
						token(opening("one")),
						token(closing),
						token(opening("gives"))),
					parent(indent(tab(space), tab(space))),
					head(input(colon(true), "jeden"))))
	}

	@Test
	fun circle_radius_one() {
		test(
			"circle: radius: x\n\t",
			tokenizer(
				writer(
					token(opening("circle")),
					token(opening("radius")),
					token(opening("x"))),
				parent(indent(tab(space, space, space))),
				head(input(colon(false), ""))))
	}

	@Test
	fun one__gives_two__gives_three() {
		test(
			"one gives: two gives: three",
			tokenizer(
				writer(
					token(opening("one")),
					token(closing),
					token(opening("gives")),
					token(opening("two")),
					token(closing),
					token(opening("gives"))),
				parent(indent(tab(space, space))),
				head(input(colon(true), "three"))))
	}

	@Test
	fun one__gives_two__gives_three__() {
		test(
			"one gives: two gives: three\n",
			tokenizer(
				writer(
					token(opening("one")),
					token(closing),
					token(opening("gives")),
					token(opening("two")),
					token(closing),
					token(opening("gives")),
					token(opening("three"))),
				parent(),
				head(indent(tab(space, space, space)))))
	}

	@Test
	fun maliciousStuff_before() {
		test(
			"one: two three",
			tokenizer(
				writer(
					token(opening("one")),
					token(opening("two")),
					token(closing)),
				parent(indent(tab(space))),
				head(input(colon(true), "three"))))
	}

	@Test
	fun maliciousStuff_after() {
		test(
			"one: two three\n",
			tokenizer(
				writer(
					token(opening("one")),
					token(opening("two")),
					token(closing),
					token(opening("three"))),
				parent(null),
				head(indent(tab(space, space)))))
	}

	@Test
	fun maliciousStuff2_before() {
		test(
			"circle\n\tradius: zero\n\tcenter",
			tokenizer(
				writer(
					token(opening("circle")),
					token(opening("radius")),
					token(opening("zero")),
					token(closing),
					token(closing)),
				parent(indent(tab(space))),
				head(input(colon(false), "center"))))
	}

	@Test
	fun maliciousStuff2_after() {
		test(
			"circle\n\tradius: zero\n\tcenter\n",
			tokenizer(
				writer(
					token(opening("circle")),
					token(opening("radius")),
					token(opening("zero")),
					token(closing),
					token(closing),
					token(opening("center"))),
				parent(),
				head(indent(tab(space), tab(space)))))
	}

	@Test
	fun complex() {
		tokenizer()
			.push("switch\n\tone gives: jeden\n\ttwo gives: dwa\nok")
			.assertEqualTo(
				tokenizer(
					writer(
						token(opening("switch")),
						token(opening("one")),
						token(closing),
						token(opening("gives")),
						token(opening("jeden")),
						token(closing),
						token(closing),
						token(opening("two")),
						token(closing),
						token(opening("gives")),
						token(opening("dwa")),
						token(closing),
						token(closing),
						token(closing)),
					parent(),
					head(input(colon(false), "ok"))))
	}
}