package leo21.parser

import leo14.Literal

sealed class Atom
data class LiteralAtom(val literal: Literal) : Atom()
data class NameAtom(val name: String) : Atom()
