# Architecture

```
├── src/
│   ├── Animal.java
│   ├── animalNames.txt
│   ├── arrivingAnimals.txt
│   ├── ZookeeperChallenge.java
│   └── zooPopulation.txt
└── SUMMARY.md
```

Animal.java is a class that contains the characteristic of Animal objects.
ZookeeperChallenge.java is the core function that use Animal.java as OOP to write output into zooPopulation.txt.

animalNames.txt and arrivingAnimals.txt has the raw data for Animal.java to utilize for ZookeeperChallenge.java.
zooPopulation.txt is written from ZookeeperChallenge.java that organizes animals to their respective habitat while listing each species ID and characteristics.

# Implementation/Execution

1. **loadNames** scans for a file with a specific formatting. If the blank line has many whitespaces, it trims it to the minimal. Since it is still a blank line, it skips to the next line. When the line has the case-sensitive keyword "Names:", speciesCategory is set to the value of what is the first word in that line. The do-while removes the whitespaces between the names of the animals. Under the do-while, the commas gets removed, but separate the names by splitting it. Then, it puts the animal names under its respective species category, and repeats for further lines that is "___ Names:".

2. **loadDetails** also scans for a file with a specific formatting. The method starts with List that can additionally store the value of the animal like age. If the array list labelled as **parts** does not have at least 5 commas because of the formatting, the function continues reading the next line unless it's the end of the file. **desc** removes any whitespace before the first comma is read. Then **token** splits these values into its own array list. **animal.species** is set to lowercase because loadNames also does that. Below the **token.length >= 4**, some words are replaced with `""` because only the value matter like color. Since the all the comma is removed, Stringbuilder is utilized to make the location origin of the animal to sound formal. It then adds to the *animals list* with the animal having its own unique features, and then continue reading the next line until the end of file. At the end of fuction, it returns the list.

3. **genUniqueID** utilizes **getSpeciesID** function. The count variable initializes and increments a new ID, if the existing ID of the species already exists. The format will always be "00" instead of "0".

4. **getBirthDay** gets the animal's age, season of the month, and arrivalDate. The birthMonth is lowercased in case the season is capitalized in the arrivingAnimals.txt. The value is returned by doing a subtraction problem.

5. **getSpeciesID** is why **loadNames** and **loadDetails** lowercases the animal names. It abbreviates the animal names into the first two characters of the species. The first character of the abbreviation is always capitalized.

6. **capitalizeFirstLetter** converts the output from "hyena Habitat:" to "Hyena Habitat:".

7. **writeReport** first sets the default time to today. The **groupedAnimals** list put all animals to their respective species category. In the first for loop, **genUniqueID** is utilized for the species respective to its ID. the **namePool** is utilized to assign the name to the animal in the list. The **count** variable is there to prevent the reuse of the animal name. Like the **namePool**, **genBirthDay** is utilized to generate value to the animal. The **sortSpecies** variable is implemented to list species in alphabetical order. At the stage of printing output (zooPopulation.txt), in the first for-loop, "___ Habitat:" is printed first before the second for-loop is executed. In the second for-loop, the description per animals are printed and formatted.

8. The **main funciton** is self-explanatory, the variables are hard-coded with a specific .txt file names.
