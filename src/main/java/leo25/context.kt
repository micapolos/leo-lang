package leo25

data class Context(
	val publicDictionary: Dictionary,
	val privateDictionary: Dictionary
)

fun context(publicDictionary: Dictionary, privateDictionary: Dictionary) =
	Context(publicDictionary, privateDictionary)

val Dictionary.context
	get() =
		context(dictionary(), this)

fun context() = context(dictionary(), dictionary())

fun Context.plus(definition: Definition): Context =
	context(publicDictionary.plus(definition), privateDictionary.plus(definition))

fun Context.plusPrivate(dictionary: Dictionary): Context =
	context(publicDictionary, privateDictionary.plus(dictionary))

val Context.private: Context
	get() =
		context(dictionary(), privateDictionary)