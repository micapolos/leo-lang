package lambda.lib

import lambda.invoke
import lambda.term

val fix = term { f -> term { x -> f(x(x)) }(term { x -> f(x(x)) }) }
