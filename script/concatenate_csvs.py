import os

base_path = "C:\\Users\Harshit\eclipse_workspace\PoliticianInformationExtractor"
politician_data_path = base_path + "\politician_data"
concatenated_csv_path = politician_data_path + "\politician_data.csv"

politicians = next(os.walk(politician_data_path))[1]

with open(concatenated_csv_path, "w") as outfile:
	outfile.write("POLITICIAN,CATEGORY,TOPIC\n")
	for politician in politicians:
		with open(politician_data_path + "\\" + politician + "\\" + politician + ".csv") as infile:
			for line in infile:
				outfile.write(line)
