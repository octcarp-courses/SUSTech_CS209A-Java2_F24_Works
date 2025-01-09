package p4t2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class P4T2 {

    public static void main(String[] args) throws IOException {
        CityAnalysis cityAnalysis = new CityAnalysis();

        cityAnalysis.printResult();
    }
}

class City {
    private String name;
    private String state;
    private int population;

    public City(String name, String state, int population) {
        this.name = name;
        this.state = state;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public int getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return "City{name='" + name + "', state='" + state + "', population=" + population + "}";
    }
}

class CityAnalysis {

    private static Stream<City> readCities() {
        try {
            String fileName = "p4t2/cities.txt";
            Path path = Path.of(CityAnalysis.class.getClassLoader().getResource(fileName).toURI());
            return Files.lines(path)
                .map(l -> l.split(", "))
                .map(a -> new City(a[0], a[1], Integer.parseInt(a[2])));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return Stream.empty();
    }

    private Map<String, Long> cityCountPerState;

    private Map<String, Integer> statePopulation;

    private Map<String, String> longestCityNameByState;

    private Map<String, Set<City>> largeCitiesByState;


    CityAnalysis() {

        Stream<City> cities;

        cities = readCities();
        // Q1: count how many cities there are for each state
        cityCountPerState = cities.collect(
            Collectors.groupingBy(City::getState, Collectors.counting())
        );


        cities = readCities();
        // Q2: count the total population for each state
        statePopulation = cities.collect(
            Collectors.groupingBy(
                City::getState, Collectors.summingInt(City::getPopulation)
            )
        );


        cities = readCities();
        // Q3: for each state, get the city with the longest name
        longestCityNameByState = cities.collect(
            Collectors.groupingBy(City::getState,
                Collectors.collectingAndThen(
                    Collectors.maxBy(
                        Comparator.comparingInt(c -> c.getName().length())
                    ),
                    c -> c.map(City::getName).orElse("")
                )
            )
        );

        cities = readCities();
        // Q4: for each state, get the set of cities with >500,000 population
        largeCitiesByState = cities.collect(
            Collectors.groupingBy(City::getState,
                Collectors.filtering(
                    c -> c.getPopulation() > 500_000, Collectors.toSet()
                )
            )
        );
    }

    void printResult() {
        StringBuilder sb;
        System.out.println("Q1: # of cities per state:");
        sb = new StringBuilder("{");
        for (Map.Entry<String, Long> entry : cityCountPerState.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",\n");
        }
        System.out.println(sb.append("}\n"));

        System.out.println("Q2: population per state:");
        sb = new StringBuilder("{");
        for (Map.Entry<String, Integer> entry : statePopulation.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",\n");
        }
        System.out.println(sb.append("}\n"));


        System.out.println("Q3: longest city name per state:");
        for (Map.Entry<String, String> entry : longestCityNameByState.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue() + ",");
        }
        System.out.println();

        System.out.println("Q4: cities with >500,000 population for each state:");
        for (Map.Entry<String, Set<City>> entry : largeCitiesByState.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }

            System.out.print(entry.getKey() + ": [");
            for (City city : entry.getValue()) {
                System.out.print(city + ", ");
            }
            System.out.println("] ");
        }
    }
}
