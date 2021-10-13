package com.jaeheonshim.towerheist.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jaeheonshim.towerheist.Assets;

public class GameStage implements Disposable {
    private Stage stage;
    private Table table;

    private Skin defaultSkin;

    private Viewport stageViewport = new ExtendViewport(800, 800);

    private PlayerPhysicsDebugWidget physicsDebugWidget;

    public GameStage() {
        stage = new Stage(stageViewport);
        table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        defaultSkin = new Skin(Gdx.files.internal("ui/uiskin.json"), Assets.instance().get("ui/uiskin.atlas"));

        physicsDebugWidget = new PlayerPhysicsDebugWidget(defaultSkin);
        table.add(physicsDebugWidget).width(400).expand().top().right();
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render () {
        stage.act(Gdx.graphics.getDeltaTime());
        stageViewport.apply();
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}
