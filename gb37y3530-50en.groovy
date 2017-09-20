// Measurments from http://image.dfrobot.com/image/data/FIT0185/FIT0185_Dimension.PNG
LengthParameter printerOffset = new LengthParameter("printerOffset",0.25,[2,0.001])
double boltHoleDiameter = 31
double shaftCenterOffset = 7.0
double shaftCollarDiameter = (12.0/2.0)+printerOffset.getMM()
double shaftCollarHeight = 5.8 
double motorBodyRadius = (37.0/2.0)+printerOffset.getMM()
double bodyLength=75.0
double shaftDiameter = 6.0
double totalShaftLength = 21
double dShaftSection = 12
double dShaftStart = totalShaftLength-dShaftSection
double shaftRadius = (shaftDiameter/2)+printerOffset.getMM()

CSG body =new Cylinder(motorBodyRadius,motorBodyRadius,bodyLength,(int)30).toCSG() // a one line Cylinder
			.toZMax()
CSG collar =new Cylinder(shaftCollarDiameter,shaftCollarDiameter,shaftCollarHeight,(int)30).toCSG() // a one line Cylinder
CSG shaft =new Cylinder(shaftRadius,shaftRadius,dShaftStart,(int)30).toCSG() // a one line Cylinder
CSG bolt = Vitamins.get("capScrew","M3")
			.toolOffset(printerOffset.getMM())
			.movez(printerOffset.getMM()/2)
			.rotx(180)
			.movey(boltHoleDiameter/2)

pshaft=(CSG)ScriptingEngine
	                    .gitScriptRun(
                                "https://github.com/WPIRoboticsEngineering/RBELabCustomParts.git", // git location of the library
	                              "dShaft.groovy" , // file to load
	                              [shaftDiameter,5.34,dShaftSection]
                        )
                        .movez(dShaftStart)
                        .union([collar,shaft])
					.movex(shaftCenterOffset)
CSG bolts = bolt
for(int i=60;i<360;i+=60){
	bolts=bolts.union(bolt.rotz(i))
}
CSG wholeMotor = CSG.unionAll([pshaft,bolts,body])
				.movex(-shaftCenterOffset)
return wholeMotor