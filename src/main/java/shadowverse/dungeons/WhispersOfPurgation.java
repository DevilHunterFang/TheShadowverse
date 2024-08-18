package shadowverse.dungeons;

import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import shadowverse.monsters.*;
import shadowverse.scenes.ShadowverseScene;
import shadowverseCharbosses.bosses.Maisha.Maisha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WhispersOfPurgation extends CustomDungeon {
    public static String ID = "shadowverse:WhispersOfPurgation";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public static final String[] TEXT = uiStrings.TEXT;

    public static final String NAME = TEXT[0];

    public WhispersOfPurgation() {
        super(NAME, ID, "images/ui/event/panel.png", true, 3, 12, 10);
        setMainMusic("sounds/HeroOfShadowverse.mp3");
    }

    public WhispersOfPurgation(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public WhispersOfPurgation(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
        super(cd, p, saveFile);
    }

    @Override
    public AbstractScene DungeonScene() {
        return new ShadowverseScene();
    }

    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.22F;
        eliteRoomChance = 0.08F;
        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;
        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;
        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.3F;
        } else {
            cardUpgradedChance = 0.55F;
        }
    }

    public String getBodyText() {
        if (CardCrawlGame.dungeon instanceof WhispersOfPurgation)
            return TEXT[2];
        String[] oldStrings = (CardCrawlGame.languagePack.getUIString(WhispersOfPurgation.ID)).TEXT;
        return oldStrings[2];
    }

    public String getOptionText() {
        if (CardCrawlGame.dungeon instanceof WhispersOfPurgation)
            return TEXT[3];
        String[] oldStrings = (CardCrawlGame.languagePack.getUIString(WhispersOfPurgation.ID)).TEXT;
        return oldStrings[3];
    }

    protected void generateMonsters() {
        generateWeakEnemies(this.weakpreset);
        generateStrongEnemies(this.strongpreset);
        generateElites(this.elitepreset);
    }

    protected void generateWeakEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo("shadowverse:3_Mechas", 2.0F));
        monsters.add(new MonsterInfo(Spider2.ID, 2.0F));
        monsters.add(new MonsterInfo(MegaEnforcer.ID, 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, count, false);
    }

    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(General.ID, 1.0F));
        monsters.add(new MonsterInfo("shadowverse:Tentacles", 1.0F));
        monsters.add(new MonsterInfo("shadowverse:Strong_Mechas", 1.0F));
        monsters.add(new MonsterInfo("shadowverse:BladeLight", 1.0F));
        monsters.add(new MonsterInfo("shadowverse:Strong_Wretch", 1.0F));
        monsters.add(new MonsterInfo("shadowverse:4_Shades", 1.0F));
        monsters.add(new MonsterInfo("shadowverse:3_OOOGGGG", 1.0F));
        monsters.add(new MonsterInfo("shadowverse:2_Spider", 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        populateFirstStrongEnemy(monsters, generateExclusions());
        populateMonsterList(monsters, count, false);
    }

    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(Tisiphone.ID, 2.0F));
        monsters.add(new MonsterInfo(Alector.ID, 2.0F));
        monsters.add(new MonsterInfo(Megaera.ID, 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, count, true);
    }

    protected ArrayList<String> generateExclusions() {
        ArrayList<String> retVal = new ArrayList<>();
        switch ((String) monsterList.get(monsterList.size() - 1)) {
            case Spider2.ID:
                retVal.add("shadowverse:2_Spider");
                break;
            case "shadowverse:3_Mechas":
            case MegaEnforcer.ID:
                retVal.add("shadowverse:Strong_Mechas");
                break;
        }
        return retVal;
    }

    protected void initializeShrineList() {
        shrineList.add("Match and Keep!");
        shrineList.add("Wheel of Change");
        shrineList.add("Golden Shrine");
        shrineList.add("Transmorgrifier");
        shrineList.add("Purifier");
        shrineList.add("Upgrade Shrine");
    }

    protected void initializeEventList() {
        eventList.add("Falling");
        eventList.add("MindBloom");
        eventList.add("The Moai Head");
        eventList.add("Mysterious Sphere");
        eventList.add("SensoryStone");
        eventList.add("Tomb of Lord Red Mask");
        eventList.add("Winding Halls");
    }

}
