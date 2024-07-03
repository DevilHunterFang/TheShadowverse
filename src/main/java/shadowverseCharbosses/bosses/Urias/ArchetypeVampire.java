package shadowverseCharbosses.bosses.Urias;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import shadowverse.action.AnimationAction;
import shadowverseCharbosses.bosses.AbstractCharBoss;
import shadowverseCharbosses.cards.AbstractBossCard;
import shadowverseCharbosses.cards.vampire.*;
import shadowverseCharbosses.powers.bossmechanicpowers.EnemyNightmareTimePower;
import shadowverseCharbosses.powers.general.EnemyDrawCardNextTurnPower;

import java.util.ArrayList;

public class ArchetypeVampire extends ArchetypeBaseVampire {
    private static ArrayList<AbstractBossCard> returnVampireCards() {
        ArrayList<AbstractBossCard> list = new ArrayList<>();
        list.add(new EnBloodPact());
        list.add(new EnDarkGeneral());
        list.add(new EnDemonCommander());
        list.add(new EnDemonStorm());
        list.add(new EnDiabolicDrain());
        list.add(new EnLonePromise());
        list.add(new EnLonePromise());
        list.add(new EnMono());
        list.add(new EnRazoryClaw());
        list.add(new EnSeox());
        list.add(new EnSummoningBloodkin());
        list.add(new EnTerrorNight());
        return list;
    }

    private boolean extraUpgrades = AbstractDungeon.ascensionLevel >= 19;

    private ArrayList<AbstractBossCard> drawPile = returnVampireCards();

    private boolean isTerrorNight;
    private boolean usedTerrorNight;

    public ArchetypeVampire() {
        super("Vampire", "Vampire");
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.maxHPModifier += 25;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.maxHPModifier += 10;
        }
        this.actNum = 2;
        this.looped = true;
    }

    public void addedPreBattle() {
        super.addedPreBattle();
        AbstractCharBoss abstractCharBoss = AbstractCharBoss.boss;
    }

    public void initialize() {
         extraUpgrades = (AbstractDungeon.ascensionLevel >= 19);
    }


    public ArrayList<AbstractCard> getThisTurnCards() {
        ArrayList<AbstractCard> cardsList = new ArrayList<>();
        int drawAmt = 2;
        if (AbstractCharBoss.boss.hasPower(EnemyDrawCardNextTurnPower.POWER_ID)){
            drawAmt += AbstractCharBoss.boss.getPower(EnemyDrawCardNextTurnPower.POWER_ID).amount;
        }
        if (AbstractCharBoss.boss.hasPower(EnemyNightmareTimePower.POWER_ID)) {
            drawAmt++;
        }
        if (isTerrorNight&&!usedTerrorNight){
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new SFXAction("Urias_Ev"));
            AbstractDungeon.actionManager.addToBottom(new AnimationAction(Urias.bigAnimation, "extra", 2.833F));
            addToList(cardsList, new EnDreadAura(),extraUpgrades);
            addToList(cardsList, new EnNightmareTime(),extraUpgrades);
            addToList(cardsList, new EnBloodyNail(),extraUpgrades);
            usedTerrorNight = true;
        }else {
            for (int i=0;i<drawAmt;i++){
                if (drawPile.size()==0){
                    drawPile = returnVampireCards();
                }
                int rnd = AbstractDungeon.aiRng.random(drawPile.size()-1);
                AbstractBossCard card = drawPile.get(rnd);
                if (card instanceof EnTerrorNight){
                    isTerrorNight = true;
                }
                addToList(cardsList, card, extraUpgrades);
                drawPile.remove(rnd);
            }
        }
        return cardsList;
    }


    public void initializeBonusRelic() {
    }
}

