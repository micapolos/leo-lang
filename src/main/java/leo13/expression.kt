package leo13

sealed class Expression

data class AtomExpression(val atom: Atom) : Expression()
data class LinkExpression(val link: ExpressionLink) : Expression()
data class BindingExpression(val binding: ExpressionBinding) : Expression()

fun expression(atom: Atom): Expression = AtomExpression(atom)
fun expression(link: ExpressionLink): Expression = LinkExpression(link)
fun expression(binding: ExpressionBinding): Expression = BindingExpression(binding)

fun Expression.atom(bindings: AtomBindings): Atom =
	when (this) {
		is AtomExpression -> atom
		is LinkExpression -> link.atom(bindings)
		is BindingExpression -> binding.atom(bindings)
	}
