package shadowverse.action;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import shadowverse.characters.AbstractShadowversePlayer;
import shadowverse.powers.Cemetery;
import shadowverse.powers.GremoryPower;
import shadowverse.powers.GremoryUsedPower;
import shadowverse.relics.Alice;

import java.util.ArrayList;

public class NecromanceAction extends AbstractGameAction {
    private AbstractPlayer p = AbstractDungeon.player;
    private int necromance;
    private ArrayList<AbstractGameAction> useNecromanceAction = new ArrayList<AbstractGameAction>();
    private AbstractGameAction commonAction;

    public NecromanceAction(int necromance, AbstractGameAction commonAction, AbstractGameAction... useNecromanceAction) {
        this.necromance = necromance;
        for (AbstractGameAction action:useNecromanceAction){
            this.useNecromanceAction.add(action);
        }
        this.commonAction = commonAction;
    }



    @Override
    public void update() {
        int playerNecromance = 0;
        if (p.hasPower(Cemetery.POWER_ID)){
            for (AbstractPower p :p.powers){
                if (p.ID.equals(Cemetery.POWER_ID))
                    playerNecromance = p.amount;
            }
        }
        if (playerNecromance >= this.necromance){
            addToBot(new ApplyPowerAction(p, p, new Cemetery(p, -necromance), -necromance));
            if (this.useNecromanceAction!=null&&this.useNecromanceAction.size()!=0){
                for (int i=0;i<this.useNecromanceAction.size();i++){
                    addToBot(this.useNecromanceAction.get(i));
                }
            }
            if (this.p.hasPower("shadowverse:GremoryPower")){
                addToBot(new RemoveSpecificPowerAction(p,p,"shadowverse:GremoryPower"));
                addToBot(new ApplyPowerAction(p,p,new GremoryUsedPower(p)));
                addToBot(new ApplyPowerAction(p, p, new Cemetery(p, necromance), necromance));
                addToBot(new GainEnergyAction(2));
            }
            if (this.p.hasRelic(Alice.ID)){
                addToBot(new DamageAllEnemiesAction(null,
                        DamageInfo.createDamageMatrix(3, true), DamageInfo.DamageType.THORNS, AttackEffect.SLASH_DIAGONAL));
            }
            if (this.p instanceof AbstractShadowversePlayer){
                ((AbstractShadowversePlayer)p).necromanceCount += this.necromance;
            }
        }
        else{
            if (null != commonAction)
                addToBot(commonAction);
        }
        this.isDone = true;
    }
}
