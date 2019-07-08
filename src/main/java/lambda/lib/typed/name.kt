package lambda.lib.typed

import lambda.Term
import lambda.lib.i32Int
import lambda.lib.i32Term

enum class Name {
	BIT,
}

val names = Name.values().toList()
val Name.term get() = ordinal.i32Term
val Term.name get() = names[i32Int]
