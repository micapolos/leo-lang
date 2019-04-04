package leo32.term

sealed class Ap

data class TemplateFieldAp(
	val templateField: TemplateField): Ap()

data class SelectorAp(
	val selector: Selector): Ap()

val TemplateField.ap get() =
	TemplateFieldAp(this) as Ap

val Selector.ap get() =
	SelectorAp(this) as Ap

infix fun String.ap(template: Template) =
	fieldTo(template).ap

fun Ap.invoke(lhs: Term, term: Term): Term =
	when (this) {
		is TemplateFieldAp -> lhs.plus(templateField.invoke(term))
		is SelectorAp -> selector.invoke(term)
	}
