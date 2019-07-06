package lambda.lib

import lambda.Term
import lambda.invoke
import lambda.term

val nil = branch0(error)
val list = branch1

val Term.link get() = switch(error, id)

val foldl get(): Term = term { folded, step, list -> list.switch(folded, step(foldl)) }