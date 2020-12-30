package trong.lixco.com.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

public class PDFMerger {

	public static byte[] concatPDFs(byte[] firstPdf, byte[] lastPdf) {

		InputStream targetStream1 = new ByteArrayInputStream(firstPdf);
		InputStream targetStream2 = new ByteArrayInputStream(lastPdf);

		List<InputStream> pdfs = new ArrayList<InputStream>();
		pdfs.add(targetStream1);
		pdfs.add(targetStream2);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		Document document = new Document();

		try {
			List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				pdf.close();
				totalPages += pdfReader.getNumberOfPages();
			}

			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data

			PdfImportedPage page;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();
				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					// // Set document size based on the original PDF's size
					Rectangle rg = pdfReader.getPageSize(++pageOfCurrentReaderPDF);

					float w = rg.getWidth();
					float h = rg.getHeight();
					rg = new Rectangle(w, h);
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					document.setPageSize(rg);
					document.newPage();

					cb.addTemplate(page, 0, 0);
				}
				pageOfCurrentReaderPDF = 0;
			}
			outputStream.flush();
			document.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		return outputStream.toByteArray();
	}

	public static byte[]  mergePdfs(byte[] firstPdf, byte[] lastPdf) {
		try {
			InputStream targetStream1 = new ByteArrayInputStream(firstPdf);
			InputStream targetStream2 = new ByteArrayInputStream(lastPdf);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			// Instantiating PDFMergerUtility class
			PDFMergerUtility PDFmerger = new PDFMergerUtility();

			// Setting the destination file
			PDFmerger.setDestinationStream(outputStream);

			// adding the source files
			PDFmerger.addSource(targetStream1);
			PDFmerger.addSource(targetStream2);

			// Merging the two documents
			PDFmerger.mergeDocuments();
			return outputStream.toByteArray();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] mergePdfs_(byte[] firstPdf, byte[] lastPdf) {

		InputStream targetStream1 = new ByteArrayInputStream(firstPdf);
		InputStream targetStream2 = new ByteArrayInputStream(lastPdf);

		ByteArrayOutputStream byteArrayOutputStream = null;
		byte[] mergedPdf = null;
		try {
			Document document = new Document();
			byteArrayOutputStream = new ByteArrayOutputStream();
			PdfWriter pdfWriter = PdfWriter.getInstance(document, byteArrayOutputStream);

			document.open();
			PdfContentByte pdfContentByte = pdfWriter.getDirectContent();
			PdfImportedPage pdfImportedPage = null;
			PdfReader pdfReader;
			if (firstPdf != null) {
				pdfReader = new PdfReader(firstPdf);
				pdfReader = unlockPdf(pdfReader);
				for (int i = 0; i < pdfReader.getNumberOfPages(); i++) {
					document.newPage();
					pdfImportedPage = pdfWriter.getImportedPage(pdfReader, i + 1);
					// float width = pdfImportedPage.getWidth();
					// float height = pdfImportedPage.getHeight();
					// document.setPageSize(new Rectangle(width, height)); //
					// doesent
					// // work
					pdfContentByte.addTemplate(pdfImportedPage, 0, 0);
				} // for
			}
			pdfReader = new PdfReader(lastPdf);
			pdfReader = unlockPdf(pdfReader);
			for (int i = 0; i < pdfReader.getNumberOfPages(); i++) {
				document.newPage();
				pdfImportedPage = pdfWriter.getImportedPage(pdfReader, i + 1);
				// float width = pdfImportedPage.getWidth();
				// float height = pdfImportedPage.getHeight();
				// document.setPageSize(new Rectangle(width, height)); //
				// doesent
				// // work
				pdfContentByte.addTemplate(pdfImportedPage, 0, 0);
			} // for

			document.close();
			mergedPdf = byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return mergedPdf;
	}
	public static byte[] getFile(String path, String name) {
		try {
			File directory = new File(path);
			File file = null;
			Iterator<File> collectors = FileUtils.listFiles(directory, new WildcardFileFilter(name + "*"), null)
					.iterator();
			while (collectors.hasNext()) {
				file = collectors.next();
				break;
			}
			if (file != null) {
				return Files.readAllBytes(file.toPath());
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void getDelete(String path, String name) {
		try {
			File directory = new File(path);
			File file = null;
			Iterator<File> collectors = FileUtils.listFiles(directory, new WildcardFileFilter(name + "*"), null)
					.iterator();
			while (collectors.hasNext()) {
				file = collectors.next();
				break;
			}
			if (file != null) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static PdfReader unlockPdf(PdfReader reader) {
		if (reader == null) {
			return reader;
		}
		try {
			Field f = reader.getClass().getDeclaredField("encrypted");
			f.setAccessible(true);
			f.set(reader, false);
		} catch (Exception e) { // ignore
		}
		return reader;
	}

}