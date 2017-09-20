motor=(CSG)ScriptingEngine
	                    .gitScriptRun(
                                "https://github.com/WPIRoboticsEngineering/RBELabCustomParts.git", // git location of the library
	                              "gb37y3530-50en.groovy" , // file to load
	                              null
                        )
					.movez(-5.8)
double vexHoleSpacing = 0.5*25.4
double vexSquare = 0.182

CSG gear =Vitamins.get("vexGear","HS36T")
		.difference(motor)
		.roty(180)
		.toZMin()
CSG mesh = Vitamins.get("vexGear","HS84T")
			
int gearRadiusIndex = (int)((gear.getMaxX()+mesh.getMaxX())/vexHoleSpacing)
println gearRadiusIndex
mesh=mesh.movex(vexHoleSpacing*gearRadiusIndex)
CSG spacer = Vitamins.get("vexSpacer","oneEighth")
			.toZMax()
			.movex(vexHoleSpacing*gearRadiusIndex)
			
return [motor,gear,mesh,spacer]