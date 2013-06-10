package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.transport.IDataLine;
import de.enwida.web.model.ChartLinesRequest;

public interface ILineService {

    public List<IDataLine> getLines(ChartLinesRequest request);
    
}