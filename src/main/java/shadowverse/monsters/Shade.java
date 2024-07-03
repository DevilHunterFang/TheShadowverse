package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import shadowverse.powers.NexusPower;
import shadowverse.powers.ShadePower;

public class Shade extends CustomMonster {
    public static final String ID = "shadowverse:Shade";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Shade");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;


    private int attackDamage;
    private int debuffDamage;
    private int buffAmt;

    private float posX;
    private float posY;


    public Shade(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(26, 30), -5.0F, -20.0F, 145.0F, 460.0F, "img/monsters/Shadows/Shade.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(26, 30);
        } else {
            setHp(24, 28);
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDamage = 9;
            this.debuffDamage = 5;
            this.buffAmt = 4;
        } else {
            this.attackDamage = 9;
            this.debuffDamage = 4;
            this.buffAmt = 3;
        }
        posX = x;
        posY = y;
        this.damage.add(new DamageInfo(this, this.attackDamage));
        this.damage.add(new DamageInfo(this, this.debuffDamage));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this,this,new ShadePower(this,5),5));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, buffAmt), buffAmt));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new WeakPower(AbstractDungeon.player, 2,true),2));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new FrailPower(AbstractDungeon.player, 2,true),2));
                break;
            case 4:
                if (numAliveShades() > 0){
                    addToBot(new RemoveSpecificPowerAction(this,this,ShadePower.POWER_ID));
                    AbstractDungeon.actionManager.addToBottom(new CannotLoseAction());
                    AbstractDungeon.actionManager.addToTop(new VFXAction(this, new InflameEffect(this), 0.2F));
                    AbstractDungeon.actionManager.addToBottom(new HideHealthBarAction(this));
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
                    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                        if (mo != null && mo != this && !mo.isDeadOrEscaped() && !mo.isDying && mo instanceof Shade){
                            addToBot(new RemoveSpecificPowerAction(mo,this,ShadePower.POWER_ID));
                            AbstractDungeon.actionManager.addToTop(new VFXAction(mo, new InflameEffect(mo), 0.2F));
                            AbstractDungeon.actionManager.addToBottom(new HideHealthBarAction(mo));
                            AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo, false));
                        }
                    }
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0F));
                    AbstractMonster assault = new Assault(this.posX - 117.5F,this.posY);
                    addToBot(new ApplyPowerAction(assault,null,new ShadePower(assault,5),5));
                    addToBot(new SpawnMonsterAction(assault,true));
                    addToBot(new CanLoseAction());
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private int numAliveShades() {
        int count = 0;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (m != null && m != this && !m.isDying && m instanceof Shade)
                count++;
        }
        return count;
    }

    @Override
    protected void getMove(int num) {
        if (num < 60) {
            if (lastMove((byte)1) || lastMove((byte)2) || lastMove((byte)3)){
                if (numAliveShades() > 0){
                    for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                        if (mo != null && mo != this && !mo.isDeadOrEscaped() && !mo.isDying && mo instanceof Shade) {
                            setMove((byte)4, Intent.UNKNOWN);
                            return;
                        }
                    }
                }
            }
            if (lastTwoMoves((byte)1)) {
                setMove((byte)2, AbstractMonster.Intent.BUFF);
            } else {
                setMove((byte)1, AbstractMonster.Intent.ATTACK, (this.damage.get(0)).base);
            }
        } else if (lastMove((byte)3)) {
            setMove((byte)3, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
        } else {
            setMove((byte)2, AbstractMonster.Intent.BUFF);
        }
    }

    public void getMove(){
        setMove((byte)1, AbstractMonster.Intent.ATTACK, (this.damage.get(0)).base);
        createIntent();
    }

    @Override
    public void die() {
        super.die();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (m.isDying || m.isDead) {
                continue;
            }
            if (m.hasPower(NexusPower.POWER_ID)) {
                m.getPower(NexusPower.POWER_ID).amount++;
                m.getPower(NexusPower.POWER_ID).updateDescription();
            }
        }
    }
}
