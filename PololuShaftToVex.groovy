LengthParameter printerOffset = new LengthParameter("printerOffset",0.25,[2,0.001])

CSG vshaft =  (CSG)ScriptingEngine
	                    .gitScriptRun(
                                "https://github.com/WPIRoboticsEngineering/RBELabCustomParts.git", // git location of the library
	                              "vexShaft.groovy" , // file to load
	                              [11]
                        )
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