package leo13

import leo.base.fold

data class ExpressionSwitch(val linkOrNull: ExpressionSwitchLink?)

fun expressionSwitch() = ExpressionSwitch(null)

fun ExpressionSwitch.plus(operation: ExpressionOperation): ExpressionSwitch =
	ExpressionSwitch(this linkTo operation)

fun switch(operation: ExpressionOperation, vararg operations: ExpressionOperation): ExpressionSwitch =
	ExpressionSwitch(expressionSwitch() linkTo operation).fold(operations) { plus(it) }

fun ExpressionSwitch.atom(bindings: AtomBindings, atom: Atom): Atom =
	atom(bindings, atom.link)

fun ExpressionSwitch.atom(bindings: AtomBindings, atomLink: AtomLink): Atom =
	linkOrNull!!.atom(bindings, atomLink)
