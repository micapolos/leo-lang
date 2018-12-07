package leo.lab.v2

sealed class Resolution

data class MatchResolution(
	val match: Match) : Resolution()

data class RecursionResolution(
	val recursion: Recursion) : Resolution()

fun resolution(match: Match): Resolution =
	MatchResolution(match)

fun resolution(recursion: Recursion): Resolution =
	RecursionResolution(recursion)
