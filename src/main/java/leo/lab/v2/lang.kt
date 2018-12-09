package leo.lab.v2

import leo.*
import leo.base.fold

infix fun Letter.caseTo(match: Match): Case =
	onlyWord caseTo pattern(end caseTo match)

fun letterCaseTo(match: Match): Case =
	letterWord caseTo pattern().fold(letterStream) { letter ->
		plus(letter caseTo match(pattern(leo.end caseTo match)))
	}

fun wordCaseTo(match: Match): Case =
	wordWord caseTo pattern(
		letterCaseTo(
			match(
				pattern(
					letterCaseTo(
						match(
							pattern(
								recursion(
									sibling.jump)))),
					end caseTo match))))
