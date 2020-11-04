package leo21.dictionary

import leo.base.fold
import leo.base.reverse
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.fieldOrNull
import leo14.lineSeq
import leo21.compiler.Bindings
import leo21.compiler.push

data class DictionaryCompiler(
	val bindings: Bindings,
	val dictionary: Dictionary
)

val Bindings.emptyDictionaryCompiler get() = DictionaryCompiler(this, emptyDictionary)

fun DictionaryCompiler.push(definition: Definition): DictionaryCompiler =
	DictionaryCompiler(
		bindings.push(definition.binding),
		dictionary.plus(definition))

fun DictionaryCompiler.plus(script: Script): DictionaryCompiler =
	fold(script.lineSeq.reverse) { plusOrNull(it)!! }

fun DictionaryCompiler.plusOrNull(scriptLine: ScriptLine): DictionaryCompiler? =
	scriptLine.fieldOrNull?.let { plusOrNull(it) }

fun DictionaryCompiler.plusOrNull(scriptField: ScriptField): DictionaryCompiler? =
	when (scriptField.string) {
		"define" -> plusDefine(scriptField.rhs)
		else -> null!!
	}

fun DictionaryCompiler.plusDefine(script: Script): DictionaryCompiler =
	push(bindings.definition(script))
