package leo13.untyped.compiler

import leo13.untyped.Pattern
import leo13.untyped.evaluator.Value

sealed class Op
data class ValueOp(val value: Value) : Op()
data class LinkOp(val link: ExpressionLink) : Op()
data class GetOp(val get: NameGet) : Op()
data class SetOp(val set: ExpressionSet) : Op()
data class SwitchOp(val switch: Switch) : Op()
data class BindOp(val bind: ExpressionBind) : Op()
data class BoundOp(val bound: Bound) : Op()
data class PatternOp(val pattern: Pattern) : Op()
