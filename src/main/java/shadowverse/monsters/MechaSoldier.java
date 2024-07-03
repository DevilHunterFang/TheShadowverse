package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class MechaSoldier extends CustomMonster {
    public static final String ID = "shadowverse:MechaSoldier";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:MechaSoldier");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;


    private int shootDmg;
    private int shootAmt;
    private int blockAmt;
    private static final String SHOOT = MOVES[0];

    public MechaSoldier(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(20, 24), -5.0F, -20.0F, 145.0F, 400.0F, null, x, y);
        this.animation = new SpriterAnimation("img/monsters/MechaSoldier/MechaSoldier.scml");
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(24, 28);
        } else {
            setHp(20, 24);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.shootDmg = 2;
            this.shootAmt = 3;
            this.blockAmt = 9;
        } else if (AbstractDungeon.ascensionLevel >= 2) {
            this.shootDmg = 1;
            this.shootAmt = 3;
            this.blockAmt = 8;
        } else {
            this.shootDmg = 1;
            this.shootAmt = 2;
            this.blockAmt = 7;
        }
        this.damage.add(new DamageInfo(this, this.shootDmg));
    }


    @Override
    public void usePreBattleAction() {
        if (AbstractDungeon.ascensionLevel >= 17) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 2)));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1)));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, 5), 5));
        addToBot(new GainBlockAction(this,this.blockAmt));
        addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,3),3));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                addToBot(new AnimateFastAttackAction(this));
                for (int i = 0;i<this.shootAmt;i++){
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this,this.blockAmt));
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,2),2));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (num < 50 && !lastMove((byte)1)) {
            setMove(SHOOT,(byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, this.shootAmt, true);
            return;
        }
        if (!lastMove((byte)2)) {
            setMove((byte)2, Intent.DEFEND_BUFF);
        }
    }

}
