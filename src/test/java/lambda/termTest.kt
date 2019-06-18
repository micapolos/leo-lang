package lambda

import leo.base.assertEqualTo
import org.junit.Test

class TermTest {
	val no = term { x -> term { y -> x } }
	val yes = term { x -> term { y -> y } }

	val id = term { x -> x }
	val nand = term { a -> term { b -> a.invoke(yes).invoke(b.invoke(yes).invoke(no)) } }
	val not = term { b -> b.invoke(yes).invoke(no) }

	fun pair(a: Term, b: Term) = term { at -> at(a, b) }
	val pairAt = term { at -> term { pair -> pair(at) } }

	fun branchFirst(first: Term) = term { ifFirst -> term { ifSecond -> ifFirst(first) } }
	fun branchSecond(second: Term) = term { ifFirst -> term { ifSecond -> ifSecond(second) } }
	val branchSwitch get() = term { ifFirst -> term { ifSecond -> term { branch -> branch(ifFirst, ifSecond) } } }

	fun Term.dot(term: Term) = term.invoke(this)

	@Test
	fun boolean() {
		no.eq(no).assertEqualTo(true)
		no.eq(yes).assertEqualTo(false)
		yes.eq(no).assertEqualTo(false)
		yes.eq(yes).assertEqualTo(true)

		nand(no, no).assertEqualTo(yes)
		nand(no, yes).assertEqualTo(yes)
		nand(yes, no).assertEqualTo(yes)
		nand(yes, yes).assertEqualTo(no)

		not(no).assertEqualTo(yes)
		not(yes).assertEqualTo(no)
		not(not(no)).assertEqualTo(no)
		not(not(yes)).assertEqualTo(yes)

		pair(pair(no, no), pair(yes, yes)).dot(pairAt(no)).assertEqualTo(pair(no, no))
		pair(pair(no, no), pair(yes, yes)).dot(pairAt((yes))).assertEqualTo(pair(yes, yes))

		branchFirst(pair(no, no)).dot(branchSwitch(id, id)).assertEqualTo(pair(no, no))
		branchSecond(pair(yes, yes)).dot(branchSwitch(id, id)).assertEqualTo(pair(yes, yes))
	}
}