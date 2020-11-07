package br.com.example.pdf.report.engine;

import br.com.example.pdf.report.aws.S3Service;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.lowagie.text.DocumentException;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

public class ReportEngine {
    private Handlebars handlebars;
    private S3Service s3Service;

    public ReportEngine() {
        s3Service = new S3Service();
        handlebars = new Handlebars();
        handlebars.registerHelpers(new HandlebarsHelpers());
    }

    public void generate(String templateFile, Map<String, Object> data) {

        String outputFile = Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());

        File pdf = new File(outputFile);

        try (OutputStream output = new FileOutputStream(pdf)) {
            Template template = handlebars.compile(templateFile);

            String mergedTemplate = template.apply(data);
            Document doc = getDocumentBuilder().parse(new ByteArrayInputStream(mergedTemplate.getBytes("UTF-8")));

            itextMountPdf(doc, output);

            s3Service.uploadPdfToS3(pdf);

        } catch (IOException | ParserConfigurationException | SAXException | DocumentException e) {
            e.printStackTrace();
        }

        pdf.delete();
    }

    private void itextMountPdf(Document doc, OutputStream outputStreamPdf) throws DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(doc, null);
        renderer.layout();
        renderer.createPDF(outputStreamPdf);
        renderer.finishPDF();
    }

    private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        fac.setNamespaceAware(false);
        fac.setValidating(false);
        fac.setFeature("http://xml.org/sax/features/namespaces", false);
        fac.setFeature("http://xml.org/sax/features/validation", false);
        fac.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        fac.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return fac.newDocumentBuilder();
    }
}

