motor=(CSG)ScriptingEngine
	                    .gitScriptRun(
                                "https://github.com/WPIRoboticsEngineering/RBELabCustomParts.git", // git location of the library
	                              "gb37y3530-50en.groovy" , // file to load
	                              null
                        )
					.movez(-5.8)

CSG vshaft =  (CSG)ScriptingEngine
	                    .gitScriptRun(
                                "https://github.com/WPIRoboticsEngineering/RBELabCustomParts.git", // git location of the library
	                              "dShaft.groovy" , // file to load
	                              [60]
                        )				
double vexHoleSpacing = 0.5*25.4
double vexSquare = 0.182

CSG gear =Vitamins.get("vexGear","HS36T")
		.difference(motor)
		.roty(180)
		.toZMin()
CSG mesh = Vitamins.get("vexGear","HS84T")
			.difference(vshaft)
			
int gearRadiusIndex = (int)((gear.getMaxX()+mesh.getMaxX())/vexHoleSpacing)
println gearRadiusIndex
mesh=mesh.movex(vexHoleSpacing*gearRadiusIndex)
CSG spacer = Vitamins.get("vexSpacer","oneEighth")
			.toZMax()
			.movex(vexHoleSpacing*gearRadiusIndex)
HashMap<String,Object> vexSpacerConfig = Vitamins.getConfiguration( "vexSpacer","oneEighth")
double innerRadius = vexSpacerConfig.innerDiameter/2
CSG vexHole = new Cylinder(innerRadius, innerRadius, vexHoleSpacing*2, 10).toCSG()
				.toZMax()

CSG vexHoleSet = vexHole

int width = 4
int length = 2

for(int i=0;i<length;i++){
	for(int j=-(width/2);j<(width/2+1);j++){
		vexHoleSet=vexHoleSet.union(vexHole
					.movex((i*vexHoleSpacing)+vexHoleSpacing)
					.movey(j*vexHoleSpacing)
					)
	}
}

CSG mountPlate = new Cube(vexHoleSpacing*(length+2.5),vexHoleSpacing*(width+1),vexHoleSpacing/2).toCSG()
				.toZMax()
				.toXMin()
				.movex(-vexHoleSpacing*2)
				.difference([motor,vexHoleSet])
mountPlate.setMfg( {toMfg ->
		toMfg.rotx(180)
			.toZMin()
})
				
return [motor,gear,mesh,spacer,mountPlate]