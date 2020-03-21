package leo14

sealed class Atom
data class LiteralAtom(val literal: Literal) : Atom()
data class NameAtom(val name: String) : Atom()

fun atom(literal: Literal): Atom = LiteralAtom(literal)
fun atom(name: String): Atom = NameAtom(name)