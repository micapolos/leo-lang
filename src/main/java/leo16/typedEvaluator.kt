package leo16

data class TypedEvaluator(val dictionary: TypedDictionary, val typed: Typed)

fun TypedDictionary.evaluator(typed: Typed) = TypedEvaluator(this, typed)
val TypedDictionary.emptyEvaluator get() = evaluator(emptyTyped)
val TypedEvaluator.begin get() = dictionary.emptyEvaluator
fun TypedEvaluator.set(typed: Typed) = copy(typed = typed)
fun TypedEvaluator.set(dictionary: TypedDictionary) = copy(dictionary = dictionary)
