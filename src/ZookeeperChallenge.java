import java.io.*;
import java.util.*;
import java.time.LocalDate;


public class ZookeeperChallenge {
    static Map<String, List<String>> namePool = new HashMap<>();
    static Map<String, Integer> speciesCounters = new HashMap<>();
    
    private static void loadNames(String file) throws IOException {
        try ( Scanner scan = new Scanner(new File(file)) ) {
            while ( scan.hasNextLine() ) {
                String line = scan.nextLine().trim();
                if ( line.isEmpty() ) continue;

                if ( line.endsWith("Names:") ) {
                    String speciesCategory = line.split(" ")[0];
                    String species = speciesCategory.toLowerCase();
                    String namesRow;

                    do {
                        if (!scan.hasNextLine() ) return;
                        namesRow = scan.nextLine().trim();
                    } while ( namesRow.isEmpty() );

                    List<String> names = new ArrayList<>();
                    for ( String name : namesRow.split(",") ) {
                        String trimmed = name.trim();
                        if ( !trimmed.isEmpty() ) {
                            names.add(trimmed);
                        }
                    }
                    namePool.put(species, names);
                }
            }
        }
    }

    private static List<Animal> loadDetails(String file) throws IOException {
        List<Animal> animals = new ArrayList<>();

        try ( Scanner scan = new Scanner(new File(file)) ) {
            while ( scan.hasNextLine() ) {
                String line = scan.nextLine().trim();
                if ( line.isEmpty() ) continue;

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    Animal animal = new Animal();

                    String desc = parts[0].trim();
                    String[] token = desc.split("\\s+");
                    animal.age = Integer.parseInt(token[0]);
                    animal.gender = token[3];

                    if (token.length >= 4) {
                    animal.species = token[4].toLowerCase().trim();
                    } else { animal.species = "unknown"; }

                    animal.birthSeason = parts[1].trim().replace("born in ", "");
                    animal.color = parts[2].trim().replace(" color", "");
                    animal.weight = Integer.parseInt(parts[3].trim().replace(" pounds", ""));

                    StringBuilder locationString = new StringBuilder();
                    for (int i = 4; i < parts.length; i++) {
                        if (i > 4) locationString.append(",");
                        locationString.append(parts[i]);
                    }
                    animal.location = locationString.toString().replace("from ", "").trim();
                    animals.add(animal);
                }                
            }
        }
        return animals;
    }

    private static String genUniqueID(String species) {
        String speciesID = getSpeciesID(species);
        int count = speciesCounters.getOrDefault(speciesID, 0) + 1;
        speciesCounters.put(speciesID, count);
        return String.format("%s%02d", speciesID, count);
    }

    private static LocalDate genBirthDay(int age, String birthSeason, LocalDate arrivalDate) {
        int birthMonth = switch ( birthSeason.toLowerCase() ) {
            case "spring" -> 3;
            case "summer" -> 6;
            case "fall" -> 9;
            case "winter" -> 12;
            default -> 3;
        };

        int birthYear = arrivalDate.getYear() - age;

        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, 21);

        if (birthDate.getMonthValue() != birthMonth) {
            birthDate = birthDate.withDayOfMonth(1);
        }

        return birthDate;
    }

    private static String getSpeciesID(String species) {
        return switch ( species.toLowerCase() ) {
            case "hyena" -> "Hy";
            case "lion" -> "Li";
            case "tiger" -> "Ti";
            case "bear" -> "Be";
            default -> species.substring(0, Math.min(2, species.length())).toUpperCase();
        };
    }

    private static void writeReport(String outFile, List<Animal> animals) throws IOException {
    
        try (PrintWriter write = new PrintWriter(new FileWriter(outFile))) {
            LocalDate arrivalDate = LocalDate.now();
            Map<String, List<Animal>> groupedAnimals = new HashMap<>();
            
            for (Animal animal : animals) {
                animal.id = genUniqueID(animal.species);
                
                String speciesKey = animal.species.toLowerCase();
                List<String> names = namePool.get(speciesKey);
                
                if (names != null && !names.isEmpty()) {
                    int count = speciesCounters.getOrDefault(getSpeciesID(animal.species), 0);
                    int nameIndex = (count - 1) % names.size();
                    animal.name = names.get(nameIndex);
                } else {
                    animal.name = "Unknown";
                }
                
                animal.birthDate = genBirthDay(animal.age, animal.birthSeason, arrivalDate);
                animal.arrivalDate = arrivalDate;
                
                groupedAnimals.computeIfAbsent(speciesKey, k -> new ArrayList<>()).add(animal);
            }
            
            List<String> sortedSpecies = new ArrayList<>(groupedAnimals.keySet());
            Collections.sort(sortedSpecies);

            write.println("=== ZOO POPULATION REPORT ===");
            
            for (String species : sortedSpecies) {
                List<Animal> speciesAnimals = groupedAnimals.get(species);
                
                write.printf("%s Habitat:%n", capitalizeFirstLetter(species));
                
                for (Animal animal : speciesAnimals) {
                    write.printf("  %s; %s; birth date: %s; %s color; %s; %d pounds; from %s; arrived %s%n",
                        animal.id,
                        animal.name,
                        animal.birthDate,
                        animal.color,
                        animal.gender,
                        animal.weight,
                        animal.location,
                        animal.arrivalDate
                    );
                }
                write.println();
            }
        }
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static void main(String[] args) {
        String arrivingAnimals = "arrivingAnimals.txt";
        String namesFile = "animalNames.txt";
        String outputFile = "zooPopulation.txt";

        try {
            loadNames(namesFile);
            List<Animal> animals = loadDetails(arrivingAnimals);
            writeReport(outputFile, animals);

            System.out.println("Report generated: " + outputFile);
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }
}