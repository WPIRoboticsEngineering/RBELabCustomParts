import com.neuronrobotics.bowlerstudio.vitamins.Vitamins;

double vexGrid = (1.0/2.0)* 25.4
String servoType = "hv5932mg"
LengthParameter printerOffset 		= new LengthParameter("printerOffset",0.5,[1.2,0])                
LengthParameter boltLen 		= new LengthParameter("Bolt Length",0.5,[1.2,0]) 
StringParameter bearingSizeParam 			= new StringParameter("Encoder Board Bearing","R8-60355K505",Vitamins.listVitaminSizes("ballBearing"))
StringParameter servoparam 			= new StringParameter("Serov to use","hv5932mg",Vitamins.listVitaminSizes("hobbyServo"))
StringParameter hornparam 			= new StringParameter("Horn type","hv6214mg_6",Vitamins.listVitaminSizes("hobbyServoHorn"))

HashMap<String, Object>  bearingData = Vitamins.getConfiguration("ballBearing",bearingSizeParam.getStrValue())
HashMap<String, Object>  vitaminData = Vitamins.getConfiguration( "hobbyServo",servoparam.getStrValue())
println vitaminData
CSG vitaminFromScript = Vitamins.get("hobbyServo",servoparam.getStrValue());
CSG horn = Vitamins.get("hobbyServoHorn",hornparam.getStrValue())
				.rotx(180)
				.movez(vitaminFromScript.getMaxZ())
CSG vshaft =  (CSG)ScriptingEngine
	                    .gitScriptRun(
                                "https://github.com/WPIRoboticsEngineering/RBELabCustomParts.git", // git location of the library
	                              "vexShaft.groovy" , // file to load
	                              [11]
                        )
 CSG bolt = Vitamins.get("capScrew","8#32")
                        
return [horn,vitaminFromScript]