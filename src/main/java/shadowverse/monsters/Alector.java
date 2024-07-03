package shadowverse.monsters;

import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import shadowverse.powers.AlectorPower;

import java.util.HashMap;
import java.util.Map;

public class Alector extends CustomMonster {
    public static final String ID = "shadowverse:Alector";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("shadowverse:Alector");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String[] DIALOG = monsterStrings.DIALOG;

    private int beamDmg;

    private int blockAmt;

    private int metaAmt;

    private int metaBuffAmt;

    private int buffAmt;

    private boolean firstTurn = true;

    private static final String STICKY_NAME = MOVES[0];

    private float spawnX = -70.0F;

    private HashMap<Integer, AbstractMonster> enemySlots = new HashMap<>();


    public Alector() {
        super(NAME, ID, 270, 0.0F, -40.0F, 350.0F, 600.0F, null, 60.0F, 35.0F);
        this.animation = new SpriterAnimation("img/monsters/Alector/Alector.scml");
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(300,300);
        } else {
            setHp(270, 270);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.metaAmt = 7;
            this.blockAmt = 10;
            this.buffAmt = 4;
            this.metaBuffAmt = 3;
        } else if (AbstractDungeon.ascensionLevel >= 3) {
            this.metaAmt = 5;
            this.blockAmt = 8;
            this.buffAmt = 3;
            this.metaBuffAmt = 2;
        } else {
            this.metaAmt = 4;
            this.blockAmt = 6;
            this.buffAmt = 3;
            this.metaBuffAmt = 2;
        }
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.beamDmg = 7;
        } else {
            this.beamDmg = 6;
        }
        this.damage.add(new DamageInfo(this, beamDmg));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AlectorPower(this, this.buffAmt), this.buffAmt));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, this.metaAmt), this.metaAmt));
        if (AbstractDungeon.ascensionLevel >= 18) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 3), 3));
        }else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 2), 2));
        }
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 3:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
                addToBot(new SFXAction("Alector_PREP"));
                AbstractMonster m1 = new MechaSoldier(this.spawnX + -135.0F * 1, MathUtils.random(-5.0F, 25.0F));
                AbstractMonster m2 = new MechaSoldier(this.spawnX + -135.0F * 2, MathUtils.random(-5.0F, 25.0F));
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m1, true));
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m2, true));
                this.enemySlots.put(1, m1);
                this.enemySlots.put(2, m2);
                if (AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m1, this, new ArtifactPower(m1, 2)));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m2, this, new ArtifactPower(m2, 2)));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m1, this, new ArtifactPower(m1, 1)));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m2, this, new ArtifactPower(m2, 1)));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m1, this, new MetallicizePower(m1, 5),5));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m2, this, new MetallicizePower(m2, 5),5));
                break;
            case 2:
                if (this.enemySlots.size()==2){
                    AbstractMonster m3 = new MechaSoldier(this.spawnX + -135.0F * 3, MathUtils.random(-5.0F, 25.0F));
                    AbstractMonster m4 = new MechaSoldier(this.spawnX + -135.0F * 4, MathUtils.random(-5.0F, 25.0F));
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m3, true));
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m4, true));
                    this.enemySlots.put(3, m3);
                    this.enemySlots.put(4, m4);
                    if (AbstractDungeon.ascensionLevel >= 17) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m3, this, new ArtifactPower(m3, 2)));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m4, this, new ArtifactPower(m4, 2)));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m3, this, new ArtifactPower(m3, 1)));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m4, this, new ArtifactPower(m4, 1)));
                    }
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m3, this, new MetallicizePower(m3, 5),5));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m4, this, new MetallicizePower(m4, 5),5));
                }
                for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                    if (!m.isDead && !m.isDying && !m.isEscaping){
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, this.blockAmt));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,this,new MetallicizePower(m,this.metaBuffAmt),this.metaBuffAmt));
                    }
                }
                break;
            case 1:
                for (int i = 0;i<2;i++){
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(this, (AbstractGameEffect)new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.GREEN_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 1.5F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage
                            .get(0), AbstractGameAction.AttackEffect.SMASH));
                }
                break;
            case 6:
                for (Map.Entry<Integer, AbstractMonster> m : this.enemySlots.entrySet()) {
                    if ((m.getValue()).isDying) {
                        AbstractMonster newMonster = new MechaSoldier(this.spawnX + -185.0F * ((Integer)m.getKey()).intValue(), MathUtils.random(-5.0F, 25.0F));
                        int key = ((Integer)m.getKey()).intValue();
                        this.enemySlots.put(Integer.valueOf(key), newMonster);
                        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(newMonster, true));
                        if (AbstractDungeon.ascensionLevel >= 17) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(newMonster, this, new ArtifactPower(newMonster, 2)));
                        } else {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(newMonster, this, new ArtifactPower(newMonster, 1)));
                        }
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(newMonster, this, new MetallicizePower(newMonster, 5),5));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(newMonster, this, new MetallicizePower(newMonster, 5),5));
                    }
                }
                break;
            default:
                System.out.println("ERROR: Default Take Turn was called on " + this.name);
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (this.firstTurn) {
            this.firstTurn = false;
            setMove(STICKY_NAME, (byte)3, Intent.UNKNOWN);
            return;
        }
        if (i <= 50 && isMinionDead() && !lastMove((byte)6)) {
            setMove((byte)6, Intent.BUFF);
            return;
        }
        if (!lastTwoMoves((byte)2)) {
            setMove((byte)2, Intent.DEFEND_BUFF);
        } else {
            setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base,2,true);
        }
    }

    private void playDeathSfx() {
        int roll = MathUtils.random(1);
        if (roll == 0) {
            CardCrawlGame.sound.play("Alector_D1");
        } else {
            CardCrawlGame.sound.play("Alector_D2");
        }
    }

    private boolean isMinionDead() {
        for (Map.Entry<Integer, AbstractMonster> m : this.enemySlots.entrySet()) {
            if ((m.getValue()).isDying)
                return true;
        }
        return false;
    }

    @Override
    public void die() {
        super.die();
        playDeathSfx();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (!m.isDead && !m.isDying) {
                AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
            }
        }
    }
}
