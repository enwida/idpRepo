package de.enwida.web.service.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface ISVGService {

	public void rasterize(InputStream in, OutputStream out) throws Exception;
	public Document sanitizeSVG(InputStream svgData) throws IOException;
	public String serializeNode(Node node) throws Exception;
	
}
