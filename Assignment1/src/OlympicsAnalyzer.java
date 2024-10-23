import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class OlympicsAnalyzer implements OlympicsAnalyzerInterface {
    String datasetPath;

    private final List<String> femaleAthleteIds = new ArrayList<>();
    private final Map<String, String> femaleIdToName = new HashMap<>();
    private Map<String, Integer> topPerformantFemaleMap;

    private final Map<String, Float> athleteBmiMap = new HashMap<>();
    private final Map<String, Set<String>> sportAthletesMap = new HashMap<>();
    private final Map<String, List<Float>> sportBmiMap = new HashMap<>();
    private Map<String, Float> sportBmiAvgMap;

    private Map<String, Set<Integer>> leastSportYearsMap;

    private Map<String, Integer> topCountryWinterMedal;

    private final Map<String, String> athleteIdToBirth = new HashMap<>();
    private final Map<String, String> countryCodeToName = new HashMap<>();
    private Map<String, Integer> topCountryAgeMap;


    public OlympicsAnalyzer(String datasetPath) {
        this.datasetPath = datasetPath;
        processCountry();
        processAthleteBio();
        processAthleteEventResults();
        processGamesMedal();
    }

    private void processCountry() {
        String countryPath = datasetPath + "/Olympics_Country.csv";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(countryPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        br.lines()
                .map(MyUtil::parseCsv)
                .skip(1).forEach(values -> {
                    String noc = values.get(0);
                    String name = values.get(1);

                    this.countryCodeToName.put(noc, name);
                });
    }

    private void processAthleteBio() {
        String athleteBioPath = datasetPath + "/Olympic_Athlete_Bio_filtered.csv";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(athleteBioPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        br.lines()
                .map(MyUtil::parseCsv)
                .skip(1).forEach(values -> {
                    String athleteId = values.get(0);
                    String sex = values.get(2);
                    String[] birthDay = values.get(3).split(" ");

                    if (sex.equals("Female")) {
                        String name = values.get(1);
                        this.femaleAthleteIds.add(athleteId);
                        this.femaleIdToName.put(athleteId, name);
                    }

                    String heiStr = values.get(4);
                    String weiStr = values.get(5);
                    if (!heiStr.isBlank() && !weiStr.isBlank()) {
                        float wei = Float.parseFloat(weiStr);
                        float hei = Float.parseFloat(heiStr) / 100;
                        float bmi = wei / (hei * hei);
                        this.athleteBmiMap.put(athleteId, bmi);
                    }

                    this.athleteIdToBirth.put(athleteId, birthDay[birthDay.length - 1]);
                });
    }

    private void processAthleteEventResults() {
        String resultsPath = datasetPath + "/Olympic_Athlete_Event_Results.csv";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(resultsPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Integer> femaleGoldCount = new HashMap<>();

        Map<String, Set<Integer>> sportYearsMap = new HashMap<>();

        Map<String, List<Integer>> nocAgesMap = new HashMap<>();

        Set<String> ageHasComputeSet = new HashSet<>();

        br.lines()
                .map(MyUtil::parseCsv)
                .skip(1).forEach(values -> {
                    String[] edition = values.get(0).split(" ");
                    String sport = values.get(3);
                    String athleteId = values.get(7);
                    String medal = values.get(9);
                    String team = values.get(10);

                    boolean isTeam = team.equals("True");

                    if (medal.equals("Gold") && !isTeam) {
                        if (this.femaleAthleteIds.contains(athleteId)) {
                            femaleGoldCount.put(athleteId, femaleGoldCount.getOrDefault(athleteId, 0) + 1);
                        }
                    }

                    Float bmi = this.athleteBmiMap.get(athleteId);
                    if (bmi != null) {
                        Set<String> sportAthletesSet = this.sportAthletesMap.computeIfAbsent(sport, k -> new HashSet<>());
                        if (!sportAthletesSet.contains(athleteId)) {
                            sportAthletesSet.add(athleteId);
                            List<Float> sportBmi = this.sportBmiMap.computeIfAbsent(sport, k -> new ArrayList<>());
                            sportBmi.add(bmi);
                        }
                    }

                    if (edition[1].equals("Summer")) {
                        Set<Integer> yearsSet = sportYearsMap.computeIfAbsent(sport, k -> new TreeSet<>());
                        yearsSet.add(Integer.parseInt(edition[0]));

                        if (edition[0].equals("2020")) {
                            if (!ageHasComputeSet.contains(athleteId)) {
                                ageHasComputeSet.add(athleteId);

                                String birthYear = this.athleteIdToBirth.get(athleteId);
                                if (birthYear != null) {
                                    String noc = values.get(2);
                                    List<Integer> ageList = nocAgesMap.computeIfAbsent(noc, k -> new ArrayList<>());

                                    int age = 2020 - Integer.parseInt(birthYear);
                                    ageList.add(age);
                                }
                            }
                        }
                    }
                });

        Map<String, Integer> femaleGoldCountName = femaleGoldCount.entrySet().stream()
                .collect(Collectors.toMap(e -> this.femaleIdToName.get(e.getKey()), Map.Entry::getValue));

        this.topPerformantFemaleMap = femaleGoldCountName.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, Integer> o) -> o.getValue()).reversed()
                        .thenComparing(Map.Entry::getKey))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        this.leastSportYearsMap = sportYearsMap.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, Set<Integer>> o) -> o.getValue().size())
                        .thenComparing(Map.Entry::getKey))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        this.sportBmiAvgMap = this.sportBmiMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    List<Float> bmiList = e.getValue();
                    double avg = bmiList.stream().collect(Collectors.averagingDouble(Float::doubleValue));
                    return MyUtil.roundOneDigit((float) avg);
                }));

        this.sportBmiAvgMap = this.sportBmiAvgMap.entrySet().stream()
                .sorted(Comparator.comparingDouble((Map.Entry<String, Float> o) -> o.getValue()).reversed()
                        .thenComparing(Map.Entry::getKey))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Map<String, Integer> countryAgeMap = nocAgesMap.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> this.countryCodeToName.get(e.getKey()),
                        e -> {
                            List<Integer> ageList = e.getValue();
                            double avg = ageList.stream().collect(Collectors.averagingInt(Integer::intValue));
                            return (int) Math.round(avg);
                        })
                );

        this.topCountryAgeMap = countryAgeMap.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, Integer> o) -> o.getValue())
                        .thenComparing(Map.Entry::getKey))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private void processGamesMedal() {
        String gamesMedalPath = datasetPath + "/Olympic_Games_Medal_Tally.csv";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(gamesMedalPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Integer> countryWinterMedal = new HashMap<>();

        br.lines()
                .map(MyUtil::parseCsv)
                .skip(1).forEach(values -> {
                    String[] edition = values.get(0).split(" ");

                    if (edition[1].equals("Winter") && edition[0].compareTo("1999") > 0) {
                        String noc = values.get(4);
                        int count = Integer.parseInt(values.get(8));

                        countryWinterMedal.put(noc, countryWinterMedal.getOrDefault(noc, 0) + count);
                    }
                });

        this.topCountryWinterMedal = countryWinterMedal.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, Integer> o) -> o.getValue()).reversed()
                        .thenComparing(Map.Entry::getKey))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }


    @Override
    public Map<String, Integer> topPerformantFemale() {
        return this.topPerformantFemaleMap;
    }

    @Override
    public Map<String, Float> bmiBySports() {
        return this.sportBmiAvgMap;
    }

    @Override
    public Map<String, Set<Integer>> leastAppearedSport() {
        return this.leastSportYearsMap;
    }

    @Override
    public Map<String, Integer> winterMedalsByCountry() {
        return this.topCountryWinterMedal;
    }

    @Override
    public Map<String, Integer> topCountryWithYoungAthletes() {
        return this.topCountryAgeMap;
    }
}

class MyUtil {
    static List<String> parseCsv(String line) {
        List<String> values = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        boolean inQuote = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuote = !inQuote;
            } else if (c == ',' && !inQuote) {
                values.add(sb.toString());
                sb = new StringBuilder();
            } else {
                if (!(sb.isEmpty() && c == ' ')) {
                    sb.append(c);
                }
            }
        }
        values.add(sb.toString());

        return values;
    }

    static float roundOneDigit(float f) {
        return Math.round(f * 10) / 10.0f;
    }
}