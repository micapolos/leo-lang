package leo16

data class TypedEvaluator(val dictionary: TypedDictionary, val typed: Typed)

fun TypedDictionary.evaluator(typed: Typed) = TypedEvaluator(this, typed)
val TypedDictionary.emptyEvaluator get() = evaluator(emptyTyped)
val TypedEvaluator.begin get() = dictionary.emptyEvaluator
fun TypedEvaluator.set(typed: Typed) = copy(typed = typed)
fun TypedEvaluator.set(dictionary: TypedDictionary) = copy(dictionary = dictionary)

val TypedEvaluator.resolve: TypedEvaluator
	get() =
		set(dictionary.resolve(typed))

fun TypedEvaluator.plus(word: String, evaluator: TypedEvaluator): TypedEvaluator =
	append(word, evaluator).normalize.resolve

fun TypedEvaluator.append(word: String, evaluator: TypedEvaluator): TypedEvaluator =
	set(typed.plus(word, evaluator.typed))

val TypedEvaluator.normalize
	get() =
		dictionary.evaluator(typed.normalize)