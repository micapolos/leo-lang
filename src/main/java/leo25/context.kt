package leo25

data class Context(
	val dictionary: Dictionary,
	val module: Module
)

fun context(dictionary: Dictionary, module: Module) = Context(dictionary, module)

fun context() = context(dictionary(), module())
val nativeContext get() = context(nativeDictionary, module())

fun Context.plus(definition: Definition): Context =
	context(dictionary.plus(definition), module.plus(definition))