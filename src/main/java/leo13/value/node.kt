package leo13.value

import leo13.value.expr.Expr

sealed class Node

data class LinkNode(val link: Link) : Node()
data class ExprNode(val expr: Expr) : Node()

fun node(link: Link): Node = LinkNode(link)
fun node(expr: Expr): Node = ExprNode(expr)
