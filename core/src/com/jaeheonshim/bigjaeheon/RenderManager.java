package com.jaeheonshim.bigjaeheon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderManager {
    private List<RenderItem> renderItemList;

    public RenderManager() {
        renderItemList = new ArrayList<>();
    }

    public void addItem(RenderItem renderItem) {
        this.renderItemList.add(renderItem);
    }

    public void initialize() {
        Collections.sort(renderItemList);
    }

    public void render(SpriteBatch spriteBatch) {
        for(RenderItem item : renderItemList) {
            item.draw(spriteBatch);
        }
    }
}
