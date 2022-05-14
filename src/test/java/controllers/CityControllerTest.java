package controllers;

import enums.tiles.TileBaseTypes;
import enums.tiles.TileFeatureTypes;
import models.Citizen;
import models.City;
import models.Civilization;
import models.Tile;
import models.units.NonCombatUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CityControllerTest {
    @Mock
    City city;
    @Mock
    NonCombatUnit nonCombatUnit;

    @BeforeEach
    public void setUpWorld() {
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add("ali"); usernames.add("hassan");
        WorldController.newWorld(usernames);
    }

    @Test
    public void getCityByNameTest() {
        when(city.getName()).thenReturn("ali1");
        WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).addCity(city);
        Assertions.assertNotNull(CityController.getCityByName("ali1"));

    }

    @Test
    public void civilizationHasDiscoveredCityTest() {
        Assertions.assertFalse(CityController.civilizationHasDiscoveredCity(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()), city));
    }

    @Test
    public void addGoodsToCityTest() {
        ArrayList<Citizen> citizens = new ArrayList<>();
        citizens.add(new Citizen(1));
        citizens.get(0).setIsWorking(true);
        citizens.get(0).setXOfWorkingTile(10);
        citizens.get(0).setYOfWorkingTile(10);
        Tile tile = new Tile(TileFeatureTypes.OASIS, TileBaseTypes.PLAIN, 10 ,10);
        tile.setCivilization("ali");
        MapController.getMap()[10][10] = tile;
        when(city.getCitizens()).thenReturn(citizens);
        when(city.getFood()).thenReturn(20.0);
        when(city.getCenterOfCity()).thenReturn(tile);
        CityController.addGoodsToCity(city);
        verify(city).setFood(0);
        verify(city).setProduction(2.0);
    }

    @Test
    public void consumeCityFoodTest() {
        ArrayList<Citizen> citizens = new ArrayList<>();
        citizens.add(new Citizen(1));
        citizens.add(new Citizen(2));
        Tile tile = new Tile(TileFeatureTypes.OASIS, TileBaseTypes.PLAIN, 10 ,10);
        tile.setCivilization("ali");
        MapController.getMap()[10][10] = tile;
        when(city.getCitizens()).thenReturn(citizens);
        when(city.getCenterOfCity()).thenReturn(tile);
        CityController.consumeCityFood(3.0, city);
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());

    }

    @Test
    public void starveCitizenTest() {
        ArrayList<Citizen> citizens = new ArrayList<>();
        citizens.add(new Citizen(1));
        citizens.get(0).setIsWorking(true);
        citizens.get(0).setXOfWorkingTile(10);
        citizens.get(0).setYOfWorkingTile(10);
        Tile tile = new Tile(TileFeatureTypes.OASIS, TileBaseTypes.PLAIN, 10 ,10);
        tile.setCivilization("ali");
        MapController.getMap()[10][10] = tile;
        when(city.getCitizens()).thenReturn(citizens);
        when(city.getCenterOfCity()).thenReturn(tile);
        CityController.starveCitizen(city);
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void lockCitizenToTileTest() {
        Tile tile = new Tile(TileFeatureTypes.OASIS, TileBaseTypes.PLAIN, 10 ,10);
        tile.setCivilization("ali");
        MapController.getMap()[10][10] = tile;
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(MapController.getTileByCoordinates(10, 10));
        tiles.add(MapController.getTileByCoordinates(11, 11));
        ArrayList<Citizen> citizens = new ArrayList<>();
        citizens.add(new Citizen(1));
        when(city.getTerritory()).thenReturn(tiles);
        when(city.getCitizens()).thenReturn(citizens);
        CityController.lockCitizenToTile(city, 1, 10, 10);
        Assertions.assertNotEquals(-1, citizens.get(0).getXOfWorkingTile());
        Assertions.assertNotEquals(-1, citizens.get(0).getYOfWorkingTile());
        Assertions.assertTrue(citizens.get(0).isWorking());
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void unlockCitizenFromTileTest() {
        Tile tile = new Tile(TileFeatureTypes.OASIS, TileBaseTypes.PLAIN, 10 ,10);
        tile.setCivilization("ali");
        MapController.getMap()[10][10] = tile;
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(MapController.getTileByCoordinates(10, 10));
        tiles.add(MapController.getTileByCoordinates(11, 11));
        ArrayList<Citizen> citizens = new ArrayList<>();
        citizens.add(new Citizen(1));
        citizens.get(0).setIsWorking(true);
        citizens.get(0).setXOfWorkingTile(10);
        citizens.get(0).setYOfWorkingTile(10);
        when(city.getCitizens()).thenReturn(citizens);
        CityController.unlockCitizenFromTile(city, 1);
        Assertions.assertEquals(-1, citizens.get(0).getXOfWorkingTile());
        Assertions.assertEquals(-1, citizens.get(0).getYOfWorkingTile());
        Assertions.assertFalse(citizens.get(0).isWorking());
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());

    }

    @Test
    public void updateCityProductionGoldPayingTest() {
        WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).addCity(city);
        when(city.getCurrentProductionRemainingCost()).thenReturn(1.0);
        when(city.isPayingGoldForCityProduction()).thenReturn(true);
        CityController.updateCityProduction(city);
        verify(city).setCurrentProductionRemainingCost(1.0);
    }

    @Test
    public void updateCityProductionNotGoldPayingTest() {
        WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).addCity(city);
        when(city.getCurrentProductionRemainingCost()).thenReturn(1.0);
        when(city.isPayingGoldForCityProduction()).thenReturn(false);
        when(city.getProduction()).thenReturn(1.0);
        CityController.updateCityProduction(city);
        verify(city).setCurrentProductionRemainingCost(0.0);
    }

    @Test
    public void cityProductionWarningsTest() {
        when(city.getCurrentUnit()).thenReturn(nonCombatUnit);
        MapController.getTileByCoordinates(10, 10).setNonCombatUnit(nonCombatUnit);
        when(city.getCenterOfCity()).thenReturn(MapController.getTileByCoordinates(10, 10));
        CityController.cityProductionWarnings(city);
    }
}
