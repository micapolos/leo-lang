package leo.lab.v2

import leo.Word
import leo.base.fold

sealed class Template

data class ScriptTemplate(
	val lhsOrNull: Template?,
	val word: Word,
	val rhsOrNull: Template?) : Template()

data class SelectorTemplate(
	val selector: Selector) : Template()

val Script.template: Template
	get() =
		ScriptTemplate(lhsOrNull?.template, word, rhsOrNull?.template)

val Selector.template: Template
	get() =
		SelectorTemplate(this)

fun template(script: Script): Template =
	script.template

fun template(selector: Selector): Template =
	selector.template

val Pair<Word, Template?>.template: ScriptTemplate
	get() =
		ScriptTemplate(null, first, second)

fun ScriptTemplate?.plus(pair: Pair<Word, Template?>): ScriptTemplate =
	ScriptTemplate(this, pair.first, pair.second)

fun template(pair: Pair<Word, Template?>, vararg pairs: Pair<Word, Template?>): ScriptTemplate =
	pair.template.fold(pairs, ScriptTemplate::plus)

fun Template.invoke(script: Script): Script? =
	when (this) {
		is ScriptTemplate -> Script(lhsOrNull?.invoke(script), word, rhsOrNull?.invoke(script))
		is SelectorTemplate -> script.select(selector)
	}
