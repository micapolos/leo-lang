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

val Dictionary.asField: Field
	get() =
		_dictionary(_definition(definitionStack.expandField { asField }))

fun Dictionary.bind(value: Value): Dictionary =
	this
		.plus(value.thingParameterDefinition)
		.fold(value.fieldStack) { plus(it.parameterDefinition) }

fun Dictionary.modeOrNull(word: String): Mode? =
	apply(_mode(_word(word())).value.evaluated)?.value?.modeOrNull

fun Dictionary.applyReflect(field: Field): Field =
	apply(_reflect(field.value).value.evaluated)?.value?.onlyFieldOrNull ?: field

fun Dictionary.reflect(value: Value): Value =
	value.traverse { this@reflect.applyReflect(this) }

fun Dictionary.applyRead(field: Field): Field =
	apply(_read(field.value).value.evaluated)
		?.value
		?.onlyFieldOrNull
		?: field.read // TODO: implement list reading as definition, and remove ".read"
