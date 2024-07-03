package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

public class MagiTrain extends CustomMonster {
    public static final String ID = "shadowverse:MagiTrain";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:MagiTrain");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    public static final int STRIKE_DMG = 20;

    private boolean firstTurn = true;

    private static final String STICKY_NAME = MOVES[0],PREP_NAME = MOVES[2],SLAM_NAME = MOVES[1];

    private int debuffAmount;

    public MagiTrain(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(100, 120), 0.0F, -26.0F, 390.0F, 390.0F, null, x, y);
        this.animation = new SpriterAnimation("img/monsters/MagiTrain/MagiTrain.scml");
        if (AbstractDungeon.ascensionLevel >= 9) {
            setHp(100, 120);
        } else {
            setHp(140, 160);
        }
        if (AbstractDungeon.ascensionLevel >= 19) {

            this.debuffAmount = 4;
        } else if (AbstractDungeon.ascensionLevel >= 4) {

            this.debuffAmount = 3;
        } else {
            this.debuffAmount = 2;
        }
        this.damage.add(new DamageInfo(this, this.STRIKE_DMG));
    }


    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 19) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 2)));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1)));
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, new Color(0.1F, 1.0F, 0.1F, 0.0F))));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                        .get(0), AbstractGameAction.AttackEffect.SMASH));
                setMove(PREP_NAME, (byte)2, Intent.UNKNOWN);
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0], 1.0F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new ShakeScreenAction(0.3F, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.LOW));
                setMove(STICKY_NAME, (byte)3, Intent.STRONG_DEBUFF);
                break;
            case 3:
                addToBot(new SFXAction("MagiTrain"));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 2, true), 2));
                setMove(SLAM_NAME, (byte)1, Intent.ATTACK, (this.damage.get(0)).base);
                break;
        }
    }

    @Override
    protected void getMove(int num) {
        if (this.firstTurn) {
            this.firstTurn = false;
            setMove(STICKY_NAME, (byte)2, Intent.STRONG_DEBUFF);
            return;
        }
    }

}
