package minecraft.sightworld.defaultlib.gamer;

public interface OnlineGamer extends IBaseGamer {

    void sendMessage(String message);
    void sendMessageLocale(String message, Object... format);
    boolean isVanish();

    void sendTitle(String title, String subTitle);

    void sendTitle(String title, String subTitle, long fadeInTime, long stayTime, long fadeOutTime);

    void sendActionBar(String msg);

}
