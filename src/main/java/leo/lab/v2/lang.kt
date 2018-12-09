package leo.lab.v2

import leo.*
import leo.base.fold

infix fun Letter.caseTo(match: Match): Case =
	onlyWord caseTo function(end caseTo match)

fun letterCaseTo(match: Match): Case =
	letterWord caseTo function().fold(letterStream) { letter ->
		plus(letter caseTo match(function(leo.end caseTo match)))
	}

fun wordCaseTo(match: Match): Case =
	wordWord caseTo function(
		letterCaseTo(
			match(
				function(
					letterCaseTo(
						match(
							function(
								recursion(
									sibling.jump)))),
					end caseTo match))))

fun bitCaseTo(match: Match): Case =
	bitWord caseTo function(
		zeroWord caseTo function(end caseTo match(function(end caseTo match))),
		oneWord caseTo function(end caseTo match(function(end caseTo match))))

fun byteCaseTo(match: Match): Case =
	byteWord caseTo function(
		bitCaseTo(match(function(
			bitCaseTo(match(function(
				bitCaseTo(match(function(
					bitCaseTo(match(function(
						bitCaseTo(match(function(
							bitCaseTo(match(function(
								bitCaseTo(match(function(
									bitCaseTo(match(function(
										end caseTo match)))))))))))))))))))))))))
