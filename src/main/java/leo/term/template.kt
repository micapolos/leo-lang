package leo.term

import leo.Word
import leo.base.fold

sealed class Template

data class TermTemplate(
	val term: Term<Template>) : Template() {
	override fun toString() = templateType.string(term)
}

data class SelectorTemplate(
	val selector: Selector) : Template()

val templateType: Type<Template> =
	Type(Template::isSimple)

val Term<Template>.template: Template
	get() =
		TermTemplate(this)

val Selector.template: Template
	get() =
		SelectorTemplate(this)

val Word.template: Template
	get() =
		term<Template>().template

fun template(template: Template, vararg applications: Application<Template>): Template =
	template.fold(applications) { apply(it).template }

fun template(application: Application<Template>, vararg applications: Application<Template>): Template =
	template(application.term.template, *applications)

fun template(selector: Selector): Template =
	selector.template

fun Template.invoke(script: Script): Script? =
	when (this) {
		is TermTemplate -> term.receiverOrNull?.invoke(script)
			.apply(term.application.word, term.application.argumentOrNull?.invoke(script))
			.script
		is SelectorTemplate -> script.select(selector)
	}

val Template.isSimple: Boolean
	get() =
		when (this) {
			is TermTemplate -> term.isSimple
			is SelectorTemplate -> true
		}