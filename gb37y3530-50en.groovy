
CSG bolt = Vitamins.get("capScrew","M3")

pshaft=(CSG)ScriptingEngine
	                    .gitScriptRun(
                                "https://github.com/WPIRoboticsEngineering/RBELabCustomParts.git", // git location of the library
	                              "dShaft.groovy" , // file to load
	                              [6.0,5.34,21]
                        )

return [pshaft,bolt]