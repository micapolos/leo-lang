package leo13.token.reader

import leo.base.assertEqualTo
import leo13.colon
import leo13.ok
import leo13.space
import leo13.status
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.token.tokens
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
					tokens(),
					parent(),
					head(input(colon(false), "f")),
					status(ok)))
	}

	@Test
	fun pushTab() {
		assertFails { tokenizer().unsafePush("\t") }
	}

	@Test
	fun pushSpace() {
		assertFails { tokenizer().unsafePush(" ") }
	}

	@Test
	fun colon() {
		assertFails { tokenizer().unsafePush(":") }
	}

	@Test
	fun newline() {
		assertFails { tokenizer().unsafePush("\n") }
	}

	@Test
	fun other() {
		assertFails { tokenizer().unsafePush("1") }
	}

	@Test
	fun letterLetter() {
		tokenizer()
			.push("fo")
			.assertEqualTo(
				tokenizer(
					tokens(),
					parent(),
					head(input(colon(false), "fo")),
					status(ok)))
	}

	@Test
	fun letterSpace() {
		tokenizer()
			.push("f ")
			.assertEqualTo(
				tokenizer(
					tokens(
						token(opening("f")),
						token(closing)),
					parent(),
					head(input(colon(false), "")),
					status(ok)))
	}

	@Test
	fun letterTab() {
		assertFails { tokenizer().unsafePush("f\t") }
	}

	@Test
	fun letterColon() {
		tokenizer()
			.push("f:")
			.assertEqualTo(
				tokenizer(
					tokens(token(opening("f"))),
					parent(indent(tab(space))),
					head(colon),
					status(ok)))
	}

	@Test
	fun letterNewline() {
		tokenizer()
			.push("f\n")
			.assertEqualTo(
				tokenizer(
					tokens(token(opening("f"))),
					parent(),
					head(indent(tab(space))),
					status(ok)))
	}

	@Test
	fun letterNewlineTab() {
		tokenizer()
			.push("f\n\t")
			.assertEqualTo(
				tokenizer(
					tokens(token(opening("f"))),
					parent(indent(tab(space))),
					head(input(colon(false), "")),
					status(ok)))
	}

	@Test
	fun letterColonSpace() {
		tokenizer()
			.push("f: ")
			.assertEqualTo(
				tokenizer(
					tokens(token(opening("f"))),
					parent(indent(tab(space))),
					head(input(colon(true), "")),
					status(ok)))
	}

	@Test
	fun letterColonSpaceLetter() {
		tokenizer()
			.push("f: g")
			.assertEqualTo(
				tokenizer(
					tokens(token(opening("f"))),
					parent(indent(tab(space))),
					head(input(colon(true), "g")),
					status(ok)))
	}

	@Test
	fun letterColonSpaceLetterNewline() {
		tokenizer()
			.push("f: g\n")
			.assertEqualTo(
				tokenizer(
					tokens(
						token(opening("f")),
						token(opening("g"))),
					parent(),
					head(indent(tab(space, space))),
					status(ok)))
	}

	@Test
	fun letterColonSpaceLetterNewlineTab() {
		tokenizer()
			.push("f: g\n\t")
			.assertEqualTo(
				tokenizer(
					tokens(
						token(opening("f")),
						token(opening("g"))),
					parent(indent(tab(space, space))),
					head(input(colon(false), "")),
					status(ok)))
	}

	@Test
	fun letterColonSpaceLetterSpace() {
		tokenizer()
			.push("f: g ")
			.assertEqualTo(
				tokenizer(
					tokens(
						token(opening("f")),
						token(opening("g")),
						token(closing)),
					parent(indent(tab(space))),
					head(input(colon(true), "")),
					status(ok)))
	}

	@Test
	fun switch_one() {
		tokenizer()
			.push("switch\n\tone")
			.assertEqualTo(
				tokenizer(
					tokens(
						token(opening("switch"))),
					parent(indent(tab(space))),
					head(input(colon(false), "one")),
					status(ok)))
	}

	@Test
	fun switch_one__() {
		tokenizer()
			.push("switch\n\tone ")
			.assertEqualTo(
				tokenizer(
					tokens(
						token(opening("switch")),
						token(opening("one")),
						token(closing)),
					parent(indent(tab(space))),
					head(input(colon(false), "")),
					status(ok)))
	}

	@Test
	fun switch_one__gives() {
		tokenizer()
			.push("switch\n\tone gives")
			.assertEqualTo(
				tokenizer(
					tokens(
						token(opening("switch")),
						token(opening("one")),
						token(closing)),
					parent(indent(tab(space))),
					head(input(colon(false), "gives")),
					status(ok)))
	}

	@Test
	fun switch_one__gives_() {
		tokenizer()
			.push("switch\n\tone gives:")
			.assertEqualTo(
				tokenizer(
					tokens(
						token(opening("switch")),
						token(opening("one")),
						token(closing),
						token(opening("gives"))),
					parent(indent(tab(space), tab(space))),
					head(colon),
					status(ok)))
	}

	@Test
	fun switch_one__gives_space() {
		tokenizer()
			.push("switch\n\tone gives: ")
			.assertEqualTo(
				tokenizer(
					tokens(
						token(opening("switch")),
						token(opening("one")),
						token(closing),
						token(opening("gives"))),
					parent(indent(tab(space), tab(space))),
					head(input(colon(true), "")),
					status(ok)))
	}

	@Test
	fun switch_one__gives_jeden() {
		tokenizer()
			.push("switch\n\tone gives: jeden")
			.assertEqualTo(
				tokenizer(
					tokens(
						token(opening("switch")),
						token(opening("one")),
						token(closing),
						token(opening("gives"))),
					parent(indent(tab(space), tab(space))),
					head(input(colon(true), "jeden")),
					status(ok)))
	}

	@Test
	fun circle_radius_one() {
		test(
			"circle: radius: x\n\t",
			tokenizer(
				tokens(
					token(opening("circle")),
					token(opening("radius")),
					token(opening("x"))),
				parent(indent(tab(space, space, space))),
				head(input(colon(false), "")),
				status(ok)))
	}

	@Test
	fun one__gives_two__gives_three() {
		test(
			"one gives: two gives: three",
			tokenizer(
				tokens(
					token(opening("one")),
					token(closing),
					token(opening("gives")),
					token(opening("two")),
					token(closing),
					token(opening("gives"))),
				parent(indent(tab(space, space))),
				head(input(colon(true), "three")),
				status(ok)))
	}

	@Test
	fun one__gives_two__gives_three__() {
		test(
			"one gives: two gives: three\n",
			tokenizer(
				tokens(
					token(opening("one")),
					token(closing),
					token(opening("gives")),
					token(opening("two")),
					token(closing),
					token(opening("gives")),
					token(opening("three"))),
				parent(),
				head(indent(tab(space, space, space))),
				status(ok)))
	}

	@Test
	fun maliciousStuff_before() {
		test(
			"one: two three",
			tokenizer(
				tokens(
					token(opening("one")),
					token(opening("two")),
					token(closing)),
				parent(indent(tab(space))),
				head(input(colon(true), "three")),
				status(ok)))
	}

	@Test
	fun maliciousStuff_after() {
		test(
			"one: two three\n",
			tokenizer(
				tokens(
					token(opening("one")),
					token(opening("two")),
					token(closing),
					token(opening("three"))),
				parent(null),
				head(indent(tab(space, space))),
				status(ok)))
	}

	@Test
	fun maliciousStuff2_before() {
		test(
			"circle\n\tradius: zero\n\tcenter",
			tokenizer(
				tokens(
					token(opening("circle")),
					token(opening("radius")),
					token(opening("zero")),
					token(closing),
					token(closing)),
				parent(indent(tab(space))),
				head(input(colon(false), "center")),
				status(ok)))
	}

	@Test
	fun maliciousStuff2_after() {
		test(
			"circle\n\tradius: zero\n\tcenter\n",
			tokenizer(
				tokens(
					token(opening("circle")),
					token(opening("radius")),
					token(opening("zero")),
					token(closing),
					token(closing),
					token(opening("center"))),
				parent(),
				head(indent(tab(space), tab(space))),
				status(ok)))
	}

	@Test
	fun complex() {
		tokenizer()
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