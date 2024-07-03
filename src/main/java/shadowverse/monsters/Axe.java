package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import shadowverseCharbosses.powers.cardpowers.EnemyFlameBarrierPower;

public class Axe extends CustomMonster {
    public static final String ID = "shadowverse:Axe";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Axe");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;


    private int attackDmg;
    private int slashDmg;
    private int blockAmt;
    private int flameAmt;

    public Axe(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(92, 100), -5.0F, -20.0F, 145.0F, 400.0F, "img/monsters/BladeLight/Axe.png", x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(97, 108);
        } else {
            setHp(92,100);
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDmg = 16;
            this.slashDmg = 40;
            this.blockAmt = 24;
            this.flameAmt = 6;
        } else {
            this.attackDmg = 14;
            this.slashDmg = 35;
            this.blockAmt = 20;
            this.flameAmt = 4;
        }
        this.damage.add(new DamageInfo(this, this.attackDmg));
        this.damage.add(new DamageInfo(this, this.slashDmg));
    }


    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                addToBot(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                break;
            case 2:
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new VFXAction(new VerticalImpactEffect(AbstractDungeon.player.hb.cX + AbstractDungeon.player.hb.width / 4.0F, AbstractDungeon.player.hb.cY - AbstractDungeon.player.hb.height / 4.0F)));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0], 1.0F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new ShakeScreenAction(0.3F, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.LOW));
                break;
            case 4:
                addToBot(new GainBlockAction(this,this.blockAmt));
                addToBot(new ApplyPowerAction(this,this,new EnemyFlameBarrierPower(this,this.flameAmt),this.flameAmt));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (lastMove((byte)3)){
            setMove((byte)2, Intent.ATTACK, (this.damage.get(1)).base);
            return;
        }
        if (num < 50 && !lastMove((byte)1)) {
            setMove((byte)1, Intent.ATTACK, (this.damage.get(0)).base);
            return;
        }else if (!lastMove((byte)3)){
            setMove((byte)3,Intent.UNKNOWN);
            return;
        }
        setMove(MOVES[0], (byte)4, Intent.DEFEND_BUFF);
    }

}
