package leo13

data class AtomLink(val leftAtom: Atom, val rightAtom: Atom)

infix fun Atom.linkTo(rightAtom: Atom) = AtomLink(this, rightAtom)
