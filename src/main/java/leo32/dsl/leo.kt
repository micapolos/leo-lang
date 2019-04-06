package leo32.dsl

import leo32.base.List
import leo32.base.list

data class Expr(val _name: String, val _value: List<Expr>)

// Primitives
fun expr(name: String, vararg xs: Expr) = Expr(name, list(*xs))
fun boolean(boolean: Boolean) = boolean(expr(boolean.toString()))
fun int(int: Int) = int(expr(int.toString()))

// Custom
fun bit(vararg xs: Expr) = expr("bit", *xs)
fun boolean(vararg xs: Expr) = expr("boolean", *xs)
fun byte(vararg xs: Expr) = expr("byte", *xs)
fun center(vararg xs: Expr) = expr("center", *xs)
fun circle(vararg xs: Expr) = expr("circle", *xs)
fun either(vararg xs: Expr) = expr("either", *xs)
fun int(vararg xs: Expr) = expr("int", *xs)
fun not(vararg xs: Expr) = expr("not", *xs)
fun one(vararg xs: Expr) = expr("one", *xs)
fun plus(vararg xs: Expr) = expr("plus", *xs)
fun radius(vararg xs: Expr) = expr("radius", *xs)
fun vec(vararg xs: Expr) = expr("vec", *xs)
fun x(vararg xs: Expr) = expr("x", *xs)
fun y(vararg xs: Expr) = expr("y", *xs)
fun z(vararg xs: Expr) = expr("z", *xs)
fun zero(vararg xs: Expr) = expr("zero", *xs)

