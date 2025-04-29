//package util;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.common.PDRectangle;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import org.apache.pdfbox.text.PDFTextStripper;
//
//import com.itextpdf.text.pdf.parser.clipper.Paths;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//public class PdfIO {
//
//	/**
//     * Tạo một tệp PDF mới với văn bản đã cho.
//     *
//     * @param outputPath Đường dẫn đến tệp PDF đầu ra.
//     * @param text       Văn bản cần thêm vào tệp PDF.
//     * @throws IOException Nếu xảy ra lỗi khi tạo hoặc ghi tệp PDF.
//     */
//    public static void createPdf(String outputPath, String text) throws IOException {
//        try (PDDocument document = new PDDocument()) {
//            PDPage page = new PDPage(PDRectangle.A4);
//            document.addPage(page);
//
//            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, 12);
//                contentStream.newLineAtOffset(50, 750);
//                contentStream.showText(text);
//                contentStream.endText();
//            }
//
//            document.save(outputPath);
//        }
//    }
//
//    /**
//     * Đọc nội dung văn bản từ một tệp PDF.
//     *
//     * @param inputPath Đường dẫn đến tệp PDF cần đọc.
//     * @return Nội dung văn bản từ tệp PDF.
//     * @throws IOException Nếu xảy ra lỗi khi đọc tệp PDF.
//     */
//    public static String readPdf(String inputPath) throws IOException {
//        try (PDDocument document = PDDocument.load(Paths.get(inputPath).toFile())) {
//            return new org.apache.pdfbox.text.PDFTextStripper().getText(document);
//        }
//    }
//
//    // Bạn có thể thêm các phương thức khác như mergePdf, splitPdf, addImageToPdf, v.v.
//}
