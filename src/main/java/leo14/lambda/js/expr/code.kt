package leo14.lambda.js.expr

import leo14.code.Code
import leo14.js.ast.expr
import leo14.js.ast.id

val Code.astExpr get() = expr(id(string))