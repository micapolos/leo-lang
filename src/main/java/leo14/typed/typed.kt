package leo14.typed

import leo14.lambda.Term

data class Typed<out T>(val term: Term<T>, val type: Type)