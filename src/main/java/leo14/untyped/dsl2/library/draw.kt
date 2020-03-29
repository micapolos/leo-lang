package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val draw = library_ {
	point
	gives {
		point {
			x { number }
			y { number }
		}
	}

	move { to { point } }
	code
	does {
		text("ctx.moveTo(")
		plus { given.move.to.point.x.number.leo.text }
		plus { text(", ") }
		plus { given.move.to.point.y.number.leo.text }
		plus { text(")") }
	}

	assert {
		move {
			to {
				point {
					x { number(10) }
					y { number(20) }
				}
			}
		}
		code
		gives { text("ctx.moveTo(10, 20)") }
	}

	line { to { point } }
	code
	does {
		text("ctx.lineTo(")
		plus { given.line.to.point.x.number.leo.text }
		plus { text(", ") }
		plus { given.line.to.point.y.number.leo.text }
		plus { text(")") }
	}

	assert {
		line {
			to {
				point {
					x { number(10) }
					y { number(20) }
				}
			}
		}
		code
		gives { text("ctx.lineTo(10, 20)") }
	}

	arc {
		center { point }
		radius { number }
		angle {
			start { degrees { number } }
			end { degrees { number } }
		}
		winding { either { clockwise.anticlockwise } }
	}
	code
	does {
		text("ctx.arc(")
		plus { given.arc.center.point.x.number.leo.text }
		plus { text(", ") }
		plus { given.arc.center.point.y.number.leo.text }
		plus { text(", ") }
		plus { given.arc.radius.number.leo.text }
		plus { text(", ") }
		plus { given.arc.angle.start.degrees.number.leo.text }
		plus { text("*p, ") }
		plus { given.arc.angle.end.degrees.number.leo.text }
		plus { text("*p, ") }
		plus {
			given.arc.winding.match {
				clockwise { text("false") }
				anticlockwise { text("true") }
			}
		}
		plus { text(")") }
	}

	assert {
		arc {
			center {
				point {
					x { number(10) }
					y { number(20) }
				}
			}
			radius { number(30) }
			angle {
				start { degrees { number(40) } }
				end { degrees { number(50) } }
			}
			winding { anticlockwise }
		}
		code
		gives { text("ctx.arc(10, 20, 30, 40*p, 50*p, true)") }
	}

	path { anything }
	code
	does {
		given.path.object_
		recursively {
			do_ {
				given.object_.equals_.match {
					true_ { text("") }
					false_ {
						given.previous.given.object_.recurse
						plus { given.last.object_.code }
						plus { text("\n") }
					}
				}
			}
		}
	}

	assert {
		path {
			move {
				to {
					point {
						x { number(10) }
						y { number(20) }
					}
				}
			}
			line {
				to {
					point {
						x { number(30) }
						y { number(40) }
					}
				}
			}
		}
		code
		gives { text("ctx.moveTo(10, 20)\nctx.lineTo(30, 40)\n") }
	}

	path { anything }
	draw
	does {
		text("<!DOCTYPE html>\n" +
			"<html style=\"background-color:hsl(240, 10%, 10%)\">\n" +
			"<body>\n" +
			"\n" +
			"<canvas id=\"myCanvas\" width=\"450\" height=\"450\"></canvas>\n" +
			"\n" +
			"<script>\n" +
			"var c = document.getElementById(\"myCanvas\");\n" +
			"var ctx = c.getContext(\"2d\");\n" +
			"var p = Math.PI*2/360;\n" +
			"\n" +
			"ctx.lineWidth = 25;\n" +
			"ctx.strokeStyle = \"hsl(300, 20%, 65%)\"\n" +
			"\n" +
			"ctx.beginPath();\n")
		plus { given.path.code }
		plus {
			text(
				"ctx.stroke();\n" +
					"\n" +
					"</script> \n" +
					"</body>\n" +
					"</html>\n")
		}
		open { html }
	}

	leo.path.gives {
		path {
			move {
				to {
					point {
						x { number(80) }
						y { number(150) }
					}
				}
			}
			arc {
				center {
					point {
						x { number(110) }
						y { number(270) }
					}
				}
				radius { number(30) }
				angle {
					start { degrees { number(180) } }
					end { degrees { number(90) } }
				}
				winding { anticlockwise }
			}
			arc {
				center {
					point {
						x { number(110) }
						y { number(250) }
					}
				}
				radius { number(50) }
				angle {
					start { degrees { number(90) } }
					end { degrees { number(60) } }
				}
				winding { anticlockwise }
			}
			arc {
				center {
					point {
						x { number(300) }
						y { number(250) }
					}
				}
				radius { number(50) }
				angle {
					start { degrees { number(240) } }
					end { degrees { number(180) } }
				}
				winding { clockwise }
			}
			arc {
				center {
					point {
						x { number(300) }
						y { number(250) }
					}
				}
				radius { number(50) }
				angle {
					start { degrees { number(180) } }
					end { degrees { number(240) } }
				}
				winding { clockwise }
			}
			move {
				to {
					point {
						x { number(250) }
						y { number(250) }
					}
				}
			}
			arc {
				center {
					point {
						x { number(200) }
						y { number(250) }
					}
				}
				radius { number(50) }
				angle {
					start { degrees { number(0) } }
					end { degrees { number(60) } }
				}
				winding { anticlockwise }
			}
		}
	}
}

fun main() = run_(draw)