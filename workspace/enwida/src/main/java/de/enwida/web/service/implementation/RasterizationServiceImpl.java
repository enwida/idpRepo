package de.enwida.web.service.implementation;

import java.awt.Color;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.springframework.stereotype.Service;

import de.enwida.web.service.interfaces.IRasterizerService;

@Service("rasterizationService")
public class RasterizationServiceImpl implements IRasterizerService {
	
	public RasterizationServiceImpl() {
		System.setProperty("org.xml.sax.driver","org.apache.xerces.parsers.SAXParser");
		XMLResourceDescriptor.setXMLParserClassName("org.apache.xerces.parsers.SAXParser");
	}
	
	public void rasterize(TranscoderInput in, TranscoderOutput out) throws Exception {
		final PNGTranscoder transcoder = new PNGTranscoder();

		transcoder.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR, Color.WHITE);
		// TODO: find a better solution to refer to this
		transcoder.addTranscodingHint(ImageTranscoder.KEY_USER_STYLESHEET_URI, "http://localhost:8080/enwida/resources/css/chart/chart-svg.css");
		transcoder.transcode(in, out);
	}
	
	@Override
	public void rasterize(InputStream in, OutputStream out) throws Exception {
		rasterize(new TranscoderInput(in), new TranscoderOutput(out));
	}

}
