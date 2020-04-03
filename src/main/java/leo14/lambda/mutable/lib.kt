package leo14.lambda.mutable

fun <T> id(): Term<T> = term { term(it) }
