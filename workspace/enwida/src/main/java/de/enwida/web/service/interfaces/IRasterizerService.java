package de.enwida.web.service.interfaces;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

public interface IRasterizerService {

	public void rasterize(TranscoderInput in, TranscoderOutput out) throws Exception;
	public void rasterize(InputStream in, OutputStream out) throws Exception;
	
}
