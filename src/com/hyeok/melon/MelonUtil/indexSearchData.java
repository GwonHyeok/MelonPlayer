package com.hyeok.melon.MelonUtil;

import com.hyeok.melon.SearchData;

/**
 * Created by GwonHyeok on 14. 12. 15..
 */
public class indexSearchData extends SearchData {
    private int id;

    public indexSearchData(String SongName, String SID, String Albumart, String Singer) {
        super(SongName, SID, Albumart, Singer);
    }

    public indexSearchData(int id, String SongName, String SID, String Albumart, String Singer) {
        super(SongName, SID, Albumart, Singer);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
