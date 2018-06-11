package com.etermax.spacehorse.core.catalog.model.csv;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.stream.Collectors;

import org.junit.After;

import com.etermax.spacehorse.core.catalog.model.csv.importer.CatalogsCSVImporter;
import com.etermax.spacehorse.core.catalog.model.csv.sheet.importer.SheetsImporterCSVs;

public class CatalogCSVTest {
	protected  final String[] FULL_SHEETS_NAMES = new String[] { "Maps", "GameModes", "Buildings", "Units", "Cards", "GameConstants" };

	protected String csv;
	protected CatalogsCSVCollection catalogs;

	@After
	public void after() {
		csv = null;
		catalogs = null;
	}

	protected void thenTheCatalogsAreValid() {
		assertThat(catalogs.getCatalogsList().stream().map(CatalogCSV::getName).collect(Collectors.toList()), hasItems(FULL_SHEETS_NAMES));
		catalogs.getCatalogsList().forEach(catalog -> assertTrue(catalog.getRowsCount() > 0));
	}

	protected void givenACsvWithOneFieldOfEachType() {

		String masterLists = CatalogsCSVImporter.MasterlistsName + "\n" + "SheetName\n" + "Sheet1\n";

		String masterListsFields =
				CatalogsCSVImporter.MasterlistsFieldsName + "\n" + "SheetName,FieldName,FieldType,EnumType\n" + "Sheet1,FieldInt,int,\n"
						+ "Sheet1,FieldIntArray,int[],\n" + "Sheet1,FieldString,string,\n" + "Sheet1,FieldStringArray,string[],\n"
						+ "Sheet1,FieldBool,bool,\n" + "Sheet1,FieldFint,fint,\n" + "Sheet1,FieldEnum,enum,TestEnum\n"
						+ "Sheet1,FieldEnumArray,enum[],TestEnum\n";

		String masterListsEnums =
				CatalogsCSVImporter.MasterlistsEnumsName + "\n" + "EnumId,Id,Value\n" + "TestEnum,val0,0\n" + "TestEnum,val1,1\n" + "TestEnum,val2,2\n";

		String sheet1 = "Sheet1\n" + "FieldInt,FieldIntArray,FieldString,FieldStringArray,FieldBool,FieldFint,FieldEnum,FieldEnumArray\n"
				+ "1,\"2,3,4\",fede,\"fede1,fede2,fede3\",true,123.25,val0,\"val1,val2\"\n";

		csv = masterLists + SheetsImporterCSVs.SheetsCsvSeparator + "\n" + masterListsFields + SheetsImporterCSVs.SheetsCsvSeparator + "\n"
				+ masterListsEnums + SheetsImporterCSVs.SheetsCsvSeparator + "\n" + sheet1;
	}

