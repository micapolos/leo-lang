package leo13.token.reader

import leo.base.assertEqualTo
import leo13.*
import leo13.locator.column
import leo13.locator.line
import leo13.locator.location
import leo13.locator.locator
import leo13.script.lineTo
import leo13.script.script
import leo13.token.Token
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
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
				processor<Token>()
					.tokenizer(
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
		tokenizer().push("\n").assertEqualTo(tokenizer())
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
				processor<Token>().tokenizer(
					parent(),
					head(input(colon(false), "fo"))))
	}

	@Test
	fun letterSpace() {
		tokenizer()
			.push("f ")
			.assertEqualTo(
				processor(
					token(opening("f")),
					token(closing)).tokenizer(
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
				processor(token(opening("f"))).tokenizer(
					parent(indent(tab(space))),
					head(colon)))
	}

	@Test
	fun letterNewline() {
		tokenizer()
			.push("f\n")
			.assertEqualTo(
				processor(token(opening("f"))).tokenizer(
					parent(),
					head(indent(tab(space)))))
	}

	@Test
	fun letterNewlineTab() {
		tokenizer()
			.push("f\n\t")
			.assertEqualTo(
				processor(token(opening("f"))).tokenizer(
					parent(indent(tab(space))),
					head(input(colon(false), ""))))
	}

	@Test
	fun letterColonSpace() {
		tokenizer()
			.push("f: ")
			.assertEqualTo(
				processor(token(opening("f"))).tokenizer(
					parent(indent(tab(space))),
					head(input(colon(true), ""))))
	}

	@Test
	fun letterColonSpaceLetter() {
		tokenizer()
			.push("f: g")
			.assertEqualTo(
				processor(token(opening("f"))).tokenizer(
					parent(indent(tab(space))),
					head(input(colon(true), "g"))))
	}

	@Test
	fun letterColonSpaceLetterNewline() {
		tokenizer()
			.push("f: g\n")
			.assertEqualTo(
				processor(
					token(opening("f")),
					token(opening("g"))).tokenizer(
					parent(),
					head(indent(tab(space, space)))))
	}

	@Test
	fun letterColonSpaceLetterNewlineTab() {
		tokenizer()
			.push("f: g\n\t")
			.assertEqualTo(
				processor(
					token(opening("f")),
					token(opening("g"))).tokenizer(
					parent(indent(tab(space, space))),
					head(input(colon(false), ""))))
	}

	@Test
	fun letterColonSpaceLetterSpace() {
		tokenizer()
			.push("f: g ")
			.assertEqualTo(
				processor(
					token(opening("f")),
					token(opening("g")),
					token(closing)).tokenizer(
					parent(indent(tab(space))),
					head(input(colon(true), ""))))
	}

	@Test
	fun switch_one() {
		tokenizer()
			.push("switch\n\tone")
			.assertEqualTo(
				processor(
					token(opening("switch"))).tokenizer(
					parent(indent(tab(space))),
					head(input(colon(false), "one"))))
	}

	@Test
	fun switch_one__() {
		tokenizer()
			.push("switch\n\tone ")
			.assertEqualTo(
				processor(
					token(opening("switch")),
					token(opening("one")),
					token(closing)).tokenizer(
					parent(indent(tab(space))),
					head(input(colon(false), ""))))
	}

	@Test
	fun switch_one__gives() {
		tokenizer()
			.push("switch\n\tone gives")
			.assertEqualTo(
				processor(
					token(opening("switch")),
					token(opening("one")),
					token(closing)).tokenizer(
					parent(indent(tab(space))),
					head(input(colon(false), "gives"))))
	}

	@Test
	fun switch_one__gives_() {
		tokenizer()
			.push("switch\n\tone gives:")
			.assertEqualTo(
				processor(
					token(opening("switch")),
					token(opening("one")),
					token(closing),
					token(opening("gives"))).tokenizer(
					parent(indent(tab(space), tab(space))),
					head(colon)))
	}

	@Test
	fun switch_one__gives_space() {
		tokenizer()
			.push("switch\n\tone gives: ")
			.assertEqualTo(
				processor(
					token(opening("switch")),
					token(opening("one")),
					token(closing),
					token(opening("gives"))).tokenizer(
					parent(indent(tab(space), tab(space))),
					head(input(colon(true), ""))))
	}

	@Test
	fun switch_one__gives_jeden() {
		tokenizer()
			.push("switch\n\tone gives: jeden")
			.assertEqualTo(
				processor(
					token(opening("switch")),
					token(opening("one")),
					token(closing),
					token(opening("gives"))).tokenizer(
					parent(indent(tab(space), tab(space))),
					head(input(colon(true), "jeden"))))
	}

	@Test
	fun circle_radius_one() {
		test(
			"circle: radius: x\n\t",
			processor(
				token(opening("circle")),
				token(opening("radius")),
				token(opening("x"))).tokenizer(
				parent(indent(tab(space, space, space))),
				head(input(colon(false), ""))))
	}

	@Test
	fun one__gives_two__gives_three() {
		test(
			"one gives: two gives: three",
			processor(
				token(opening("one")),
				token(closing),
				token(opening("gives")),
				token(opening("two")),
				token(closing),
				token(opening("gives"))).tokenizer(
				parent(indent(tab(space, space))),
				head(input(colon(true), "three"))))
	}

	@Test
	fun one__gives_two__gives_three__() {
		test(
			"one gives: two gives: three\n",
			processor(
				token(opening("one")),
				token(closing),
				token(opening("gives")),
				token(opening("two")),
				token(closing),
				token(opening("gives")),
				token(opening("three"))).tokenizer(
				parent(),
				head(indent(tab(space, space, space)))))
	}

	@Test
	fun maliciousStuff_before() {
		test(
			"one: two three",
			processor(
				token(opening("one")),
				token(opening("two")),
				token(closing)).tokenizer(
				parent(indent(tab(space))),
				head(input(colon(true), "three"))))
	}

	@Test
	fun maliciousStuff_after() {
		test(
			"one: two three\n",
			processor(
				token(opening("one")),
				token(opening("two")),
				token(closing),
				token(opening("three"))).tokenizer(
				parent(null),
				head(indent(tab(space, space)))))
	}

	@Test
	fun maliciousStuff2_before() {
		test(
			"circle\n\tradius: zero\n\tcenter",
			processor(
				token(opening("circle")),
				token(opening("radius")),
				token(opening("zero")),
				token(closing),
				token(closing)).tokenizer(
				parent(indent(tab(space))),
				head(input(colon(false), "center"))))
	}

	@Test
	fun maliciousStuff2_after() {
		test(
			"circle\n\tradius: zero\n\tcenter\n",
			processor(
				token(opening("circle")),
				token(opening("radius")),
				token(opening("zero")),
				token(closing),
				token(closing),
				token(opening("center"))).tokenizer(
				parent(),
				head(indent(tab(space), tab(space)))))
	}

	@Test
	fun newlineFlushesIndent() {
		test(
			"circle\n\tradius\n\n",
			processor(
				token(opening("circle")),
				token(opening("radius")),
				token(closing),
				token(closing)).tokenizer(
				parent(),
				head(input(colon(false), ""))))
	}

	@Test
	fun newlineTwiceDoesNothing() {
		test(
			"circle\n\tradius\n\n\n",
			processor(
				token(opening("circle")),
				token(opening("radius")),
				token(closing),
				token(closing)).tokenizer(
				parent(),
				head(input(colon(false), ""))))
	}

	@Test
	fun complex() {
		tokenizer()
			.push("switch\n\tone gives: jeden\n\ttwo gives: dwa\nok")
			.assertEqualTo(
				processor(
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
					token(closing)).tokenizer(
					parent(),
					head(input(colon(false), "ok"))))
	}

	@Test
	fun endOfTransmission() {
		tokenizer()
			.push("switch\n\u0004")
			.assertEqualTo(
				processor(
					token(opening("switch")),
					token(closing),
					token(closing))
					.tokenizer())
	}

	@Test
	fun withLocator() {
		traced {
			processor<Token>()
				.tokenizer()
				.locator()
				.charProcess("jajko)")
		}.assertFailsWith(
			location(line(1), column(6)).scriptingLine,
			"tokenizer" lineTo script(')'.scriptLine))
	}
}