package leo13

data class ExpressionSwitch(val switchOrNull: ExpressionSwitch?, val operation: ExpressionOperation)

fun ExpressionSwitch.atom(bindings: AtomBindings, atom: Atom): Atom =
	atom(bindings, atom.link)

fun ExpressionSwitch.atom(bindings: AtomBindings, atomLink: AtomLink): Atom =
	if (atomLink.leftAtom == atom) operation.atom(bindings, atomLink.rightAtom)
	else switchOrNull!!.atom(bindings, atom(atomLink.leftAtom.link.leftAtom linkTo atomLink.rightAtom))
