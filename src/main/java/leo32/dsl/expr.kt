package leo32.dsl

import leo32.base.List
import leo32.base.list

data class Expr(val _name: String, val _value: List<Expr>)

fun expr(name: String, vararg xs: Expr) = Expr(name, list(*xs))
fun boolean(boolean: Boolean) = boolean(expr(boolean.toString()))
fun int(int: Int) = int(expr(int.toString()))
