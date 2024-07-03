package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AngerPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;

public class Megaera extends CustomMonster {
    public static final String ID = "shadowverse:Megaera";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Megaera");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int beamDmg;

    private int blockAmt;

    private int regenAmt;

    private boolean firstTurn = true;

    private static final String STICKY_NAME = MOVES[0];

    private static final String SHOOT = MOVES[1];

    private static final String RECHARGE = MOVES[2];

    public Megaera() {
        super(NAME, ID, 500, 0.0F, -40.0F, 350.0F, 470.0F, null, -70.0F, 25.0F);
        this.animation = new SpriterAnimation("img/monsters/Megaera/Megaera.scml");
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(560, 560);
        } else {
            setHp(500, 500);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.regenAmt = 42;
            this.blockAmt = 50;
        } else if (AbstractDungeon.ascensionLevel >= 3) {
            this.regenAmt = 36;
            this.blockAmt = 45;
        } else {
            this.regenAmt = 30;
            this.blockAmt = 40;
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.beamDmg = 24;
        } else {
            this.beamDmg = 20;
        }
        this.damage.add(new DamageInfo((AbstractCreature)this, beamDmg));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, (AbstractPower)new SlowPower((AbstractCreature)this, 0)));
        if (AbstractDungeon.ascensionLevel >= 18) {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, (AbstractPower)new AngerPower((AbstractCreature)this, 4), 4));
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, (AbstractPower)new ArtifactPower((AbstractCreature)this, 13), 13));
        }else {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, (AbstractPower)new AngerPower((AbstractCreature)this, 3), 3));
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, (AbstractPower)new ArtifactPower((AbstractCreature)this, 11), 11));
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 3:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ShoutAction((AbstractCreature)this, DIALOG[0], 1.0F, 2.0F));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new SFXAction("Megaera_PREP"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new GainBlockAction((AbstractCreature)this, blockAmt));
                setMove(SHOOT, (byte)2, Intent.ATTACK,((DamageInfo)this.damage.get(0)).base);
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new VFXAction((AbstractGameEffect)new LaserBeamEffect(this.hb.cX, this.hb.cY + 60.0F * Settings.scale), 1.5F));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new WaitAction(0.8F));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new DamageAction((AbstractCreature)AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.NONE));
                setMove(RECHARGE, (byte)1, Intent.BUFF);
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new HealAction(this,this,regenAmt));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RemoveDebuffsAction((AbstractCreature)this));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, (AbstractPower)new SlowPower((AbstractCreature)this, 0)));
                if (AbstractDungeon.ascensionLevel >= 18) {
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, (AbstractPower)new AngerPower((AbstractCreature)this, 4), 4));
                }else {
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new ApplyPowerAction((AbstractCreature)this, (AbstractCreature)this, (AbstractPower)new AngerPower((AbstractCreature)this, 3), 3));
                }
                setMove(STICKY_NAME, (byte)3, Intent.UNKNOWN);
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            setMove(STICKY_NAME, (byte)3, Intent.UNKNOWN);
            return;
        }
    }

    private void playDeathSfx() {
        int roll = MathUtils.random(1);
        if (roll == 0) {
            CardCrawlGame.sound.play("Megaera_D1");
        } else {
            CardCrawlGame.sound.play("Megaera_D2");
        }
    }

    @Override
    public void die() {
        super.die();
        playDeathSfx();
    }
}
