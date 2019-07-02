package lambda

import leo.base.assertEqualTo
import leo.base.byte
import leo.binary.*
import org.junit.Test

val no = term { x -> term { y -> x } }
val yes = term { x -> term { y -> y } }

val id = term { x -> x }
val nand = term { a -> term { b -> a.invoke(yes).invoke(b.invoke(yes).invoke(no)) } }
val not = term { b -> b.invoke(yes).invoke(no) }

fun Term.dot(term: Term) = term.invoke(this)

fun pair(a: Term, b: Term) = term { at -> at(a, b) }
val pairAt = term { at -> term { pair -> pair(at) } }
fun Term.pairAt(bit: Term) = dot(lambda.pairAt(bit))

fun branchFirst(first: Term) = term { ifFirst -> term { ifSecond -> ifFirst(first) } }
fun branchSecond(second: Term) = term { ifFirst -> term { ifSecond -> ifSecond(second) } }
val branchSwitch get() = term { ifFirst -> term { ifSecond -> term { branch -> branch(ifFirst, ifSecond) } } }

val Term.bool
	get() =
		newVariable.let { falseVariable ->
			newVariable.let { trueVariable ->
				invoke(term(falseVariable), term(trueVariable)).eq(term(trueVariable))
			}
		}

val Term.pair: Pair<Term, Term>
	get() =
		pairAt(no) to pairAt(yes)

val Bit.term get() = if (isZero) no else yes
val Term.bit get() = if (eq(no)) bit0 else bit1

val Byte.term
	get() = pair(
		pair(
			pair(bit7.term, bit6.term),
			pair(bit5.term, bit4.term)),
		pair(
			pair(bit3.term, bit2.term),
			pair(bit1.term, bit0.term)))

val Term.byte
	get() =
		byte(
			pairAt(bit0.term).pairAt(bit0.term).pairAt(bit0.term).bit,
			pairAt(bit0.term).pairAt(bit0.term).pairAt(bit1.term).bit,
			pairAt(bit0.term).pairAt(bit1.term).pairAt(bit0.term).bit,
			pairAt(bit0.term).pairAt(bit1.term).pairAt(bit1.term).bit,
			pairAt(bit1.term).pairAt(bit0.term).pairAt(bit0.term).bit,
			pairAt(bit1.term).pairAt(bit0.term).pairAt(bit1.term).bit,
			pairAt(bit1.term).pairAt(bit1.term).pairAt(bit0.term).bit,
			pairAt(bit1.term).pairAt(bit1.term).pairAt(bit1.term).bit)

class TermTest {
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

		byte(154).term.byte.assertEqualTo(byte(154))
	}
}