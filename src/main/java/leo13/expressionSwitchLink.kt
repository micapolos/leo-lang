package leo13

data class ExpressionSwitchLink(val switch: ExpressionSwitch, val operation: ExpressionOperation)

infix fun ExpressionSwitch.linkTo(operation: ExpressionOperation) = ExpressionSwitchLink(this, operation)

fun ExpressionSwitchLink.atom(bindings: AtomBindings, atomLink: AtomLink): Atom =
	if (atomLink.leftAtom == atom) operation.atom(bindings, atomLink.rightAtom)
	else switch.atom(bindings, atomLink.leftAtom.link.leftAtom linkTo atomLink.rightAtom)
