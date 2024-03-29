package _base;

public interface Constant {
    String ROOT_DIR = System.getProperty("user.dir");
    String NEXT_LINE = "\n";
    String COMMA_AND_SPACE = ", ";
    String SEPARATOR = System.getProperty("file.separator");
    String DOWNLOAD_ATTACHMENT_FOLDER = System.getProperty("user.dir")
            + SEPARATOR + "target"
            + SEPARATOR + "download_attachments"
            + SEPARATOR;
    String IMAGE_FOLDER = System.getProperty("user.dir")
            + SEPARATOR + "src"
            + SEPARATOR + "test"
            + SEPARATOR + "resources"
            + SEPARATOR + "images"
            + SEPARATOR;
}