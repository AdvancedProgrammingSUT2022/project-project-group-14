package controllers;

import enums.Improvements;
import models.*;
import models.units.*;

public class UnitController {

    public static String setUnitDestinationCoordinates(Unit unit, int x, int y) {
        String reason;
        if (!unit.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "the unit is not under your control";
        } else if ((reason = MoveController.impossibleToMoveToTile(x, y, unit)) != null) {
            return reason;
        } else {
            unit.setDestinationCoordinates(x, y);
        }
        return null;
    }

    public static String cancelMission(Unit unit) {
        if (!unit.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "unit is not under your control";
        } else {
            unit.cancelMission();
        }
        return null;
    }

    public static String sleepUnit(Unit unit) {
        if (!unit.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "unit is not under your control";
        } else {
            unit.putToSleep();
        }
        return null;
    }

    public static String wakeUp(Unit unit) {
        if (!unit.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "unit is not under your control";
        } else {
            unit.wakeUp();
        }
        return null;
    }

    public static String alertUnit(CombatUnit unit) {
        if (!unit.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName()))
            return "unit is not under your control";
        unit.alertUnit();
        return null;
    }

    public static String fortifyUnit(CombatUnit unit) {
        if (!unit.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName()))
            return "unit is not under your control";
        unit.healUnit(5);
        return null;
    }

    public static String fortifyUnitUntilHealed(CombatUnit unit) {
        if (!unit.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName()))
            return "unit is not under your control";
        unit.fortifyUnitTillHealed();
        unit.addHealthPoint(5);
        return null;
    }

    public static String setupRangedUnit(Unit unit, int x, int y) {
        if (!unit.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "unit is not under your control";
        } else if (unit instanceof Ranged && ((Ranged) unit).isSiegeUnit()) {
            ((Ranged) unit).setCoordinatesToSetup(x, y);
        } else {
            return "this unit doesn't have the ability to setup";
        }
        return null;
    }

    public static String garrisonCity(CombatUnit combatUnit) {
        Tile currentTile = MapController.getTileByCoordinates(combatUnit.getCurrentX(), combatUnit.getCurrentY());
        if (!combatUnit.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "the unit is not under your control";
        } else if (currentTile.getCity() == null) {
            return "you should be in a city to garrison";
        } else {
            combatUnit.garrisonUnit();
        }
        return null;
    }

    public static String foundCity(Settler settler) {
        Tile currentTile = MapController.getTileByCoordinates(settler.getCurrentX(), settler.getCurrentY());
        if (!settler.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "the unit is not under your control";
        } else if (currentTile.getCity() != null) {
            return "can not found a city over another city";
        } else if (currentTile.getCivilizationName() != null
                && !currentTile.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "can not found a city in enemy territory";
        } else {
            City city = new City();
            currentTile.setCity(city);
            WorldController.getWorld().getCivilizationByName(settler.getCivilizationName()).addCity(city);
        }
        return null;
    }

    public static String buildRoad(Worker worker) {
        Tile currentTile = MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY());
        if (!worker.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "the unit is not under your control";
        } else if (currentTile.hasRoad()) {
            return "there is already road on this tile";
        } else {
            //TODO build roads
        }
        return null;
    }

    public static String buildRailRoad(Worker worker) {
        Tile currentTile = MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY());
        if (!worker.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "the unit is not under your control";
        } else if (currentTile.hasRailRoad()) {
            return "there is already railRoad on this tile";
        } else {
            //TODO build railRoads
        }
        return null;
    }

    public static String removeRouteFromTile(Worker worker) {
        Tile currentTile = MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY());
        if (!worker.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "the unit is not under your control";
        } else if (!currentTile.hasRoad() && !currentTile.hasRailRoad()) {
            return "there is not any roads or railRoads on this tile";
        } else {
            currentTile.setHasRoad(false);
            currentTile.setHasRailRoad(false);
        }
        return null;
    }

    public static String buildProgress(Worker worker, Improvements progress) {
        Tile currentTile = MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY());
        if (!worker.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "the unit is not under your control";
        } else if (currentTile.getProgress().equals(progress)) {
            return "there is already this kind of progress on this tile";
        } else {
            //TODO build progress
        }
        return null;
    }

    public static String removeJungleFromTile(Worker worker) {
        Tile currentTile = MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY());
        if (!worker.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "the unit is not under your control";
        } else if (true) {
            //TODO check that the tile has a jungle
            return "there is not a jungle on this tile";
        } else {
            //TODO remove the jungle
        }
        return null;
    }

    public static String repairTile(Worker worker) {
        Tile currentTile = MapController.getTileByCoordinates(worker.getCurrentX(), worker.getCurrentY());
        if (!worker.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "the unit is not under your control";
        } else if (currentTile.isPillaged()) {
            return "this tile has not been pillaged";
        } else {
            worker.putToWork();
            //TODO repair the tile
        }
        return null;
    }

    public static String delete(Unit unit) {
        Civilization wantedCivilization = WorldController.getWorld().getCivilizationByName(unit.getCivilizationName());
        if (!unit.getCivilizationName().equals(WorldController.getWorld().getCurrentCivilizationName())) {
            return "unit is not under your control";
        } else {
            if (unit instanceof Ranged) {
                wantedCivilization.removeRangedUnit((Ranged) unit);
            } else if (unit instanceof Melee) {
                wantedCivilization.removeMeleeUnit((Melee) unit);
            } else if (unit instanceof Worker) {
                wantedCivilization.removeWorker((Worker) unit);
            } else if (unit instanceof Settler) {
                wantedCivilization.removeSettler((Settler) unit);
            }
        }
        return null;
    }
}