	protected void givenAFullTestCsv() {
		csv = "Masterlists\n" + "\"SheetName\"\n" + "\"Maps\"\n" + "\"GameModes\"\n" + "\"Buildings\"\n" + "\"Units\"\n" + "\"Cards\"\n"
				+ "\"GameConstants\"\n" + SheetsImporterCSVs.SheetsCsvSeparator + "\n" + "MasterlistsFields\n"
				+ "\"SheetName\",\"FieldName\",\"FieldType\",\"EnumType\"\n" + "\"Maps\",\"Id\",\"string\",\"\"\n"
				+ "\"Maps\",\"Name\",\"string\",\"\"\n" + "\"GameModes\",\"Id\",\"string\",\"\"\n" + "\"GameModes\",\"Name\",\"string\",\"\"\n"
				+ "\"GameModes\",\"ValidCardsIds\",\"string[]\",\"\"\n" + "\"GameModes\",\"ValidMapsIds\",\"string[]\",\"\"\n"
				+ "\"GameModes\",\"BattleWinCondition\",\"enum\",\"BattleWinCondition\"\n" + "\"GameModes\",\"ViewRangeDelta\",\"fint\",\"\"\n"
				+ "\"GameModes\",\"AttackRangeDelta\",\"fint\",\"\"\n" + "\"GameModes\",\"MainTrackUnitId\",\"string\",\"\"\n"
				+ "\"GameModes\",\"NumberOfCompanionTrackUnits\",\"int\",\"\"\n" + "\"GameModes\",\"CompanionTrackUnitIds\",\"string[]\",\"\"\n"
				+ "\"GameModes\",\"ValidRandomTrackObjectTypes\",\"enum[]\",\"TrackObjectType\"\n" + "\"Buildings\",\"Id\",\"string\",\"\"\n"
				+ "\"Buildings\",\"Name\",\"string\",\"\"\n" + "\"Buildings\",\"Model\",\"string\",\"\"\n"
				+ "\"Buildings\",\"Health\",\"fint\",\"\"\n" + "\"Units\",\"Id\",\"string\",\"\"\n" + "\"Units\",\"Name\",\"string\",\"\"\n"
				+ "\"Units\",\"Model\",\"string\",\"\"\n" + "\"Units\",\"Health\",\"fint\",\"\"\n" + "\"Units\",\"MoveSpeed\",\"fint\",\"\"\n"
				+ "\"Units\",\"GroupId\",\"string\",\"\"\n" + "\"Units\",\"AttackSplashDamageRadius\",\"fint\",\"\"\n"
				+ "\"Units\",\"AttackDamage\",\"fint\",\"\"\n" + "\"Units\",\"AttackDamageType\",\"enum\",\"DamageType\"\n"
				+ "\"Units\",\"AttackDelay\",\"fint\",\"\"\n" + "\"Units\",\"AttackRange\",\"fint\",\"\"\n"
				+ "\"Units\",\"AttackType\",\"enum\",\"AttackType\"\n" + "\"Units\",\"AttackTargets\",\"enum[]\",\"UnitAttackTargetType\"\n"
				+ "\"Units\",\"AttackGroupPriorities\",\"string[]\",\"\"\n" + "\"Units\",\"ViewRange\",\"fint\",\"\"\n"
				+ "\"Units\",\"Size\",\"fint\",\"\"\n" + "\"Units\",\"ProjectileSpeed\",\"fint\",\"\"\n"
				+ "\"Units\",\"UnitType\",\"enum\",\"UnitType\"\n" + "\"Units\",\"PowerUpType\",\"enum\",\"PowerUpType\"\n"
				+ "\"Units\",\"PowerUpParameter\",\"fint\",\"\"\n" + "\"Units\",\"PowerUpRange\",\"fint\",\"\"\n"
				+ "\"Units\",\"PowerUpDuration\",\"fint\",\"\"\n" + "\"Units\",\"TargetTeam\",\"enum\",\"TargetTeamType\"\n"
				+ "\"Units\",\"DropTrackObjectType\",\"enum\",\"TrackObjectType\"\n" + "\"Cards\",\"Id\",\"string\",\"\"\n"
				+ "\"Cards\",\"Name\",\"string\",\"\"\n" + "\"Cards\",\"EnergyCost\",\"int\",\"\"\n"
				+ "\"Cards\",\"CardAction\",\"enum\",\"CardActionType\"\n" + "\"Cards\",\"CardTarget\",\"enum\",\"CardTargetType\"\n"
				+ "\"Cards\",\"CardTargetRadius\",\"fint\",\"\"\n" + "\"Cards\",\"TargetTeam\",\"enum\",\"TargetTeamType\"\n"
				+ "\"Cards\",\"Icon\",\"string\",\"\"\n" + "\"Cards\",\"UnitId\",\"string\",\"\"\n" + "\"Cards\",\"UnitsAmount\",\"int\",\"\"\n"
				+ "\"Cards\",\"PowerUpType\",\"enum\",\"PowerUpType\"\n" + "\"Cards\",\"Duration\",\"fint\",\"\"\n"
				+ "\"Cards\",\"CastTime\",\"fint\",\"\"\n" + "\"Cards\",\"PowerUpParameter\",\"fint\",\"\"\n"
				+ "\"Cards\",\"PowerUpRange\",\"fint\",\"\"\n" + "\"Cards\",\"TrackObjectType\",\"enum\",\"TrackObjectType\"\n"
				+ "\"GameConstants\",\"Id\",\"string\",\"\"\n" + "\"GameConstants\",\"Value\",\"string\",\"\"\n"
				+ SheetsImporterCSVs.SheetsCsvSeparator + "\n" + "MasterlistsEnums\n" + "\"EnumId\",\"Id\",\"Value\"\n"
				+ "\"TestEnum\",\"val0\",\"0\"\n" + "\"TestEnum\",\"val1\",\"1\"\n" + "\"TestEnum\",\"val2\",\"2\"\n"
				+ "\"TestEnum\",\"val3\",\"3\"\n" + "\"BattleWinCondition\",\"DetroyEnemyBuildings\",\"0\"\n"
				+ "\"BattleWinCondition\",\"DestroyEnemyTrackUnits\",\"2\"\n" + "\"AttackType\",\"Melee\",\"0\"\n"
				+ "\"AttackType\",\"Ranged\",\"1\"\n" + "\"UnitType\",\"Default\",\"0\"\n" + "\"UnitType\",\"TrackUnit\",\"1\"\n"
				+ "\"UnitType\",\"TrackActivable\",\"3\"\n" + "\"UnitType\",\"TrackDropObject\",\"4\"\n" + "\"UnitType\",\"MainTrackUnit\",\"5\"\n"
				+ "\"UnitType\",\"Ship\",\"6\"\n" + "\"UnitType\",\"Stationary\",\"7\"\n" + "\"UnitType\",\"CompanionTrackUnit\",\"8\"\n"
				+ "\"TargetTeamType\",\"SameTeam\",\"0\"\n" + "\"TargetTeamType\",\"OpponentTeam\",\"1\"\n" + "\"PowerUpType\",\"None\",\"0\"\n"
				+ "\"PowerUpType\",\"Invisible\",\"4\"\n" + "\"PowerUpType\",\"DisableCards\",\"5\"\n"
				+ "\"PowerUpType\",\"ReduceVisibility\",\"6\"\n" + "\"PowerUpType\",\"Protection\",\"7\"\n"
				+ "\"PowerUpType\",\"RemoveNegativeEffects\",\"10\"\n" + "\"PowerUpType\",\"Arrows\",\"13\"\n"
				+ "\"PowerUpType\",\"Meteorite\",\"14\"\n" + "\"PowerUpType\",\"Laser\",\"15\"\n" + "\"CardActionType\",\"None\",\"0\"\n"
				+ "\"CardActionType\",\"SpawnUnit\",\"1\"\n" + "\"CardActionType\",\"UsePowerUp\",\"2\"\n"
				+ "\"CardActionType\",\"DropTrackObject\",\"3\"\n" + "\"CardActionType\",\"MovableTrackObject\",\"4\"\n"
				+ "\"CardTargetType\",\"None\",\"0\"\n" + "\"CardTargetType\",\"DeployArea\",\"1\"\n" + "\"CardTargetType\",\"TrackUnit\",\"2\"\n"
				+ "\"CardTargetType\",\"TrackRace\",\"3\"\n" + "\"CardTargetType\",\"Anywhere\",\"5\"\n"
				+ "\"CardTargetType\",\"NearTrackUnit\",\"6\"\n" + "\"TrackObjectType\",\"None\",\"0\"\n" + "\"TrackObjectType\",\"Poop\",\"1\"\n"
				+ "\"TrackObjectType\",\"Mana\",\"2\"\n" + "\"TrackObjectType\",\"BearTrap\",\"3\"\n" + "\"TrackObjectType\",\"Wall\",\"4\"\n"
				+ "\"TrackObjectType\",\"Missile\",\"5\"\n" + "\"TrackObjectType\",\"Decoy\",\"6\"\n" + "\"DamageType\",\"None\",\"0\"\n"
				+ "\"DamageType\",\"Default\",\"1\"\n" + "\"DamageType\",\"Heal\",\"2\"\n" + "\"UnitAttackTargetType\",\"None\",\"0\"\n"
				+ "\"UnitAttackTargetType\",\"UnitDefinition\",\"1\"\n" + "\"UnitAttackTargetType\",\"TrackUnit\",\"2\"\n"
				+ "\"UnitAttackTargetType\",\"TrackObject\",\"3\"\n" + SheetsImporterCSVs.SheetsCsvSeparator + "\n" + "Maps\n" + "\"Id\",\"Name\"\n"
				+ "\"Map1\",\"Map1\"\n" + "\"Map2\",\"Map2\"\n" + "\"Map3\",\"Map3\"\n" + "\"Map4\",\"Map4\"\n"
				+ "\"MapTrackSpaceBattle1\",\"MapTrackSpaceBattle1\"\n" + "\"MapTrackSpaceBattle2\",\"MapTrackSpaceBattle2\"\n"
				+ "\"MapTrackSpaceBattle3\",\"MapTrackSpaceBattle3\"\n" + SheetsImporterCSVs.SheetsCsvSeparator + "\n" + "GameModes\n"
				+ "\"Id\",\"Name\",\"ValidCardsIds\",\"ValidMapsIds\",\"BattleWinCondition\",\"ViewRangeDelta\",\"AttackRangeDelta\",\"MainTrackUnitId\",\"NumberOfCompanionTrackUnits\",\"CompanionTrackUnitIds\",\"ValidRandomTrackObjectTypes\"\n"
				+ "\"DefaultMode\",\"Classic Mode\",\"card_heavy,card_light,card_archer,card_worker,card_catapult,card_cavalry\",\"Map1,Map2,Map3,Map4\",\"DetroyEnemyBuildings\",\"0\",\"0\",\"\",\"0\",\"\",\"\"\n"
				+ "\"TrackSpaceBattle\",\"Space Battle\",\"card_fighter_space,card_bomber_space,card_corvette_space,card_corvette_bomber_space,card_frigate_space,card_cruiser_space,card_missile_space,card_laser_space,card_decoy_space,,card_mine_space\",\"MapTrackSpaceBattle1,MapTrackSpaceBattle2,MapTrackSpaceBattle3\",\"DestroyEnemyTrackUnits\",\"0\",\"0\",\"MainSpaceship\",\"0\",\"\",\"\"\n"
				+ "\"TrackSpaceBattle-1\",\"Space Battle\n"
				+ "(1 companion)\",\"card_fighter_space,card_bomber_space,card_corvette_space,card_corvette_bomber_space,card_frigate_space,card_cruiser_space,card_missile_space,card_laser_space,card_decoy_space,card_mine_space\",\"MapTrackSpaceBattle1,MapTrackSpaceBattle2,MapTrackSpaceBattle3\",\"DestroyEnemyTrackUnits\",\"0\",\"0\",\"MainSpaceship\",\"1\",\"CompanionSpaceshipAlone\",\"\"\n"
				+ "\"TrackSpaceBattle-2\",\"Space Battle\n"
				+ "(2 companions)\",\"card_fighter_space,card_bomber_space,card_corvette_space,card_corvette_bomber_space,card_frigate_space,card_cruiser_space,card_missile_space,card_laser_space,card_decoy_space,card_mine_space\",\"MapTrackSpaceBattle1,MapTrackSpaceBattle2,MapTrackSpaceBattle3\",\"DestroyEnemyTrackUnits\",\"0\",\"0\",\"MainSpaceship\",\"2\",\"CompanionSpaceshipFront,CompanionSpaceshipBack\",\"\"\n"
				+ SheetsImporterCSVs.SheetsCsvSeparator + "\n" + "Buildings\n" + "\"Id\",\"Name\",\"Model\",\"Health\"\n"
				+ "\"Building\",\"Building\",\"Building\",\"750\"\n" + SheetsImporterCSVs.SheetsCsvSeparator + "\n" + "Units\n"
				+ "\"Id\",\"Name\",\"Model\",\"Health\",\"MoveSpeed\",\"GroupId\",\"AttackSplashDamageRadius\",\"AttackDamage\",\"AttackDamageType\",\"AttackDelay\",\"AttackRange\",\"AttackType\",\"AttackTargets\",\"AttackGroupPriorities\",\"ViewRange\",\"Size\",\"ProjectileSpeed\",\"UnitType\",\"PowerUpType\",\"PowerUpParameter\",\"PowerUpRange\",\"PowerUpDuration\",\"TargetTeam\",\"DropTrackObjectType\"\n"
				+ "\"HeavyInfantry\",\"Heavy Infantry\",\"HeavyInfantry\",\"40\",\"1\",\"\",\"0\",\"15\",\"Default\",\"0.9\",\"0.6\",\"Melee\",\"UnitDefinition\",\"\",\"2\",\"0.25\",\"999\",\"Default\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"LightInfantry\",\"Light Infantry\",\"LightInfantry\",\"60\",\"1\",\"\",\"0\",\"8\",\"Default\",\"1.1\",\"0.6\",\"Melee\",\"UnitDefinition\",\"\",\"2\",\"0.25\",\"999\",\"Default\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"Archer\",\"Archer\",\"Archer\",\"25\",\"1\",\"\",\"0\",\"8\",\"Default\",\"1\",\"2.5\",\"Ranged\",\"UnitDefinition\",\"\",\"3\",\"0.25\",\"8\",\"Default\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"Cavalry\",\"Cavalry\",\"Cavalry\",\"90\",\"1.5\",\"\",\"0\",\"13\",\"Default\",\"1\",\"0.6\",\"Melee\",\"UnitDefinition\",\"\",\"2\",\"0.25\",\"999\",\"Default\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"Worker\",\"Weak Infantry\",\"Worker\",\"20\",\"1\",\"\",\"0\",\"8\",\"Default\",\"0.8\",\"0.6\",\"Melee\",\"UnitDefinition\",\"\",\"2\",\"0.25\",\"999\",\"Default\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"Catapult\",\"Catapult\",\"Catapult\",\"20\",\"0.5\",\"\",\"1\",\"15\",\"Default\",\"3\",\"3\",\"Ranged\",\"UnitDefinition\",\"\",\"4\",\"0.25\",\"5\",\"Default\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"MainSpaceship\",\"Main Spaceship\",\"Space/MainSpaceship\",\"2500\",\"0.3\",\"Building\",\"0\",\"25\",\"Default\",\"0.6\",\"1.5\",\"Ranged\",\"UnitDefinition\",\"\",\"3\",\"0.5\",\"12\",\"MainTrackUnit\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"CompanionSpaceshipAlone\",\"Companion Spaceship Front\",\"Space/CompanionSpaceship\",\"1500\",\"0.3\",\"Building\",\"0\",\"30\",\"Default\",\"0.8\",\"1.5\",\"Ranged\",\"UnitDefinition\",\"\",\"3\",\"0.25\",\"12\",\"CompanionTrackUnit\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"CompanionSpaceshipFront\",\"Companion Spaceship Front\",\"Space/CompanionSpaceship\",\"1500\",\"0.3\",\"Building\",\"0\",\"30\",\"Default\",\"0.8\",\"1.5\",\"Ranged\",\"UnitDefinition\",\"\",\"3\",\"0.25\",\"12\",\"CompanionTrackUnit\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"CompanionSpaceshipBack\",\"Companion Spaceship Back\",\"Space/CompanionSpaceship\",\"800\",\"0.3\",\"Building\",\"0\",\"30\",\"Default\",\"0.8\",\"1.5\",\"Ranged\",\"UnitDefinition\",\"\",\"3\",\"0.25\",\"12\",\"CompanionTrackUnit\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"FighterSpace\",\"Fighter\",\"Space/Fighter\",\"41\",\"1.3\",\"Small\",\"0\",\"13\",\"Default\",\"0.5\",\"0.8\",\"Ranged\",\"UnitDefinition\",\"Small\",\"3\",\"0.25\",\"10\",\"Ship\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"BomberSpace\",\"Bomber\",\"Space/Bomber\",\"31\",\"0.6\",\"Small\",\"0\",\"40\",\"Default\",\"1.5\",\"1.5\",\"Ranged\",\"UnitDefinition\",\"Big\",\"3\",\"0.25\",\"3\",\"Ship\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"CorvetteSpace\",\"Corvette\",\"Space/Corvette\",\"525\",\"0.9\",\"Medium\",\"0\",\"20\",\"Default\",\"0.4\",\"1.5\",\"Ranged\",\"UnitDefinition\",\"Small\",\"3\",\"0.25\",\"8\",\"Ship\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"CorvetteBomberSpace\",\"Corvette Bomber\",\"Space/CorvetteBomber\",\"300\",\"0.3\",\"Medium\",\"1\",\"55\",\"Default\",\"2\",\"4\",\"Ranged\",\"UnitDefinition\",\"Big\",\"5\",\"0.25\",\"2\",\"Ship\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"FrigateSpace\",\"Frigate\",\"Space/Frigate\",\"1245\",\"0.5\",\"Big\",\"0\",\"128\",\"Default\",\"2\",\"1.4\",\"Ranged\",\"UnitDefinition\",\"Medium\",\"3\",\"0.25\",\"6\",\"Ship\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"CruiserSpace\",\"Cruiser\",\"Space/Cruiser\",\"1810\",\"0.4\",\"Big\",\"0\",\"100\",\"Default\",\"2.5\",\"1.4\",\"Ranged\",\"UnitDefinition\",\"Building\",\"3\",\"0.25\",\"5\",\"Ship\",\"None\",\"0\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ "\"MineSpace\",\"Mine\",\"Space/Mine\",\"2000\",\"0\",\"Big\",\"0\",\"250\",\"Default\",\"0.5\",\"1\",\"Ranged\",\"UnitDefinition\",\"\",\"3\",\"0.25\",\"0\",\"Stationary\",\"None\",\"20\",\"0\",\"0\",\"OpponentTeam\",\"None\"\n"
				+ SheetsImporterCSVs.SheetsCsvSeparator + "\n" + "Cards\n"
				+ "\"Id\",\"Name\",\"EnergyCost\",\"CardAction\",\"CardTarget\",\"CardTargetRadius\",\"TargetTeam\",\"Icon\",\"UnitId\",\"UnitsAmount\",\"PowerUpType\",\"Duration\",\"CastTime\",\"PowerUpParameter\",\"PowerUpRange\",\"TrackObjectType\"\n"
				+ "\"card_heavy\",\"Heavy\",\"2\",\"SpawnUnit\",\"DeployArea\",\"0\",\"SameTeam\",\"\",\"HeavyInfantry\",\"1\",\"None\",\"0\",\"0\",\"0\",\"0\",\"None\"\n"
				+ "\"card_light\",\"Light\",\"2\",\"SpawnUnit\",\"DeployArea\",\"0\",\"SameTeam\",\"\",\"LightInfantry\",\"1\",\"None\",\"0\",\"0\",\"0\",\"0\",\"None\"\n"
				+ "\"card_archer\",\"Archer\",\"3\",\"SpawnUnit\",\"DeployArea\",\"0\",\"SameTeam\",\"\",\"Archer\",\"1\",\"None\",\"0\",\"0\",\"0\",\"0\",\"None\"\n"
				+ "\"card_cavalry\",\"Cavalry\",\"5\",\"SpawnUnit\",\"DeployArea\",\"0\",\"SameTeam\",\"\",\"Cavalry\",\"1\",\"None\",\"0\",\"0\",\"0\",\"0\",\"None\"\n"
				+ "\"card_worker\",\"Weak\",\"1\",\"SpawnUnit\",\"DeployArea\",\"0\",\"SameTeam\",\"\",\"Worker\",\"1\",\"None\",\"0\",\"0\",\"0\",\"0\",\"None\"\n"
				+ "\"card_catapult\",\"Catapult\",\"4\",\"SpawnUnit\",\"DeployArea\",\"0\",\"SameTeam\",\"\",\"Catapult\",\"1\",\"None\",\"0\",\"0\",\"0\",\"0\",\"None\"\n"
				+ "\"card_fighter_space\",\"Fighter\",\"25\",\"SpawnUnit\",\"NearTrackUnit\",\"5\",\"SameTeam\",\"\",\"FighterSpace\",\"5\",\"None\",\"0\",\"1\",\"0\",\"0\",\"None\"\n"
				+ "\"card_bomber_space\",\"Bomber\",\"20\",\"SpawnUnit\",\"NearTrackUnit\",\"5\",\"SameTeam\",\"\",\"BomberSpace\",\"3\",\"None\",\"0\",\"1\",\"0\",\"0\",\"None\"\n"
				+ "\"card_corvette_space\",\"Corvette\",\"30\",\"SpawnUnit\",\"NearTrackUnit\",\"5\",\"SameTeam\",\"\",\"CorvetteSpace\",\"1\",\"None\",\"0\",\"1\",\"0\",\"0\",\"None\"\n"
				+ "\"card_corvette_bomber_space\",\"Corvette Bomber\",\"30\",\"SpawnUnit\",\"NearTrackUnit\",\"5\",\"SameTeam\",\"\",\"CorvetteBomberSpace\",\"1\",\"None\",\"0\",\"1\",\"0\",\"0\",\"None\"\n"
				+ "\"card_frigate_space\",\"Frigate\",\"45\",\"SpawnUnit\",\"NearTrackUnit\",\"5\",\"SameTeam\",\"\",\"FrigateSpace\",\"1\",\"None\",\"0\",\"1\",\"0\",\"0\",\"None\"\n"
				+ "\"card_cruiser_space\",\"Cruiser\",\"60\",\"SpawnUnit\",\"NearTrackUnit\",\"5\",\"SameTeam\",\"\",\"CruiserSpace\",\"1\",\"None\",\"0\",\"1\",\"0\",\"0\",\"None\"\n"
				+ "\"card_laser_space\",\"Laser\",\"40\",\"UsePowerUp\",\"TrackUnit\",\"0\",\"OpponentTeam\",\"\",\"\",\"0\",\"Laser\",\"3\",\"2.5\",\"75\",\"0.5\",\"None\"\n"
				+ "\"card_missile_space\",\"Missile\",\"50\",\"MovableTrackObject\",\"None\",\"0\",\"SameTeam\",\"\",\"\",\"0\",\"None\",\"0\",\"1\",\"600\",\"0\",\"Missile\"\n"
				+ "\"card_decoy_space\",\"Decoy\",\"50\",\"MovableTrackObject\",\"None\",\"0\",\"SameTeam\",\"\",\"\",\"0\",\"None\",\"0\",\"1\",\"50\",\"0\",\"Decoy\"\n"
				+ "\"card_mine_space\",\"Mine\",\"20\",\"SpawnUnit\",\"NearTrackUnit\",\"5\",\"SameTeam\",\"Mine\",\"MineSpace\",\"1\",\"None\",\"0\",\"1\",\"0\",\"0\",\"None\"\n"
				+ SheetsImporterCSVs.SheetsCsvSeparator + "\n" + "GameConstants\n" + "\"Id\",\"Value\"\n" + "\"StartingEnergy\",\"50\"\n"
				+ "\"MaxEnergy\",\"100\"\n" + "\"EnergyPerSecond\",\"3.5\"\n" + "\"MainHorseRecoveringTime\",\"5\"\n"
				+ "\"MainHorseRecoveringHealthPercent\",\"100\"\n"
				+ "\"SomeDifficultString\",\"Huang =\\\\\"\n";
	}
}
