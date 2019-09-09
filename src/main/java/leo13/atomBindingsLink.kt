package leo13

data class AtomBindingsLink(val bindings: AtomBindings, val atom: Atom)

infix fun AtomBindings.linkTo(atom: Atom) = AtomBindingsLink(this, atom)
