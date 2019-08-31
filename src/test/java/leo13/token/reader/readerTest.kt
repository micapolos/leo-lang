package leo13.token.reader

import leo.base.assertEqualTo
import leo13.space
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.token.tokens
import kotlin.test.Test
import kotlin.test.assertFails

class ReaderTest {
	@Test
	fun letter() {
		reader()
			.push("f")
			.assertEqualTo(
				reader(
					tokens(),
					null,
					head("f")))
	}

	@Test
	fun tab() {
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
					null,
					head("fo")))
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
					null,
					head("")))
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
					indent(indent(space)),
					head(colon)))
	}

	@Test
	fun letterNewline() {
		reader()
			.push("f\n")
			.assertEqualTo(
				reader(
					tokens(token(opening("f"))),
					null,
					head(indent(indent(space)))))
	}

	@Test
	fun letterNewlineTab() {
		reader()
			.push("f\n\t")
			.assertEqualTo(
				reader(
					tokens(token(opening("f"))),
					indent(indent(space)),
					head("")))
	}

	@Test
	fun letterColonSpace() {
		reader()
			.push("f: ")
			.assertEqualTo(
				reader(
					tokens(token(opening("f"))),
					indent(indent(space)),
					head("")))
	}

	@Test
	fun letterColonSpaceLetter() {
		reader()
			.push("f: g")
			.assertEqualTo(
				reader(
					tokens(token(opening("f"))),
					indent(indent(space)),
					head("g")))
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
					null,
					head(indent(indent(space, space)))))
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
					indent(indent(space, space)),
					head("")))
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
					indent(indent(space)),
					head("")))
	}

	@Test
	fun switch_one() {
		reader()
			.push("switch\n\tone")
			.assertEqualTo(
				reader(
					tokens(
						token(opening("switch"))),
					indent(indent(space)),
					head("one")))
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
					indent(indent(space)),
					head("")))
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
					indent(indent(space)),
					head("gives")))
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
					indent(indent(space), indent(space)),
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
					indent(indent(space), indent(space)),
					head("")))
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
					indent(indent(space), indent(space)),
					head("jeden")))
	}

	@Test
	fun complex() {
		tokens("switch\n\tone gives: jeden\n\ttwo gives: dwa\n")
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