package de.enwida.web.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonResponse {
	 
    class Col {
        String id;
        final String _type;
        final String _label;
 
        public Col(String type, String label) {
            _type = type;
            _label = label;
        }
 
        public String getId() {
            return id;
        }
 
        public String getType() {
            return _type;
        }
 
        public String getLabel() {
            return _label;
        }
    };
 
    public static class Cell {
        private final Object _v;
 
        public Cell(Object v) {
            _v = v;
        }
 
        public Object getV() {
            return _v;
        }
    }
 
    class Row {
        List<Cell> _c = new ArrayList<Cell>();
 
        public void addCells(Cell... cells) {
            _c.addAll(Arrays.asList(cells));
        }
 
        public List<Cell> getC() {
            return _c;
        }
    };
 
    /* GoogleResonResponse attributes starts here */
    private List<Col> _cols = new ArrayList<Col>();
     
    private List<Row> _rows = new ArrayList<Row>();
 
    public void addColl(String type, String label) {
        Col col = new Col(type, label);
        _cols.add(col);
    }
 
    public void addRow(Cell... cells) {
        Row row = new Row();
        row.addCells(cells);
        _rows.add(row);
    }
 
    public List<Col> getCols() {
        return _cols;
    }
 
    public List<Row> getRows() {
        return _rows;
    }
}
