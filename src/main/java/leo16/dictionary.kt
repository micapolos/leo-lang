package leo16

import leo13.Stack
import leo13.fold
import leo13.mapFirst
import leo13.push
import leo13.reverse
import leo13.stack
import leo16.names.*

data class Dictionary(val definitionStack: Stack<Definition>) {
	override fun toString() = asField.toString()
}

val Stack<Definition>.dictionary get() = Dictionary(this)
val emptyDictionary = stack<Definition>().dictionary

operator fun Dictionary.plus(definition: Definition): Dictionary =
	definitionStack.push(definition).dictionary

operator fun Dictionary.plus(dictionary: Dictionary): Dictionary =
	fold(dictionary.definitionStack.reverse) { plus(it) }

inline fun Dictionary.apply(evaluated: Evaluated): Evaluated? =
	definitionStack.mapFirst { apply(evaluated) }

inline fun Dictionary.resolve(evaluated: Evaluated): Evaluated =
	apply(evaluated) ?: evaluated

inline fun Dictionary.compile(value: Value): Evaluated =
	emptyScope.emptyEvaluator.plus(value).evaluated

inline fun Dictionary.evaluate(value: Value): Value =
	compile(value).value

val Dictionary.asField: Sentence
	get() =
		_dictionary(_definition(definitionStack.expandSentence { asSentence }))

fun Dictionary.bind(value: Value): Dictionary =
	this
		.plus(value.thingParameterDefinition)
		.fold(value.parameterDefinitionStack.reverse) { plus(it) }

fun Dictionary.modeOrNull(word: String): Mode? =
	apply(value(_mode(_word(word()))).evaluated)?.value?.modeOrNull

fun Dictionary.applyReflect(value: Value): Value =
	apply(value(_reflect(value)).evaluated)?.value ?: value

fun Dictionary.applyRead(sentence: Sentence): Sentence =
	apply(value(_read(sentence)).evaluated)
		?.value
		?.onlySentenceOrNull
		?: sentence.read // TODO: implement list reading as definition, and remove ".read"
