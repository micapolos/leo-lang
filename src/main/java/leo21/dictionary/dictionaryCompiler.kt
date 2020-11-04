package leo21.dictionary

import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.fieldOrNull
import leo14.lambda.fn
import leo14.matchInfix
import leo21.compiled.Bindings
import leo21.compiled.push
import leo21.compiled.typed
import leo21.type.arrowTo
import leo21.type.type
import leo21.typed.of

data class DictionaryCompiler(
	val bindings: Bindings,
	val dictionary: Dictionary
)

val Bindings.emptyDictionaryCompiler get() = DictionaryCompiler(this, emptyDictionary)

fun DictionaryCompiler.push(definition: Definition): DictionaryCompiler =
	DictionaryCompiler(
		bindings.push(definition.binding),
		dictionary.plus(definition))

fun DictionaryCompiler.plusDefine(script: Script): DictionaryCompiler =
	script.matchInfix("does") { lhs, rhs ->
		lhs.type.let { lhsType ->
			bindings.push(lhsType).typed(rhs).let { typed ->
				push(definition(fn(typed.term).of(lhsType arrowTo typed.type)))
			}
		}
	}!!

fun DictionaryCompiler.plusOrNull(scriptLine: ScriptLine): DictionaryCompiler? =
	scriptLine.fieldOrNull?.let { plusOrNull(it) }

fun DictionaryCompiler.plusOrNull(scriptField: ScriptField): DictionaryCompiler? =
	when (scriptField.string) {
		"define" -> plusDefine(scriptField.rhs)
		else -> null!!
	}
