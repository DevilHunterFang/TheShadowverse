package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import shadowverse.action.SummonShadeAction;
import shadowverse.cards.curse.Betray;
import shadowverse.orbs.Minion;
import shadowverse.powers.NexusPower;
import shadowverse.powers.ShadePower;

public class Commander extends CustomMonster {
    public static final String ID = "shadowverse:Commander";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Commander");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;
    public AbstractMonster[] shades = new AbstractMonster[2];

    public float[] POSX = new float[2];

    private int strAmt;

    private int blockAmt;

    private int STAB_DMG = 5;

    private int heavyDmg;

    private boolean combatStart;

    public Commander(float x,float y) {
        super(NAME, ID, 148, 0.0F, -40.0F, 350.0F, 550.0F, "img/monsters/Shadows/Commander.png", x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(140, 145);
        } else {
            setHp(135,143);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.strAmt = 5;
            this.blockAmt = 8;
            this.heavyDmg = 10;
        } else if (AbstractDungeon.ascensionLevel >= 3) {
            this.strAmt = 4;
            this.blockAmt = 6;
            this.heavyDmg = 8;
        } else {
            this.strAmt = 3;
            this.blockAmt = 6;
            this.heavyDmg = 7;
        }
        this.damage.add(new DamageInfo(this, this.STAB_DMG));
        this.damage.add(new DamageInfo(this, this.heavyDmg));
        POSX[0] = x - 331.0F;
        POSX[1] = x - 566.0F;
    }

    public Commander(){
        this(65.0F,-30.0F);
    }

    @Override
    public void usePreBattleAction() {
        if (!this.hasPower(MinionPower.POWER_ID)){
            AbstractDungeon.actionManager.addToBottom(new SummonShadeAction(shades,POSX[0],POSX[1]));
            AbstractDungeon.actionManager.addToBottom(new SummonShadeAction(shades,POSX[0],POSX[1]));
        }
        addToBot(new ApplyPowerAction(this,this,new ShadePower(this,5),5));
    }

    @Override
    public void takeTurn() {
        if (!combatStart){
            combatStart = true;
        }
        switch (this.nextMove) {
            case 2:
                if (numAliveShades() == 0){
                    AbstractDungeon.actionManager.addToBottom(new SummonShadeAction(shades,POSX[0],POSX[1]));
                    AbstractDungeon.actionManager.addToBottom(new SummonShadeAction(shades,POSX[0],POSX[1]));
                }else {
                    AbstractDungeon.actionManager.addToBottom(new SummonShadeAction(shades,POSX[0],POSX[1]));
                }
                break;
            case 3:
                for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                    if (m == this) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.strAmt), this.strAmt));
                        continue;
                    }
                    if (!m.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.strAmt), this.strAmt));
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, this.blockAmt));
                    }
                }
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL, true));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 5:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                    if (!mo.isDeadOrEscaped()){
                        addToBot(new HealAction(mo,this,20));
                    }
                }
                addToBot(new MakeTempCardInDiscardAction(new Betray(),1));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (numAliveShades() == 0) {
            if (num < 75) {
                if (!lastMove((byte)2) && combatStart) {
                    setMove((byte)2, AbstractMonster.Intent.UNKNOWN);
                } else {
                    setMove((byte)4, AbstractMonster.Intent.ATTACK, this.STAB_DMG, 3, true);
                }
            } else if (!lastMove((byte)4)) {
                setMove((byte)4, AbstractMonster.Intent.ATTACK, this.STAB_DMG, 3, true);
            } else {
                setMove((byte)5, Intent.ATTACK_BUFF, this.heavyDmg);
            }
        } else if (numAliveShades() < 2) {
            if (num < 50) {
                if (!lastMove((byte)2)) {
                    setMove((byte)2, AbstractMonster.Intent.UNKNOWN);
                } else {
                    getMove(AbstractDungeon.aiRng.random(50, 99));
                }
            } else if (num < 80) {
                if (!lastMove((byte)3)) {
                    setMove((byte)3, AbstractMonster.Intent.DEFEND_BUFF);
                } else {
                    setMove((byte)4, AbstractMonster.Intent.ATTACK, this.STAB_DMG, 3, true);
                }
            } else if (!lastMove((byte)4)) {
                setMove((byte)4, AbstractMonster.Intent.ATTACK, this.STAB_DMG, 3, true);
            } else {
                getMove(AbstractDungeon.aiRng.random(0, 80));
            }
        } else if (numAliveShades() > 1) {
            if (num < 66) {
                if (!lastMove((byte)3)) {
                    setMove((byte)3, AbstractMonster.Intent.DEFEND_BUFF);
                } else {
                    setMove((byte)4, AbstractMonster.Intent.ATTACK, this.STAB_DMG, 3, true);
                }
            } else if (!lastMove((byte)4)) {
                setMove((byte)4, AbstractMonster.Intent.ATTACK, this.STAB_DMG, 3, true);
            } else {
                setMove((byte)3, AbstractMonster.Intent.DEFEND_BUFF);
            }
        }
    }

    private int numAliveShades() {
        int count = 0;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (m != null && m != this && !m.isDeadOrEscaped() && !(m instanceof Nexus))
                count++;
        }
        return count;
    }

    @Override
    public void die() {
        super.die();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (m.hasPower(NexusPower.POWER_ID)) {
                m.getPower(NexusPower.POWER_ID).amount++;
                m.getPower(NexusPower.POWER_ID).updateDescription();
                break;
            }
            if (!m.isDeadOrEscaped() && m.hasPower(MinionPower.POWER_ID)){
                AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(m));
            }
        }
    }
}
