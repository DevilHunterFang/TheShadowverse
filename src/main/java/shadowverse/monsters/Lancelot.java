package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.GainBlockRandomMonsterAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;

public class Lancelot extends CustomMonster {
    public static final String ID = "shadowverse:Lancelot";

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Lancelot");

    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private static final float IDLE_TIMESCALE = 0.8F;

    private static final int HP_MIN = 76;

    private static final int HP_MAX = 80;

    private static final int A_2_HP_MIN = 78;

    private static final int A_2_HP_MAX = 83;

    private static final int SLASH_DMG = 12;

    private static final int FURY_DMG = 6;

    private static final int FURY_HITS = 3;

    private static final int A_2_SLASH_DMG = 14;

    private static final int A_2_FURY_DMG = 7;

    private int slashDmg;

    private int furyDmg;

    private int furyHits;

    private int blockAmount;

    private int BLOCK_AMOUNT = 15;

    private int A_17_BLOCK_AMOUNT = 20;

    private int beamDmg;

    private static final String SHOOT = MOVES[0];

    private static final byte SLASH = 1;

    private static final byte PROTECT = 2;

    private static final byte FURY = 3;

    public Lancelot(float x, float y) {
        super(NAME, ID, 300, -14.0F, -20.0F, 250.0F, 445.0F, null, x, y);
        this.animation = new SpriterAnimation("img/monsters/Lancelot/Lancelot.scml");
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(350, 350);
        } else {
            setHp(300, 300);
        }
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.blockAmount = this.A_17_BLOCK_AMOUNT;
            this.slashDmg = 18;
            this.furyDmg = 5;
            this.beamDmg = 25;
        } else {
            this.blockAmount = this.BLOCK_AMOUNT;
            this.slashDmg = 16;
            this.furyDmg = 4;
            this.beamDmg = 23;
        }
        this.furyHits = 5;
        this.damage.add(new DamageInfo((AbstractCreature)this, this.slashDmg));
        this.damage.add(new DamageInfo((AbstractCreature)this, this.furyDmg));
        this.damage.add(new DamageInfo((AbstractCreature)this, this.beamDmg));
    }

    public void takeTurn() {
        int i;
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new TalkAction((AbstractCreature)this, DIALOG[0]));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SFXAction("Suzaku_SLASH"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new DamageAction((AbstractCreature)AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new GainBlockRandomMonsterAction((AbstractCreature)this, this.blockAmount));
                break;
            case 3:
                for (i = 0; i < this.furyHits; i++) {
                    AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new DamageAction((AbstractCreature)AbstractDungeon.player, this.damage
                            .get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                }
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new TalkAction((AbstractCreature)this, DIALOG[1]));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new SFXAction("Suzaku_BEAM"));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new VFXAction((AbstractGameEffect)new LaserBeamEffect(this.hb.cX, this.hb.cY + 60.0F * Settings.scale), 0.5F));
                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new DamageAction((AbstractCreature)AbstractDungeon.player, this.damage
                        .get(2), AbstractGameAction.AttackEffect.NONE));
                break;
        }
        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new RollMoveAction(this));
    }



    protected void getMove(int num) {
        if (num < 40 && !lastTwoMoves((byte)2) && !lastTwoMoves((byte)3)) {
            int i = 0;
            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                if (!m.isDying && !m.isEscaping)
                    i++;
            }
            if (i > 1) {
                setMove((byte)2, Intent.DEFEND);
                return;
            }
            setMove((byte)3, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, this.furyHits, true);
            return;
        }
        if (num<70&& !lastTwoMoves((byte)4)){
            setMove(SHOOT,(byte)4, Intent.ATTACK, ((DamageInfo)this.damage.get(2)).base);
            return;
        }
        if (!lastTwoMoves((byte)1)) {
            setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            return;
        }
        int aliveCount = 0;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (!m.isDying && !m.isEscaping)
                aliveCount++;
        }
        if (aliveCount > 1) {
            setMove((byte)2, Intent.DEFEND);
            return;
        }
        setMove((byte)3, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base, this.furyHits, true);
    }


    public void die() {
        useShakeAnimation(5.0F);
        super.die();
    }
}
