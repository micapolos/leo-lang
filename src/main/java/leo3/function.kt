package leo3

import leo.base.Seq
import leo.binary.Bit
import leo32.base.Node
import leo32.base.at
import leo32.base.write

data class Function(val matchNode: Node<Match>)

fun function(matchNode: Node<Match>) = Function(matchNode)

fun Function.matchAt(bit: Bit): Match? =
	matchNode.at(bit)

fun Appendable.append(function: Function): Appendable =
	append('?')

fun Writer.writePattern(function: Function): Writer =
	write(function.matchNode) { writePattern(it) }

val Function.bitSeq: Seq<Bit> get() = TODO()