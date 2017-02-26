package de.entwicklerpages.java.schoolgame.game;


public class CutScene {

    private String cutSceneImage;
    private String dialogId;

    public CutScene(String cutSceneImage, String dialogId)
    {
        this.cutSceneImage = cutSceneImage;
        this.dialogId = dialogId;
    }

    public String getCutSceneImage() {
        return cutSceneImage;
    }

    public String getDialogId() {
        return dialogId;
    }
}
