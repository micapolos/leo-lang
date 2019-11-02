package leo13.lambda.js

import leo13.js.ast.open
import leo13.js.ast.show

val Value.open get() = expr.open
val Value.show get() = expr.show
