package lambda.lib

import lambda.invoke
import lambda.term

val fix = term { fn -> term { x -> fn(x(x)) }(term { x -> fn(x(x)) }) }