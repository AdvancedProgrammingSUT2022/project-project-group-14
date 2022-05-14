import controllers.MapController;
import controllers.MoveController;
import controllers.UnitController;
import controllers.WorldController;
import enums.Improvements;
import enums.tiles.TileBaseTypes;
import enums.tiles.TileFeatureTypes;
import models.City;
import models.Civilization;
import models.Tile;
import models.units.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class UnitControllerTest {
    @Mock
    Unit unit;
    @Mock
    Melee melee;
    @Mock
    Ranged ranged;
    @Mock
    Settler settler;
    @Mock
    Worker worker;
    @Mock
    Improvements improvement;


    @BeforeEach
    public void setUpWorld() {
        ArrayList<String> usernames = new ArrayList<>();
        usernames.add("ali"); usernames.add("hassan");
        WorldController.newWorld(usernames);
    }

    @Test
    public void setUnitDestinationCoordinatesTest() {
        when(unit.getCivilizationName()).thenReturn("ali");
        UnitController.setUnitDestinationCoordinates(unit, 10, 10);
        if (MoveController.impossibleToMoveToTile(10, 10, unit) == null)
            verify(unit).setDestinationCoordinates(10, 10);
    }

    @Test
    public void resetMovingPointsTest() {
        Civilization currentCivilization = WorldController.getWorld().getCivilizationByName("ali");
        currentCivilization.addMeleeUnit(melee);
        when(melee.getName()).thenReturn("warrior");
        UnitController.resetMovingPoints(currentCivilization);
        verify(melee).setMovementPoint(2);
    }

    @Test
    public void cancelMissionTest() {
        when(unit.getCivilizationName()).thenReturn("ali");
        UnitController.cancelMission(unit);
        verify(unit).cancelMission();
    }

    @Test
    public void sleepUnitTest() {
        when(unit.getCivilizationName()).thenReturn("ali");
        UnitController.sleepUnit(unit);
        verify(unit).putToSleep();
    }

    @Test
    public void wakeUpTest() {
        when(melee.getCivilizationName()).thenReturn("ali");
        UnitController.wakeUp(melee);
        verify(melee).wakeUp();
        verify(melee).wakeUpFromAlert();
    }

    @Test
    public void alertUnitTest() {
        when(melee.getCivilizationName()).thenReturn("ali");
        UnitController.alertUnit(melee);
        verify(melee).alertUnit();
    }

    @Test
    public void fortifyUnitTest() {
        when(melee.getCivilizationName()).thenReturn("ali");
        UnitController.fortifyUnit(melee);
        verify(melee).healUnit(5);
    }

    @Test
    public void fortifyUnitUntilHealedTest() {
        when(melee.getCivilizationName()).thenReturn("ali");
        UnitController.fortifyUnitUntilHealed(melee);
        verify(melee).fortifyUnitTillHealed();
        verify(melee).healUnit(5);
    }

    @Test
    public void setupRangedUnitTest() {
        when(ranged.getCivilizationName()).thenReturn("ali");
        when(ranged.isSiegeUnit()).thenReturn(true);
        UnitController.setupRangedUnit(ranged, 10, 10);
        verify(ranged).setCoordinatesToSetup(10, 10);
    }

    @Test
    public void garrisonCityTest() {
        when(melee.getCivilizationName()).thenReturn("ali");
        MapController.getTileByCoordinates(melee.getCurrentX(), melee.getCurrentY()).setCity(new City("ali1", melee.getCurrentX(), melee.getCurrentY()));
        UnitController.garrisonCity(melee);
        verify(melee).garrisonUnit();
        Assertions.assertTrue(MapController.getTileByCoordinates(melee.getCurrentX(), melee.getCurrentY()).getCity().getNumberOfGarrisonedUnit() > 0);
    }

    @Test
    public void foundCityTest() {
        when(settler.getCivilizationName()).thenReturn("ali");
        UnitController.foundCity(settler);
        Assertions.assertNotNull(MapController.getTileByCoordinates(settler.getCurrentX(), settler.getCurrentY()).getCity());
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getCities());
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void buildRoadTest() {
        when(worker.getCivilizationName()).thenReturn("ali");
        UnitController.buildRoad(worker);
        Assertions.assertEquals(3, MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY()).getRoadState());
        verify(worker).putToWork(3);
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void buildRailRoadTest() {
        when(worker.getCivilizationName()).thenReturn("ali");
        UnitController.buildRailRoad(worker);
        Assertions.assertEquals(3, MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY()).getRailRoadState());
        verify(worker).putToWork(3);
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void removeRouteFromTileTest() {
        when(worker.getCivilizationName()).thenReturn("ali");
        MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY()).setRoadState(0);
        UnitController.removeRouteFromTile(worker);
        Assertions.assertEquals(9999, MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY()).getRailRoadState());
        Assertions.assertEquals(9999, MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY()).getRoadState());
        verify(worker).putToWork(3);
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void buildImprovementTest() {
        when(worker.getCivilizationName()).thenReturn("ali");
        when(improvement.getRequiredTechnology()).thenReturn(null);
        when(improvement.getPossibleTiles()).thenReturn(new HashSet<>(List.of(MapController.getTileByCoordinates(0, 0).getType())));
        when(improvement.name()).thenReturn("kham");
        WorldController.getWorld().getCivilizationByName(worker.getCivilizationName()).getTechnologies().put(improvement.getRequiredTechnology(), 0);
        UnitController.buildImprovement(worker, improvement);
        Assertions.assertEquals(improvement, MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY()).getImprovement());
        Assertions.assertEquals(6, MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY()).getImprovementTurnsLeftToBuild());
        verify(worker).putToWork(6);
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void removeJungleFromTileTest() {
        when(worker.getCivilizationName()).thenReturn("ali");
        MapController.getTileByCoordinates(0, 0).setFeature(TileFeatureTypes.JUNGLE);
        UnitController.removeJungleFromTile(worker);
        Assertions.assertNull(MapController.getTileByCoordinates(0, 0).getFeature());
        verify(worker).putToWork(3);
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void removeForestFromTileTest() {
        when(worker.getCivilizationName()).thenReturn("ali");
        MapController.getTileByCoordinates(0, 0).setFeature(TileFeatureTypes.FOREST);
        UnitController.removeForestFromTile(worker);
        Assertions.assertNull(MapController.getTileByCoordinates(0, 0).getFeature());
        verify(worker).putToWork(3);
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void removeMarshFromTileTest() {
        when(worker.getCivilizationName()).thenReturn("ali");
        MapController.getTileByCoordinates(0, 0).setFeature(TileFeatureTypes.SWAMP);
        UnitController.removeMarshFromTile(worker);
        Assertions.assertNull(MapController.getTileByCoordinates(0, 0).getFeature());
        verify(worker).putToWork(3);
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void repairTileTest() {
        when(worker.getCivilizationName()).thenReturn("ali");
        MapController.getTileByCoordinates(0, 0).setPillageState(9999);
        UnitController.repairTile(worker);
        Assertions.assertEquals(3, MapController.getTileByCoordinates(0, 0).getPillageState());
        verify(worker).putToWork(3);
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void pillageTest() {
        when(melee.getCivilizationName()).thenReturn("ali");
        WorldController.setSelectedCombatUnit(melee);
        MapController.getTileByCoordinates(0, 0).setPillageState(0);
        UnitController.pillage(0, 0);
        Assertions.assertEquals(9999, MapController.getTileByCoordinates(0, 0).getPillageState());
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void deleteTest() {
        when(settler.getCivilizationName()).thenReturn("ali");
        WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).addSettler(settler);
        UnitController.delete(settler);
        Assertions.assertFalse(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getAllUnits().contains(worker));
        Assertions.assertNotNull(WorldController.getWorld().getCivilizationByName(WorldController.getWorld().getCurrentCivilizationName()).getNotifications());
    }

    @Test
    public void upgradeUnitTest() {
        WorldController.setSelectedCombatUnit(melee);
        enums.units.Unit unitEnum = enums.units.Unit.SWORD_MAN;

    }
}
