
package models;

import controllers.MapController;
import controllers.MoveController;
import controllers.WorldController;
import enums.Researches;
import enums.Technologies;
import enums.resources.LuxuryResourceTypes;
import enums.resources.StrategicResourceTypes;
import enums.tiles.TileBaseTypes;
import models.resources.LuxuryResource;
import models.resources.StrategicResource;
import models.units.*;

import java.util.*;

public class Civilization {
    private String name;
    private int[][] visionStatesOfMap = new int[MapController.getWidth()][MapController.getLength()];
    private Tile[][] revealedTiles = new Tile[MapController.getWidth()][MapController.getLength()];

    private ArrayList<Melee> melees = new ArrayList<>();
    private ArrayList<Ranged> ranges = new ArrayList<>();
    private ArrayList<Worker> workers = new ArrayList<>();
    private ArrayList<Settler> settlers = new ArrayList<>();

    private ArrayList<City> cities = new ArrayList<>();
    private ArrayList<String> citiesNames = new ArrayList<>();
    private HashMap<String, Integer> strategicResources = new HashMap<>();
    private HashMap<String, Integer> luxuryResources = new HashMap<>();
    private City firstCapital;
    private City currentCapital;

    private HashMap<Technologies, Integer> technologies = new HashMap<>();
    private Technologies currentTechnology;
    private ArrayList<Researches> researches = new ArrayList<>();

    private double gold, happiness, science;

    ArrayList<String> notifications = new ArrayList<>();

    public Civilization(String name) {
        Random random = new Random();
        int randomX, randomY;
        while (true){
            randomX = random.nextInt(MapController.width);
            randomY = random.nextInt(MapController.length);
            if (MapController.getMap()[randomX][randomY].getType().getMovingPoint() != 9999)
                break;
        }
        Melee melee = new Melee(enums.units.Unit.getUnitByName("warrior"), randomX, randomY, name);
        Settler settler = new Settler(enums.units.Unit.getUnitByName("settler"), randomX, randomY, name);
        addMeleeUnit(melee);
        addSettler(settler);
        this.name = name;
        this.happiness = 10;
        citiesNames.add(name + "1"); citiesNames.add(name + "2"); citiesNames.add(name + "3");

        for (Technologies technology : Technologies.values()) {
            technologies.put(technology, technology.getCost());
        }

        for (StrategicResourceTypes strategicResource : StrategicResourceTypes.values()) {
            strategicResources.put(strategicResource.name(), 0);
        }

        for (LuxuryResourceTypes luxuryResource : LuxuryResourceTypes.values()) {
            luxuryResources.put(luxuryResource.name(), 0);
        }
    }

    public String getName() {
        return name;
    }

    public int[][] getVisionStatesOfMap() {
        return this.visionStatesOfMap;
    }

    public Tile[][] getRevealedTiles() {
        return this.revealedTiles;
    }

    public void addMeleeUnit(Melee unit) {
        melees.add(unit);
    }

    public void addRangedUnit(Ranged unit) {
        ranges.add(unit);
    }

    public void addSettler(Settler unit) {
        settlers.add(unit);
    }

    public void addWorker(Worker unit) {
        workers.add(unit);
    }

    public void removeMeleeUnit(Melee unit) {
        melees.remove(unit);
    }

    public void removeRangedUnit(Ranged unit) {
        ranges.remove(unit);
    }

    public void removeWorker(Worker unit) {
        workers.remove(unit);
    }

    public void removeSettler(Settler unit) {
        settlers.remove(unit);
    }

    public ArrayList<Unit> getAllUnits() {
        ArrayList<Unit> allUnits = new ArrayList<>();
        allUnits.addAll(melees);
        allUnits.addAll(ranges);
        allUnits.addAll(workers);
        allUnits.addAll(settlers);
        return allUnits;
    }

    public ArrayList<Melee> getMelees() {
        return melees;
    }

    public ArrayList<Ranged> getRanges() {
        return ranges;
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public ArrayList<Settler> getSettlers() {
        return settlers;
    }

    public double getGold() {
        return gold;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void addCity(City city) {
        if(cities.size() == 0 && firstCapital == null) {
            firstCapital = city;
            currentCapital = city;
        }
        cities.add(city);
        happiness -= 2;
    }

    public void removeCity(City city) {
        if (cities.size() == 1) {
            WorldController.getWorld().removeCivilization(this);
        } else {
            cities.remove(city);
            currentCapital = cities.get(0);
            happiness += 2;
        }
    }

    public String getCityName() {
        String name = citiesNames.get(0);
        citiesNames.remove(0);
        return name;
    }

    public HashMap<String, Integer> getStrategicResources() {
        return strategicResources;
    }

    public HashMap<String, Integer> getLuxuryResources() {
        return luxuryResources;
    }

    public HashMap<Technologies, Integer> getTechnologies() {
        return technologies;
    }

    public City getCurrentCapital() {
        return currentCapital;
    }

    public Technologies getCurrentTechnology() {
        return currentTechnology;
    }

    public void setCurrentTechnology(Technologies wantedTechnology) {
        this.currentTechnology = wantedTechnology;
    }

    public void addGold(double amount) {
        gold += amount;
    }

    public void addScience(double amount) {
        this.science += amount;
    }

    public void setHappiness(double happiness) {
        this.happiness = happiness;
    }

    public void setScience(double science) {
        this.science = science;
    }

    public void addHappiness(double  amount) {
        this.happiness += amount;
    }

    public double getHappiness() {
        return happiness;
    }

    public double getScience() {
        return science;
    }

    public City getFirstCapital() {
        return firstCapital;
    }

    public void addNotification(String notification) {
        notifications.add(notification);
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public String getInfo() {
        int totalTiles = 0, totalTechnologiesAcquired = 0;
        for (City city : cities) {
            totalTiles += city.getTerritory().size();
        }
        for (Integer value : technologies.values()) {
            if (value <= 0)
                totalTechnologiesAcquired++;
        }

        return "Total tiles in map : " + totalTiles + "\n" +
                "Gold : " + gold + "\n" +
                "Happiness : " + happiness + "\n" +
                "Science : " + science + "\n" +
                "Total number of units : " + getAllUnits().size() + "\n" +
                "Total number of cities : " + cities.size() + "\n" +
                "Total Technologies Acquired : " + totalTechnologiesAcquired + "\n";
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

}
