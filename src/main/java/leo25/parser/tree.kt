package leo25.parser

import leo.base.fold
import leo13.Stack
import leo13.push
import leo13.stack

data class Tree<T>(val item: T, val childForest: Forest<T>)
data class Forest<T>(val treeStack: Stack<Tree<T>>)

operator fun <T> Forest<T>.plus(tree: Tree<T>): Forest<T> = Forest(treeStack.push(tree))
fun <T> forest(vararg trees: Tree<T>): Forest<T> = Forest<T>(stack()).fold(trees) { plus(it) }
infix fun <T> T.treeTo(forest: Forest<T>): Tree<T> = Tree(this, forest)

val <T> Parser<T>.parenthesisedTreeParser: Parser<Tree<T>>
	get() =
		bind { item ->
			parenthesisedForestParser
				.parenthesised
				.map { childForest -> Tree(item, childForest) }
		}

val <T> Parser<T>.parenthesisedForestParser: Parser<Forest<T>>
	get() =
		parenthesisedTreeParser.stackParser.map { Forest(it) }
