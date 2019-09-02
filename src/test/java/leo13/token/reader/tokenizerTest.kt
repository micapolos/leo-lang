package leo13.token.reader

import leo.base.assertEqualTo
import leo13.colon
import leo13.space
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.token.tokens
import kotlin.test.Test
import kotlin.test.assertFails

fun test(string: String, tokenizer: Tokenizer) =
	reader().push(string).assertEqualTo(tokenizer)

class TokenizerTest {
	@Test
	fun letter() {
		reader()
			.push("f")
			.assertEqualTo(
				reader(
					tokens(),
					parent(),
					head(input(colon(false), "f"))))
	}

	@Test
	fun pushTab() {
		assertFails { reader().push("\t") }
	}

	@Test
	fun pushSpace() {
		assertFails { reader().push(" ") }
	}

	@Test
	fun colon() {
		assertFails { reader().push(":") }
	}

	@Test
	fun newline() {
		assertFails { reader().push("\n") }
	}

	@Test
	fun other() {
		assertFails { reader().push("1") }
	}

	@Test
	fun letterLetter() {
		reader()
			.push("fo")
			.assertEqualTo(
				reader(
					tokens(),
					parent(),
					head(input(colon(false), "fo"))))
	}

	@Test
	fun letterSpace() {
		reader()
			.push("f ")
			.assertEqualTo(
				reader(
					tokens(
						token(opening("f")),
						token(closing)),
					parent(),
					head(input(colon(false), ""))))
	}

	@Test
	fun letterTab() {
		assertFails { reader().push("f\t") }
	}

	@Test
	fun letterColon() {
		reader()
			.push("f:")
			.assertEqualTo(
				reader(
					tokens(token(opening("f"))),
					parent(indent(tab(space))),
					head(colon)))
	}

	@Test
	fun letterNewline() {
		reader()
			.push("f\n")
			.assertEqualTo(
				reader(
					tokens(token(opening("f"))),
					parent(),
					head(indent(tab(space)))))
	}

	@Test
	fun letterNewlineTab() {
		reader()
			.push("f\n\t")
			.assertEqualTo(
				reader(
					tokens(token(opening("f"))),
					parent(indent(tab(space))),
					head(input(colon(false), ""))))
	}

	@Test
	fun letterColonSpace() {
		reader()
			.push("f: ")
			.assertEqualTo(
				reader(
					tokens(token(opening("f"))),
					parent(indent(tab(space))),
					head(input(colon(true), ""))))
	}

	@Test
	fun letterColonSpaceLetter() {
		reader()
			.push("f: g")
			.assertEqualTo(
				reader(
					tokens(token(opening("f"))),
					parent(indent(tab(space))),
					head(input(colon(true), "g"))))
	}

	@Test
	fun letterColonSpaceLetterNewline() {
		reader()
			.push("f: g\n")
			.assertEqualTo(
				reader(
					tokens(
						token(opening("f")),
						token(opening("g"))),
					parent(),
					head(indent(tab(space, space)))))
	}

	@Test
	fun letterColonSpaceLetterNewlineTab() {
		reader()
			.push("f: g\n\t")
			.assertEqualTo(
				reader(
					tokens(
						token(opening("f")),
						token(opening("g"))),
					parent(indent(tab(space, space))),
					head(input(colon(false), ""))))
	}

	@Test
	fun letterColonSpaceLetterSpace() {
		reader()
			.push("f: g ")
			.assertEqualTo(
				reader(
					tokens(
						token(opening("f")),
						token(opening("g")),
						token(closing)),
					parent(indent(tab(space))),
					head(input(colon(false), ""))))
	}

	@Test
	fun switch_one() {
		reader()
			.push("switch\n\tone")
			.assertEqualTo(
				reader(
					tokens(
						token(opening("switch"))),
					parent(indent(tab(space))),
					head(input(colon(false), "one"))))
	}

	@Test
	fun switch_one__() {
		reader()
			.push("switch\n\tone ")
			.assertEqualTo(
				reader(
					tokens(
						token(opening("switch")),
						token(opening("one")),
						token(closing)),
					parent(indent(tab(space))),
					head(input(colon(false), ""))))
	}

	@Test
	fun switch_one__gives() {
		reader()
			.push("switch\n\tone gives")
			.assertEqualTo(
				reader(
					tokens(
						token(opening("switch")),
						token(opening("one")),
						token(closing)),
					parent(indent(tab(space))),
					head(input(colon(false), "gives"))))
	}

	@Test
	fun switch_one__gives_() {
		reader()
			.push("switch\n\tone gives:")
			.assertEqualTo(
				reader(
					tokens(
						token(opening("switch")),
						token(opening("one")),
						token(closing),
						token(opening("gives"))),
					parent(indent(tab(space), tab(space))),
					head(colon)))
	}

	@Test
	fun switch_one__gives_space() {
		reader()
			.push("switch\n\tone gives: ")
			.assertEqualTo(
				reader(
					tokens(
						token(opening("switch")),
						token(opening("one")),
						token(closing),
						token(opening("gives"))),
					parent(indent(tab(space), tab(space))),
					head(input(colon(true), ""))))
	}

	@Test
	fun switch_one__gives_jeden() {
		reader()
			.push("switch\n\tone gives: jeden")
			.assertEqualTo(
				reader(
					tokens(
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
			reader(
				tokens(
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
			reader(
				tokens(
					token(opening("one")),
					token(closing),
					token(opening("gives")),
					token(opening("two")),
					token(closing),
					token(opening("gives"))),
				parent(indent(tab(space), tab(space))),
				head(input(colon(true), "three"))))
	}

	@Test
	fun one__gives_two__gives_three__() {
		test(
			"one gives: two gives: three\n",
			reader(
				tokens(
					token(opening("one")),
					token(closing),
					token(opening("gives")),
					token(opening("two")),
					token(closing),
					token(opening("gives")),
					token(opening("three"))),
				parent(),
				head(indent(tab(space, space), tab(space)))))
	}

	@Test
	fun maliciousStuff_before() {
		test(
			"one: two three",
			reader(
				tokens(
					token(opening("one")),
					token(opening("two")),
					token(closing)),
				parent(indent(tab(space))),
				head(input(colon(false), "three"))))
	}

	@Test
	fun maliciousStuff_after() {
		test(
			"one: two three\n",
			reader(
				tokens(
					token(opening("one")),
					token(opening("two")),
					token(closing),
					token(opening("three"))),
				parent(null),
				head(indent(tab(space, space)))))
	}

	@Test
	fun complex() {
		reader()
			.push("switch\n\tone gives: jeden\n\ttwo gives: dwa\n")
			.finish
			.assertEqualTo(
				tokens(
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
					token(closing)))
	}
}