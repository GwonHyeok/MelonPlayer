package com.hyeok.melon.CustomSwingContent;

import com.hyeok.melon.MelonUtil.indexSearchData;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by GwonHyeok on 14. 12. 14..
 */
public class MelonPlayListTableModel extends AbstractTableModel {
    private String[] COLUMN = new String[]{"곡명", "아티스트"};
    private ArrayList<indexSearchData> datas;

    public MelonPlayListTableModel(ArrayList<indexSearchData> datas) {
        this.datas = datas;
    }

    @Override
    public int getRowCount() {
        return datas.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return datas.get(rowIndex).getSongName();
        } else if (columnIndex == 1) {
            return datas.get(rowIndex).getSinger();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN[column];
    }

    public indexSearchData getSelectedData(int rowIndex) {
        return datas.get(rowIndex);
    }

    public ArrayList<indexSearchData> getSelectDataIndex(int data[]) {
        ArrayList<indexSearchData> indexSearchDatas = new ArrayList<indexSearchData>();
        for (Integer row : data) {
            indexSearchDatas.add(datas.get(row));
        }
        return indexSearchDatas;
    }
}
