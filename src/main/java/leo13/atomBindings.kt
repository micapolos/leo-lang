package leo13

data class AtomBindings(val linkOrNull: AtomBindingsLink?)

fun atomBindings(linkOrNull: AtomBindingsLink?) = AtomBindings(linkOrNull)
fun AtomBindings.plus(atom: Atom) = atomBindings(this linkTo atom)
