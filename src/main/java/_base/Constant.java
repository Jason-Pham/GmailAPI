package _base;

public interface Constant {
    String ROOT_DIR = System.getProperty("user.dir");
    String SEPARATOR = System.getProperty("file.separator");
    String IMAGE_FOLDER = System.getProperty("user.dir")
            + SEPARATOR + "src"
            + SEPARATOR + "test"
            + SEPARATOR + "resources"
            + SEPARATOR + "images"
            + SEPARATOR;

}