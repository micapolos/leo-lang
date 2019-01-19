package leo.graph

import leo.base.Stack

sealed class Node

class Source : Node()

class Computation(
	val fn: () -> Unit,
	val inputs: Stack<Node>) : Node()

class Branch(
	val selector: Node,
	val cases: Stack<Node>) : Node()

class Graph(
	val terminals: Stack<Node>)