package shadowverseCharbosses.bosses.Maisha;

import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import shadowverse.powers.MaishaPower;
import shadowverseCharbosses.bosses.AbstractCharBoss;
import shadowverseCharbosses.cards.AbstractBossCard;
import shadowverseCharbosses.cards.nemesis.*;
import shadowverseCharbosses.powers.cardpowers.EnemyTrueSwordPower;
import shadowverseCharbosses.powers.cardpowers.MaishaPower2;
import shadowverseCharbosses.powers.general.EnemyDrawCardNextTurnPower;

import java.util.ArrayList;

public class ArchetypeMaisha extends ArchetypeBaseMaisha {
    private static ArrayList<AbstractBossCard> returnMaishaCards() {
        ArrayList<AbstractBossCard> list = new ArrayList<>();
        list.add(new DualSwords());
        list.add(new ElementalBrink());
        list.add(new EnHeartlessBattle());
        list.add(new EnKarula());
        list.add(new EnMoonAndSun());
        list.add(new EnOmenOfCraving());
        list.add(new EnPurgationBlade());
        list.add(new EnRukina());
        list.add(new EnUnnamedDetermination());
        list.add(new FalseSword());
        list.add(new Riftdweller());
        list.add(new EnAnalyzeArtifact());
        list.add(new EnAnalyzeArtifact());
        list.add(new EnEdgeArtifact());
        list.add(new EnProtectArtifact());
        list.add(new EnAncientArtifact());
        list.add(new EnSion());
        list.add(new TrueSword());
        return list;
    }

    private static ArrayList<AbstractBossCard> returnMaishaCards2() {
        ArrayList<AbstractBossCard> list = new ArrayList<>();
        list.add(new DualSwords());
        list.add(new ElementalBrink());
        list.add(new EnHeartlessBattle());
        list.add(new EnKarula());
        list.add(new EnMoonAndSun());
        list.add(new EnOmenOfCraving());
        list.add(new EnPurgationBlade());
        list.add(new EnRukina());
        list.add(new EnUnnamedDetermination());
        list.add(new Riftdweller());
        list.add(new EnAnalyzeArtifact());
        list.add(new EnAnalyzeArtifact());
        list.add(new EnEdgeArtifact());
        list.add(new EnProtectArtifact());
        list.add(new EnAncientArtifact());
        list.add(new EnSion());
        return list;
    }

    private static ArrayList<AbstractBossCard> returnNoCraving() {
        ArrayList<AbstractBossCard> list = new ArrayList<>();
        list.add(new DualSwords());
        list.add(new ElementalBrink());
        list.add(new EnHeartlessBattle());
        list.add(new EnKarula());
        list.add(new EnMoonAndSun());
        list.add(new EnPurgationBlade());
        list.add(new EnRukina());
        list.add(new EnUnnamedDetermination());
        list.add(new Riftdweller());
        list.add(new EnAnalyzeArtifact());
        list.add(new EnAnalyzeArtifact());
        list.add(new EnEdgeArtifact());
        list.add(new EnProtectArtifact());
        list.add(new EnAncientArtifact());
        list.add(new EnSion());
        return list;
    }

    private static ArrayList<AbstractBossCard> returnBPCards() {
        ArrayList<AbstractBossCard> list = new ArrayList<>();
        list.add(new MaishaSkill());
        list.add(new MaishaSpell());
        list.add(new MaishaUltimate());
        return list;
    }

    private boolean extraUpgrades = AbstractDungeon.ascensionLevel >= 19;

    private ArrayList<AbstractBossCard> drawPile = returnMaishaCards();

    private boolean noCraving = false;
    public ArchetypeMaisha() {
        super("Maisha", "Maisha");
        this.actNum = 3;
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
        if (AbstractCharBoss.boss.hasPower(MaishaPower2.POWER_ID)) {
            int bp = AbstractCharBoss.boss.getPower(MaishaPower2.POWER_ID).amount;
            AbstractCard bpCard = null;
            if (bp > 9){
                bpCard = returnBPCards().get(2);
            }else {
                int rnd = AbstractDungeon.aiRng.random(bp > 5?2:1);
                bpCard = returnBPCards().get(rnd);
            }
            addToList(cardsList, bpCard, extraUpgrades);
        }
        int drawAmt = 2;
        if (AbstractCharBoss.boss.hasPower(EnemyDrawCardNextTurnPower.POWER_ID)) {
            drawAmt += AbstractCharBoss.boss.getPower(EnemyDrawCardNextTurnPower.POWER_ID).amount;
        }

        if (AbstractCharBoss.boss.hasPower(MaishaPower.POWER_ID)) {
            AbstractCharBoss.boss.getPower(MaishaPower.POWER_ID).amount -= drawAmt;
            if (AbstractCharBoss.boss.getPower(MaishaPower.POWER_ID).amount <= 0){
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(AbstractCharBoss.boss,true));
            }
            AbstractCharBoss.boss.getPower(MaishaPower.POWER_ID).updateDescription();
        }
        ArrayList<AbstractCard> list = new ArrayList<>();
        if (AbstractDungeon.actionManager.turn > 4){
            int count = 0;
            if (drawPile.size() > 0){
                for (int i = 0; i < drawPile.size();i++){
                    if (drawPile.get(i) instanceof EnOmenOfCraving){
                        addToList(cardsList, drawPile.get(i), extraUpgrades);
                        count = i;
                        addToList(cardsList, new EnRavenousSweetness(),extraUpgrades);
                        noCraving = true;
                    }
                }
                drawPile.remove(count);
            }
            if (AbstractCharBoss.boss.hasPower(EnemyTrueSwordPower.POWER_ID)){
                addToList(cardsList, new TrueSword(),extraUpgrades);
            }
        }
        for (int i = 0; i < drawAmt; i++) {
            if (drawPile.size() == 0) {
                drawPile = noCraving?returnNoCraving():returnMaishaCards2();
            }
            int rnd = AbstractDungeon.aiRng.random(drawPile.size() - 1);
            AbstractBossCard card = drawPile.get(rnd);
            list.add(card);
            //addToList(cardsList, card, extraUpgrades);
            drawPile.remove(rnd);
        }
        for (int i = 0; i < list.size();i++){
            if (list.get(i) instanceof FalseSword){
                AbstractCard temp = list.get(0);
                list.set(0,list.get(i));
                list.set(i,temp);
            }
            if (list.get(i) instanceof TrueSword){
                AbstractCard temp = list.get(0);
                list.set(0,list.get(i));
                list.set(i,temp);
            }
            if (list.get(i) instanceof EnPurgationBlade){
                AbstractCard temp = list.get(0);
                if (temp instanceof TrueSword && list.size() > 1){
                    AbstractCard temp2= list.get(1);
                    list.set(1,temp);
                    list.set(0,temp2);
                    temp = temp2;
                }
                list.set(0,list.get(i));
                list.set(i,temp);
            }
        }
        for (AbstractCard card : list) {
            addToList(cardsList, card,extraUpgrades);
        }

        return cardsList;
    }


    public void initializeBonusRelic() {
    }
}

