package leo13

import leo.base.fold

data class AtomBindings(val linkOrNull: AtomBindingsLink?)

fun atomBindings(linkOrNull: AtomBindingsLink?) = AtomBindings(linkOrNull)
fun AtomBindings.plus(atom: Atom) = atomBindings(this linkTo atom)
fun atomBindings(vararg atoms: Atom) = atomBindings(null).fold(atoms) { plus(it) }
