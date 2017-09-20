// Measurments from http://image.dfrobot.com/image/data/FIT0185/FIT0185_Dimension.PNG

double boltHoleDiameter = 31
double shaftCenterOffset = 7.0
double shaftCollarDiameter = 12.0/2.0
double shaftCollarHeight = 5.8 
double motorBodyRadius = 37.0/2.0
double bodyLength=75.0
CSG body =new Cylinder(motorBodyRadius,motorBodyRadius,bodyLength,(int)30).toCSG() // a one line Cylinder
			.toZMax()
CSG collar =new Cylinder(shaftCollarDiameter,shaftCollarDiameter,shaftCollarHeight,(int)30).toCSG() // a one line Cylinder
CSG bolt = Vitamins.get("capScrew","M3")
			.rotx(180)
			.movey(boltHoleDiameter/2)

pshaft=(CSG)ScriptingEngine
	                    .gitScriptRun(
                                "https://github.com/WPIRoboticsEngineering/RBELabCustomParts.git", // git location of the library
	                              "dShaft.groovy" , // file to load
	                              [6.0,5.34,21]
                        )
                        .union(collar)
					.movex(shaftCenterOffset)
CSG bolts = bolt
for(int i=60;i<360;i+=60){
	bolts=bolts.union(bolt.rotz(i))
}
CSG wholeMotor = CSG.unionAll([pshaft,bolts,body])
return wholeMotor