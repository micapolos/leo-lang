package lambda.lib

import lambda.term

val id = term { x -> x }
val const = term { x -> term { y -> x } }