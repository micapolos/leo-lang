package lambda.lib

import lambda.term

val error = term { _ -> error("fail") }
