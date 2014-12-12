package com.hyeok.melon.MelonUtil;

/**
 * Created by GwonHyeok on 14. 12. 12..
 */
public class MemberInfo {

    private static MemberInfo instance;
    private String keyCookie;

    public synchronized static MemberInfo getInstance() {
        if (instance == null) {
            instance = new MemberInfo();
        }
        return instance;
    }

    public String getKeyCookie() {
        return keyCookie;
    }

    public void setKeyCookie(String keyCookie) {
        this.keyCookie = keyCookie;
    }
}
