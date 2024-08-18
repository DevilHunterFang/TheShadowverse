package shadowverse.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import shadowverse.monsters.*;
import shadowverseCharbosses.bosses.Maisha.Maisha;

public class ShadowverseScene extends AbstractScene {
    private TextureAtlas.AtlasRegion bg;

    public ShadowverseScene() {
        super("img/scene/atlas.atlas");
        this.bg = this.atlas.findRegion("mod/Rivayle");
        this.ambianceName = "AMBIANCE_CITY";
        fadeInAmbiance();
    }

    public void update() {
        super.update();
    }

    public void nextRoom(AbstractRoom room) {
        super.nextRoom(room);
        randomizeScene();
        if (room instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss)
            CardCrawlGame.music.silenceBGM();
        if (room.monsters != null) {
            for (AbstractMonster mo : room.monsters.monsters) {
                if (mo instanceof Zecilwenshe) {
                    this.bg = this.atlas.findRegion("mod/Train");
                    continue;
                }
                if (mo instanceof VincentBOSS) {
                    this.bg = this.atlas.findRegion("mod/Office");
                    continue;
                }
                if (mo instanceof Iceschillendrig) {
                    this.bg = this.atlas.findRegion("mod/MainStreet");
                    continue;
                }
                if (mo instanceof Naht) {
                    this.bg = this.atlas.findRegion("mod/Wealth");
                    continue;
                }
                if (mo instanceof TaketsumiBOSS || mo instanceof chushou1 || mo instanceof chushou2){
                    this.bg = this.atlas.findRegion("mod/Naterran");
                    continue;
                }
                if (mo instanceof Belphomet){
                    this.bg = this.atlas.findRegion("mod/Isunia");
                    continue;
                }
                if (mo instanceof Maisha){
                    this.bg = this.atlas.findRegion("mod/Vellsar");
                    continue;
                }
                if (mo instanceof Nexus){
                    this.bg = this.atlas.findRegion("mod/Desert");
                    continue;
                }
                if (mo instanceof Megaera || mo instanceof Tisiphone || mo instanceof Alector || mo instanceof Surveyor || mo instanceof MegaEnforcer){
                    this.bg = this.atlas.findRegion("mod/Aialon");
                    continue;
                }
                if (mo instanceof Spider2){
                    this.bg = this.atlas.findRegion("mod/Corridor");
                    continue;
                }
                if (mo instanceof OOOGGGG){
                    this.bg = this.atlas.findRegion("mod/Forest");
                    continue;
                }
                if (mo instanceof Sword || mo instanceof Axe){
                    this.bg = this.atlas.findRegion("mod/Barracks");
                    continue;
                }
                this.bg = this.atlas.findRegion("mod/Rivayle");
            }
        } else {
            this.bg = this.atlas.findRegion("mod/Rivayle");
        }
        if (AbstractDungeon.actNum == 4){
            this.bg = this.atlas.findRegion("mod/Spatialdistortion");
        }
        fadeInAmbiance();
    }

    public void renderCombatRoomBg(SpriteBatch sb) {
        sb.setColor(Color.WHITE.cpy());
        renderAtlasRegionIf(sb, this.bg, true);
        sb.setBlendFunction(770, 771);
    }

    public void renderCombatRoomFg(SpriteBatch sb) {
        sb.setColor(Color.WHITE.cpy());
        sb.setColor(Color.WHITE.cpy());
    }

    public void renderCampfireRoom(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        renderAtlasRegionIf(sb, this.campfireBg, true);
        sb.setBlendFunction(770, 1);
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, MathUtils.cosDeg((float)(System.currentTimeMillis() / 3L % 360L)) / 10.0F + 0.8F));
        renderQuadrupleSize(sb, this.campfireGlow, !CampfireUI.hidden);
        sb.setBlendFunction(770, 771);
        sb.setColor(Color.WHITE);
        renderAtlasRegionIf(sb, this.campfireKindling, true);
    }

    @Override
    public void randomizeScene() {
    }
}
