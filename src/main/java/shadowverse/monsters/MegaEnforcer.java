package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

public class MegaEnforcer extends CustomMonster {
    public static final String ID = "shadowverse:MegaEnforcer";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:MegaEnforcer");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;


    private int attackDmg;
    private int laserDmg;
    private int blockAmt;

    public MegaEnforcer(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(86,90), -5.0F, -20.0F, 145.0F, 460.0F, "img/monsters/Aialon/MegaEnforcer.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(90, 96);
        } else {
            setHp(86,90);
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDmg = 8;
            this.laserDmg = 16;
            this.blockAmt = 25;
        } else {
            this.attackDmg = 7;
            this.laserDmg = 15;
            this.blockAmt = 20;
        }
        this.damage.add(new DamageInfo(this, this.attackDmg));
        this.damage.add(new DamageInfo(this,this.laserDmg));
    }


    public MegaEnforcer(){
        this(0.0F,-30.0F);
    }

    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 2)));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, 8), 8));
            addToBot(new GainBlockAction(this,8));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1)));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, 6), 6));
            addToBot(new GainBlockAction(this,6));
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                addToBot(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 2:
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.1F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.3F));
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAndDeckAction(new Burn()));
                break;
            case 3:
                addToBot(new GainBlockAction(this,this.blockAmt));
                addToBot(new ApplyPowerAction(this,this,new RegenerateMonsterPower(this,6),6));
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,2),2));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (num < 60) {
            if (!lastTwoMoves((byte)1)) {
                setMove(MOVES[0],(byte)1, Intent.ATTACK, (this.damage.get(0)).base,3,true);
            } else {
                setMove((byte)2, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
            }
        } else if (!lastTwoMoves((byte)3)) {
            setMove(MOVES[1],(byte)3, Intent.DEFEND_BUFF);
        } else {
            setMove((byte)1, Intent.ATTACK, (this.damage.get(0)).base,3,true);
        }
    }

}
