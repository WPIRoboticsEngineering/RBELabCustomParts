LengthParameter printerOffset = new LengthParameter("printerOffset",0.25,[2,0.001])

double vshaftSize = 3.12
double finalV = vshaftSize+(printerOffset.getMM()*1.5)


CSG vshaft =  new RoundedCube(	finalV,// X dimention
				finalV,// Y dimention
				11//  Z dimention
				)
				.cornerRadius(0.5)// sets the radius of the corner
				.toCSG()// converts it to a CSG tor display
				.toZMax()
				.movez(-1)
pshaft=(CSG)ScriptingEngine
	                    .gitScriptRun(
                                "https://github.com/WPIRoboticsEngineering/RBELabCustomParts.git", // git location of the library
	                              "dShaft.groovy" , // file to load
	                              [6.0,5.34,11]
                        )

CSG coupler = new Cube(	12,// X dimention
			12,// Y dimention
			20//  Z dimention
			).toCSG()
			.difference([pshaft,vshaft])
			.rotx(-90)
			.toZMin()
return [coupler]