package leo15.terms

import kotlin.test.Test

class TermsTest {
	@Test
	fun primitives() {
		10.term.intPlus(20.term).assertGives(30.term)
		30.term.intMinus(20.term).assertGives(10.term)
		10.term.intTimes(20.term).assertGives(200.term)
		10.term.intString.assertGives("10".term)

		"Hello, ".term.stringPlus("world!".term).assertGives("Hello, world!".term)
		"Hello, world!".term.stringLength.assertGives(13.term)
	}

	@Test
	fun and() {
		10.term.and(20.term).run {
			first.assertGives(10.term)
			second.assertGives(20.term)
		}
	}

	@Test
	fun or() {
		10.term.firstOr
			.ifFirst { intPlus(1.term) }
			.orSecond { intPlus(2.term) }
			.assertGives(11.term)

		10.term.secondOr
			.ifFirst { intPlus(1.term) }
			.orSecond { intPlus(2.term) }
			.assertGives(12.term)
	}

	@Test
	fun option() {
		absent
			.ifAbsent { 0.term }
			.orPresent { intPlus(1.term) }
			.assertGives(0.term)

		10.term.present
			.ifAbsent { 0.term }
			.orPresent { intPlus(1.term) }
			.assertGives(11.term)
	}

	@Test
	fun link() {
		empty.linkTo(10.term).run {
			head.assertGives(10.term)
			tail.assertGives(empty)
		}
	}

	@Test
	fun list() {
		empty
			.ifEmpty { 0.term }
			.orLink { head }
			.assertGives(0.term)

		empty.append(10.term)
			.ifEmpty { 0.term }
			.orLink { head }
			.assertGives(10.term)
	}
}