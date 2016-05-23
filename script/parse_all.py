import os

base_path = "C:\\Users\Harshit\eclipse_workspace\PoliticianInformationExtractor"
politician_data_path = base_path + "\politician_data"
jar_path = base_path + "\\target\PoliticianInformationExtractor-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
config_path = base_path + "\src\config\config.xml"

politicians = next(os.walk(politician_data_path))[1]

for politician in politicians:
	command = "java -jar " + jar_path + " " + config_path + " " + politician
	os.system(command)
