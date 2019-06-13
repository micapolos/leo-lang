package leo5.lang

import kotlin.test.Test

class SyntaxTest {
	@Test
	fun syntax() {
		leo {
			circle {
				radius = float(12)
				center {
					x = int(12)
					y = int(15)
				}
			}
			move {
				x = int(15)
				y = int(11)
			}
			rotate { degrees = int(90) }
			scale { times = int(2) }
		}
	}

	@Test
	fun scripting() {
		script {
			line {
				word {
					int(12)
				}
				script {}
			}
			line {
				word = int(12)
				script {}
			}
			line { word = int(12); script() }
			line(word(int(12)), script())
			line(word(int(12)).script)
			line(word(int(12)).script)
		}
	}

	@Test
	fun structs() {
		kotlin {
			word { contains { string } }
			script {
				split {
					empty
					non { empty { contains {

					} } }
				} } }
				is_ { list { of { line } } }
				line { contains { word; script } }
			}
		}
	}
}