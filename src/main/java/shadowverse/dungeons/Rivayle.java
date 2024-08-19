package shadowverse.dungeons;

import actlikeit.dungeons.CustomDungeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import shadowverse.monsters.*;
import shadowverse.scenes.ShadowverseScene;

import java.util.ArrayList;

public class Rivayle extends CustomDungeon {
    public static String ID = "shadowverse:Rivayle";

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public static final String[] TEXT = uiStrings.TEXT;

    public static final String NAME = TEXT[0];

    public Rivayle() {
        super(NAME, ID, "images/ui/event/panel.png", true, 3, 12, 10);
        setMainMusic("sounds/Rivayle_Normal.mp3");
    }

    public Rivayle(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public Rivayle(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
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
        eventRoomChance = 0.25F;
        eliteRoomChance = 0.08F;
        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;
        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;
        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.15F;
        } else {
            cardUpgradedChance = 0.3F;
        }
    }

    public String getBodyText() {
        if (CardCrawlGame.dungeon instanceof Rivayle)
            return TEXT[2];
        String[] oldStrings = (CardCrawlGame.languagePack.getUIString(Rivayle.ID)).TEXT;
        return oldStrings[2];
    }

    public String getOptionText() {
        if (CardCrawlGame.dungeon instanceof Rivayle)
            return TEXT[3];
        String[] oldStrings = (CardCrawlGame.languagePack.getUIString(Rivayle.ID)).TEXT;
        return oldStrings[3];
    }

    protected void generateMonsters() {
        generateWeakEnemies(this.weakpreset);
        generateStrongEnemies(this.strongpreset);
        generateElites(this.elitepreset);
    }

    protected void generateWeakEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo("shadowverse:2_Scoundrel", 2.0F));
        monsters.add(new MonsterInfo("shadowverse:2_Deputy_Weak", 2.0F));
        monsters.add(new MonsterInfo(Wretch.ID, 2.0F));
        monsters.add(new MonsterInfo(Underling.ID, 2.0F));
        monsters.add(new MonsterInfo("shadowverse:3_Henchman", 2.0F));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, count, false);
    }

    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo("shadowverse:2_Deputy", 2.0F));
        monsters.add(new MonsterInfo("shadowverse:2_Henchman", 2.0F));
        monsters.add(new MonsterInfo(Assault.ID, 6.0F));
        monsters.add(new MonsterInfo(Wretch6.ID, 4.0F));
        monsters.add(new MonsterInfo("shadowverse:Underlings", 3.0F));
        monsters.add(new MonsterInfo("shadowverse:Underling_Ista", 6.0F));
        monsters.add(new MonsterInfo("shadowverse:Rivayle_Mix", 3.0F));
        monsters.add(new MonsterInfo("shadowverse:Wretch_Shade", 3.0F));
        MonsterInfo.normalizeWeights(monsters);
        populateFirstStrongEnemy(monsters, generateExclusions());
        populateMonsterList(monsters, count, false);
    }

    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(Zecilwenshe.ID, 1.0F));
        monsters.add(new MonsterInfo(Commander.ID, 1.0F));
        monsters.add(new MonsterInfo("shadowverse:Unturned", 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, count, true);
    }

    protected ArrayList<String> generateExclusions() {
        ArrayList<String> retVal = new ArrayList<>();
        switch ((String)monsterList.get(monsterList.size() - 1)) {
            case Wretch.ID:
                retVal.add("shadowverse:Wretch_Shade");
                retVal.add(Wretch6.ID);
                break;
            case "shadowverse:2_Deputy_Weak":
                retVal.add("shadowverse:2_Deputy");
                break;
            case Underling.ID:
                retVal.add("shadowverse:Underlings");
                retVal.add("shadowverse:Underling_Ista");
                break;
            case "shadowverse:3_Henchman":
                retVal.add("shadowverse:2_Henchman");
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
        eventList.add("Addict");
        eventList.add("Back to Basics");
        eventList.add("Beggar");
        eventList.add("Colosseum");
        eventList.add("Cursed Tome");
        eventList.add("Drug Dealer");
        eventList.add("Forgotten Altar");
        eventList.add("Ghosts");
        eventList.add("Masked Bandits");
        eventList.add("Nest");
        eventList.add("The Library");
        eventList.add("The Mausoleum");
        eventList.add("Vampires");
    }
}
